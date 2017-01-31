package net.sharksystem.sharknet.inbox;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import net.sharksystem.sharknet.ParentActivity;
import net.sharksystem.sharknet.R;

public class InboxNewFeedActivity extends ParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setLayoutResource(R.layout.inbox_detail_activity);
        setOptionsMenu(R.menu.profile_detail_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                /*SharkNetEngine.getSharkNet.newFeed(new ImplContent(text),
                        new ImplInterest("Sport",null,null,null),
                        SharkNetEngine.getSharkNet.getMyProfile().getContact());
                finish();
            }
        });
                        */

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.profile_save:

                EditText message = (EditText) findViewById(R.id.feed_new_message);
                String text = message.getText().toString();
                //TODO:
                /*SharkNetEngine.getSharkNet.newFeed(new ImplContent(text),
                        new ImplInterest("Sport",null,null,null),
                        SharkNetEngine.getSharkNet.getMyProfile().getContact());
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
