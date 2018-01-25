package net.sharksystem.sharknet.broadcast;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.SemanticTag;
import net.sharkfw.knowledgeBase.SharkCSAlgebra;
import net.sharkfw.knowledgeBase.SharkKB;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharkfw.knowledgeBase.sync.manager.SyncComponent;
import net.sharkfw.knowledgeBase.sync.manager.port.SyncMergeKP;
import net.sharkfw.routing.SemanticRoutingKP;
import net.sharkfw.system.L;
import net.sharksystem.api.models.Broadcast;
import net.sharksystem.api.models.Chat;
import net.sharksystem.api.models.Contact;
import net.sharksystem.api.models.Message;
import net.sharksystem.api.models.Profile;
import net.sharksystem.api.shark.peer.NearbyPeer;
import net.sharksystem.api.shark.peer.NearbyPeerManager;
import net.sharksystem.api.shark.protocols.bluetooth.BluetoothStreamStub;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.RxSingleBaseActivity;
import net.sharksystem.sharknet.RxSingleNavigationDrawerActivity;
import net.sharksystem.sharknet.chat.ChatAnnotationActivity;
import net.sharksystem.sharknet.chat.ChatAnnotationLocationActivity;
import net.sharksystem.sharknet.chat.ChatAnnotationPeerActivity;
import net.sharksystem.sharknet.chat.ChatAnnotationTimeActivity;
import net.sharksystem.sharknet.chat.ChatDetailMsgListAdapter;
import net.sharksystem.sharknet.chat.ChatSettingsActivity;
import net.sharksystem.sharknet.radar.RadarListAdapter;
import net.sharksystem.sharknet.schnitzeljagd.locator.Locator;

import java.util.ArrayList;
import java.util.List;




/**
 * Created by Dustin Feurich
 */

public class BroadcastActivity extends RxSingleNavigationDrawerActivity<List<Message>> implements SemanticRoutingKP.SemanticRoutingListener, NearbyPeerManager.NearbyPeerListener {
    public static final String EXTRA_MESSAGE = "extra_message";
    private ChatDetailMsgListAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private Broadcast broadcast;
    private RadarListAdapter radarListAdapter;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private String topicSI = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (broadcast == null) {
            broadcast = getSharkApp().getBroadcast();
        }
        configureLayout();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.CHANGE_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_EXTERNAL_STORAGE);
        }
        setTitle("Semantic Broadcast");
        setProgressMessage(R.string.chat_progress_load_messages);
        radarListAdapter = new RadarListAdapter(this);
    }

    @Override
    public void onBackPressed() {
        getSharkApp().resetChat();
        super.onBackPressed();
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        super.onServiceConnected(name, service);
        mApi.getSharkEngine().getBroadcastManager().addSemanticRoutingListener(this);
        mApi.addRadarListener(this);
    }

    private void configureLayout() {
        setLayoutResource(R.layout.chat_detail_activity);
        setOptionsMenu(R.menu.chat_detail_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecyclerView = (RecyclerView) findViewById(R.id.chat_msg_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new ChatDetailMsgListAdapter(this, getSharkApp());
        mAdapter.setMessages(broadcast.getMessages());
        mRecyclerView.setAdapter(mAdapter);
        final LinearLayout mRevealView = (LinearLayout) findViewById(R.id.reveal_items);
        mRevealView.setVisibility(View.GONE);
        //broadcast = new Broadcast();
        final CardView sendButton = (CardView) findViewById(R.id.message_send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.message_edit_text);

                String msg_string;

                if (editText != null) {
                    msg_string = editText.getText().toString().trim();

                    if (msg_string.isEmpty()) {
                        Snackbar.make(sendButton, "No message entered!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    } else {
                        Message message = new Message(mApi.getAccount());
                        message.setContent(msg_string);
                        if (!TextUtils.isEmpty(topicSI)) {
                            message.setTopic(InMemoSharkKB.createInMemoSemanticTag("", topicSI));
                            topicSI = "";
                        }
                        else {
                            message.setTopic(InMemoSharkKB.createInMemoSemanticTag(Message.MESSAGE_ID, message.getSender().getTag().getName() + message.getDate().getTime()));
                            topicSI = "";
                        }
                        mApi.getSharkEngine().getBroadcastManager().getSentMessages().put(message.getTopic().getSI()[0], "message");
                        broadcast = mApi.getBroadcast();
                        broadcast.addMessage(message);
                        List<PeerSemanticTag> nearbyPeers = new ArrayList<>();
                        for (NearbyPeer peer : radarListAdapter.getmNearbyPeers()) {
                            nearbyPeers.add(peer.getSender());
                        }
                        mApi.updateBroadcast(broadcast, message, nearbyPeers);
                        broadcast = mApi.getBroadcast();
                        editText.getText().clear();
                        startSubscription();
                        mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
                        Toast.makeText(getApplicationContext(), "Sent to " + nearbyPeers.size() + " Peers from "+ BluetoothStreamStub.staticLocalAddress,Toast.LENGTH_LONG).show();
                        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor prefsEditor = mPrefs.edit();
                        prefsEditor.remove("ChatAnnotationList");
                        prefsEditor.commit();

                    }
                }
            }
        });

        final CardView semanticButton = (CardView) findViewById(R.id.semantic_button);
        semanticButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRevealView.getVisibility() == View.GONE) {
                    mRevealView.setVisibility(View.VISIBLE);
                }
                else {
                    mRevealView.setVisibility(View.GONE);
                }
            }
        });


        final ImageButton topicButton = (ImageButton) findViewById(R.id.imageButtonTopic);
        topicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRevealView.setVisibility(View.GONE);
                Intent intent = new Intent(getApplicationContext(), ChatAnnotationActivity.class);
                intent.putExtra("purpose",1);
                intent.putExtra("type","topic");
//                if (!TextUtils.isEmpty(topicSI)) {
//                    intent.putExtra(EXTRA_MESSAGE, topicSI);
//                }
                startActivityForResult(intent, 1);
            }
        });

        final ImageButton typeButton = (ImageButton) findViewById(R.id.imageButtonType);
        typeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRevealView.setVisibility(View.GONE);
                Intent intent = new Intent(getApplicationContext(), ChatAnnotationActivity.class);
                intent.putExtra("purpose",1);
                intent.putExtra("type","type");
                startActivityForResult(intent, 1);
            }
        });

        final ImageButton peerButton = (ImageButton) findViewById(R.id.imageButtonSender);
        peerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRevealView.setVisibility(View.GONE);
                Intent intent = new Intent(getApplicationContext(), ChatAnnotationPeerActivity.class);
                intent.putExtra(EXTRA_MESSAGE, "Peer");
                startActivity(intent);
            }
        });

        final ImageButton timeButton = (ImageButton) findViewById(R.id.imageButtonTime);
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRevealView.setVisibility(View.GONE);
                Intent intent = new Intent(getApplicationContext(), ChatAnnotationTimeActivity.class);
                intent.putExtra(EXTRA_MESSAGE, "Location");
                startActivity(intent);
                //DialogFragment newFragment = new TimePickerFragment();
                //newFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });

        final ImageButton locationButton = (ImageButton) findViewById(R.id.imageButtonLocation);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRevealView.setVisibility(View.GONE);
                Intent intent = new Intent(getApplicationContext(), ChatAnnotationLocationActivity.class);
                intent.putExtra(EXTRA_MESSAGE, "Location");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                topicSI = data.getStringExtra("result");
                System.out.println("_________ " + topicSI + " ___________AAAA");
            }
            else {
                topicSI = null;
            }
        }
    }

    @Override
    protected List<Message> doOnBackgroundThread() throws Exception {
        return broadcast.getMessages();
    }

    @Override
    protected void doOnUIThread(List<Message> list) {
        mAdapter.setMessages(list);
    }

    @Override
    protected void doOnError(Throwable error) {
        error.printStackTrace();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Reset clicked data
                getSharkApp().setBroadcast(null);
                this.finish();
                return true;
            case R.id.chat_attachment_take_photo:
                return true;
            case R.id.chat_settings:
                startActivity(new Intent(this, ChatSettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onNewMerge(SyncComponent component, SharkKB changes, boolean accepted, boolean forwarded) {
        L.d("Received a new message for the Broadcast channel", this);
        if (accepted) {
            broadcast = mApi.getBroadcast();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(broadcast!=null){
                        mAdapter.setMessages(broadcast.getMessages());
                        mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
                    }
                }
            });
            if (forwarded) {
                List<PeerSemanticTag> nearbyPeers = new ArrayList<>();
                for (NearbyPeer peer : radarListAdapter.getmNearbyPeers()) {
                    nearbyPeers.add(peer.getSender());
                }
                Toast.makeText(getApplicationContext(), "Forwarded to " + nearbyPeers.size() + " Peers",Toast.LENGTH_LONG).show();
                mApi.getSharkEngine().getBroadcastManager().sendBroadcastMessage(component, nearbyPeers);
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "Incoming Message rejected!",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onNearbyPeersFound(final ArrayList<NearbyPeer> peers) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                radarListAdapter.updateList(peers);
            }
        });
        //Toast.makeText(this, "Amount of Peers found: " + peers.size(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNearbyPeerFound(NearbyPeer peer) {

    }
}
