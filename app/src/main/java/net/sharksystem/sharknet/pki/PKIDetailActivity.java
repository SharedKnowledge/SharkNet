package net.sharksystem.sharknet.pki;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import net.sharkfw.security.SharkCertificate;
import net.sharksystem.sharknet.ParentActivity;
import net.sharksystem.sharknet.R;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static net.sharksystem.api.shark.Application.getContext;

/**
 * Created by j4rvis on 2/19/17.
 */

public class PKIDetailActivity extends ParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutResource(R.layout.pki_detail_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final PKICertificateHolder holder = PKIDataHolder.getInstance().getHolder();
        setTitle(holder.getOwner().getName() + "'s Zertifikat");

        TextView owner = (TextView) findViewById(R.id.text_view_owner_content);
        TextView ownerKey = (TextView) findViewById(R.id.text_view_owner_key_content);
        TextView validity = (TextView) findViewById(R.id.text_view_validity_content);
        TextView fingerprint = (TextView) findViewById(R.id.text_view_fingerprint_content);
//        TextView dateReceived = (TextView) findViewById(R.id.text_view_date_received_content);

        final SharkCertificate certificate = holder.getCertificates().get(0);

        owner.setText(certificate.getOwner().getName() + "\n"
                + Arrays.toString(certificate.getOwner().getSI()) + "\n"
                + Arrays.toString(certificate.getOwner().getAddresses()));

        ownerKey.setText(certificate.getOwnerPublicKey().toString());

        Date date = new Date(certificate.getValidity());
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.GERMAN);
        validity.setText(format.format(date));

        fingerprint.setText(String.format("%064x", new java.math.BigInteger(1, certificate.getFingerprint())));

//        Date receiveDate = new Date(certificate.receiveDate());
//        SimpleDateFormat receiveFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.GERMAN);
//        dateReceived.setText(receiveFormat.format(receiveDate));
//
//        TextView signature = (TextView) findViewById(R.id.text_view_signature_content);
//        signature.setText(String.format("%064x", new java.math.BigInteger(1, certificate.getSignature())));

        ListView signers = (ListView) findViewById(R.id.list_view_signers);
//        signers.setClickable(false);
        signers.setAdapter(new SignerListAdapter(holder.getCertificates()));
//        signers.setEnabled(false);
        justifyListViewHeightBasedOnChildren(signers);

        Button buttonDelete = (Button) findViewById(R.id.button_delete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                certificate.delete();
                onBackPressed();
            }
        });
    }

    public static void justifyListViewHeightBasedOnChildren (ListView listView) {

        ListAdapter adapter = listView.getAdapter();

        if (adapter == null) {
            return;
        }
        ViewGroup vg = listView;
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, vg);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
    }

    @Override
    protected boolean doInBackground() {
        return false;
    }

    @Override
    protected void doWhenFinished(boolean success) {

    }

    @Override
    public void onBackPressed() {
        // Reset clicked data
        PKIDataHolder.getInstance().setHolder(null);
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Reset clicked data
                PKIDataHolder.getInstance().setHolder(null);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class SignerListAdapter extends BaseAdapter {

        private final List<SharkCertificate> certificates;

        private SignerListAdapter(List<SharkCertificate> certificates) {
            this.certificates = certificates;
        }

        @Override
        public int getCount() {
            return this.certificates.size();
        }

        @Override
        public Object getItem(int position) {
            return this.certificates.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.pki_detail_line_item, parent, false);
            }

            SharkCertificate certificate = (SharkCertificate) getItem(position);

            TextView signerName = (TextView) convertView.findViewById(R.id.text_view_signer_name);
            TextView dateSigned = (TextView) convertView.findViewById(R.id.text_view_date_signed);
            TextView dateReceived = (TextView) convertView.findViewById(R.id.text_view_date_received);

            signerName.setText(certificate.getSigner().getName());

            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.GERMAN);
            Date signingDate = new Date(certificate.signingDate());
            dateSigned.setText(format.format(signingDate));

            Date receiveDate = new Date(certificate.receiveDate());
            dateReceived.setText(format.format(receiveDate));

            return convertView;
        }
    }
}
