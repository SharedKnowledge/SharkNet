package net.sharksystem.sharknet.activities;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

import static net.sharksystem.sharknet.activities.ChatDetailActivity.CHAT_ID;

public class ChatDetailSettings extends AppCompatActivity implements View.OnClickListener
{

    private String chatID;
    public static final String CHAT_ID = "CHAT_ID";
    private net.sharksystem.api.interfaces.Chat chat ;
    private ImageView chatPicture;
    private TextView chatTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState)
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
        setContentView(R.layout.activity_chat_detail_settins);
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
                if(Objects.equals(chat.getID(), chatID))
                {
                    try {
                        this.chat = chat;

                        chat.getContacts();
                        chat.getAdmin();
                        chat.getPicture();
                        chat.getOwner();
                        chat.getTitle();
//                        chat.setAdmin();
//                        chat.setPicture();
                    } catch (SharkKBException e) {
                        e.printStackTrace();
                    }
                }
            } catch (SharkKBException e) {
                e.printStackTrace();
            }
            try {
                if(this.chat.getPicture().getInputStream() != null)
                {

                    Content image = this.chat.getPicture();
                    try {
                        if(image.getInputStream().available() <= 0)
                        {

                        }
                        else
                        {
                            chatPicture.setImageBitmap(BitmapFactory.decodeStream(image.getInputStream()));
                        }
                    } catch (SharkKBException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (SharkKBException e) {
                e.printStackTrace();
            }

            try {
                chatTitle.setText(chat.getTitle());
            } catch (SharkKBException e) {
                e.printStackTrace();
            }


        }

//        SharkNetEngine.getSharkNet().getChats().get()
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btn_leave_chat:


        }

    }
}
