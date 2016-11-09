package net.sharksystem.sharknet.activities;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.interfaces.Message;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.adapters.MsgListAdapter;

import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ChatDetailActivity extends AppCompatActivity {

    public static final int ADD_CONTACT = 1050;
    private static final int REQUEST_MICROPHONE = 101;
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
    private MediaRecorder mediaRecorder;





    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null)
        {
            // Restore value of members from saved state
            chatID = savedInstanceState.getString(CHAT_ID);
        }
        else
        {
            chatID = getIntent().getStringExtra(ChatActivity.CHAT_ID);
        }
        setContentView(R.layout.activity_chat_detail);
        this.image_capture  = (ImageView) findViewById(R.id.image_capture);
        // pics taken by the camera using this application.
        this.dir_photo = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/SharkNet/";
        File newdir = new File(dir_photo);
        newdir.mkdirs();

        send = (ImageButton) findViewById(R.id.send_button);
        record = (ImageButton) findViewById(R.id.record);

        record.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                int hasWriteContactsPermission = 0;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    hasWriteContactsPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
                if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED)
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    {
                        requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                123);
                    }
                }
                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(ChatDetailActivity.this,
                            new String[]{Manifest.permission.RECORD_AUDIO},
                            REQUEST_MICROPHONE);
                }
                mediaRecorder = new MediaRecorder();
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                String time = String.valueOf(System.nanoTime());
                file_path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+ time+".mp4";
                mediaRecorder.setOutputFile(file_path);

                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                try {
                    mediaRecorder.prepare();
                } catch (IOException e) {
                    Log.e("MediaRecorterError", "prepare() failed");
                }
                mediaRecorder.start();

                return false;
            }
        });



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

        record.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if(event.getAction()== MotionEvent.ACTION_UP && mediaRecorder != null)
                {
                    mediaRecorder.stop();
                    mediaRecorder.reset();
                    mediaRecorder.release();
                    mediaRecorder=null;

                    String fileExtension = "audio/mp4";
                    File file = new File(file_path);

                    FileInputStream fileStream = null;
                    try {
                        fileStream = new FileInputStream(file
                        );
                    } catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }

                    try {
                        chat.sendMessage(fileStream,file_path,fileExtension);
                    } catch (JSONException | SharkKBException e) {
                        e.printStackTrace();
                    }
                    chat.update();
                    msgListAdapter.notifyDataSetChanged();

                    try {
                        for(net.sharksystem.api.interfaces.Chat c: MainActivity.implSharkNet.getChats())
                        {
                            if(Objects.equals(c.getID(), chat.getID()))
                            {
                                msgListAdapter = new MsgListAdapter(chat.getMessages(false));
                                RecyclerView lv = (RecyclerView)findViewById(R.id.msg_list_view);
                                if (lv != null)
                                {
                                    LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                                    lv.setLayoutManager(llm);
                                    llm.setStackFromEnd(true);
                                    lv.setItemAnimator(new DefaultItemAnimator());
                                    lv.setAdapter(msgListAdapter);
                                    lv.scrollToPosition(chat.getMessages(false).size()-1);
                                }
                            }
                        }
                    } catch (SharkKBException e) {
                        e.printStackTrace();
                    }
                }

                return false;
            }
        });

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

    private final TextWatcher  TextEditorWatcher = new TextWatcher()
    {

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
            if (resultCode == RESULT_OK)
            {
                Uri file = data.getData();
                String fileName = file.getLastPathSegment();
                String fileExtension = "file/unknown";
                if(fileName.contains("."))
                {
                    String [] splitName = fileName.split("\\.");
                    fileExtension = splitName[splitName.length-1];

                }

                InputStream fileStream = null;
                try {
                    fileStream = getContentResolver().openInputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    this.chat.sendMessage(fileStream,fileName,"file/"+fileExtension);
                } catch (JSONException | SharkKBException e) {
                    e.printStackTrace();
                }
            }
        }
        if( requestCode == SELECT_PHOTO)
        {
            if (resultCode == RESULT_OK)
            {
                Uri selectedImage = data.getData();
//                String fileName = selectedImage.getLastPathSegment();
//                Log.d("Picture", selectedImage.getPath());
                InputStream imageStream = null;
                try {
                    imageStream = getContentResolver().openInputStream(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    this.chat.sendMessage(imageStream,selectedImage.getPath(),"image/png");
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
                this.chat.sendMessage(new FileInputStream(file_path),file_path,"image/png");
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
        Intent addOtherContact = new Intent(this, ChatDetailAddContactActivity.class);
        addOtherContact.putExtra(ChatActivity.CHAT_ID,chatID);
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
        savedInstanceState.putString(CHAT_ID, getIntent().getStringExtra(ChatActivity.CHAT_ID));
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
