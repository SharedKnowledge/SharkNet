package net.sharksystem.sharknet.nfc;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.sharkfw.asip.ASIPKnowledge;
import net.sharkfw.asip.engine.serializer.SharkProtocolNotSupportedException;
import net.sharkfw.knowledgeBase.SemanticNet;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.inmemory.InMemoASIPKnowledge;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharkfw.system.L;
import net.sharkfw.system.SharkNotSupportedException;
import net.sharksystem.api.impl.SharkNetEngine;
import net.sharksystem.api.interfaces.Contact;
import net.sharksystem.api.interfaces.SharkNet;
import net.sharksystem.api.shark.peer.AndroidSharkEngine;
import net.sharksystem.api.shark.protocols.nfc.NfcMessageStub;
import net.sharksystem.sharknet.NavigationDrawerActivity;
import net.sharksystem.sharknet.R;

import java.io.IOException;
import java.util.List;

public class NFCActivity extends NavigationDrawerActivity implements SharkNet.NFCContentListener, NfcMessageStub.NFCMessageListener{

    final Context context = this;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutResource(R.layout.nfc2_activity);

        final AndroidSharkEngine sharkEngine = SharkNetEngine.getSharkNet().getSharkEngine();

        sharkEngine.setActivity(this);
        SharkNetEngine.getSharkNet().exchangeContactNFC(this, this);

        final EditText edit = (EditText) findViewById(R.id.edittext_nfc);
        final Button setData = (Button) findViewById(R.id.button_set_data);

        final Context context = this;
        setData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                L.d("SetData clicked", this);
                String string = edit.getText().toString();
                String message = "";
                try {
                    InMemoASIPKnowledge knowledge = new InMemoASIPKnowledge();
                    knowledge.addInformation(string, InMemoSharkKB.createInMemoASIPInterest());
                    sharkEngine.offerNFC(knowledge);
                } catch (SharkProtocolNotSupportedException e) {
                    e.printStackTrace();
                } catch (SharkNotSupportedException e) {
                    e.printStackTrace();
                } catch (SharkKBException e) {
                    e.printStackTrace();
                } finally {
                    if(message.isEmpty()) message = "Data has been set!";
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }
            }
        });

        button = (Button) findViewById(R.id.button_nfc_start);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                button.setText("NFC started...");
                SharkNetEngine.getSharkNet().startSendingViaNFC();
                L.d("NFC started", this);
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

    @Override
    public void onMessageReceived(final String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.nfc_message_received_msg).setTitle(R.string.nfc_message_received_title);

        AlertDialog alertDialog = builder.create();

        alertDialog.show();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onExchangeComplete(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onExchangeFailure(final String failure) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    SharkNetEngine.getSharkNet().getSharkEngine().stopNfc();
                } catch (SharkProtocolNotSupportedException e) {
                    e.printStackTrace();
                }
                button.setText("Start NFC");
                Toast.makeText(context, failure, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onNewContact(Contact contact, List<Contact> contacts) throws SharkKBException {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    SharkNetEngine.getSharkNet().getSharkEngine().stopNfc();
                } catch (SharkProtocolNotSupportedException e) {
                    e.printStackTrace();
                }
                button.setText("Start NFC");
                Toast.makeText(context, "We received a new contact!", Toast.LENGTH_SHORT).show();
            }
        });
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
//        SharkNetEngine.getSharkNet().exchangeContactNFC(this);
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
