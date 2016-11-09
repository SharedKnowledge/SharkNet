package berlin.htw.schneider.viktor.sharknet.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import berlin.htw.schneider.viktor.sharknet.adapters.ChatNewConListAdapter;
import berlin.htw.schneider.viktor.sharknet.R;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.interfaces.Contact;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ChatNewAcivity extends AppCompatActivity {

    private List<Contact> contacts;
    private List<Contact> selected_contacts;
    private ListView lv;

    @Override
    protected void onResume() {
        super.onResume();
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chat_new,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.save:
                View v;
                CheckBox ck;
                for (int i = 0; i < lv.getCount(); i++)
                {
                    v = lv.getChildAt(i);
                    ck = (CheckBox) v.findViewById(R.id.checkBox);
                    if (ck.isChecked())
                    {
                        selected_contacts.add(contacts.get(i));

                        try {
                            Log.d("CHECKED",contacts.get(i).getNickname());
                        } catch (SharkKBException e) {
                            e.printStackTrace();
                        }
                    }

//                    if (checked.get(i))
//                    {
//                        selected_contacts.add(contacts.get(i));
//                    }
                }
                if(!selected_contacts.isEmpty())
                {
                    EditText title = (EditText) findViewById(R.id.chat_new_title);
                    net.sharksystem.api.interfaces.Chat c = null;
                    try {
                        c = MainActivity.implSharkNet.newChat(selected_contacts);
                    } catch (SharkKBException | JSONException e) {
                        e.printStackTrace();
                    }

                    assert title != null;
                    if(!title.getText().toString().trim().isEmpty())
                    {
                        try {
                            assert c != null;
                            c.setTitle(title.getText().toString());
                        } catch (SharkKBException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        assert c != null;
                        Log.d("ChatNewID", c.getID());
                    } catch (SharkKBException e) {
                        e.printStackTrace();
                    }
                    try {
                        c.sendMessage(null,"ChatActivity was created by "
                                + MainActivity.implSharkNet.getMyProfile().getNickname(),null);
                    } catch (JSONException | SharkKBException e) {
                        e.printStackTrace();
                    }

                    startActivity(new Intent( ChatNewAcivity.this, ChatActivity.class ));
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
        selected_contacts = new ArrayList<>();
        try {
            this.contacts = MainActivity.implSharkNet.getContacts();
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
        try {
            contacts.remove(MainActivity.implSharkNet.getMyProfile());
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
        ChatNewConListAdapter chatListAdapter = new ChatNewConListAdapter(this, R.layout.line_item_con_new_chat,contacts);
        lv = (ListView)findViewById(R.id.con_list_view);
        if (lv != null)
        {
            lv.setAdapter(chatListAdapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
//                    if(selected_contacts != null)
//                    {
//                        if(!selected_contacts.contains(contacts.get(position)))
//                        {
//                            selected_contacts.add(contacts.get(position));
//                            lv.getChildAt(position).setBackgroundColor(Color.rgb(255,64,124));
//                        }
//                        else
//                        {
//                            selected_contacts.remove(contacts.get(position));
//                            lv.getChildAt(position).setBackgroundColor(Color.WHITE);
//
//                        }
//                    }
//                    else
//                    {
//                        selected_contacts.add(contacts.get(position));
//                        lv.getChildAt(position).setBackgroundColor(Color.rgb(255,64,124));
//                    }
                }
            });
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}