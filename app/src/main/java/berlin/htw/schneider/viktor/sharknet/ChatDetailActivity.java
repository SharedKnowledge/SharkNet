package berlin.htw.schneider.viktor.sharknet;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
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

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ChatDetailActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private net.sharksystem.api.interfaces.Chat chat ;
    private MsgListAdapter msgListAdapter;
    ImageView image_capture;

    private ImageButton send, record;
    private String dir_photo;
    private int TAKE_PHOTO_CODE = 0;
    private String file_path;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);
        image_capture  = (ImageView) findViewById(R.id.image_capture);
        // Here, we are making a folder named picFolder to store
        // pics taken by the camera using this application.
        this.dir_photo = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/SharkNet/";
        File newdir = new File(dir_photo);
        newdir.mkdirs();


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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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


            File image = new File(file_path);
            Log.d("IMAGE_ERROR",file_path);
            Bitmap bmp = BitmapFactory.decodeFile(image.getPath());
            assert image_capture != null;
            assert bmp != null;
            image_capture.setImageBitmap(bmp);
            Log.d("CameraDemo", "Pic saved");
        }
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
