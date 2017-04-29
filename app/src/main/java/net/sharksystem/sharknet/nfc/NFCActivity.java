package net.sharksystem.sharknet.nfc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import net.sharkfw.knowledgeBase.Knowledge;
import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.security.SharkCertificate;
import net.sharksystem.api.models.Contact;
import net.sharksystem.api.shark.peer.AndroidSharkEngine;
import net.sharksystem.api.shark.ports.NfcPkiPortEventListener;
import net.sharksystem.api.shark.ports.NfcPkiPortListener;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.RxSingleNavigationDrawerActivity;

import java.util.List;

public class NFCActivity extends RxSingleNavigationDrawerActivity<Knowledge> implements NfcPkiPortListener {

    final Context context = this;
    private Button button;
    private NfcPkiPortEventListener mNfcPkiPortEventListener;
    //    private SharkNetEngine sharkNet;
    private AndroidSharkEngine sharkEngine;
    private AlertDialog onMessageDialog;
    private AlertDialog onPublicKeyDialog;
    private AlertDialog onCertificateDialog;
    private boolean mIsStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutResource(R.layout.nfc_activity);

        setProgressMessage("Preparing Exchange");
    }

    @Override
    public void onMessageReceived() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (onMessageDialog == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(NFCActivity.this);
                    builder.setMessage(R.string.nfc_message_received_msg).setTitle(R.string.nfc_message_received_title);
                    onMessageDialog = builder.create();
                    onMessageDialog.show();
                }

            }
        });
    }

    @Override
    public void onExchangeFailed(final String reason) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                button.setText("Start NFC");
                Toast.makeText(context, reason, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPublicKeyReceived(final PeerSemanticTag owner) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (onMessageDialog!=null && onMessageDialog.isShowing()) {
                    onMessageDialog.dismiss();
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(NFCActivity.this);
                builder.setMessage("Is this person in front of you really " + owner.getName() + "?").setTitle("New Key from " + owner.getName());
                builder.setPositiveButton("Sign", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mNfcPkiPortEventListener.onPublicKeyDecision(true);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mNfcPkiPortEventListener.onPublicKeyDecision(false);
                    }
                });
                onPublicKeyDialog = builder.create();
                onPublicKeyDialog.show();
            }
        });

    }

    @Override
    public void onCertificatesReceived(final List<SharkCertificate> certificates, final List<Contact> contacts) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (onPublicKeyDialog!=null&&onPublicKeyDialog.isShowing()) {
                    onPublicKeyDialog.dismiss();
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(NFCActivity.this);
                builder.setMessage("We received " + certificates.size() + " certificates from " + certificates.get(0).getSigner().getName() + ". There where also " + contacts.size() + " Contacts" + ". Do you want to include them?").setTitle("New Certificates and Contacts");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mNfcPkiPortEventListener.onCertificatesDecision(true);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mNfcPkiPortEventListener.onCertificatesDecision(false);
                    }
                });
                onCertificateDialog = builder.create();
                onCertificateDialog.show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(onMessageDialog!=null && onMessageDialog.isShowing()){
            onMessageDialog.dismiss();
        }
        if(onPublicKeyDialog!=null && onPublicKeyDialog.isShowing()){
            onPublicKeyDialog.dismiss();
        }
        if(onCertificateDialog!=null && onCertificateDialog.isShowing()){
            onCertificateDialog.dismiss();
        }
    }

    @Override
    protected Knowledge doOnBackgroundThread() throws Exception {
        mNfcPkiPortEventListener = mApi.initNFC(this);
        return null;
    }

    @Override
    protected void doOnUIThread(Knowledge knowledge) {
        button = (Button) findViewById(R.id.button_nfc_start);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mIsStarted){
                    button.setText("Start NFC");
                    mApi.stopNFC();
                    mIsStarted = false;
                } else {
                    button.setText("Stop NFC");
                    mApi.startNFC();
                    mIsStarted = true;
                }
            }
        });
    }

    @Override
    protected void doOnError(Throwable error) {

    }
}