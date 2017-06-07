package net.sharksystem.sharknet.radar;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import net.sharkfw.system.L;
import net.sharksystem.api.models.Chat;
import net.sharksystem.api.models.Contact;
import net.sharksystem.api.shark.peer.NearbyPeer;
import net.sharksystem.api.shark.peer.NearbyPeerManager;
import net.sharksystem.sharknet.NavigationDrawerActivity;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.chat.ChatDetailActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class RadarActivity extends NavigationDrawerActivity implements NearbyPeerManager.NearbyPeerListener {

    private RadarListAdapter mListAdapter;
    private ListView mListView;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutResource(R.layout.radar_activity);
        mListView = (ListView) findViewById(R.id.radar_list_view);
        mListAdapter = new RadarListAdapter(this);
        mListView.setAdapter(mListAdapter);
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNavigationView.setCheckedItem(R.id.sidenav_radar);

//        verifyStoragePermissions(this);
//        try {
//            File target = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "databases");
//            target.mkdirs();
//            File file = new File(target, "contacts.db");
//            copy(new File("/data/data/" + getPackageName() + "/contacts.db"), file);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
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
