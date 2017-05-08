package net.sharksystem.sharknet.chat;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import net.sharkfw.knowledgeBase.SharkCSAlgebra;
import net.sharkfw.knowledgeBase.SharkKB;
import net.sharkfw.knowledgeBase.sync.manager.SyncComponent;
import net.sharkfw.knowledgeBase.sync.manager.port.SyncMergeKP;
import net.sharksystem.api.dao_impl.SharkNetApiImpl;
import net.sharksystem.api.models.Chat;
import net.sharksystem.api.models.Contact;
import net.sharksystem.api.models.Message;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.RxSingleBaseActivity;

import java.util.List;

/**
 * Created by j4rvis on 3/5/17.
 */

public class ChatDetailActivity extends RxSingleBaseActivity<List<Message>> implements SyncMergeKP.SyncMergeListener {
    private ChatDetailMsgListAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private Chat mChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureLayout();
        if (mChat == null) {
            mChat = getSharkApp().getChat();
        }
        // TODO what if we reload the messages?
        List<Contact> contacts = mChat.getContacts();
        contacts.remove(getSharkApp().getAccount());

        if (mChat.getTitle() != null) {
            setTitle(mChat.getTitle());
        } else if (contacts.size() == 1) {
            setTitle(contacts.get(0).getName());
        }

        setProgressMessage(R.string.chat_progress_load_messages);
    }

    @Override
    public void onBackPressed() {
        getSharkApp().resetChat();
        super.onBackPressed();
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        super.onServiceConnected(name, service);
        mApi.getSharkEngine().getSyncManager().addSyncMergeListener(this);
    }

    private void configureLayout() {
        setLayoutResource(R.layout.chat_detail_activity);
        setOptionsMenu(R.menu.chat_detail_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecyclerView = (RecyclerView) findViewById(R.id.chat_msg_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new ChatDetailMsgListAdapter(this, getSharkApp());
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
                        Snackbar.make(sendButton, "No message entered!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    } else {
                        Message message = new Message(mApi.getAccount());
                        message.setContent(msg_string);
                        mChat.addMessage(message);
                        // TODO Save message
                        mApi.updateChat(mChat);
                        editText.getText().clear();
                        startSubscription();
                        mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
                    }
                }
            }
        });
    }

    @Override
    protected List<Message> doOnBackgroundThread() throws Exception {
        return mChat.getMessages();
    }

    @Override
    protected void doOnUIThread(List<Message> list) {
        mAdapter.setMessages(list);
    }

    @Override
    protected void doOnError(Throwable error) {
        error.printStackTrace();
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

    @Override
    public void onNewMerge(SyncComponent component, SharkKB changes) {
        if(SharkCSAlgebra.identical(mChat.getId(), component.getUniqueName())){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    startSubscription();
                }
            });
        }
    }
}
