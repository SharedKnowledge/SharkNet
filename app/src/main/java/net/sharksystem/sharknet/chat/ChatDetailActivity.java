package net.sharksystem.sharknet.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.impl.SharkNetEngine;
import net.sharksystem.api.interfaces.Chat;
import net.sharksystem.sharknet.ParentActivity;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.SharkApp;

import org.json.JSONException;

/**
 * Created by j4rvis on 3/5/17.
 */

public class ChatDetailActivity extends ParentActivity {
    private ChatDetailMsgListAdapter mAdapter;
    private Chat mChat;
    public static final String CHAT_ID = "CHAT_ID";
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutResource(R.layout.chat_detail_activity);
        setOptionsMenu(R.menu.chat_detail_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            mChat = getSharkApp().getChat();
//            mChat = SharkNetEngine.getSharkNet().getChatById(chatID);
            mAdapter = new ChatDetailMsgListAdapter(this, (SharkApp) getApplication(), mChat.getMessages(false));
            setTitle(mChat.getTitle());
        } catch (SharkKBException e) {
            e.printStackTrace();
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.chat_msg_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
//        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mRecyclerView.setAdapter(mAdapter);

        final CardView sendButton = (CardView) findViewById(R.id.message_send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.message_edit_text);

                String msg_string;

                if (editText != null) {
                    msg_string = editText.getText().toString().trim();

                    if (msg_string.isEmpty()) {
                        Snackbar.make(sendButton, "No message entered!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } else {
                        try {
                            mChat.sendMessage(msg_string);
                            editText.getText().clear();
                            mAdapter.setMessages(mChat.getMessages(false));
                            mRecyclerView.scrollToPosition(mAdapter.getItemCount()-1);
                        } catch (JSONException | SharkKBException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        getSharkApp().resetChat();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Reset clicked data
                getSharkApp().resetChat();
                this.finish();
                return true;
            case R.id.chat_attachment_take_photo:
                return true;
            case R.id.chat_settings:
                startActivity(new Intent(this, ChatSettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
