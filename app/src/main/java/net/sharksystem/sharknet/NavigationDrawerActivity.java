package net.sharksystem.sharknet;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.impl.SharkNetEngine;
import net.sharksystem.sharknet.chat.ChatActivity;
import net.sharksystem.sharknet.contact.ContactsActivity;
import net.sharksystem.sharknet.nfc.NFCActivity;
import net.sharksystem.sharknet.pki.PKIActivity;
import net.sharksystem.sharknet.profile.ProfileActivity;
import net.sharksystem.sharknet.radar.RadarActivity;

/**
 * Created by j4rvis on 1/31/17.
 */

public abstract class NavigationDrawerActivity extends ParentActivity implements NavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.system_navigation_drawer_activity);
        // Make FloatingActionButton invisible at default
        findViewById(R.id.fab).setVisibility(View.GONE);
        installActionBarAndSideNavDrawer();
    }

    private Menu installActionBarAndSideNavDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.sidenav_drawer_open, R.string.sidenav_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.sidenav_view);
        TextView textView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.me);
        try {
            textView.setText(SharkNetEngine.getSharkNet().getMyProfile().getName());
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
        navigationView.setNavigationItemSelectedListener(this);
        return navigationView.getMenu();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case R.id.sidenav_chat:
                startActivity(new Intent(this, ChatActivity.class));
                return true;
            case R.id.sidenav_contact:
                startActivity(new Intent(this, ContactsActivity.class));
                return true;
            case R.id.sidenav_profile:
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            case R.id.sidenav_radar:
                startActivity(new Intent(this, RadarActivity.class));
                return true;
            case R.id.sidenav_nfc:
                startActivity(new Intent(this, NFCActivity.class));
                return true;
            case R.id.sidenav_pki:
                startActivity(new Intent(this, PKIActivity.class));
                return true;
            default:
                break;

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
