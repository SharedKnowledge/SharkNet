package net.sharksystem.sharknet.contact;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.interfaces.Contact;
import net.sharksystem.sharknet.ParentActivity;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.SharkApp;

public class ContactsDetailActivity extends ParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutResource(R.layout.contact_detail_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        startBackgroundTask("Lade Kontaktinformationen");
    }

    @Override
    protected boolean doInBackground() {
        Contact contact = ((SharkApp) getApplication()).getContact();

        //Typeface type = Typeface.createFromAsset(getAssets(),"fonts/RockSalt.TTF");

        TextView nickname = (TextView) findViewById(R.id.con_nickname_edit);
        TextView name = (TextView) findViewById(R.id.con_name_edit);
        TextView email = (TextView) findViewById(R.id.con_email_edit);
        ImageView image = (ImageView) findViewById(R.id.con_profile_image);

        try {
            setToolbarTitle(contact.getNickname());
            if(contact.getPicture()!=null){
                image.setImageBitmap(BitmapFactory.decodeStream(contact.getPicture().getInputStream()));
            }
            nickname.setText(contact.getNickname());
            name.setText(contact.getName());
            email.setText(contact.getEmail());
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    protected void doWhenFinished(boolean success) {

    }

    @Override
    public void onBackPressed() {
        ((SharkApp) getApplication()).resetContact();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Reset clicked data
                ((SharkApp) getApplication()).resetContact();
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
