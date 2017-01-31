package net.sharksystem.sharknet.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.impl.SharkNetEngine;
import net.sharksystem.api.interfaces.Chat;
import net.sharksystem.sharknet.ParentActivity;
import net.sharksystem.sharknet.R;

import java.util.List;
import java.util.Objects;

public class ChatDetailSettings extends ParentActivity implements View.OnClickListener {

    private String chatID;
    private Chat chat;
    private ImageView chatPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutResource(R.layout.chat_detail_settings_activity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button btn_leave_chat = (Button) findViewById(R.id.btn_leave_chat);
        btn_leave_chat.setOnClickListener(this);

        if (savedInstanceState != null) {
            // Restore value of members from saved state
            chatID = savedInstanceState.getString(ChatActivity.CHAT_ID);
        } else {
            chatID = getIntent().getStringExtra(ChatActivity.CHAT_ID);
        }

        List<Chat> chats = null;
        try {
            chats = SharkNetEngine.getSharkNet().getChats();
        } catch (SharkKBException e) {
            e.printStackTrace();
        }

        assert chats != null;
        for (net.sharksystem.api.interfaces.Chat chat : chats) {
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
        TextView chatTitle = (TextView) findViewById(R.id.ChatName);
        try {
            chatTitle.setText(chat.getTitle());
        } catch (SharkKBException e) {
            e.printStackTrace();
        }

        TextView chatOwner = (TextView) findViewById(R.id.chat_owner);
        try {
            chatOwner.setText(chat.getOwner().getNickname());
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
    }

    /**
     * click Back-Button on Phone
     */
    @Override
    public void onBackPressed() {
        Intent returnChatIDIntent = getIntent();
        returnChatIDIntent.putExtra(ChatActivity.CHAT_ID, chatID);
        setResult(Activity.RESULT_OK, returnChatIDIntent);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.btn_leave_chat:
                this.chat.delete();
                Intent intent = new Intent(ChatDetailSettings.this, ChatActivity.class);
                startActivity(intent);
        }
    }
}
