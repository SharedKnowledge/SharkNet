package berlin.htw.schneider.viktor.sharknet;


import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.interfaces.Message;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ChatDetailActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private net.sharksystem.api.interfaces.Chat chat ;
    private MsgListAdapter msgListAdapter;

    private ImageButton send, record;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);

        send = (ImageButton) findViewById(R.id.send_button);
        record = (ImageButton) findViewById(R.id.record);
        String chatID = getIntent().getStringExtra(Chat.CHAT_ID);

        List<Message> msgs = new ArrayList<>();
        List<net.sharksystem.api.interfaces.Chat> chats = null;
        try {
            chats = MainActivity.implSharkNet.getChats();
        } catch (SharkKBException e) {
            e.printStackTrace();
        }

        Toolbar t = (Toolbar) findViewById(R.id.toolbar_chatdetail);
        setSupportActionBar(t);

        assert chats != null;
        for(net.sharksystem.api.interfaces.Chat chat : chats)
        {
            try {
                if(Objects.equals(chat.getID(), chatID))
                {
                    try {
                        msgs = chat.getMessages(false);
                    } catch (SharkKBException e) {
                        e.printStackTrace();
                    }
                    this.chat = chat;
                    try {
                        getSupportActionBar().setTitle(this.chat.getTitle());
                    } catch (SharkKBException e) {
                        e.printStackTrace();
                    }
                }
            } catch (SharkKBException e) {
                e.printStackTrace();
            }
        }

        this.msgListAdapter = new MsgListAdapter(msgs);
        RecyclerView lv = (RecyclerView)findViewById(R.id.msg_list_view);
        if (lv != null)
        {
            LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
            lv.setLayoutManager(llm);
            lv.setItemAnimator(new DefaultItemAnimator());
            lv.setAdapter(msgListAdapter);
        }

        EditText msg_text = (EditText) findViewById(R.id.write_msg_edit_text);
        //Typeface type = Typeface.createFromAsset(getAssets(),"fonts/RockSalt.TTF");
        ImageButton b = (ImageButton) findViewById(R.id.send_button);
        assert b != null;
        //b.setTypeface(type);
        assert msg_text != null;
        msg_text.addTextChangedListener(TextEditorWatcher);
        //msg_text.setTypeface(type);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private final TextWatcher  TextEditorWatcher = new TextWatcher() {

        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {
            // When No Password Entered

        }

        public void onTextChanged(CharSequence s, int start, int before, int count)
        {

        }

        public void afterTextChanged(Editable s)
        {
            String t = s.toString().trim();
            if(t.length()==0)
            {
                //TODO: Button soll von Send zu Micro sich ändern
                send.setVisibility(View.GONE);
                record.setVisibility(View.VISIBLE);
            }
            else
            {
                send.setVisibility(View.VISIBLE);
                record.setVisibility(View.GONE);
            }

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.chat_detail, menu);
        return true;
    }

    public void sendMessage(View view) throws JSONException, SharkKBException {
        EditText msg_text = (EditText) findViewById(R.id.write_msg_edit_text);

        String msg_string;

        if (msg_text != null)
        {
            msg_string = msg_text.getText().toString().trim();

            if(msg_string.isEmpty())
            {
                Snackbar.make(view, "No message entered!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
            else
            {
                chat.sendMessage(null,msg_string,null);
                this.chat.update();
                this.msgListAdapter.notifyDataSetChanged();
                msg_text.getText().clear();
                for(net.sharksystem.api.interfaces.Chat c: MainActivity.implSharkNet.getChats())
                {
                    if(c.getID()==this.chat.getID())
                    {
                        this.msgListAdapter = new MsgListAdapter(this.chat.getMessages(false));
                        RecyclerView lv = (RecyclerView)findViewById(R.id.msg_list_view);
                        if (lv != null)
                        {
                            LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                            lv.setLayoutManager(llm);
                            lv.setItemAnimator(new DefaultItemAnimator());
                            lv.setAdapter(msgListAdapter);
                            lv.scrollToPosition(this.chat.getMessages(false).size()-1);
                        }
                    }
                }
            }
        }


    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }

    public void recordAudio(View view)
    {

    }

    public void takePicture(View view)
    {

    }

    public void sendFile(View view)
    {

    }

    public void addContact(View view)
    {

    }

    public void sendPicture(View view)
    {

    }
}
