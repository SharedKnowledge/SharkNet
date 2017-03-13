package net.sharksystem.sharknet.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.impl.SharkNetEngine;
import net.sharksystem.api.interfaces.Chat;
import net.sharksystem.sharknet.NavigationDrawerActivity;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.SharkApp;

import java.util.List;

public class ChatActivity extends NavigationDrawerActivity implements SharkNetEngine.EventListener {

    public static final String CHAT_ID = "CHAT_ID";
    private List<net.sharksystem.api.interfaces.Chat> chats;
    private ChatListAdapter chatListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutResource(R.layout.chat_activity);
        setTitle("Chats");

        startBackgroundTask("Chats werden geladen");
    }

    @Override
    protected boolean doInBackground() {

        try {
            chats = SharkNetEngine.getSharkNet().getChats();
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void doWhenFinished(boolean success) {

        this.chatListAdapter = new ChatListAdapter(this, R.layout.chat_line_item, chats);
        ListView lv = (ListView) findViewById(R.id.chatsListView);

        if (lv != null) {
            lv.setAdapter(chatListAdapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(ChatActivity.this, ChatDetailActivity.class);
                    ((SharkApp) getApplication()).setChat(chats.get(position));
                    //identifies the chat for the detailView
//                    try {
//                        intent.putExtra(CHAT_ID, chats.get(position).getID());
//                    } catch (SharkKBException e) {
//                        e.printStackTrace();
//                    }
                    startActivity(intent);
                }
            });
        }

        FloatingActionButton fab = activateFloatingActionButton();
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChatActivity.this, ChatNewActivity.class));
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO needs to bea renewed?
//        startBackgroundTask("Chats werden geladen");
    }

    @Override
    public void onNewChat(Chat chat) {
        try {
            chats = SharkNetEngine.getSharkNet().getChats();
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
        this.chatListAdapter = new ChatListAdapter(this, R.layout.chat_line_item, chats);
        ListView lv = (ListView) findViewById(R.id.chatsListView);

        if (lv != null) {
            lv.setAdapter(chatListAdapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(ChatActivity.this, ChatDetailActivity.class);
                    //identifies the chat for the detailView
                    try {
                        intent.putExtra(CHAT_ID, chats.get(position).getID());
                    } catch (SharkKBException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                }
            });
        }
    }
}
