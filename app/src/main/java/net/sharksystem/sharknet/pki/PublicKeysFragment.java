package net.sharksystem.sharknet.pki;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import net.sharksystem.sharknet.R;

import java.util.ArrayList;

/**
 * Created by j4rvis on 2/12/17.
 */

public class PublicKeysFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pki_list_fragment, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        ListView listView = (ListView) getView().findViewById(R.id.list_view_pki);
        PKIPublicKeyListAdapter adapter = new PKIPublicKeyListAdapter();
        listView.setAdapter(adapter);

        ArrayList<Object> objects = new ArrayList<>();

        objects.add(new Object());
        objects.add(new Object());
        objects.add(new Object());
        objects.add(new Object());
        objects.add(new Object());
        objects.add(new Object());
        objects.add(new Object());
        objects.add(new Object());
        objects.add(new Object());
        objects.add(new Object());
        objects.add(new Object());
        objects.add(new Object());
        objects.add(new Object());

        adapter.updateItems(objects);
    }
}
