package net.sharksystem.sharknet.contact;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.impl.SharkNetEngine;
import net.sharksystem.api.interfaces.Contact;
import net.sharksystem.sharknet.NavigationDrawerActivity;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.SharkApp;

import java.util.List;

public class ContactsActivity extends NavigationDrawerActivity {

    private static final String CONTACT_NICKNAME = "CONTACT_NICKNAME";
    private List<Contact> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutResource(R.layout.contact_activity);

        FloatingActionButton fab = activateFloatingActionButton();
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactsActivity.this, ContactsNewActivity.class);
                startActivity(intent);
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
    protected void onResume() {
        super.onResume();
        try {
            contacts = SharkNetEngine.getSharkNet().getContactsWithoutMe();
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
        ContactsListAdapter contactsListAdapter = new ContactsListAdapter(this, R.layout.contact_line_item, contacts);
        ListView lv = (ListView) findViewById(R.id.con_list_view);
        if (lv != null) {
            lv.setAdapter(contactsListAdapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(ContactsActivity.this, ContactsDetailActivity.class);
                    ((SharkApp) getApplication()).setContact(contacts.get(position));
                    startActivity(intent);
                }
            });
        }
    }
}
