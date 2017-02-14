package net.sharksystem.sharknet.pki;


import android.os.Bundle;
import android.view.MenuItem;

import net.sharkfw.security.SharkPublicKey;
import net.sharksystem.sharknet.ParentActivity;
import net.sharksystem.sharknet.R;

/**
 * Created by j4rvis on 2/13/17.
 */

public class PublicKeyDetailActivity extends ParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutResource(R.layout.pki_public_key_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharkPublicKey publicKey = PKIDataHolder.getInstance().getPublicKey();
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
