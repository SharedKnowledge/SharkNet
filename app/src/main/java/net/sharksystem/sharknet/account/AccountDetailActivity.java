package net.sharksystem.sharknet.account;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import net.sharksystem.api.dao_impl.SharkNetApi;
import net.sharksystem.api.models.Contact;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.RxSingleBaseActivity;

public class AccountDetailActivity extends RxSingleBaseActivity<Contact> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutResource(R.layout.contact_detail_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setProgressMessage("Lade Account...");

        startSubscription();
    }

    @Override
    public void onBackPressed() {
        getSharkApp().resetContact();
        super.onBackPressed();
    }

    @Override
    protected Contact doOnBackgroundThread() throws Exception {
        return SharkNetApi.getInstance().getAccount();
    }

    @Override
    protected void doOnUIThread(Contact contact) {
        TextView name = (TextView) findViewById(R.id.contact_name);
        TextView email = (TextView) findViewById(R.id.contact_email);
        ImageView image = (ImageView) findViewById(R.id.contact_image);

        setToolbarTitle("Account");
        if (contact.getImage() != null) {
            image.setImageBitmap(contact.getImage());
        } else {
            image.setImageResource(R.drawable.ic_person_white_24dp);
        }
        name.setText(contact.getName());
        email.setText(contact.getEmail());
    }

    @Override
    protected void doOnError(Throwable error) {
        error.printStackTrace();
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
