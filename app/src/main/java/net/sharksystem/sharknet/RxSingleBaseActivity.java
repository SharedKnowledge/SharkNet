package net.sharksystem.sharknet;

import android.app.ProgressDialog;

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

    protected void startSubscription(){

        if(!mProgressMessage.isEmpty()){
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(mProgressMessage);
            mProgressDialog.show();
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

    abstract T doOnBackgroundThread();

    abstract void doOnUIThread(T t);

    abstract void doOnError(Throwable error);

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mSubscription!=null && !mSubscription.isUnsubscribed()){
            mSubscription.unsubscribe();
        }
    }
}
