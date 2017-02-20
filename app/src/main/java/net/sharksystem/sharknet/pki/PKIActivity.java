package net.sharksystem.sharknet.pki;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.security.PkiStorage;
import net.sharkfw.security.SharkCertificate;
import net.sharkfw.system.L;
import net.sharksystem.api.impl.SharkNetEngine;
import net.sharksystem.sharknet.NavigationDrawerActivity;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.dummy.Dummy;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Created by j4rvis on 2/11/17.
 */

public class PKIActivity extends NavigationDrawerActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private PkiStorage pkiStorage;
    private PKIListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutResource(R.layout.pki_activity);
        setOptionsMenu(R.menu.pki);

        L.setLogLevel(L.LOGLEVEL_ALL);

        pkiStorage = SharkNetEngine.getSharkNet().getSharkEngine().getPKIStorage();
        adapter = new PKIListAdapter();

        Button button = (Button) findViewById(R.id.button_reload_list);
        button.setOnClickListener(this);

        List<SharkCertificate> certificates = null;
        try {
            certificates = this.pkiStorage.getAllSharkCertificates();
            L.d("Certifcates: " + certificates.size(), this);
        } catch (SharkKBException e) {
            e.printStackTrace();
        }

        adapter.updateItems(certificates);

        ListView listView = (ListView) findViewById(R.id.list_view_certificates);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

//        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
//        tabLayout.addTab(tabLayout.newTab().setText("Certificates"));
//        tabLayout.addTab(tabLayout.newTab().setText("Unsigned PublicKeys"));
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
//        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//
//        // pass the listFragments to the pager
//        final PageAdapter adapter = new PageAdapter(getSupportFragmentManager());
//        adapter.addFragment(new CertificateListFragment());
//        adapter.addFragment(new PublicKeyListFragment());
//        viewPager.setAdapter(adapter);
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.generate_dummy_data:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Dummy.createDummyPkiData();
                    }
                }).start();
                item.setVisible(false);
                return true;
            case R.id.init_owner:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SharkNetEngine sharkNetEngine = SharkNetEngine.getSharkNet();
                        PkiStorage pkiStorage = sharkNetEngine.getSharkEngine().getPKIStorage();
                        try {
                            pkiStorage.setPkiStorageOwner(sharkNetEngine.getMyProfile().getPST());
                            pkiStorage.generateNewKeyPair();
                        } catch (SharkKBException | NoSuchAlgorithmException | IOException e) {
                            L.e(e.getMessage(), this);
                        }
                    }
                }).start();
                item.setVisible(false);
                return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        List<SharkCertificate> certificates = null;
        try {
            certificates = this.pkiStorage.getAllSharkCertificates();
            L.d("Certifcates: " + certificates.size(), this);
        } catch (SharkKBException e) {
            e.printStackTrace();
        }

        adapter.updateItems(certificates);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        List<SharkCertificate> certificates = null;
        try {
            certificates = this.pkiStorage.getAllSharkCertificates();
            L.d("Certifcates: " + certificates.size(), this);
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
        PKIDataHolder.getInstance().setCertificate(certificates.get(position));

        Intent intent = new Intent(this, CertificateDetailActivity.class);
        startActivity(intent);
    }
}
