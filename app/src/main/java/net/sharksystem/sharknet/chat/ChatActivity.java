package net.sharksystem.sharknet.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.system.L;
import net.sharksystem.api.dao_impl.SharkNetApiImpl;
import net.sharksystem.api.models.Chat;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.RxSingleNavigationDrawerActivity;

import java.util.List;

public class ChatActivity extends RxSingleNavigationDrawerActivity<List<Chat>> {

    private ChatListAdapter mChatListAdapter;
    private boolean mIsRefreshing;
    private SwipeRefreshLayout mRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureLayout();

        setProgressMessage(R.string.chat_progress_load_chats);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNavigationView.setCheckedItem(R.id.sidenav_chat);
    }

    private void configureLayout() {
        setLayoutResource(R.layout.chat_activity);
        setTitle("Chats");
        mChatListAdapter = new ChatListAdapter(this, getSharkApp());
        RecyclerView mChatRecyclerView = (RecyclerView) findViewById(R.id.chats_recylcer_view);
        mChatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mChatRecyclerView.setAdapter(mChatListAdapter);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mIsRefreshing = true;
                L.d("Start refresh", this);
                startSubscription();
            }
        });

        activateFloatingActionButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChatActivity.this, ChatNewActivity.class));
            }
        });
    }

    @Override
    protected List<Chat> doOnBackgroundThread() throws SharkKBException {
        return mApi.getChats();
    }

    @Override
    protected void doOnUIThread(List<Chat> chats) {
        mChatListAdapter.setChats(chats);
        if(mIsRefreshing){
            mRefreshLayout.setRefreshing(false);
            mIsRefreshing = false;
        }
    }

    @Override
    protected void doOnError(Throwable error) {
        error.printStackTrace();
    }
}
