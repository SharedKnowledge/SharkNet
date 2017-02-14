package net.sharksystem.sharknet.pki;


import android.os.Bundle;
import android.view.MenuItem;

import net.sharkfw.security.SharkCertificate;
import net.sharksystem.sharknet.ParentActivity;
import net.sharksystem.sharknet.R;

/**
 * Created by j4rvis on 2/13/17.
 */

public class CertificateDetailActivity extends ParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutResource(R.layout.pki_certificate_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharkCertificate certificate = PKIDataHolder.getInstance().getCertificate();
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
