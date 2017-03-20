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

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.interfaces.Chat;
import net.sharksystem.api.interfaces.Message;
import net.sharksystem.sharknet.ParentActivity;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.SharkApp;

import org.json.JSONException;

import java.util.List;
import java.util.concurrent.Callable;

import rx.Single;
import rx.SingleSubscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by j4rvis on 3/5/17.
 */

public class ChatDetailActivity extends ParentActivity {
    private ChatDetailMsgListAdapter mAdapter;
    private Chat mChat;
    private RecyclerView mRecyclerView;
    private Subscription mSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutResource(R.layout.chat_detail_activity);
        setOptionsMenu(R.menu.chat_detail_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        createLayout();
        retrieveChat();
    }

    private void createLayout(){
        mRecyclerView = (RecyclerView) findViewById(R.id.chat_msg_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new ChatDetailMsgListAdapter(this, (SharkApp) getApplication());
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

    private void retrieveChat(){
        Single<List<Message>> single = Single.fromCallable(new Callable<List<Message>>() {
            @Override
            public List<Message> call() throws Exception {
                mChat = getSharkApp().getChat();
                setTitle(mChat.getTitle());
                return mChat.getMessages(false);
            }
        });

        mSubscription = single
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<List<Message>>() {

            @Override
            public void onSuccess(List<Message> value) {
                mAdapter.setMessages(value);
            }

            @Override
            public void onError(Throwable error) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mSubscription!=null && !mSubscription.isUnsubscribed()){
            mSubscription.unsubscribe();
        }
        if(mAdapter.mSubscription!=null && !mAdapter.mSubscription.isUnsubscribed()){
            mAdapter.mSubscription.unsubscribe();
        }
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
