package net.sharksystem.sharknet.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ListView;

import net.sharksystem.sharknet.ParentActivity;
import net.sharksystem.sharknet.R;

public class ProfileDetailInterestsActivity extends ParentActivity {

    @Override
    protected void onResume() {
        super.onResume();
        InterestsListAdapter interestsListAdapter = null;
        interestsListAdapter = new InterestsListAdapter(this, R.layout.line_item_interest,
                //TODO: Interessen noch nicht implementiert
                //SharkNetEngine.getSharkNet.getMyProfile().getInterests().getAllTopics()
                null
        );
        ListView lv = (ListView) findViewById(R.id.listView_interests);
        if (lv != null) {
            lv.setAdapter(interestsListAdapter);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutResource(R.layout.profile_detail_interests_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        InterestsListAdapter interestsListAdapter = null;
        interestsListAdapter = new InterestsListAdapter(this, R.layout.line_item_interest,
                //TODO: Interessen noch nicht implementiert
                //SharkNetEngine.getSharkNet.getMyProfile().getContact().getInterests().getAllTopics()
                null
        );
        ListView lv = (ListView) findViewById(R.id.listView_interests);
        if (lv != null) {
            lv.setAdapter(interestsListAdapter);
        }

        FloatingActionButton fab = activateFloatingActionButton();
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //SharkNetEngine.getSharkNet.getMyProfile().getContact();
                Intent intent = new Intent(ProfileDetailInterestsActivity.this, NewInterestActivity.class);
                startActivity(intent);
            }
        });
    }

}
