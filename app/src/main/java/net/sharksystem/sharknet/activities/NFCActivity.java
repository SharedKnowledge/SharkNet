package net.sharksystem.sharknet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.impl.SharkNetEngine;
import net.sharksystem.api.interfaces.Contact;
import net.sharksystem.api.interfaces.SharkNet;
import net.sharksystem.sharknet.R;

public class NFCActivity extends AppCompatActivity implements View.OnClickListener, SharkNet.NFCListener{

    private String con_nickname;
    private static final String CONTACT_NICKNAME = "CONTACT_NICKNAME";
    private TextView name, key, found_name, found_key;
    private LinearLayout layout_found_name,layout_found_key;
    private Button accept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.con_nickname = getIntent().getStringExtra("CONTACT_NICKNAME");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NFCActivity.this, ContactsDetailViewActivity.class);
                //identifies the chat
                intent.putExtra(CONTACT_NICKNAME, con_nickname);
                startActivity(intent);
            }
        });
        accept = (Button) findViewById(R.id.nfc_accept);
        accept.setOnClickListener(this);
        // hört auf das NFC
        SharkNetEngine.getSharkNet().exchangeContactNFC(this);
        name = (TextView) findViewById(R.id.nfc_name);
        key  = (TextView) findViewById(R.id.nfc_key);
        found_name = (TextView) findViewById(R.id.nfc_found_name);
        found_key = (TextView) findViewById(R.id.nfc_found_key);

        layout_found_name = (LinearLayout) findViewById(R.id.nfc_layout_found_name);
        layout_found_key = (LinearLayout) findViewById(R.id.nfc_layout_found_key);

        try {
            name.setText(SharkNetEngine.getSharkNet().getMyProfile().getName());
            key.setText(SharkNetEngine.getSharkNet().getMyProfile().getPublicKey());
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
    }

    /**
     * click Back-Button on Phone
     * */
    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(NFCActivity.this, ContactsDetailViewActivity.class);
        //identifies the chat
        intent.putExtra(CONTACT_NICKNAME, con_nickname);
        startActivity(intent);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId()) {
            case R.id.home:
                onBackPressed();
                break;
            case R.id.nfc_accept:
                //TODO: newContact will nur Nickname keinen namen
                //TODO: kenne die UID nicht !!
                try {
                    SharkNetEngine.getSharkNet().newContact(found_name.getText().toString(),"null",found_key.getText().toString());
                } catch (SharkKBException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(NFCActivity.this,ContactsActivity.class);
                startActivity(intent);
//                finish();
        }
    }

    @Override
    public void onNewContactViaNFC(Contact contact) throws SharkKBException {
        findViewById(R.id.nfc_loading).setVisibility(View.GONE);
        found_name.setText(contact.getName());
        found_key.setText(contact.getPublicKey());
        layout_found_name.setVisibility(View.VISIBLE);
        layout_found_key.setVisibility(View.VISIBLE);
        accept.setVisibility(View.VISIBLE);

    }
}
