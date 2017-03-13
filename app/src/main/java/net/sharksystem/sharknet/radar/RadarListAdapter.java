package net.sharksystem.sharknet.radar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.interfaces.Contact;
import net.sharksystem.sharknet.R;

import java.util.ArrayList;

/**
 * Created by j4rvis on 11/11/16.
 */

public class RadarListAdapter extends BaseAdapter {

    private Context mContext = null;
    private ArrayList<Contact> mContacts = new ArrayList<>();

    public RadarListAdapter(Context context) {
        mContext = context;
    }

    public void updateList(ArrayList<Contact> contacts) {
        mContacts = contacts;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mContacts.size();
    }

    @Override
    public Contact getItem(int position) {
        return mContacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Contact contact = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.radar_contact_list_item, parent, false);
        }

        try {
            ((TextView) convertView.findViewById(R.id.radar_contact_name)).setText(contact.getName());
            ((TextView) convertView.findViewById(R.id.radar_contact_last_seen)).setText(contact.getLastWifiContact().toString());
        } catch (SharkKBException e) {
            e.printStackTrace();
        }

        return convertView;
    }
}
