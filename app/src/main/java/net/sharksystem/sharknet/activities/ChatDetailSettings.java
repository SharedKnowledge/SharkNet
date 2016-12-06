package net.sharksystem.sharknet.activities;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.impl.SharkNetEngine;
import net.sharksystem.api.interfaces.Chat;
import net.sharksystem.api.interfaces.Content;
import net.sharksystem.sharknet.R;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ChatDetailSettings extends AppCompatActivity implements View.OnClickListener
{

    private String chatID;
    public static final String CHAT_ID = "CHAT_ID";
    private Chat chat;
    private ImageView chatPicture;
    private TextView chatTitle;
    private TextView chatOwner;
    private Button btn_leave_chat;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatDetailSettings.this, ChatDetailActivity.class);
                //identifies the chat
                intent.putExtra(CHAT_ID, chatID);
                startActivity(intent);
            }
        });

        btn_leave_chat = (Button) findViewById(R.id.btn_leave_chat);
        btn_leave_chat.setOnClickListener(this);

        if (savedInstanceState != null)
        {
            // Restore value of members from saved state
            chatID = savedInstanceState.getString(CHAT_ID);
        }
        else
        {
            chatID = getIntent().getStringExtra(ChatActivity.CHAT_ID);
        }

        List<Chat> chats = null;
        try {
            chats = SharkNetEngine.getSharkNet().getChats();
        } catch (SharkKBException e) {
            e.printStackTrace();
        }

        assert chats != null;
        for(net.sharksystem.api.interfaces.Chat chat : chats)
        {
            try {
                if (Objects.equals(chat.getID(), chatID)) {
                    this.chat = chat;
                }
            } catch (SharkKBException e) {
                e.printStackTrace();
            }
        }
//            TODO: inputstream bwz. Content ist immer leer
//            Content image = this.chat.getPicture();
//            chatPicture.setImageBitmap(BitmapFactory.decodeStream(image.getInputStream()));
        chatTitle = (TextView) findViewById(R.id.ChatName);
        try {
            chatTitle.setText(chat.getTitle());
        } catch (SharkKBException e) {
            e.printStackTrace();
        }

        chatOwner= (TextView) findViewById(R.id.chat_owner);
        try {
            chatOwner.setText(chat.getOwner().getNickname());
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
    }

    /**
     * click Back-Button on Phone
     * */
    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(ChatDetailSettings.this, ChatDetailActivity.class);
        //identifies the chat
        intent.putExtra(CHAT_ID, chatID);
        startActivity(intent);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.home:
                onBackPressed();
                break;
            case R.id.btn_leave_chat:
                this.chat.delete();
                Intent intent = new Intent(ChatDetailSettings.this, ChatActivity.class);
                startActivity(intent);
        }
    }
}
