package net.sharksystem.sharknet.pki;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.SharkCSAlgebra;
import net.sharkfw.security.SharkCertificate;
import net.sharksystem.api.dao_impl.SharkNetApi;
import net.sharksystem.sharknet.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by j4rvis on 2/11/17.
 */
public class PKIListAdapter extends BaseAdapter {

    private List<PKICertificateHolder> items = new ArrayList<>();

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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pki_line_item, parent, false);
        }

        final PKICertificateHolder item = (PKICertificateHolder) this.getItem(position);

        TextView owner = (TextView) convertView.findViewById(R.id.text_view_owner);
        TextView isValid = (TextView) convertView.findViewById(R.id.text_view_is_valid);
        TextView selfSigned = (TextView) convertView.findViewById(R.id.text_view_self_signed);
        TextView numberOfSigners = (TextView) convertView.findViewById(R.id.text_view_number_of_signers);

        owner.setText(item.getOwner().getName());

        long current = System.currentTimeMillis();
        long validity = item.getCertificates().get(0).getValidity();

        // calculate days remaining
        String text;
        long difference = validity - current;
        if (difference <= 0) {
            text = "not valid";
        } else {
            double day = 1000 * 60 * 60 * 24;
            double daysRemaining = difference / day;

            if (daysRemaining < 1) {
                double hoursRemaining = daysRemaining * 24;
                text = (long) hoursRemaining + " hours";
            } else {
                text = (long) daysRemaining + " days";
            }
        }
        Date date = new Date(validity);

        if (date.after(new Date(current))) {
            isValid.setTextColor(Color.GREEN);
        } else {
            isValid.setTextColor(Color.RED);
        }
        isValid.setText(text);

        numberOfSigners.setText("" + item.getCertificates().size());

        PeerSemanticTag tag = SharkNetApi.getInstance().getAccount().getTag();
        boolean isSelfSigned = false;

        Iterator<SharkCertificate> iterator = item.getCertificates().iterator();
        while (iterator.hasNext()) {
            SharkCertificate next = iterator.next();
            if (SharkCSAlgebra.identical(next.getSigner(), tag)) {
                isSelfSigned = true;
                break;
            }
        }
        selfSigned.setText(isSelfSigned ? "Yes" : "no");


        return convertView;
    }

    public void updateItems(List<PKICertificateHolder> items) {
        this.items = items;
        notifyDataSetChanged();
    }
}
