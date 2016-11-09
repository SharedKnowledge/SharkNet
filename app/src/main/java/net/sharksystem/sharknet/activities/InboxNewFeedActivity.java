package net.sharksystem.sharknet.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import net.sharksystem.sharknet.R;

public class InboxNewFeedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline_detail_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                EditText message = (EditText) findViewById(R.id.feed_new_message);
                String text = message.getText().toString();
                //TODO:
                /*MainActivity.implSharkNet.newFeed(new ImplContent(text),
                        new ImplInterest("Sport",null,null,null),
                        MainActivity.implSharkNet.getMyProfile().getContact());
                finish();
            }
        });
                        */

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_detail_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.profile_save:

                EditText message = (EditText) findViewById(R.id.feed_new_message);
                String text = message.getText().toString();
                //TODO:
                /*MainActivity.implSharkNet.newFeed(new ImplContent(text),
                        new ImplInterest("Sport",null,null,null),
                        MainActivity.implSharkNet.getMyProfile().getContact());
                        */
                finish();
                return true;



            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }


    }

}
