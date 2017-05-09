package net.sharksystem.sharknet.pki;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import net.sharkfw.knowledgeBase.SharkCSAlgebra;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.security.PkiStorage;
import net.sharkfw.security.SharkCertificate;
import net.sharkfw.system.L;
import net.sharksystem.api.dao_impl.SharkNetApiImpl;
import net.sharksystem.sharknet.NavigationDrawerActivity;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.RxSingleNavigationDrawerActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by j4rvis on 2/11/17.
 */

public class PKIActivity
        extends RxSingleNavigationDrawerActivity<List<PKICertificateHolder>>
        implements AdapterView.OnItemClickListener {

    private PKIListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutResource(R.layout.pki_activity);
        setOptionsMenu(R.menu.pki);
        L.setLogLevel(L.LOGLEVEL_ALL);
        setProgressMessage("Lade Zertifikate...");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNavigationView.setCheckedItem(R.id.sidenav_pki);
    }

    @Override
    protected List<PKICertificateHolder> doOnBackgroundThread() throws Exception {
        PkiStorage pkiStorage = mApi.getSharkEngine().getPKIStorage();

        List<SharkCertificate> certificates = pkiStorage.getAllSharkCertificates();
        return mapCertificates(certificates);
    }

    @Override
    protected void doOnUIThread(List<PKICertificateHolder> pkiCertificateHolders) {
        mAdapter = new PKIListAdapter(getSharkApp());
        mAdapter.updateItems(pkiCertificateHolders);
        ListView listView = (ListView) findViewById(R.id.list_view_certificates);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    protected void doOnError(Throwable error) {
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        super.onServiceConnected(name, service);
    }

    private List<PKICertificateHolder> mapCertificates(List<SharkCertificate> certificates) {
        List<PKICertificateHolder> holderList = new ArrayList<>();

        for (SharkCertificate nextCertificate : certificates) {
            if (holderList.isEmpty()) {
                PKICertificateHolder certificateHolder = new PKICertificateHolder();
                certificateHolder.addCertificate(nextCertificate);
                holderList.add(certificateHolder);
            } else {

                boolean certificateAdded = false;

                for (PKICertificateHolder nextHolder : holderList) {
                    if (SharkCSAlgebra.identical(nextHolder.getOwner(), nextCertificate.getOwner())) {
                        nextHolder.addCertificate(nextCertificate);
                        certificateAdded = true;
                        break;
                    }
                }

                if (!certificateAdded) {
                    PKICertificateHolder certificateHolder = new PKICertificateHolder();
                    certificateHolder.addCertificate(nextCertificate);
                    holderList.add(certificateHolder);
                }
            }
        }
        return holderList;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PKIDataHolder.getInstance().setHolder((PKICertificateHolder) mAdapter.getItem(position));

        Intent intent = new Intent(this, PKIDetailActivity.class);
        startActivity(intent);
    }
}
