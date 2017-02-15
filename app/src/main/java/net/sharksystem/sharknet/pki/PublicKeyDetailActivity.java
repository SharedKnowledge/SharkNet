package net.sharksystem.sharknet.pki;


import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.security.SharkPublicKey;
import net.sharkfw.system.L;
import net.sharksystem.api.impl.SharkNetEngine;
import net.sharksystem.sharknet.ParentActivity;
import net.sharksystem.sharknet.R;

import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

/**
 * Created by j4rvis on 2/13/17.
 */

public class PublicKeyDetailActivity extends ParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutResource(R.layout.pki_public_key_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final SharkPublicKey publicKey = PKIDataHolder.getInstance().getPublicKey();

        TextView owner = (TextView) findViewById(R.id.text_view_owner_content);
        TextView ownerKey = (TextView) findViewById(R.id.text_view_owner_key_content);
        TextView validity = (TextView) findViewById(R.id.text_view_validity_content);
        TextView fingerprint = (TextView) findViewById(R.id.text_view_fingerprint_content);
        TextView dateReceived = (TextView) findViewById(R.id.text_view_date_received_content);

        owner.setText(publicKey.getOwner().getName() + "\n"
                + Arrays.toString(publicKey.getOwner().getSI()) + "\n"
                + Arrays.toString(publicKey.getOwner().getAddresses()));

        ownerKey.setText(publicKey.getOwnerPublicKey().toString());

        Date date = new Date(publicKey.getValidity());
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.GERMAN);
        validity.setText(format.format(date));

        fingerprint.setText(new String(publicKey.getFingerprint()));

        Date receiveDate = new Date(publicKey.receiveDate());
        SimpleDateFormat receiveFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.GERMAN);
        dateReceived.setText(receiveFormat.format(receiveDate));

        Button buttonSign = (Button) findViewById(R.id.button_public_key_sign);
        Button buttonDelete = (Button) findViewById(R.id.button_public_key_delete);

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publicKey.delete();
                onBackPressed();
            }
        });

        buttonSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SharkNetEngine.getSharkNet().getSharkEngine().getPKIStorage().sign(publicKey);
                    onBackPressed();
                } catch (SharkKBException | SecurityException e) {
                    L.d(e.getMessage(), this);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Reset clicked data
        PKIDataHolder.getInstance().setPublicKey(null);
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Reset clicked data
                PKIDataHolder.getInstance().setPublicKey(null);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
