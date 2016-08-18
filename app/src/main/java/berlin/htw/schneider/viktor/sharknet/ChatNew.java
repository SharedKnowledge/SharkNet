package berlin.htw.schneider.viktor.sharknet;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.sharknet_api_android.dummy_impl.ImplContent;
import net.sharksystem.sharknet_api_android.interfaces.*;


import java.util.ArrayList;
import java.util.List;

public class ChatNew extends AppCompatActivity {

    private List<Contact> contacts;
    private List<Contact> selcted_contacts;

    @Override
    protected void onResume() {
        super.onResume();
        selcted_contacts = new ArrayList<>();
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_detail_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.profile_save:
                if(!selcted_contacts.isEmpty())
                {
                    EditText title = (EditText) findViewById(R.id.chat_new_title);
                    net.sharksystem.sharknet_api_android.interfaces.Chat c = null;
                    try {
                        c = MainActivity.implSharkNet.newChat(selcted_contacts);
                    } catch (SharkKBException e) {
                        e.printStackTrace();
                    }

                    assert title != null;
                    if(!title.getText().toString().trim().isEmpty())
                    {
                        try {
                            c.setTitle(title.getText().toString());
                        } catch (SharkKBException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.d("ChatNewID", String.valueOf(c.getID()));
                    // TODO: geht leider wegen der api noch nicht so richtig
                    try {
                        c.sendMessage(new ImplContent("Chat was created by "
                                +MainActivity.implSharkNet.getMyProfile().getNickname(),MainActivity.implSharkNet.getMyProfile()));
                    } catch (SharkKBException e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent( ChatNew.this, Chat.class ));
                }
                else
                {
                    //TODO: soll den user mit Snackbar angezeigt werden
                    Log.d("NewChat","kein Kontakt ausgewählt");
                }

                return true;



            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_new);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        selcted_contacts = new ArrayList<>();
        this.contacts = MainActivity.implSharkNet.getContacts();
        // TODO: nimmt Timo noch raus den einen Contact
        contacts.remove(MainActivity.implSharkNet.getMyProfile());
        ConListAdapterNewChat chatListAdapter = new ConListAdapterNewChat(this,R.layout.line_item_con_new_chat,contacts);
        final ListView lv = (ListView)findViewById(R.id.con_list_view);
        if (lv != null)
        {
            lv.setAdapter(chatListAdapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    if(selcted_contacts != null)
                    {
                        if(!selcted_contacts.contains(contacts.get(position)))
                        {
                            selcted_contacts.add(contacts.get(position));
                            lv.getChildAt(position).setBackgroundColor(Color.rgb(255,64,124));
                        }
                        else
                        {
                            selcted_contacts.remove(contacts.get(position));
                            lv.getChildAt(position).setBackgroundColor(Color.WHITE);

                        }
                    }
                    else
                    {
                        selcted_contacts.add(contacts.get(position));
                        lv.getChildAt(position).setBackgroundColor(Color.rgb(255,64,124));
                    }
                }
            });
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
