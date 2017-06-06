package net.sharksystem.sharknet;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import net.sharkfw.system.L;
import net.sharksystem.api.dao_interfaces.SharkNetApi;
import net.sharksystem.api.service.SharkService;

/**
 * Created by mn-io on 22.01.16.
 */
public abstract class BaseActivity extends AppCompatActivity implements ServiceConnection {

    public static final int LAYOUT_OPTION_RESOURCE = 1;
    public static final int LAYOUT_OPTION_FRAGMENT = 2;
    public static final int LAYOUT_OPTION_NULL = -1;
    protected Menu menu;
    protected SharkNetApi mApi;
    private int layoutInUse = LAYOUT_OPTION_NULL;
    private Fragment usedFragment;
    private int optionsMenuResource = 0;
    private boolean mBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.setLogLevel(L.LOGLEVEL_ALL);
        setContentView(R.layout.system_parent_activity);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        // Make FloatingActionButton invisible at default
        findViewById(R.id.fab).setVisibility(View.GONE);
        Intent intent = new Intent(getApplicationContext(), SharkService.class);
        bindService(intent, this, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBound) {
            unbindService(this);
            mBound = false;
        }
    }

    public SharkApp getSharkApp() {
        return (SharkApp) getApplication();
    }

    protected void setToolbarTitle(String title) {
        ((Toolbar) findViewById(R.id.toolbar)).setTitle(title);
    }

    protected FloatingActionButton activateFloatingActionButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        return fab;
    }

    protected void setOptionsMenu(int resource) {
        optionsMenuResource = resource;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer!= null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (optionsMenuResource <= 0) {
            return super.onCreateOptionsMenu(menu);
        }
        getMenuInflater().inflate(optionsMenuResource, menu);
        this.menu = menu;
        return true;
    }

    protected void setLayoutResource(int resource) {
        checkIfLayoutIsUsed(LAYOUT_OPTION_RESOURCE);

        View includeContainer = findViewById(R.id.include);

        RelativeLayout rl = (RelativeLayout) includeContainer;
        ViewGroup rootView = (ViewGroup) includeContainer.getRootView();
        View inflate = getLayoutInflater().inflate(resource, rootView, false);
        rl.addView(inflate);
    }

    protected void setFragment(Fragment fragment) {
        checkIfLayoutIsUsed(LAYOUT_OPTION_FRAGMENT);
        usedFragment = fragment;

        getFragmentManager().beginTransaction().replace(R.id.include, fragment).commit();
    }

    protected void clearView() {
        layoutInUse = LAYOUT_OPTION_NULL;
        if (usedFragment != null) {
            getFragmentManager().beginTransaction().remove(usedFragment).commit();
            usedFragment = null;
        }
        RelativeLayout includeContainer = (RelativeLayout) findViewById(R.id.include);
        includeContainer.removeAllViews();
    }

    private void checkIfLayoutIsUsed(int layoutOption) {
        if (layoutInUse != LAYOUT_OPTION_NULL) {
            throw new IllegalStateException("Layout already set.");
        }
        layoutInUse = layoutOption;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mBound = true;
        SharkService sharkService = ((SharkService.LocalBinder) service).getService();
        mApi = sharkService.getApi();
        L.d("Service connected.", this);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mBound = false;
        L.d("Service disconnected.", this);
    }
}

