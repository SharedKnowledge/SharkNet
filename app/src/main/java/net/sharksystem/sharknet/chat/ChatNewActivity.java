package net.sharksystem.sharknet.chat;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.system.L;
import net.sharksystem.api.impl.SharkNetEngine;
import net.sharksystem.api.interfaces.Chat;
import net.sharksystem.api.interfaces.Contact;
import net.sharksystem.sharknet.ParentActivity;
import net.sharksystem.sharknet.R;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ChatNewActivity extends ParentActivity {

    private List<Contact> contacts;
    private List<Contact> selected_contacts;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutResource(R.layout.chat_new_activity);
        setOptionsMenu(R.menu.menu_chat_new);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        selected_contacts = new ArrayList<>();
        try {
            this.contacts = SharkNetEngine.getSharkNet().getContacts();
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
        ChatNewConListAdapter chatListAdapter = new ChatNewConListAdapter(this, R.layout.chat_new_contact_line_item, contacts);
        lv = (ListView) findViewById(R.id.con_list_view);
        lv.setAdapter(chatListAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                for (int i = 0; i < lv.getCount(); i++) {
                    View v = lv.getChildAt(i);
                    if(v!=null){
                        CheckBox ck = (CheckBox) v.findViewById(R.id.checkBox);
                        if (ck.isChecked()) {
                            selected_contacts.add(contacts.get(i));
                        }
                    }
                }
                if (!selected_contacts.isEmpty()) {
                    EditText title = (EditText) findViewById(R.id.chat_new_title);
                    try {
                        Chat chat = SharkNetEngine.getSharkNet().newChat(selected_contacts);
                        if (!title.getText().toString().isEmpty()) {
                            chat.setTitle(title.getText().toString());
                        }
                        getSharkApp().setChat(chat);
                    } catch (SharkKBException | JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(ChatNewActivity.this, ChatDetailActivity.class));
                } else {
                    Toast.makeText(this, "Kein Kontakt ausgewählt", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }


    }

}
