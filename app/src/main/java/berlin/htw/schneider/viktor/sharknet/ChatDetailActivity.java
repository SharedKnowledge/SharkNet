package berlin.htw.schneider.viktor.sharknet;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.interfaces.Message;
import org.json.JSONException;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ChatDetailActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final int ADD_CONTACT = 1050;
    private net.sharksystem.api.interfaces.Chat chat ;
    private MsgListAdapter msgListAdapter;
    ImageView image_capture;
    private String chatID;
    public static final String CHAT_ID = "CHAT_ID";

    private ImageButton send, record;
    private String dir_photo;
    private int TAKE_PHOTO_CODE = 0;
    private static final int SELECT_PHOTO = 100;
    private static final int SELECT_FILE = 101;
    private String file_path;
    private List<Message> msgs ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null)
        {
            // Restore value of members from saved state
            chatID = savedInstanceState.getString(CHAT_ID);
            Log.d("!!!CREATE","########## Aus SaveInstanceSTATE ##########");
            Log.d("!!!CREATE","########## "+chatID+" ##########");
        }
        else
        {
            chatID = getIntent().getStringExtra(Chat.CHAT_ID);
            Log.d("!!!CREATE","########## Aus EXTRA ###########");
        }
        setContentView(R.layout.activity_chat_detail);
        this.image_capture  = (ImageView) findViewById(R.id.image_capture);
        // Here, we are making a folder named picFolder to store
            Log.d("!!!CREATE","########## "+chatID+" ##########");
        // pics taken by the camera using this application.
        this.dir_photo = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/SharkNet/";
        File newdir = new File(dir_photo);
        newdir.mkdirs();


        send = (ImageButton) findViewById(R.id.send_button);
        record = (ImageButton) findViewById(R.id.record);


        msgs = new ArrayList<>();
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
            llm.setStackFromEnd(true);
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

    public void sendMessage(View view) throws JSONException, SharkKBException
    {
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
                this.chat.sendMessage(null,msg_string,null);
                this.chat.update();
                this.msgListAdapter.notifyDataSetChanged();
                msg_text.getText().clear();
                for(net.sharksystem.api.interfaces.Chat c: MainActivity.implSharkNet.getChats())
                {
                    if(Objects.equals(c.getID(), this.chat.getID()))
                    {
                        this.msgListAdapter = new MsgListAdapter(this.chat.getMessages(false));
                        RecyclerView lv = (RecyclerView)findViewById(R.id.msg_list_view);
                        if (lv != null)
                        {
                            LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                            lv.setLayoutManager(llm);
                            llm.setStackFromEnd(true);
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
        //TODO: waiting for the bug-fix
    }

    public void takePicture(View view)
    {
        SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
        String format = s.format(new java.util.Date());
        this.file_path = this.dir_photo+format+".png";
        File newfile = new File(file_path);
        try {
            newfile.createNewFile();
        }
        catch (IOException e)
        {
        }
        int hasWriteContactsPermission = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hasWriteContactsPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        123);
            }
            return;
        }
        Uri outputFileUri = Uri.fromFile(newfile);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);


        if( requestCode == SELECT_FILE)
        {
            //TODO:
            if (resultCode == RESULT_OK)
            {
                Uri file = data.getData();
                InputStream fileStream = null;

                // TODO: file-name soll noch angezeigt werden
                // alles nach den letzten "/" anzeigen
                // vielleicht verschiedene icons für pdf usw anzeigen lassen

                Log.d("FILE_SELECTED",file.toString());
                try {
                    fileStream = getContentResolver().openInputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    this.chat.sendMessage(fileStream,"File","file/doc");
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (SharkKBException e) {
                    e.printStackTrace();
                }
            }
        }
        if( requestCode == SELECT_PHOTO)
        {
            if (resultCode == RESULT_OK)
            {
                Uri selectedImage = data.getData();
                InputStream imageStream = null;
                try {
                    imageStream = getContentResolver().openInputStream(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    this.chat.sendMessage(imageStream,"Bild","image/png");
                } catch (JSONException | SharkKBException e) {
                    e.printStackTrace();
                }
                this.chat.update();
                this.msgListAdapter.notifyDataSetChanged();

                try {
                    for(net.sharksystem.api.interfaces.Chat c: MainActivity.implSharkNet.getChats())
                    {
                        if(Objects.equals(c.getID(), this.chat.getID()))
                        {
                            this.msgListAdapter = new MsgListAdapter(this.chat.getMessages(false));
                            RecyclerView lv = (RecyclerView)findViewById(R.id.msg_list_view);
                            if (lv != null)
                            {
                                LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                                lv.setLayoutManager(llm);
                                llm.setStackFromEnd(true);
                                lv.setItemAnimator(new DefaultItemAnimator());
                                lv.setAdapter(msgListAdapter);
                                lv.scrollToPosition(this.chat.getMessages(false).size()-1);
                            }
                        }
                    }
                } catch (SharkKBException e) {
                    e.printStackTrace();
                }
            }
        }

        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK)
        {
            int hasWriteContactsPermission = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                hasWriteContactsPermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                            123);
                }
                return;
            }
            Drawable pic = Drawable.createFromPath(file_path);
            this.image_capture  = (ImageView) findViewById(R.id.image_capture);
            if (image_capture != null)
            {
                image_capture.setImageDrawable(pic);
            }
            try {
                this.chat.sendMessage(new FileInputStream(file_path),"Bild","image/png");
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (SharkKBException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            this.chat.update();
            this.msgListAdapter.notifyDataSetChanged();

            try {
                for(net.sharksystem.api.interfaces.Chat c: MainActivity.implSharkNet.getChats())
                {
                    if(Objects.equals(c.getID(), this.chat.getID()))
                    {
                        this.msgListAdapter = new MsgListAdapter(this.chat.getMessages(false));
                        RecyclerView lv = (RecyclerView)findViewById(R.id.msg_list_view);
                        if (lv != null)
                        {
                            LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                            lv.setLayoutManager(llm);
                            llm.setStackFromEnd(true);
                            lv.setItemAnimator(new DefaultItemAnimator());
                            lv.setAdapter(msgListAdapter);
                            lv.scrollToPosition(this.chat.getMessages(false).size()-1);
                        }
                    }
                }
            } catch (SharkKBException e) {
                e.printStackTrace();
            }

        }

        this.msgListAdapter.notifyDataSetChanged();
        try {
            for(net.sharksystem.api.interfaces.Chat c: MainActivity.implSharkNet.getChats())
            {
                if(Objects.equals(c.getID(), this.chat.getID()))
                {
                    this.msgListAdapter = new MsgListAdapter(this.chat.getMessages(false));
                    RecyclerView lv = (RecyclerView)findViewById(R.id.msg_list_view);
                    if (lv != null)
                    {
                        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                        lv.setLayoutManager(llm);
                        llm.setStackFromEnd(true);
                        lv.setItemAnimator(new DefaultItemAnimator());
                        lv.setAdapter(msgListAdapter);
                        lv.scrollToPosition(this.chat.getMessages(false).size()-1);
                    }
                }
            }
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
    }

    public void sendFile(View view)
    {
        Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        fileIntent.setType("file/*"); // intent type to filter application based on your requirement
        startActivityForResult(fileIntent, SELECT_FILE);
    }

    public void addContact(View view)
    {
        //TODO: beim back-click kein chatverlauf mehr
        Intent addOtherContact = new Intent(this, ChatDetailAddContact.class);
        addOtherContact.putExtra(Chat.CHAT_ID,chatID);
        startActivityForResult(addOtherContact,ADD_CONTACT);
//        startActivity(addOtherContact);
    }

    public void sendPicture(View view)
    {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(CHAT_ID, getIntent().getStringExtra(Chat.CHAT_ID));
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState)
    {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance
        chatID = savedInstanceState.getString(CHAT_ID);
    }
}
