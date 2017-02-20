package net.sharksystem.sharknet.pki;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.sharkfw.security.SharkCertificate;
import net.sharkfw.security.SharkPublicKey;
import net.sharkfw.system.L;
import net.sharksystem.sharknet.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static net.sharksystem.api.shark.Application.getContext;

/**
 * Created by j4rvis on 2/11/17.
 */
public class PKIListAdapter extends BaseAdapter {

    private List<SharkCertificate> items = new ArrayList<>();

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

    public void updateItems(List<SharkCertificate> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.pki_line_item, parent, false);
        }

        final SharkCertificate item = (SharkCertificate) this.getItem(position);

        TextView owner = (TextView) convertView.findViewById(R.id.text_view_owner);
        TextView isValid = (TextView) convertView.findViewById(R.id.text_view_is_valid);

        owner.setText(item.getOwner().getName());

        long current = System.currentTimeMillis();
        long validity = item.getValidity();

        // calculate days remaining
        String text;
        long difference = validity - current;
        if (difference <= 0){
            text = "0";
        } else {
            double day = 1000*60*60*24;
            double daysRemaining = difference / day;

            if(daysRemaining < 1){
                double hoursRemaining = daysRemaining * 24;
                text =  (long) hoursRemaining + " Stunden";
            } else {
                text = (long) daysRemaining + " Tage";
            }
        }
        Date date = new Date(validity);

        if(date.after(new Date(current))){
            isValid.setTextColor(Color.GREEN);
        } else {
            isValid.setTextColor(Color.RED);
        }
        isValid.setText(text);

        return convertView;
    }
}
