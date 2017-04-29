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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutResource(R.layout.radar_activity);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ListView listView = (ListView) findViewById(R.id.radar_list_view);
        mListAdapter = new RadarListAdapter(this);
        listView.setAdapter(mListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                NearbyPeer peer = mListAdapter.getItem(position);

                Contact contact = new Contact(peer.getSender());
                mApi.addContact(contact);

                Chat chat = new Chat(mApi.getAccount(), contact);
                mApi.addChat(chat);
                getSharkApp().setChat(chat);
                startActivity(new Intent(RadarActivity.this, ChatDetailActivity.class));
            }
        });
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        super.onServiceConnected(name, service);
        mApi.addRadarListener(this);
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
        mApi.addContact(new Contact(peer.getSender()));
    }
}
