package berlin.htw.schneider.viktor.sharknet;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
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
import net.sharksystem.api.interfaces.Feed;
import java.util.List;

public class Inbox extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{


    private List<Feed> feeds;
    public TimelineListAdapter timelineListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        try {
            this.feeds = MainActivity.implSharkNet.getFeeds(true);
        } catch (SharkKBException e) {
            e.printStackTrace();
        }

        this.timelineListAdapter = new TimelineListAdapter(this, R.layout.line_item_timeline,feeds);
        ListView feeds_liste = (ListView) findViewById(R.id.feeds_listView);
        if (feeds_liste != null)
        {
            feeds_liste.setAdapter(timelineListAdapter);
            feeds_liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    Intent intent = new Intent(Inbox.this,Timeline.class);
                    startActivity(intent);
                }
            });
        }


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
/*
        //TODO: noch nicht klar wie ich jedes menuitem ansprechen kann
        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/RockSalt.TTF");
        MenuItem mi = (MenuItem) findViewById(R.id.inbox);
        assert mi != null;
        TextView t = (TextView) mi.getActionView();
        t.setTypeface(type);
*/
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
