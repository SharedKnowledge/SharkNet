package net.sharksystem.sharknet.contact;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.impl.SharkNetEngine;
import net.sharksystem.sharknet.ParentActivity;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.nfc.NFCActivity;

public class ContactsNewActivity extends ParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutResource(R.layout.contact_new_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.sidenav_nfc)
        {
            Intent intent = new Intent(ContactsNewActivity.this,NFCActivity.class);
            startActivity(intent);
        }
        if (id == R.id.save)
        {
            //TODO: image muss zu ImageButton geändert werden
            // damit beim click menu mit der auswahl der fotos kommt
            ImageView imageView = (ImageView) findViewById(R.id.con_profile_image);
            EditText nickname = (EditText) findViewById(R.id.con_nickname_edit);
            EditText name = (EditText) findViewById(R.id.con_name_edit);
            EditText email = (EditText) findViewById(R.id.con_email_edit);
            EditText phone = (EditText) findViewById(R.id.con_phone_edit);
            EditText note = (EditText) findViewById(R.id.con_not_edit);

            assert nickname != null;
            // TODO: muss noch geschaut werden ob es so richtig ist
            // kommt eine newContact(Contact)
            try {
                SharkNetEngine.getSharkNet().newContact(nickname.getText().toString(),"234234234","public key lkajljk234234");
            } catch (SharkKBException e) {
                e.printStackTrace();
            }

            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
