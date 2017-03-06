package net.sharksystem.sharknet.chat;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.impl.SharkNetEngine;
import net.sharksystem.api.interfaces.Chat;
import net.sharksystem.sharknet.ParentActivity;
import net.sharksystem.sharknet.R;

/**
 * Created by j4rvis on 3/5/17.
 */

public class ChatDetailActivity extends ParentActivity {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private ChatDetailMsgListAdapter mAdapter;
    private String chatID;

    public static final String CHAT_ID = "CHAT_ID";

    @Override
    protected boolean doInBackground() {
        return false;
    }

    @Override
    protected void doWhenFinished(boolean success) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutResource(R.layout.chat_detail_activity);
        setOptionsMenu(R.menu.chat_detail);

        if (savedInstanceState != null) {
            // Restore value of members from saved state
            chatID = savedInstanceState.getString(CHAT_ID);
        } else {
            chatID = getIntent().getStringExtra(ChatActivity.CHAT_ID);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.chat_msg_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        try {
            Chat chatById = SharkNetEngine.getSharkNet().getChatById(chatID);
            mAdapter = new ChatDetailMsgListAdapter(chatById.getMessages(true));
        } catch (SharkKBException e) {
            e.printStackTrace();
        }

        // specify an adapter (see also next example)
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(CHAT_ID, getIntent().getStringExtra(ChatActivity.CHAT_ID));
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance
        chatID = savedInstanceState.getString(CHAT_ID);
    }
}
