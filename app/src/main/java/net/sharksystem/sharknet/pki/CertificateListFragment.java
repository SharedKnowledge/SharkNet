package net.sharksystem.sharknet.pki;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import net.sharkfw.security.SharkCertificate;
import net.sharksystem.sharknet.R;

import java.util.ArrayList;

/**
 * Created by j4rvis on 2/12/17.
 */

public class CertificateListFragment extends ListFragment {

    private final ArrayList<SharkCertificate> certificates = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.pki_list_fragment, container, false);

        CertificateListAdapter adapter = new CertificateListAdapter();

        adapter.updateItems(certificates);

        setListAdapter(adapter);

        return rootView;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        PKIDataHolder.getInstance().setCertificate(this.certificates.get(position));

        Intent intent = new Intent(getContext(), CertificateDetailActivity.class);
        startActivity(intent);
    }
}
