package net.sharksystem.sharknet.pki;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import net.sharksystem.sharknet.R;

import java.util.ArrayList;
import java.util.List;

import static net.sharksystem.api.shark.Application.getContext;

/**
 * Created by j4rvis on 2/11/17.
 */
public class CertificateListAdapter extends BaseAdapter {

    private List<Object> items = new ArrayList<>();

    public CertificateListAdapter() {
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Object getItem(int position) {
        return this.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void updateItems(List<Object> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.pki_certificate_line_item, parent, false);
        }

//        SharkCertificate item = (SharkCertificate) this.getItem(position);

        return convertView;
    }
}
