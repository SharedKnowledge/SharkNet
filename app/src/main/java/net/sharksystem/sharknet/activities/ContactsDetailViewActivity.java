package net.sharksystem.sharknet.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.impl.SharkNetEngine;
import net.sharksystem.api.interfaces.Contact;
import net.sharksystem.sharknet.R;

import java.util.List;
import java.util.Objects;

public class ContactsDetailViewActivity extends AppCompatActivity {

    private String con_nickname;
    private Contact contact;
    private List<Contact> contacts;
    private String name,email,phone,note;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.con_detail_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_con_detail_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_con_detail);
        setSupportActionBar(toolbar);
        this.con_nickname = getIntent().getStringExtra("CONTACT_NICKNAME");
        getSupportActionBar().setTitle(con_nickname);
        //Typeface type = Typeface.createFromAsset(getAssets(),"fonts/RockSalt.TTF");
        try {
            this.contacts = SharkNetEngine.getSharkNet().getContacts();
        } catch (SharkKBException e) {
            e.printStackTrace();
        }

        TextView nickname = (TextView) findViewById(R.id.con_nickname_edit);
        //t.setTypeface(type);
        TextView name = (TextView) findViewById(R.id.con_name_edit);
        //n.setTypeface(type);
        TextView email = (TextView) findViewById(R.id.con_email_edit);
        //e.setTypeface(type);
        TextView phone = (TextView) findViewById(R.id.con_phone_edit);
        //p.setTypeface(type);
        TextView note = (TextView) findViewById(R.id.con_note_edit);
        //no.setTypeface(type);

        for(Contact contact : contacts)
        {
            try {
                if(Objects.equals(contact.getNickname(), con_nickname))
                {
                    this.contact = contact;
                    assert nickname != null;
                    if(contact.getNickname() != null)
                    {
                        //nickname.setTypeface(type);
                        nickname.setText(contact.getNickname());
                    }

                    if(contact.getName() != null)
                    {
                        //name.setTypeface(type);
                        name.setText(contact.getName());
                    }

                    if(contact.getEmail() != null)
                    {
                        //name.setTypeface(type);
                        email.setText(contact.getEmail());
                    }

                    //TODO: bei mehreren Nummern muss noch eine TextEdit rangehangen werden
                    if(contact.getTelephoneNumber() != null)
                    {
                        //name.setTypeface(type);
                        phone.setText(contact.getTelephoneNumber().get(0));
                    }


                    if(contact.getNote() != null)
                    {
                        //name.setTypeface(type);
                        note.setText(contact.getNote());
                    }

                }
            } catch (SharkKBException e1) {
                e1.printStackTrace();
            }
        }


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
        if (id == R.id.save)
        {
            EditText nickname = (EditText)findViewById(R.id.con_nickname_edit);
            assert nickname != null;
            ContactsDetailViewActivity.this.con_nickname = nickname.getText().toString();
            try {
                ContactsDetailViewActivity.this.contact.setNickname(con_nickname);
            } catch (SharkKBException e) {
                e.printStackTrace();
            }
            //TODO: muss noch in der API erweitert werden um email usw.
            //ContactsDetailViewActivity.this.contact.setUID();
            //ContactsDetailViewActivity.this.contact.setPicture();
            //ContactsDetailViewActivity.this.contact.setPublicKey();
            finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

}
