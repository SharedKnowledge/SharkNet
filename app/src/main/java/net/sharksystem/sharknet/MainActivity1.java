package net.sharksystem.sharknet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import net.sharkfw.asip.engine.serializer.SharkProtocolNotSupportedException;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.system.L;
import net.sharksystem.api.impl.SharkNetEngine;
import net.sharksystem.sharknet.chat.ChatActivity;
import net.sharksystem.sharknet.dummy.Dummy;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class MainActivity1 extends ParentActivity {

    int index = 0;
    private List<net.sharksystem.api.interfaces.Profile> profiles = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.setLogLevel(L.LOGLEVEL_ALL);

        SharkNetEngine.getSharkNet().setContext(this);

        try {
            Dummy.createDummyData(this);
            this.profiles = SharkNetEngine.getSharkNet().getProfiles();
        } catch (SharkKBException | JSONException | InterruptedException | IOException e) {
            e.printStackTrace();
        }
        openChat();

        setContentView(R.layout.activity_main);
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
