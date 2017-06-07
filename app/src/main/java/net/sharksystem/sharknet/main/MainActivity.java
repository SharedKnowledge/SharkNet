package net.sharksystem.sharknet.main;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import net.sharkfw.asip.engine.serializer.SharkProtocolNotSupportedException;
import net.sharkfw.system.L;
import net.sharksystem.api.dao_interfaces.SharkNetApi;
import net.sharksystem.api.models.Contact;
import net.sharksystem.sharknet.BaseActivity;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.chat.ChatActivity;
import net.sharksystem.sharknet.dummy.Dummy;
import net.sharksystem.sharknet.radar.RadarActivity;

import java.io.IOException;
import java.util.concurrent.Callable;

import rx.Single;
import rx.SingleSubscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements StartupFragment.StartupFragmentButtonListener, NewProfileFragment.NewProfileFragmentButtonListener, NewProfileAddressFragment.NewProfileAddressFragmentButtonListener {

    private StartupFragment mStartupFragment;
    private NewProfileFragment mNewProfileFragment;
    private NewProfileAddressFragment mNewProfileAddressFragment;
    private Subscription mSingleSubscription;
    private ProgressDialog mProgressDialog;

    public Bitmap mContactImage;
    public String mContactName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState != null) {
            return;
        }
        mStartupFragment = new StartupFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mStartupFragment).commit();
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        super.onServiceConnected(name, service);
        // Switch dummy content
        getSharkApp().activateDummy();

        if(!wifiEnabled() || !bluetoothEnabled()){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Shark Settings");
            alertDialogBuilder
                    .setMessage("Please activate Wifi and Bluetooth to use SharkNet!")
                    .setCancelable(false)
                    .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            finish();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else {
            if(getSharkApp().isDummy()) onCreateDummyDataSelected();
        }
    }


    private boolean wifiEnabled(){
        WifiManager wifiManager = (WifiManager)this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        return wifiManager.isWifiEnabled();
    }

    private boolean bluetoothEnabled(){
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return mBluetoothAdapter.isEnabled();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSingleSubscription != null && !mSingleSubscription.isUnsubscribed()) {
            mSingleSubscription.unsubscribe();
        }
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
        mProgressDialog.setMessage("Lade Dummy Daten...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        Single<Void> single = Single.fromCallable(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                Dummy.createDummyData(MainActivity.this, mApi);
                getSharkApp().setAccount(mApi.getAccount());
                return null;
            }
        });

        mSingleSubscription = single.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleSubscriber<Void>() {
            @Override
            public void onSuccess(Void value) {
                try {
                    mApi.getSharkEngine().startBluetooth();
                } catch (SharkProtocolNotSupportedException | IOException e) {
                    e.printStackTrace();
                }
                mApi.startRadar();
                mApi.allowSyncInvitation(true);
                mApi.setNotificationResultActivity(new Intent(MainActivity.this, ChatActivity.class));
                // Radar
                startActivity(new Intent(MainActivity.this, RadarActivity.class));
            }

            @Override
            public void onError(Throwable error) {
                L.e(error.getMessage(), MainActivity.this);
                Toast.makeText(MainActivity.this, "Creating Dummy Data failed.", Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
            }
        });

    }

    @Override
    public void onUsePreviousProfileSelected() {
        Toast.makeText(this, "This is not yet supported.", Toast.LENGTH_SHORT).show();
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
    public void onCreateProfile(final Contact contact) {
        Single<Object> single = Single.fromCallable(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                mApi.setAccount(contact);
                getSharkApp().setAccount(mApi.getAccount());
                mApi.initPki();
                return null;
            }
        });

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Erstelle Kontakt...");
        mProgressDialog.show();

        mSingleSubscription = single.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleSubscriber<Object>() {
            @Override
            public void onSuccess(Object value) {
                try {
                    mApi.getSharkEngine().startBluetooth();
                } catch (SharkProtocolNotSupportedException | IOException e) {
                    e.printStackTrace();
                }
                mApi.startRadar();
                mApi.allowSyncInvitation(true);
                mApi.setNotificationResultActivity(new Intent(MainActivity.this, ChatActivity.class));
                // Radar
                startActivity(new Intent(MainActivity.this, RadarActivity.class));
            }

            @Override
            public void onError(Throwable error) {
                L.e(error.getMessage(), MainActivity.this);
                Toast.makeText(MainActivity.this, "Kontakt Erstellen schlug fehl...", Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
            }
        });

    }

    @Override
    public void onBackToNewProfileFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.remove(mNewProfileAddressFragment).commit();
        getSupportFragmentManager().popBackStack();
    }

    public SharkNetApi getApi(){
        return mApi;
    }
}
