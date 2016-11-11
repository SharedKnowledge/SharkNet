package net.sharksystem.sharknet.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import net.sharksystem.api.impl.SharkNetEngine;
import net.sharksystem.api.interfaces.Contact;
import net.sharksystem.api.interfaces.RadarListener;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.adapters.RadarListAdapter;

import java.util.ArrayList;
import java.util.List;

public class RadarActivity extends BaseActivity implements RadarListener {

    private RadarListAdapter mListAdapter;

    @Override
    protected void onResume() {
        super.onResume();

        SharkNetEngine.getSharkNet().addRadarListener(this);
        ListView listView = (ListView) findViewById(R.id.radar_list_view);
        mListAdapter = new RadarListAdapter(this);
        listView.setAdapter(mListAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_radar);
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chat, menu);
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


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNewRadarContact(List<Contact> contacts) {
        mListAdapter.updateList((ArrayList<Contact>) contacts);
    }
}
