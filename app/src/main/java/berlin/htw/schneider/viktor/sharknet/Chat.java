package berlin.htw.schneider.viktor.sharknet;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import net.sharkfw.knowledgeBase.SharkKBException;

import java.util.List;

public class Chat extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{

    public static final String CHAT_ID ="CHAT_ID" ;
    private List<net.sharksystem.api.interfaces.Chat> chats;
    private ChatListAdapter chatListAdapter;

    @Override
    protected void onResume() {
        super.onResume();

        try {
            chats = MainActivity.implSharkNet.getChats();
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
        this.chatListAdapter = new ChatListAdapter(this, R.layout.line_item_chat,chats);
        ListView lv = (ListView)findViewById(R.id.chatsListView);

        if (lv != null)
        {
            lv.setAdapter(chatListAdapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    Intent intent = new Intent(Chat.this,ChatDetailActivity.class);
                    //identifies the chat for the detailView
                    intent.putExtra(CHAT_ID,chats.get(position).getID());
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(Chat.this,ChatNew.class));
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert drawer != null;
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        try {
            chats = MainActivity.implSharkNet.getChats();
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
        this.chatListAdapter = new ChatListAdapter(this, R.layout.line_item_chat,chats);
        ListView lv = (ListView)findViewById(R.id.chatsListView);

        if (lv != null)
        {
            lv.setAdapter(chatListAdapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    Intent intent = new Intent(Chat.this,ChatDetailActivity.class);
                    //identifies the chat for the detailView
                    intent.putExtra(CHAT_ID,chats.get(position).getID());
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id)
        {
            case R.id.chat:
                startActivity(new Intent(this, Chat.class));
                return true;
            case R.id.timeline:
                startActivity(new Intent(this, Timeline.class));
                return true;
            case R.id.contact:
                startActivity(new Intent(this, Contacts.class));
                return true;
            case R.id.profile:
                startActivity(new Intent(this, Profile.class));
                return true;
            case R.id.inbox:
                startActivity(new Intent(this, Inbox.class));
                return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
