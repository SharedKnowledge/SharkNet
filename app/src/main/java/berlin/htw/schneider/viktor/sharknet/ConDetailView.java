package berlin.htw.schneider.viktor.sharknet;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.sharknet_api_android.interfaces.Contact;

import java.util.List;
import java.util.Objects;

public class ConDetailView extends AppCompatActivity {

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
        this.contacts = MainActivity.implSharkNet.getContacts();

        TextView t = (TextView) findViewById(R.id.con_nickname_label);
        //t.setTypeface(type);
        TextView n = (TextView) findViewById(R.id.con_name_label);
        //n.setTypeface(type);
        TextView e = (TextView) findViewById(R.id.con_email_label);
        //e.setTypeface(type);
        TextView p = (TextView) findViewById(R.id.con_phone_label);
        //p.setTypeface(type);
        TextView no = (TextView) findViewById(R.id.con_note_label);
        //no.setTypeface(type);

        for(Contact contact : contacts)
        {
            try {
                if(Objects.equals(contact.getNickname(), con_nickname))
                {
                    this.contact = contact;
                    EditText nickname = (EditText)findViewById(R.id.con_nickname_edit);
                    assert nickname != null;
                    if(contact.getNickname() == null)
                    {
                        //nickname.setTypeface(type);
                        nickname.setText(this.con_nickname);
                    }
                    else
                    {
                        nickname.setText(contact.getNickname());
                    }

                    // TODO: die funktionen sind noch nicht implementiert
                    this.name = "no Name entered";
                    EditText name = (EditText)findViewById(R.id.con_name_edit);
                    assert name != null;
                    //name.setTypeface(type);
                    name.setHint(this.name);


                    this.email= "no email";
                    EditText email = (EditText)findViewById(R.id.con_email_edit);
                    assert email != null;
                    //email.setTypeface(type);
                    email.setHint(this.email);


                    this.phone= "no phone";
                    EditText phone = (EditText)findViewById(R.id.con_phone_edit);
                    assert phone != null;
                    //phone.setTypeface(type);
                    phone.setHint(this.phone);

                    this.note= "no note";
                    EditText note = (EditText)findViewById(R.id.con_not_edit);
                    assert note != null;
                    //note.setTypeface(type);
                    note.setHint(this.note);
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
            ConDetailView.this.con_nickname = nickname.getText().toString();
            try {
                ConDetailView.this.contact.setNickname(con_nickname);
            } catch (SharkKBException e) {

            }
            //TODO: muss noch in der API erweitert werden um email usw.
            //ConDetailView.this.contact.setUID();
            //ConDetailView.this.contact.setPicture();
            //ConDetailView.this.contact.setPublicKey();
            finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

}
