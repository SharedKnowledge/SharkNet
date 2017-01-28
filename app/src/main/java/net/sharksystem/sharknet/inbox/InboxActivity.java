package net.sharksystem.sharknet.inbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.impl.SharkNetEngine;
import net.sharksystem.api.interfaces.Feed;
import net.sharksystem.sharknet.BaseActivity;
import net.sharksystem.sharknet.R;

import java.util.List;

public class InboxActivity extends BaseActivity {
    private List<Feed> feeds;
    public InboxListAdapter inboxListAdapter;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_timeline);
        super.onCreate(savedInstanceState);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.inbox, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
