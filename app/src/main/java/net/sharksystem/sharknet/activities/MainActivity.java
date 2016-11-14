package net.sharksystem.sharknet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import net.sharkfw.kep.SharkProtocolNotSupportedException;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.system.L;
import net.sharksystem.api.impl.SharkNetEngine;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.dummy.Dummy;

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

        SharkNetEngine.getSharkNet().setContext(this);

        try {
            Dummy.createDummyData();
        } catch (SharkKBException | JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
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
    }

    public void openChat(View view) {
        Intent inbox = new Intent(this, InboxActivity.class);

        try {
            SharkNetEngine.getSharkNet().setActiveProfile(this.profiles.get(index), "");

            SharkNetEngine.getSharkNet().startShark();
        } catch (SharkKBException | JSONException | SharkProtocolNotSupportedException | IOException e) {
            e.printStackTrace();
        }

        /*

        Typeface face= Typeface.createFromAsset(getAssets(),"fonts/RockSalt.ttf");
        NavigationView nv = (NavigationView) findViewById(R.id.nav_view);
        Menu m = nv.getMenu();
        txtV.setTypeface(face);
*/
        startActivity(inbox);
    }

    public void backProfile(View view) throws SharkKBException {
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


    public void nextProfile(View view) throws SharkKBException {
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
