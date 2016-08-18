package berlin.htw.schneider.viktor.sharknet;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.TXSemanticTag;
import net.sharksystem.sharknet_api_android.*;
import net.sharksystem.sharknet_api_android.dummy_impl.ImplSharkNet;
import net.sharksystem.sharknet_api_android.interfaces.Profile;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static ImplSharkNet implSharkNet;
    private List<Profile> profiles = null;
    int index ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        implSharkNet = new ImplSharkNet();
        implSharkNet.fillWithDummyData();
        index = 0;
        this.profiles = implSharkNet.getProfiles();

        EditText userid = (EditText) findViewById(R.id.userid);
        assert userid != null;
        try {
            userid.setText(this.profiles.get(index).getNickname());
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
        String[][] dummyInterests = {
                {"Sport", "https://de.wikipedia.org/wiki/Sport"},
                {"Musik", "https://de.wikipedia.org/wiki/Musik"},
                {"Literatur", "https://de.wikipedia.org/wiki/Literatur"},
        };

        String[][] sportsTopics = {
                {"Fußball", "https://de.wikipedia.org/wiki/Fußball"},
                {"Handball", "https://de.wikipedia.org/wiki/Handball"},
                {"Turmspringen", "https://de.wikipedia.org/wiki/Turmspringen"},
        };

//        for (int i = 0; i < dummyInterests.length; i++) {
//            TXSemanticTag parentTag = implSharkNet.getMyProfile().getInterests().addInterest(dummyInterests[i][0], dummyInterests[i][0]);
//            if (i == 0) {
//                for(String[] child : sportsTopics) {
//                    implSharkNet.getMyProfile().getInterests().addInterest(child[0], child[1]).move(parentTag);
//                }
//            }
//        }


    }

    public void openChat(View view)
    {
        Intent inbox = new Intent(this, Inbox.class);

        implSharkNet.setActiveProfile(this.profiles.get(index),"");

        /*

        Typeface face= Typeface.createFromAsset(getAssets(),"fonts/RockSalt.ttf");
        NavigationView nv = (NavigationView) findViewById(R.id.nav_view);
        Menu m = nv.getMenu();
        txtV.setTypeface(face);
*/
        startActivity(inbox);
    }

    public void backProfile(View view)
    {
        if(index > 0)
        {
            index--;
            EditText userid = (EditText) findViewById(R.id.userid);
            assert userid != null;
            try {
                userid.setText(this.profiles.get(index).getNickname());
            } catch (SharkKBException e) {
                e.printStackTrace();
            }
            Toast.makeText(this,"back",Toast.LENGTH_SHORT).show();
        }
        else
        {
           index = this.profiles.size()-1;
            EditText userid = (EditText) findViewById(R.id.userid);
            assert userid != null;
            try {
                userid.setText(this.profiles.get(index).getNickname());
            } catch (SharkKBException e) {
                e.printStackTrace();
            }
        }
    }


    public void nextProfile(View view)
    {
        if(index < this.profiles.size()-1)
        {
            index++;
            EditText userid = (EditText) findViewById(R.id.userid);
            assert userid != null;
            try {
                userid.setText(this.profiles.get(index).getNickname());
            } catch (SharkKBException e) {
                e.printStackTrace();
            }
        }
        else
        {
            index = 0;
            EditText userid = (EditText) findViewById(R.id.userid);
            assert userid != null;
            try {
                userid.setText(this.profiles.get(index).getNickname());
            } catch (SharkKBException e) {
                e.printStackTrace();
            }
        }
    }
}
