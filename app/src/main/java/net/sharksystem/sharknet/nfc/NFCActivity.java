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
import net.sharksystem.api.impl.SharkNetEngine;
import net.sharksystem.api.shark.peer.AndroidSharkEngine;
import net.sharksystem.api.shark.ports.NfcPkiPortEventListener;
import net.sharksystem.api.shark.ports.NfcPkiPortListener;
import net.sharksystem.sharknet.NavigationDrawerActivity;
import net.sharksystem.sharknet.R;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class NFCActivity extends NavigationDrawerActivity implements NfcPkiPortListener {

    final Context context = this;
    private Button button;
    private NfcPkiPortEventListener nfcPkiPortEventListener;
    private SharkNetEngine sharkNet;
    private AndroidSharkEngine sharkEngine;
    private AlertDialog onMessageDialog;
    private AlertDialog onPublicKeyDialog;
    private AlertDialog onCertificateDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutResource(R.layout.nfc2_activity);

        sharkNet = SharkNetEngine.getSharkNet();
        sharkEngine = sharkNet.getSharkEngine();
        try {
            L.d(sharkEngine.getPKIStorage().getOwnerPublicKey().toString(), this);
            nfcPkiPortEventListener = sharkNet.setupNfc(this, this, sharkEngine.getPKIStorage().getPublicKeyAsKnowledge(true));
        } catch (SharkProtocolNotSupportedException | SharkNotSupportedException e) {
            e.printStackTrace();
        } catch (SharkKBException e) {
            e.printStackTrace();
        }

        button = (Button) findViewById(R.id.button_nfc_start);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                button.setText("NFC started...");
                try {
                    sharkNet.startSendingViaNfc();
                } catch (SharkProtocolNotSupportedException | IOException e) {
                    e.printStackTrace();
                }
                L.d("NFC started", this);
            }
        });


    }

    @Override
    public void onMessageReceived() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (onMessageDialog==null) {
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
                try {
                    SharkNetEngine.getSharkNet().getSharkEngine().stopNfc();
                } catch (SharkProtocolNotSupportedException e) {
                    e.printStackTrace();
                }
                try {
                    sharkEngine.stopNfc();
                } catch (SharkProtocolNotSupportedException e) {
                    e.printStackTrace();
                }
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
    public void onCertificatesReceived(final List<SharkCertificate> certificates) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (onPublicKeyDialog.isShowing()) {
                    onPublicKeyDialog.dismiss();
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(NFCActivity.this);
                builder.setMessage("We received "
                        + certificates.size()
                        + " certificates from "
                        + certificates.get(0).getSigner().getName()
                        + ". Do you want to include them?")
                        .setTitle("New Certificates");
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


    @Override
    protected boolean doInBackground() {
        return false;
    }

    @Override
    protected void doWhenFinished(boolean success) {

    }


    //
//    private String con_nickname;
//    private static final String CONTACT_NICKNAME = "CONTACT_NICKNAME";
//    private TextView name, key, found_name, found_key;
//    private LinearLayout layout_found_name,layout_found_key;
//    private Button accept;
//
//    @Override
//    protected void onResume(){
//        super.onResume();
//
//        this.con_nickname = getIntent().getStringExtra("CONTACT_NICKNAME");
//
//        accept = (Button) findViewById(R.id.nfc_accept);
//        accept.setOnClickListener(this);
//        // hört auf das NFC
//        SharkNetEngine.getSharkNet().setupNfc(this);
//        name = (TextView) findViewById(R.id.nfc_name);
//        key  = (TextView) findViewById(R.id.nfc_key);
//        found_name = (TextView) findViewById(R.id.nfc_found_name);
//        found_key = (TextView) findViewById(R.id.nfc_found_key);
//
//        layout_found_name = (LinearLayout) findViewById(R.id.nfc_layout_found_name);
//        layout_found_key = (LinearLayout) findViewById(R.id.nfc_layout_found_key);
//
//        try {
//            name.setText(SharkNetEngine.getSharkNet().getMyProfile().getName());
//            key.setText(SharkNetEngine.getSharkNet().getMyProfile().getPublicKey());
//        } catch (SharkKBException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        setContentView(R.layout.activity_nfc);
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public void onClick(View view)
//    {
//        switch (view.getId()) {
//            case R.id.home:
//                onBackPressed();
//                break;
//            case R.id.nfc_accept:
//                //TODO: newContact will nur Nickname keinen namen
//                //TODO: kenne die UID nicht !!
//                try {
//                    SharkNetEngine.getSharkNet().newContact(found_name.getText().toString(),"null",found_key.getText().toString());
//                } catch (SharkKBException e) {
//                    e.printStackTrace();
//                }
//                Intent intent = new Intent(NFCActivity.this,ContactsActivity.class);
//                startActivity(intent);
////                finish();
//        }
//    }
//
//    @Override
//    public void onNewContactViaNFC(Contact contact) throws SharkKBException {
//        findViewById(R.id.nfc_loading).setVisibility(View.GONE);
//        found_name.setText(contact.getName());
//        found_key.setText(contact.getPublicKey());
//        layout_found_name.setVisibility(View.VISIBLE);
//        layout_found_key.setVisibility(View.VISIBLE);
//        accept.setVisibility(View.VISIBLE);
//
//    }
}
