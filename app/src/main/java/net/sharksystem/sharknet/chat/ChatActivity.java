package net.sharksystem.sharknet.chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import net.sharksystem.api.impl.SharkNetEngine;
import net.sharksystem.api.interfaces.Chat;
import net.sharksystem.sharknet.NavigationDrawerActivity;
import net.sharksystem.sharknet.R;

import java.util.List;
import java.util.concurrent.Callable;

import rx.Single;
import rx.SingleSubscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChatActivity extends NavigationDrawerActivity {

    private ChatListAdapter mChatListAdapter;
    private RecyclerView mChatRecyclerView;
    private Subscription mSubscription;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureLayout();

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Lade Chats...");
        mProgressDialog.show();

        retrieveChats();
    }

    private void configureLayout() {
        setLayoutResource(R.layout.chat_activity);
        setTitle("Chats");
        mChatListAdapter = new ChatListAdapter(this, getSharkApp());
        mChatRecyclerView = (RecyclerView) findViewById(R.id.chats_recylcer_view);
        mChatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mChatRecyclerView.setAdapter(mChatListAdapter);

        activateFloatingActionButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChatActivity.this, ChatNewActivity.class));
            }
        });
    }

    private void retrieveChats() {
        Single<List<Chat>> single = Single.fromCallable(new Callable<List<Chat>>() {
            @Override
            public List<Chat> call() throws Exception {
                return SharkNetEngine.getSharkNet().getChats();
            }
        });

        mSubscription = single.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleSubscriber<List<Chat>>() {
            @Override
            public void onSuccess(List<Chat> value) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                mChatListAdapter.setChats(value);
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }

        if (mChatListAdapter.mSubscription != null && !mChatListAdapter.mSubscription.isUnsubscribed()) {
            mChatListAdapter.mSubscription.unsubscribe();
        }
    }
}
