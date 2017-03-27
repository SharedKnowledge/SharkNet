package net.sharksystem.sharknet.chat;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import net.sharksystem.api.models.Message;
import net.sharksystem.sharknet.BaseActivity;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.SharkApp;

/**
 * Created by j4rvis on 3/10/17.
 */

public class ChatMessageDetailActivity extends BaseActivity {

    private SharkApp application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutResource(R.layout.chat_msg_detail_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        application = getSharkApp();
        Message message = application.getMessage();

        ((TextView) findViewById(R.id.text_view_content)).setText(message.getContent());
        ((TextView) findViewById(R.id.text_view_sender)).setText(message.getSender().getName());
        ((TextView) findViewById(R.id.text_view_encrypted)).setText(String.valueOf(message.isEncrypted()));
        ((TextView) findViewById(R.id.text_view_signed)).setText(String.valueOf(message.isSigned()));
        ((TextView) findViewById(R.id.text_view_verified)).setText(String.valueOf(message.isVerified()));
//            ((TextView) findViewById(R.id.text_view_disliked)).setText(String.valueOf(message.isDisliked()));

    }

    @Override
    public void onBackPressed() {
        application.resetMessage();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Reset clicked data
                application.resetMessage();
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
