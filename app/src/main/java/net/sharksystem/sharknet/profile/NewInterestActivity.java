package net.sharksystem.sharknet.profile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import net.sharksystem.sharknet.ParentActivity;
import net.sharksystem.sharknet.R;

public class NewInterestActivity extends ParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutResource(R.layout.content_new_interest);
        setOptionsMenu(R.menu.profile_detail_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*
        FloatingActionButton fab = activateFloatingActionButton();
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText name = (EditText) findViewById(R.id.new_interest_name_editText);
                String s = name.getText().toString();
                SharkNetEngine.getSharkNet.getMyProfile().getContact().getInterests().addInterest(s,"wiki/"+s);
                finish();
            }
        });*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.profile_save:

                EditText name = (EditText) findViewById(R.id.new_interest_name_editText);
                String s = name.getText().toString();
                // TODO: Interessen müssen noch gemacht werden
                //SharkNetEngine.getSharkNet.getMyProfile().getInterests().addInterest(s,"wiki/"+s);
                finish();
                return true;



            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }


    }
}
