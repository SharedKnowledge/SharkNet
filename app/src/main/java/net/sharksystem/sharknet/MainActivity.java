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

public class MainActivity extends ParentActivity {

    private List<net.sharksystem.api.interfaces.Profile> profiles = null;
    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        L.setLogLevel(L.LOGLEVEL_ALL);

        SharkNetEngine.getSharkNet().setContext(this);

        startBackgroundTask("Dummy Daten werden erzeugt...");
    }

    @Override
    protected boolean doInBackground() {
        try {
            Dummy.createDummyData(this);
            this.profiles = SharkNetEngine.getSharkNet().getProfiles();
        } catch (SharkKBException | JSONException | InterruptedException e) {
            e.printStackTrace();
        }
        return this.profiles!=null;
    }

    @Override
    protected void doWhenFinished(boolean success) {

        if(success){
            openChat();
        }

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

        EditText userid = (EditText) findViewById(R.id.userid);

        try {
            userid.setText(this.profiles.get(index).getNickname());
        } catch (SharkKBException e) {
            e.printStackTrace();
        }

    }

    public void openChat() {
        Intent intent = new Intent(this, ChatActivity.class);

        try {
            // TODO reactivate
//            SharkNetEngine.getSharkNet().setActiveProfile(this.profiles.get(index), "");
            SharkNetEngine.getSharkNet().startShark();

        } catch (SharkKBException | SharkProtocolNotSupportedException | IOException e) {
            e.printStackTrace();
        }

        startActivity(intent);
    }

    public void previousProfile() throws SharkKBException {
        EditText editText = (EditText) findViewById(R.id.userid);
        if (index > 0) {
            index--;
        } else {
            index = this.profiles.size() - 1;
        }
        editText.setText(this.profiles.get(index).getNickname());
    }


    public void nextProfile() throws SharkKBException {
        EditText editText = (EditText) findViewById(R.id.userid);
        if (index < this.profiles.size() - 1) {
            index++;
        } else {
            index = 0;
        }
        editText.setText(this.profiles.get(index).getNickname());
    }
}
