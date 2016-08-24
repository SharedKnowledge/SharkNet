package berlin.htw.schneider.viktor.sharknet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import net.sharkfw.knowledgeBase.SharkKBException;

public class ConNew extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_con_new);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.con_new, menu);
        return true;
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
            //TODO: image muss zu ImageButton geändert werden
            // damit beim click menu mit der auswahl der fotos kommt
            ImageView imageView = (ImageView) findViewById(R.id.con_profile_image);
            EditText nickname = (EditText) findViewById(R.id.con_nickname_edit);
            EditText name = (EditText) findViewById(R.id.con_name_edit);
            EditText email = (EditText) findViewById(R.id.con_email_edit);
            EditText phone = (EditText) findViewById(R.id.con_phone_edit);
            EditText note = (EditText) findViewById(R.id.con_not_edit);

            assert nickname != null;
            // TODO: bei Timmo nachfragen ob ich so richtig mache
            try {
                MainActivity.implSharkNet.newContact(nickname.getText().toString(),"234234234","public key lkajljk234234");
            } catch (SharkKBException e) {
                e.printStackTrace();
            }

            finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
