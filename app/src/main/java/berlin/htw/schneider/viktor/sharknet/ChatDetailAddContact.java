package berlin.htw.schneider.viktor.sharknet;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.interfaces.Contact;

import java.util.ArrayList;
import java.util.List;

public class ChatDetailAddContact extends AppCompatActivity
{
    private List<Contact> contacts;
    private List<Contact> selected_contacts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail_add_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        selected_contacts = new ArrayList<>();
        try {
            this.contacts = MainActivity.implSharkNet.getContacts();
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
        // TODO: nimmt Timo noch raus den einen Contact
        try {
            contacts.remove(MainActivity.implSharkNet.getMyProfile());
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
        ConListAdapterNewChat chatListAdapter = new ConListAdapterNewChat(this, R.layout.line_item_con_new_chat,contacts);
        final ListView lv = (ListView)findViewById(R.id.con_list_view);
        if (lv != null)
        {
            lv.setAdapter(chatListAdapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    if(selected_contacts != null)
                    {
                        if(!selected_contacts.contains(contacts.get(position)))
                        {
                            selected_contacts.add(contacts.get(position));
                            lv.getChildAt(position).setBackgroundColor(Color.rgb(255,64,124));
                        }
                        else
                        {
                            selected_contacts.remove(contacts.get(position));
                            lv.getChildAt(position).setBackgroundColor(Color.WHITE);

                        }
                    }
                    else
                    {
                        selected_contacts.add(contacts.get(position));
                        lv.getChildAt(position).setBackgroundColor(Color.rgb(255,64,124));
                    }
                }
            });
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
