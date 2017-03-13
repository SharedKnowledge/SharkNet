package net.sharksystem.sharknet.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.impl.SharkNetEngine;
import net.sharksystem.api.interfaces.Contact;
import net.sharksystem.sharknet.ParentActivity;
import net.sharksystem.sharknet.R;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatDetailAddContactActivity extends ParentActivity {
    private List<Contact> contacts;
    private List<Contact> selected_contacts;
    private String chatID;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutResource(R.layout.chat_detail_add_contact_activity);
        setOptionsMenu(R.menu.menu_chat_detail_add_con);

        this.chatID = getIntent().getStringExtra(ChatActivity.CHAT_ID);

        selected_contacts = new ArrayList<>();
        try {
            this.contacts = SharkNetEngine.getSharkNet().getContactsWithoutMe();
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
        // TODO: Kontakte die bereits im ChatActivity sind sollen rausgenommen werden durch ChatID!!
//        TODO: nicht rausnehmen sondern kennzeichnen mit icon dass sie schon drin sind

        ChatNewConListAdapter chatListAdapter = new ChatNewConListAdapter(this, R.layout.chat_new_contact_line_item, contacts);
        lv = (ListView) findViewById(R.id.con_list_view);
        if (lv != null) {
            lv.setAdapter(chatListAdapter);
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

    @Override
    public void onBackPressed() {
        Intent returnChatIDIntent = getIntent();
        returnChatIDIntent.putExtra(ChatActivity.CHAT_ID, this.chatID);
        setResult(Activity.RESULT_OK, returnChatIDIntent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                View v;
                CheckBox ck;
                for (int i = 0; i < lv.getCount(); i++) {
                    v = lv.getChildAt(i);
                    ck = (CheckBox) v.findViewById(R.id.checkBox);
                    if (ck.isChecked()) {
                        selected_contacts.add(contacts.get(i));

                        try {
                            Log.d("CHECKED", contacts.get(i).getNickname());
                        } catch (SharkKBException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (!selected_contacts.isEmpty()) {
                    try {
                        for (net.sharksystem.api.interfaces.Chat chat : SharkNetEngine.getSharkNet().getChats()) {
                            if (Objects.equals(chatID, chat.getID())) {
                                for (Contact c : selected_contacts) {
                                    chat.addContact(c);
                                    chat.sendMessage(null, c.getNickname() + " was added.", "contact");
                                }
                            }
                        }
                    } catch (SharkKBException | JSONException e) {
                        e.printStackTrace();
                    }

                    Intent returnChatIDIntent = getIntent();
                    returnChatIDIntent.putExtra(ChatActivity.CHAT_ID, chatID);
                    setResult(Activity.RESULT_OK, returnChatIDIntent);
                    finish();
//                    startActivity(new Intent( ChatDetailAddContactActivity.this, ChatObsoleteDetailActivity.class ));
                }

                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }


}
