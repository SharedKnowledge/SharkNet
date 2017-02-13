package net.sharksystem.sharknet.pki;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import net.sharkfw.system.L;
import net.sharksystem.sharknet.R;

import java.util.ArrayList;

/**
 * Created by j4rvis on 2/12/17.
 */

public class PublicKeyListFragment extends ListFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.pki_list_fragment, container, false);

        PublicKeyListAdapter adapter = new PublicKeyListAdapter();

        final ArrayList<Object> objects = new ArrayList<>();

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

        setListAdapter(adapter);

        return rootView;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        L.d("clicked", this);
        Intent intent = new Intent(getContext(), PublicKeyDetailActivity.class);
        startActivity(intent);
    }
}
