package net.sharksystem.sharknet.contact;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.impl.SharkNetEngine;
import net.sharksystem.api.interfaces.Contact;
import net.sharksystem.sharknet.ParentActivity;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.nfc.NFCActivity;

import java.util.List;
import java.util.Objects;

public class ContactsDetailActivity extends ParentActivity{

    private String con_nickname;
    private List<Contact> contacts;
    private static final String CONTACT_NICKNAME = "CONTACT_NICKNAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutResource(R.layout.contact_detail_activity);

        this.con_nickname = getIntent().getStringExtra("CONTACT_NICKNAME");
        setToolbarTitle(con_nickname);
        //Typeface type = Typeface.createFromAsset(getAssets(),"fonts/RockSalt.TTF");
        try {
            this.contacts = SharkNetEngine.getSharkNet().getContacts();
        } catch (SharkKBException e) {
            e.printStackTrace();
        }

        TextView nickname = (TextView) findViewById(R.id.con_nickname_edit);
        //t.setTypeface(type);
        TextView name = (TextView) findViewById(R.id.con_name_edit);
        //n.setTypeface(type);
        TextView email = (TextView) findViewById(R.id.con_email_edit);
        //e.setTypeface(type);

        for (Contact contact : contacts) {
            try {
                if (Objects.equals(contact.getNickname(), con_nickname)) {
                    assert nickname != null;
                    if (contact.getNickname() != null) {
                        //nickname.setTypeface(type);
                        nickname.setText(contact.getNickname());
                    }

                    if (contact.getName() != null) {
                        //name.setTypeface(type);
                        name.setText(contact.getName());
                    }

                    if (contact.getEmail() != null) {
                        //name.setTypeface(type);
                        email.setText(contact.getEmail());
                    }


                }
            } catch (SharkKBException e1) {
                e1.printStackTrace();
            }
        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected boolean doInBackground() {
        return false;
    }

    @Override
    protected void doWhenFinished(boolean success) {

    }

}
