package berlin.htw.schneider.viktor.sharknet;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import net.sharkfw.knowledgeBase.SemanticTag;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.TXSemanticTag;
//import net.sharksystem.sharknet_api_android.interfaces.Interest;

import java.util.List;

public class ProfileDetailInterests extends AppCompatActivity
{
    private TextView textView;

    @Override
    protected void onResume()
    {
        super.onResume();
        InterestsListAdapter interestsListAdapter = null;
        //interestsListAdapter = new InterestsListAdapter(this,R.layout.line_item_interest,
        //        MainActivity.implSharkNet.getMyProfile().getInterests().getAllTopics());
        ListView lv = (ListView) findViewById(R.id.listView_interests);
        if (lv != null)
        {
            lv.setAdapter(interestsListAdapter);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail_interests);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        InterestsListAdapter interestsListAdapter = null;
        //interestsListAdapter = new InterestsListAdapter(this,R.layout.line_item_interest,
                //MainActivity.implSharkNet.getMyProfile().getInterests().getAllTopics());
        ListView lv = (ListView) findViewById(R.id.listView_interests);
        if (lv != null)
        {
            lv.setAdapter(interestsListAdapter);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //MainActivity.implSharkNet.getMyProfile();
                Intent intent = new Intent(ProfileDetailInterests.this,NewInterest.class);
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
