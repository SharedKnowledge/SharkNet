package net.sharksystem.sharknet.contact;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import net.sharksystem.api.models.Contact;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.RxSingleBaseActivity;

public class ContactDetailActivity extends RxSingleBaseActivity<Contact> implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutResource(R.layout.contact_detail_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setProgressMessage(R.string.chat_progress_load_contacts);
    }

    @Override
    public void onBackPressed() {
        getSharkApp().resetContact();
        super.onBackPressed();
    }

    @Override
    protected Contact doOnBackgroundThread() throws Exception {
        return getSharkApp().getContact();
    }

    @Override
    protected void doOnUIThread(Contact contact) {
        TextView name = (TextView) findViewById(R.id.contact_name);
        TextView email = (TextView) findViewById(R.id.contact_email);
        ImageView image = (ImageView) findViewById(R.id.round_image);

        setToolbarTitle(contact.getName());
        if (contact.getImage() != null) {
            image.setImageBitmap(contact.getImage());
            image.setPadding(0, 0, 0, 0);
        }
        name.setText(contact.getName());
        email.setText(contact.getEmail());

        Button buttonDelete = (Button) findViewById(R.id.contact_delete);
        buttonDelete.setOnClickListener(this);
    }

    @Override
    protected void doOnError(Throwable error) {
        error.printStackTrace();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getSharkApp().resetContact();
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        mApi.removeContact(getSharkApp().getContact());
        finish();
    }
}
