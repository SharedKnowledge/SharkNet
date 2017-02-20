package net.sharksystem.sharknet.pki;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.sharkfw.security.SharkCertificate;
import net.sharksystem.sharknet.ParentActivity;
import net.sharksystem.sharknet.R;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

/**
 * Created by j4rvis on 2/19/17.
 */

public class PKIDetailActivity extends ParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutResource(R.layout.pki_detail_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final SharkCertificate certificate = PKIDataHolder.getInstance().getCertificate();

        TextView owner = (TextView) findViewById(R.id.text_view_owner_content);
        TextView ownerKey = (TextView) findViewById(R.id.text_view_owner_key_content);
        TextView validity = (TextView) findViewById(R.id.text_view_validity_content);
        TextView fingerprint = (TextView) findViewById(R.id.text_view_fingerprint_content);
        TextView dateReceived = (TextView) findViewById(R.id.text_view_date_received_content);

        owner.setText(certificate.getOwner().getName() + "\n"
                + Arrays.toString(certificate.getOwner().getSI()) + "\n"
                + Arrays.toString(certificate.getOwner().getAddresses()));

        ownerKey.setText(certificate.getOwnerPublicKey().toString());

        Date date = new Date(certificate.getValidity());
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.GERMAN);
        validity.setText(format.format(date));

        fingerprint.setText(new String(certificate.getFingerprint()));

        Date receiveDate = new Date(certificate.receiveDate());
        SimpleDateFormat receiveFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.GERMAN);
        dateReceived.setText(receiveFormat.format(receiveDate));

        Button buttonDelete = (Button) findViewById(R.id.button_public_key_delete);

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                certificate.delete();
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Reset clicked data
        PKIDataHolder.getInstance().setCertificate(null);
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Reset clicked data
                PKIDataHolder.getInstance().setCertificate(null);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
