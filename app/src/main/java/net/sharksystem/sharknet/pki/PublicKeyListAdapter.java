package net.sharksystem.sharknet.pki;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.sharksystem.sharknet.R;

import static net.sharksystem.api.shark.Application.getContext;

/**
 * Created by j4rvis on 2/12/17.
 */

public class PublicKeyListAdapter extends RecyclerView.Adapter<PublicKeyListViewHolder> {
    @Override
    public PublicKeyListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.pki_public_key_line_item, parent, false);
        return new PublicKeyListViewHolder(root);
    }

    @Override
    public void onBindViewHolder(PublicKeyListViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
