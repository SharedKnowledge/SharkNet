package net.sharksystem.sharknet.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import net.sharkfw.system.L;
import net.sharksystem.api.impl.SharkNetEngine;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.dummy.Dummy;

import java.util.concurrent.Callable;

import rx.Single;
import rx.SingleSubscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity
        implements StartupFragment.StartupFragmentButtonListener,
            NewProfileFragment.NewProfileFragmentButtonListener,
            NewProfileAddressFragment.NewProfileAddressFragmentButtonListener{

    private StartupFragment mStartupFragment;
    private NewProfileFragment mNewProfileFragment;
    private NewProfileAddressFragment mNewProfileAddressFragment;
    private Subscription mSingleSubscription;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        L.setLogLevel(L.LOGLEVEL_ALL);

        if (savedInstanceState != null) {
            return;
        }

        mStartupFragment = new StartupFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mStartupFragment).commit();
    }

    @Override
    public void onCreateNewProfileSelected() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        mNewProfileFragment = new NewProfileFragment();
        transaction.replace(R.id.fragment_container, mNewProfileFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onCreateDummyDataSelected() {

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading Dummy Data");
        mProgressDialog.show();

        final Context that = this;
        SharkNetEngine.getSharkNet().setContext(this);
        Single<Void> single = Single.fromCallable(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                Dummy.createDummyData(that);
                SharkNetEngine.getSharkNet().startShark();
                return null;
            }
        });

        mSingleSubscription = single
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<Void>() {
            @Override
            public void onSuccess(Void value) {
                if(mProgressDialog.isShowing()){
                    mProgressDialog.dismiss();
                }

                Toast.makeText(that, "It worked!!!!", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(that, ChatActivity.class));
            }

            @Override
            public void onError(Throwable error) {
                L.e(error.getMessage(), that);
                Toast.makeText(that, "Creating Dummy Data failed.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onUsePreviousProfileSelected() {
        Toast.makeText(this, "Load the profile and load the chat activity.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNextFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        mNewProfileAddressFragment = new NewProfileAddressFragment();
        transaction.replace(R.id.fragment_container, mNewProfileAddressFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackToStartupFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.remove(mNewProfileFragment).commit();
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onCreateProfile() {

    }

    @Override
    public void onBackToNewProfileFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.remove(mNewProfileAddressFragment).commit();
        getSupportFragmentManager().popBackStack();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mSingleSubscription != null && !mSingleSubscription.isUnsubscribed()){
            mSingleSubscription.unsubscribe();
        }
    }
}
