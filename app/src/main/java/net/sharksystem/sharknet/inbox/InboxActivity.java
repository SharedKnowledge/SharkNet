package net.sharksystem.sharknet.inbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.impl.SharkNetEngine;
import net.sharksystem.api.interfaces.Feed;
import net.sharksystem.sharknet.NavigationDrawerActivity;
import net.sharksystem.sharknet.R;

import java.util.List;

public class InboxActivity extends NavigationDrawerActivity {
    private List<Feed> feeds;
    public InboxListAdapter inboxListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutResource(R.layout.content_timeline);
        setOptionsMenu(R.menu.inbox);

        FloatingActionButton fab = activateFloatingActionButton();
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InboxActivity.this, InboxNewFeedActivity.class);
                startActivity(intent);
            }
        });

        try {
            this.feeds = SharkNetEngine.getSharkNet().getFeeds(true);
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
        this.inboxListAdapter = new InboxListAdapter(this, R.layout.line_item_timeline, feeds);
        ListView feeds_liste = (ListView) findViewById(R.id.feeds_listView);
        if (feeds_liste != null) {
            feeds_liste.setAdapter(inboxListAdapter);
            feeds_liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Intent intent = new Intent(InboxActivity.this,InboxNewFeedActivity.class);
                    //startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            this.feeds = SharkNetEngine.getSharkNet().getFeeds(true);
        } catch (SharkKBException e) {
            e.printStackTrace();
        }

        this.inboxListAdapter = new InboxListAdapter(this, R.layout.line_item_timeline, feeds);
        ListView feeds_liste = (ListView) findViewById(R.id.feeds_listView);
        if (feeds_liste != null) {
            feeds_liste.setAdapter(inboxListAdapter);
            feeds_liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Intent intent = new Intent(InboxActivity.this,InboxNewFeedActivity.class);
                    //startActivity(intent);
                }
            });
        }
    }
}
