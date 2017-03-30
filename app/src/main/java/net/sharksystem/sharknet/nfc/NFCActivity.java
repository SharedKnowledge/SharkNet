package net.sharksystem.sharknet.nfc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import net.sharkfw.asip.engine.serializer.SharkProtocolNotSupportedException;
import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.security.SharkCertificate;
import net.sharkfw.system.L;
import net.sharkfw.system.SharkNotSupportedException;
import net.sharksystem.api.models.Contact;
import net.sharksystem.api.shark.peer.AndroidSharkEngine;
import net.sharksystem.api.shark.ports.NfcPkiPortEventListener;
import net.sharksystem.api.shark.ports.NfcPkiPortListener;
import net.sharksystem.sharknet.NavigationDrawerActivity;
import net.sharksystem.sharknet.R;

import java.io.IOException;
import java.util.List;

public class NFCActivity extends NavigationDrawerActivity implements NfcPkiPortListener {

    final Context context = this;
    private Button button;
    private NfcPkiPortEventListener nfcPkiPortEventListener;
//    private SharkNetEngine sharkNet;
    private AndroidSharkEngine sharkEngine;
    private AlertDialog onMessageDialog;
    private AlertDialog onPublicKeyDialog;
    private AlertDialog onCertificateDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutResource(R.layout.nfc_activity);

//        sharkNet = SharkNetEngine.getSharkNet();
//        sharkEngine = sharkNet.getSharkEngine();
//        try {
//            L.d(sharkEngine.getPKIStorage().getOwnerPublicKey().toString(), this);
//            nfcPkiPortEventListener = sharkNet.setupNfc(this, this, sharkEngine.getPKIStorage().getPublicKeyAsKnowledge(true));
//        } catch (SharkProtocolNotSupportedException | SharkNotSupportedException e) {
//            e.printStackTrace();
//        } catch (SharkKBException e) {
//            e.printStackTrace();
//        }

        button = (Button) findViewById(R.id.button_nfc_start);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                button.setText("NFC started...");
//                try {
//                    sharkNet.startSendingViaNfc();
//                } catch (SharkProtocolNotSupportedException | IOException e) {
//                    e.printStackTrace();
//                }
                L.d("NFC started", this);
            }
        });


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
//                try {
//                    SharkNetEngine.getSharkNet().getSharkEngine().stopNfc();
//                } catch (SharkProtocolNotSupportedException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    sharkEngine.stopNfc();
//                } catch (SharkProtocolNotSupportedException e) {
//                    e.printStackTrace();
//                }
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

                if (onMessageDialog.isShowing()) {
                    onMessageDialog.dismiss();
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(NFCActivity.this);
                builder.setMessage("Is this person in front of you really " + owner.getName() + "?").setTitle("New Key from " + owner.getName());
                builder.setPositiveButton("Sign", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nfcPkiPortEventListener.onPublicKeyDecision(true);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nfcPkiPortEventListener.onPublicKeyDecision(false);
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

                if (onPublicKeyDialog.isShowing()) {
                    onPublicKeyDialog.dismiss();
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(NFCActivity.this);
                builder.setMessage(
                        "We received " + certificates.size()
                                + " certificates from " + certificates.get(0).getSigner().getName()
                                + ". There where also " + contacts.size() + " Contacts"
                                + ". Do you want to include them?").setTitle("New Certificates and Contacts");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nfcPkiPortEventListener.onCertificatesDecision(true);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nfcPkiPortEventListener.onCertificatesDecision(false);
                    }
                });
                onCertificateDialog = builder.create();
                onCertificateDialog.show();
            }
        });
    }

}