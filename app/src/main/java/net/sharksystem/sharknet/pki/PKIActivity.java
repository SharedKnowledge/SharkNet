package net.sharksystem.sharknet.pki;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import net.sharkfw.security.PkiStorage;
import net.sharksystem.api.impl.SharkNetEngine;
import net.sharksystem.api.shark.peer.AndroidSharkEngine;
import net.sharksystem.sharknet.NavigationDrawerActivity;
import net.sharksystem.sharknet.R;

/**
 * Created by j4rvis on 2/11/17.
 */

public class PKIActivity extends NavigationDrawerActivity {

    private PkiStorage pkiStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutResource(R.layout.pki_activity);
        setOptionsMenu(R.menu.pki);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Certificates"));
        tabLayout.addTab(tabLayout.newTab().setText("Unsigned PublicKeys"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PageAdapter adapter = new PageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // Shark
        pkiStorage = SharkNetEngine.getSharkNet().getSharkEngine().getPKIStorage();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.generate_dummy_data:

                return true;
        }
        return false;
    }
}
