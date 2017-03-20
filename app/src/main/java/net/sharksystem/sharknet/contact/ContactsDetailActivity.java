package net.sharksystem.sharknet.contact;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import net.sharksystem.api.interfaces.Contact;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.RxSingleBaseActivity;

public class ContactsDetailActivity extends RxSingleBaseActivity<ContactsDetailActivity.ContactDataHolder> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutResource(R.layout.contact_detail_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setProgressMessage("Lade Kontakt...");

        startSubscription();
    }

    @Override
    protected ContactDataHolder doOnBackgroundThread() throws Exception {
        Contact contact = getSharkApp().getContact();
        Bitmap image = null;
        if (contact.getPicture().getLength() > 0) {
            image = BitmapFactory.decodeStream(contact.getPicture().getInputStream());
        }
        return new ContactDataHolder(image, contact.getName(), contact.getNickname(), contact.getEmail());
    }

    @Override
    protected void doOnUIThread(ContactDataHolder contactDataHolder) {
        TextView nickname = (TextView) findViewById(R.id.contact_nickname);
        TextView name = (TextView) findViewById(R.id.contact_name);
        TextView email = (TextView) findViewById(R.id.contact_email);
        ImageView image = (ImageView) findViewById(R.id.contact_image);

        setToolbarTitle(contactDataHolder.name);
        if(contactDataHolder.image != null){
            image.setImageBitmap(contactDataHolder.image);
        } else {
            image.setImageResource(R.drawable.ic_person_white_24dp);
        }
        nickname.setText(contactDataHolder.nickname);
        name.setText(contactDataHolder.name);
        email.setText(contactDataHolder.address);
    }

    @Override
    protected void doOnError(Throwable error) {
        error.printStackTrace();
    }

    @Override
    public void onBackPressed() {
        getSharkApp().resetContact();
        super.onBackPressed();
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

    class ContactDataHolder {
        Bitmap image;
        String name;
        String nickname;
        String address;

        public ContactDataHolder(Bitmap image, String contactName, String contactNickName, String contactAddress) {
            this.image = image;
            this.name = contactName;
            this.nickname = contactNickName;
            this.address = contactAddress;
        }
    }
}
