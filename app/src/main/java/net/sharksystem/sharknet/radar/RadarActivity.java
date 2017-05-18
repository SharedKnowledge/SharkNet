package net.sharksystem.sharknet.radar;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import net.sharksystem.api.models.Chat;
import net.sharksystem.api.models.Contact;
import net.sharksystem.api.shark.peer.NearbyPeer;
import net.sharksystem.api.shark.peer.NearbyPeerManager;
import net.sharksystem.sharknet.NavigationDrawerActivity;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.chat.ChatDetailActivity;

import java.util.ArrayList;

public class RadarActivity extends NavigationDrawerActivity implements NearbyPeerManager.NearbyPeerListener {

    private RadarListAdapter mListAdapter;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutResource(R.layout.radar_activity);
        mListView = (ListView) findViewById(R.id.radar_list_view);
        mListAdapter = new RadarListAdapter(this);
        mListView.setAdapter(mListAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNavigationView.setCheckedItem(R.id.sidenav_radar);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        super.onServiceConnected(name, service);
        mApi.addRadarListener(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                NearbyPeer peer = mListAdapter.getItem(position);

                Contact contact = mApi.getContact(peer.getSender());
                if(contact == null){
                    contact = new Contact(peer.getSender());
                    mApi.addContact(contact);
                }

                // TODO If we already have this contact just open the chat and do not create a new one
                Chat chat = new Chat(mApi.getAccount(), contact);
                getSharkApp().setChat(chat);
                startActivity(new Intent(RadarActivity.this, ChatDetailActivity.class));
            }
        });
    }

    @Override
    public void onNearbyPeersFound(final ArrayList<NearbyPeer> peers) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mListAdapter.updateList(peers);
            }
        });
    }

    @Override
    public void onNearbyPeerFound(NearbyPeer peer) {
    }
}
