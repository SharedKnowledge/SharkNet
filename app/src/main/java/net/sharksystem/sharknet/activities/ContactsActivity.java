package net.sharksystem.sharknet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.impl.SharkNetEngine;
import net.sharksystem.api.interfaces.Contact;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.adapters.ContactsListAdapter;

import java.util.List;

public class ContactsActivity extends BaseActivity {

    private static final String CONTACT_NICKNAME = "CONTACT_NICKNAME";
    private List<Contact> contacts;

    @Override
    protected void onResume()
    {
        super.onResume();
        try {
            contacts = SharkNetEngine.getSharkNet().getContacts();
        } catch (SharkKBException e) {
            e.printStackTrace();
        }

        ContactsListAdapter contactsListAdapter = new ContactsListAdapter(this, R.layout.line_item_con,contacts);
        ListView lv = (ListView)findViewById(R.id.con_list_view);
        if (lv != null)
        {
            lv.setAdapter(contactsListAdapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    Intent intent = new Intent(ContactsActivity.this,ContactsDetailViewActivity.class);
                    try {
                        intent.putExtra(CONTACT_NICKNAME,contacts.get(position).getNickname());
                    } catch (SharkKBException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_contacts);
        super.onCreate(savedInstanceState);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(ContactsActivity.this,ContactsNewActivity.class);
                startActivity(intent);
            }
        });

        try {
            contacts = SharkNetEngine.getSharkNet().getContacts();
        } catch (SharkKBException e) {
            e.printStackTrace();
        }

        ContactsListAdapter contactsListAdapter = new ContactsListAdapter(this, R.layout.line_item_con,contacts);
        ListView lv = (ListView)findViewById(R.id.con_list_view);
        if (lv != null)
        {
            lv.setAdapter(contactsListAdapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    Intent intent = new Intent(ContactsActivity.this,ContactsDetailViewActivity.class);
                    try {
                        intent.putExtra(CONTACT_NICKNAME,contacts.get(position).getNickname());
                    } catch (SharkKBException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.contacts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
