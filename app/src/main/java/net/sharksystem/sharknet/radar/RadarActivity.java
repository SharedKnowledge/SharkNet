package net.sharksystem.sharknet.radar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.impl.SharkNetEngine;
import net.sharksystem.api.interfaces.Chat;
import net.sharksystem.api.interfaces.Contact;
import net.sharksystem.api.interfaces.RadarListener;
import net.sharksystem.sharknet.NavigationDrawerActivity;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.chat.ChatDetailActivity;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class RadarActivity extends NavigationDrawerActivity implements RadarListener {

    public static final String CHAT_ID = "CHAT_ID";

    private RadarListAdapter mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutResource(R.layout.radar_activity);
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

        SharkNetEngine.getSharkNet().addRadarListener(this);
        ListView listView = (ListView) findViewById(R.id.radar_list_view);
        mListAdapter = new RadarListAdapter(this);
        listView.setAdapter(mListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Contact contact = mListAdapter.getItem(position);

                ArrayList<Contact> recipients = new ArrayList<>();
                recipients.add(contact);
                try {
                    recipients.add(SharkNetEngine.getSharkNet().getMyProfile());
                } catch (SharkKBException e) {
                    e.printStackTrace();
                }

                try {
                    Chat chat = SharkNetEngine.getSharkNet().newChat(recipients);
                    Intent intent = new Intent(RadarActivity.this, ChatDetailActivity.class);
                    intent.putExtra(CHAT_ID, chat.getID());
                    startActivity(intent);
                } catch (SharkKBException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onNewRadarContact(List<Contact> contacts) {
        mListAdapter.updateList((ArrayList<Contact>) contacts);
    }
}
