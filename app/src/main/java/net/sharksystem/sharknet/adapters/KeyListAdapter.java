package net.sharksystem.sharknet.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.impl.SharkNetEngine;
import net.sharksystem.api.interfaces.Contact;
import net.sharksystem.sharknet.R;

import java.util.List;

/**
 * Created by viktorowich on 12/12/2016.
 */

public class KeyListAdapter extends RecyclerView.Adapter<KeyListAdapter.ViewHolderBase>
{
    private List<Contact> contacts;

    public KeyListAdapter(List<Contact> objects)
    {
        contacts = objects;
    }

    @Override
    public KeyListAdapter.ViewHolderBase onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView;

        switch (viewType)
        {
            case 0:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.line_item_key, parent, false);
                return new KeyListAdapter.ViewHolderKey(itemView);
            case 1:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.line_item_my_key, parent, false);
                return new KeyListAdapter.ViewHolderMyKey(itemView);
        }
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.line_item_key, parent, false);
        return new KeyListAdapter.ViewHolderKey(itemView);
    }

    @Override
    public void onBindViewHolder(KeyListAdapter.ViewHolderBase holder, int position)
    {
        Contact contact = contacts.get(position);
        if(contact != null)
        {
//            TODO: zeit wann er abläuft ist null... Dummy erweitern
//            String expiration = new java.text.SimpleDateFormat("HH:mm dd.MM.yyyy").format(contact.getPublicKeyExpiration());
    //        TODO: wann der key erstellt bzw. aufgenommen wird von der API nicht unterstützt bis jetzt

            holder.textView_created.setText("");
//            holder.textView_expiration.setText(expiration);
            try {
                holder.textView_key.setText(contact.getPublicKey());
                holder.textView_name.setText(contact.getName());
            } catch (SharkKBException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        Contact contact = contacts.get(position);

        try {
            if(SharkNetEngine.getSharkNet().getMyProfile().getUID() == contact.getUID())
            {
                return 1;
            }
            else
                return 0;
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public class ViewHolderBase extends RecyclerView.ViewHolder {

        public TextView textView_key, textView_name, textView_created, textView_expiration;

        public ViewHolderBase(View itemView) {
            super(itemView);
            textView_name = (TextView) itemView.findViewById(R.id.key_owner);
            textView_created = (TextView) itemView.findViewById(R.id.key_created);
            textView_expiration = (TextView) itemView.findViewById(R.id.key_expiration);
            textView_key = (TextView) itemView.findViewById(R.id.key_value);
        }

    }

    public class ViewHolderMyKey extends ViewHolderBase
    {

        public ViewHolderMyKey(View itemView) {
            super(itemView);
        }
    }

    public class ViewHolderKey extends ViewHolderBase
    {
        public ViewHolderKey(View itemView) {
            super(itemView);
        }
    }
}
