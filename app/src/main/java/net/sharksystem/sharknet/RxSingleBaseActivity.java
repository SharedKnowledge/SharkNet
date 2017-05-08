package net.sharksystem.sharknet;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.os.IBinder;

import java.util.concurrent.Callable;

import rx.Single;
import rx.SingleSubscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by j4rvis on 3/20/17.
 */

public abstract class RxSingleBaseActivity<T> extends BaseActivity {

    private Subscription mSubscription;
    private String mProgressMessage;
    private ProgressDialog mProgressDialog;

    protected void setProgressMessage(String message){
        mProgressMessage = message;
    }
    protected void setProgressMessage(int resource){
        mProgressMessage = getResources().getString(resource);
    }

    protected void startSubscription(){

        if(!mProgressMessage.isEmpty()){
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(mProgressMessage);
            if(!isFinishing()) mProgressDialog.show();
        }

        Single<T> single = Single.fromCallable(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return doOnBackgroundThread();
            }
        });

        mSubscription = single
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<T>() {
            @Override
            public void onSuccess(T value) {
                doOnUIThread(value);
                if(mProgressDialog!=null && mProgressDialog.isShowing()){
                    mProgressDialog.dismiss();
                }
            }

            @Override
            public void onError(Throwable error) {
                doOnError(error);
                if(mProgressDialog!=null && mProgressDialog.isShowing()){
                    mProgressDialog.dismiss();
                }
            }
        });
    }

    protected abstract T doOnBackgroundThread() throws Exception;

    protected abstract void doOnUIThread(T t);

    protected abstract void doOnError(Throwable error);

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mSubscription!=null && !mSubscription.isUnsubscribed()){
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        super.onServiceConnected(name, service);
        startSubscription();
    }
}
