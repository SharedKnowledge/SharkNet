package net.sharksystem.sharknet.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.impl.SharkNetEngine;
import net.sharksystem.api.interfaces.Chat;
import net.sharksystem.sharknet.BaseActivity;
import net.sharksystem.sharknet.NavigationDrawerActivity;
import net.sharksystem.sharknet.ParentActivity;
import net.sharksystem.sharknet.R;

import java.util.List;

public class ChatActivity extends NavigationDrawerActivity implements SharkNetEngine.EventListener {

    public static final String CHAT_ID = "CHAT_ID";
    private List<net.sharksystem.api.interfaces.Chat> chats;
    private ChatListAdapter chatListAdapter;

    @Override
    protected void onResume() {
        super.onResume();

        try {
            chats = SharkNetEngine.getSharkNet().getChats();
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
        this.chatListAdapter = new ChatListAdapter(this, R.layout.line_item_chat, chats);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_chat);
        setLayoutResource(R.layout.content_chat);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChatActivity.this, ChatNewActivity.class));
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        try {
            chats = SharkNetEngine.getSharkNet().getChats();
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
        this.chatListAdapter = new ChatListAdapter(this, R.layout.line_item_chat, chats);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNewChat(Chat chat) {
        try {
            chats = SharkNetEngine.getSharkNet().getChats();
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
        this.chatListAdapter = new ChatListAdapter(this, R.layout.line_item_chat, chats);
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
