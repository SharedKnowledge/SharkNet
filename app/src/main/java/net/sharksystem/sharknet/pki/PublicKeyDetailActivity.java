package net.sharksystem.sharknet.pki;


import android.os.Bundle;
import android.view.MenuItem;

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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
