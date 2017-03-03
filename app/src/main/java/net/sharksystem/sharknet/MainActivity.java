package net.sharksystem.sharknet;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import net.sharkfw.asip.engine.serializer.SharkProtocolNotSupportedException;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.system.L;
import net.sharksystem.api.impl.SharkNetEngine;
import net.sharksystem.sharknet.chat.ChatActivity;
import net.sharksystem.sharknet.dummy.Dummy;
import net.sharksystem.sharknet.nfc.NFCActivity;
import net.sharksystem.sharknet.pki.PKIActivity;
import net.sharksystem.sharknet.radar.RadarActivity;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<net.sharksystem.api.interfaces.Profile> profiles = null;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        L.setLogLevel(L.LOGLEVEL_ALL);

        Button login = (Button) findViewById(R.id.button_login);
        ImageButton next = (ImageButton) findViewById(R.id.button_next_profile);
        ImageButton previous = (ImageButton) findViewById(R.id.button_previous_profile);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChat();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    nextProfile();
                } catch (SharkKBException e) {
                    e.printStackTrace();
                }
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    previousProfile();
                } catch (SharkKBException e) {
                    e.printStackTrace();
                }
            }
        });


//        WifiManager wifiManager = (WifiManager)this.getSystemService(Context.WIFI_SERVICE);
//        wifiManager.setWifiEnabled(false);
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        wifiManager.setWifiEnabled(true);

        SharkNetEngine.getSharkNet().setContext(this);

        try {
            Dummy.createDummyData();
        } catch (SharkKBException | JSONException | InterruptedException e) {
            e.printStackTrace();
        }
        index = 0;
        try {
            this.profiles = SharkNetEngine.getSharkNet().getProfiles();
        } catch (SharkKBException e) {
            e.printStackTrace();
        }

        EditText userid = (EditText) findViewById(R.id.userid);
        assert userid != null;

        try {
            userid.setText(this.profiles.get(index).getNickname());
        } catch (SharkKBException e) {
            e.printStackTrace();
        }

        // TODO just for tests - simulates auto-login
        openChat();
    }

    public void openChat() {
        Intent intent = new Intent(this, ChatActivity.class);

        try {
            SharkNetEngine.getSharkNet().setActiveProfile(this.profiles.get(index), "");
            SharkNetEngine.getSharkNet().startShark();

        } catch (SharkKBException | JSONException | SharkProtocolNotSupportedException | IOException e) {
            e.printStackTrace();
        }

        startActivity(intent);
    }

    public void previousProfile() throws SharkKBException {
        if (index > 0) {
            index--;
            EditText userid = (EditText) findViewById(R.id.userid);
            assert userid != null;
            userid.setText(this.profiles.get(index).getNickname());
            Toast.makeText(this, "back", Toast.LENGTH_SHORT).show();
        } else {
            index = this.profiles.size() - 1;
            EditText userid = (EditText) findViewById(R.id.userid);
            assert userid != null;
            userid.setText(this.profiles.get(index).getNickname());
        }
    }


    public void nextProfile() throws SharkKBException {
        if (index < this.profiles.size() - 1) {
            index++;
            EditText userid = (EditText) findViewById(R.id.userid);
            assert userid != null;
            userid.setText(this.profiles.get(index).getNickname());
        } else {
            index = 0;
            EditText userid = (EditText) findViewById(R.id.userid);
            assert userid != null;
            userid.setText(this.profiles.get(index).getNickname());
        }
    }
}
