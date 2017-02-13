package net.sharksystem.sharknet.pki;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import net.sharksystem.sharknet.R;

/**
 * Created by j4rvis on 2/12/17.
 */

public class PublicKeyListViewHolder extends RecyclerView.ViewHolder {

    private TextView owner;
    private TextView isValid;
    private ImageButton deleteButton;

    public PublicKeyListViewHolder(View root) {
        super(root);

        owner = (TextView) root.findViewById(R.id.text_view_owner);
        isValid = (TextView) root.findViewById(R.id.text_view_is_valid);
        deleteButton = (ImageButton) root.findViewById(R.id.imageButton);
    }

    public TextView getOwner() {
        return owner;
    }

    public TextView getIsValid() {
        return isValid;
    }

    public ImageButton getDeleteButton() {
        return deleteButton;
    }

}
