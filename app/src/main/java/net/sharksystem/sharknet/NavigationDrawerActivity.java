package net.sharksystem.sharknet;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.sharkfw.asip.ASIPInterest;
import net.sharkfw.asip.ASIPSpace;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharksystem.api.dao_impl.SharkNetApiImpl;
import net.sharksystem.api.models.Broadcast;
import net.sharksystem.api.models.Chat;
import net.sharksystem.api.models.Profile;
import net.sharksystem.api.utils.SharkNetUtils;
import net.sharksystem.sharknet.account.AccountDetailActivity;
import net.sharksystem.sharknet.broadcast.BroadcastActivity;
import net.sharksystem.sharknet.chat.ChatActivity;
import net.sharksystem.sharknet.contact.ContactActivity;
import net.sharksystem.sharknet.nfc.NFCActivity;
import net.sharksystem.sharknet.pki.PKIActivity;
import net.sharksystem.sharknet.radar.RadarActivity;
import net.sharksystem.sharknet.schnitzeljagd.SchnitzeljagdMainActivity;

/**
 * Created by j4rvis on 1/31/17.
 */

public abstract class NavigationDrawerActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, DrawerLayout.DrawerListener {

    private boolean mDrawerOpen;
    private DrawerLayout mDrawer;
    protected NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.system_navigation_drawer_activity);
        // Make FloatingActionButton invisible at default
        findViewById(R.id.fab).setVisibility(View.GONE);
        installActionBarAndSideNavDrawer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mDrawerOpen){
            mDrawer.closeDrawer(GravityCompat.START, false);
        }
    }
    private void installActionBarAndSideNavDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.sidenav_drawer_open, R.string.sidenav_drawer_close);
        mDrawer.addDrawerListener(toggle);
        mDrawer.addDrawerListener(this);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.sidenav_view);
        TextView textView = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.me);
//        textView.setText(getSharkApp().getAccount().getName());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AccountDetailActivity.class));
            }
        });
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        super.onServiceConnected(name, service);

        TextView textView = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.me);
        textView.setText(mApi.getAccount().getName());

        ImageView imageView = (ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.round_image);
        imageView.setImageBitmap(mApi.getAccount().getImage());
        imageView.setPadding(0, 0, 0, 0);}

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        Intent intent = null;

        switch (id) {
            case R.id.sidenav_chat:
                if(this instanceof ChatActivity) closeDrawer();
                intent = new Intent(this, ChatActivity.class);
                break;
            case R.id.sidenav_contact:
                if(this instanceof ContactActivity) closeDrawer();
                intent = new Intent(this, ContactActivity.class);
                break;
            case R.id.sidenav_radar:
                if(this instanceof RadarActivity) closeDrawer();
                intent = new Intent(this, RadarActivity.class);
                break;
            case R.id.sidenav_nfc:
                if(this instanceof NFCActivity) closeDrawer();
                intent = new Intent(this, NFCActivity.class);
                break;
            case R.id.sidenav_pki:
                if(this instanceof PKIActivity) closeDrawer();
                intent = new Intent(this, PKIActivity.class);
                break;
            case R.id.sidenav_semantic_broadcast:
                if(this instanceof BroadcastActivity) closeDrawer();
                Profile profile = mApi.getProfile(null);
                if (profile.getActiveEntryInterest() != null) {
                    System.out.println("________________Entry Profile in Manager_____________");
                    mApi.getSharkEngine().getBroadcastManager().setActiveEntryProfile(profile.getActiveEntryInterest());
                }
                if (profile.getActiveOutInterest() != null) {
                    mApi.getSharkEngine().getBroadcastManager().setActiveOutProfile(profile.getActiveOutInterest());
                }
                getSharkApp().setBroadcast(mApi.getBroadcast());
                intent = new Intent(this, BroadcastActivity.class);
                break;
            case R.id.sidenav_schnitzeljagd:
                //if(this instanceof SchnitzeljagdMainActivity) closeDrawer();
                intent = new Intent(this, SchnitzeljagdMainActivity.class);
                break;
            default:
                break;
        }
        if(intent != null){
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
        return true;
    }

    private void closeDrawer(){
        mDrawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {
        mDrawerOpen = true;
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        mDrawerOpen = false;
    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }
}
