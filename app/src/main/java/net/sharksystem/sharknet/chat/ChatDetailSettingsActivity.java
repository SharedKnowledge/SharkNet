package net.sharksystem.sharknet.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.impl.SharkNetEngine;
import net.sharksystem.api.interfaces.Chat;
import net.sharksystem.sharknet.BaseActivity;

import java.util.List;

public class ChatDetailSettingsActivity extends BaseActivity implements View.OnClickListener {

    private String chatID;
    private Chat chat;
    private ImageView chatPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState != null) {
            // Restore value of members from saved state
        }

        List<Chat> chats = null;
        try {
            chats = SharkNetEngine.getSharkNet().getChats();
        } catch (SharkKBException e) {
            e.printStackTrace();
        }

        assert chats != null;
    }

    /**
     * click Back-Button on Phone
     */
    @Override
    public void onBackPressed() {
        Intent returnChatIDIntent = getIntent();
        setResult(Activity.RESULT_OK, returnChatIDIntent);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
    }
}
