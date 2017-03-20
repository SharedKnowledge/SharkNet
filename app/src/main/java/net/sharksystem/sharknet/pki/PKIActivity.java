package net.sharksystem.sharknet.pki;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import net.sharkfw.knowledgeBase.SharkCSAlgebra;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.security.PkiStorage;
import net.sharkfw.security.SharkCertificate;
import net.sharkfw.system.L;
import net.sharksystem.api.impl.SharkNetEngine;
import net.sharksystem.sharknet.NavigationDrawerActivity;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.dummy.Dummy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by j4rvis on 2/11/17.
 */

public class PKIActivity extends NavigationDrawerActivity implements AdapterView.OnItemClickListener {

    private PkiStorage pkiStorage;
    private PKIListAdapter adapter;
    private List<PKICertificateHolder> holderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutResource(R.layout.pki_activity);
        setOptionsMenu(R.menu.pki);

        L.setLogLevel(L.LOGLEVEL_ALL);
        pkiStorage = SharkNetEngine.getSharkNet().getSharkEngine().getPKIStorage();

        List<SharkCertificate> certificates = null;
        try {
            certificates = this.pkiStorage.getAllSharkCertificates();
            L.d("Certifcates: " + certificates.size(), this);
            if(certificates.isEmpty()){
//                Dummy.createDummyPkiData();
                certificates = this.pkiStorage.getAllSharkCertificates();
            }
        } catch (SharkKBException e) {
            e.printStackTrace();
        }

        holderList = mapCertificates(certificates);
        adapter = new PKIListAdapter();
        adapter.updateItems(holderList);

        ListView listView = (ListView) findViewById(R.id.list_view_certificates);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    private List<PKICertificateHolder> mapCertificates(List<SharkCertificate> certificates) {
        List<PKICertificateHolder> holderList = new ArrayList<>();

        Iterator<SharkCertificate> certificateIterator = certificates.iterator();
        while (certificateIterator.hasNext()) {
            SharkCertificate nextCertificate = certificateIterator.next();

            if (holderList.isEmpty()) {
                PKICertificateHolder certificateHolder = new PKICertificateHolder();
                certificateHolder.addCertificate(nextCertificate);
                holderList.add(certificateHolder);
            } else {

                boolean certificateAdded = false;

                Iterator<PKICertificateHolder> holderIterator = holderList.iterator();
                while (holderIterator.hasNext()) {
                    PKICertificateHolder nextHolder = holderIterator.next();

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
        PKIDataHolder.getInstance().setHolder(this.holderList.get(position));

        Intent intent = new Intent(this, PKIDetailActivity.class);
        startActivity(intent);
    }
}
