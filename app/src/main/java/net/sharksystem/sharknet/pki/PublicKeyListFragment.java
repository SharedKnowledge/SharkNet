package net.sharksystem.sharknet.pki;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.security.PkiStorage;
import net.sharkfw.security.SharkPublicKey;
import net.sharkfw.system.L;
import net.sharksystem.api.impl.SharkNetEngine;
import net.sharksystem.sharknet.R;

import java.util.List;

/**
 * Created by j4rvis on 2/12/17.
 */

public class PublicKeyListFragment extends ListFragment implements View.OnClickListener {

    private PkiStorage pkiStorage;
    private PublicKeyListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.pki_list_fragment, container, false);

        pkiStorage = SharkNetEngine.getSharkNet().getSharkEngine().getPKIStorage();
        adapter = new PublicKeyListAdapter();

        Button button = (Button) rootView.findViewById(R.id.button_reload_list);
        button.setOnClickListener(this);

        List<SharkPublicKey> unsignedPublicKeys = null;
        try {
            unsignedPublicKeys = this.pkiStorage.getUnsignedPublicKeys();
            L.d("Keys: " + unsignedPublicKeys.size(), this);
        } catch (SharkKBException e) {
            e.printStackTrace();
        }

        adapter.updateItems(unsignedPublicKeys);

        setListAdapter(adapter);

        return rootView;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        List<SharkPublicKey> unsignedPublicKeys = null;
        try {
            unsignedPublicKeys = this.pkiStorage.getUnsignedPublicKeys();
        } catch (SharkKBException e) {
            e.printStackTrace();
        }

        PKIDataHolder.getInstance().setPublicKey(unsignedPublicKeys.get(position));

        Intent intent = new Intent(getContext(), PublicKeyDetailActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        List<SharkPublicKey> unsignedPublicKeys = null;
        try {
            unsignedPublicKeys = this.pkiStorage.getUnsignedPublicKeys();
            L.d("Keys: " + unsignedPublicKeys.size(), this);
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
        adapter.updateItems(unsignedPublicKeys);
    }
}
