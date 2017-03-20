package net.sharksystem.sharknet.chat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.interfaces.Chat;
import net.sharksystem.api.interfaces.Contact;
import net.sharksystem.api.interfaces.Message;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.RxSingleBaseActivity;
import net.sharksystem.sharknet.SharkApp;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by j4rvis on 3/5/17.
 */

public class ChatDetailActivity extends RxSingleBaseActivity<List<ChatDetailActivity.MessageDataHolder>> {
    private ChatDetailMsgListAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private Chat mChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureLayout();
        setProgressMessage("Lade Nachrichten...");
        startSubscription();
    }

    @Override
    public void onBackPressed() {
        getSharkApp().resetChat();
        super.onBackPressed();
    }

    private void configureLayout() {
        setLayoutResource(R.layout.chat_detail_activity);
        setOptionsMenu(R.menu.chat_detail_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecyclerView = (RecyclerView) findViewById(R.id.chat_msg_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new ChatDetailMsgListAdapter(this, (SharkApp) getApplication());
        mRecyclerView.setAdapter(mAdapter);

        final CardView sendButton = (CardView) findViewById(R.id.message_send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.message_edit_text);

                String msg_string;

                if (editText != null) {
                    msg_string = editText.getText().toString().trim();

                    if (msg_string.isEmpty()) {
                        Snackbar.make(sendButton, "No message entered!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    } else {
                        try {
                            mChat.sendMessage(msg_string);
                            editText.getText().clear();
//                            mAdapter.setMessages(mChat.getMessages(false));
                            startSubscription();
                            mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
                        } catch (JSONException | SharkKBException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    @Override
    protected List<MessageDataHolder> doOnBackgroundThread() throws Exception {
        if (mChat == null) {
            mChat = getSharkApp().getChat();
        }
        // TODO what if we reload the messages?
        setTitle(mChat.getTitle());

        List<Message> messages = mChat.getMessages(false);
        ArrayList<MessageDataHolder> list = new ArrayList<>();
        for (Message message : messages) {
            String messageContent = message.getContent().getMessage();
            Bitmap authorImage = null;
            if (message.getSender().getPicture() != null) {
                authorImage = BitmapFactory.decodeStream(message.getSender().getPicture().getInputStream());
            }
            int stateResource;
            if (message.isVerified()) {
                stateResource = R.drawable.ic_verified_user_green_24dp;
            } else if (message.isSigned()) {
                stateResource = R.drawable.ic_warning_dark_grey_24dp;
            } else {
                stateResource = R.drawable.ic_warning_red_24dp;
            }
            Contact contact = message.getSender();
            String authorName = contact.getName();
            SimpleDateFormat format = new SimpleDateFormat("d. MMM yyyy, HH:mm");
            String date = format.format(message.getDateReceived());

            boolean isEncrypted = message.isEncrypted();
            boolean isMine = message.isMine();

            list.add(new MessageDataHolder(message, contact, authorImage, authorName, messageContent, date, stateResource, isEncrypted, isMine));
        }
        return list;
    }

    @Override
    protected void doOnUIThread(List<MessageDataHolder> list) {
        mAdapter.setMessages(list);
    }

    @Override
    protected void doOnError(Throwable error) {
        error.printStackTrace();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Reset clicked data
                getSharkApp().resetChat();
                this.finish();
                return true;
            case R.id.chat_attachment_take_photo:
                return true;
            case R.id.chat_settings:
                startActivity(new Intent(this, ChatSettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class MessageDataHolder {
        Message message;
        Contact contact;
        Bitmap authorImageBitMap;
        String authorName;
        String messageContent;
        String date;
        int stateResource;
        boolean isEncrypted;
        boolean isMine;

        private MessageDataHolder(Message message, Contact contact, Bitmap authorImageBitMap, String authorName, String messageContent, String date, int stateResource, boolean isEncrypted, boolean isMine) {
            this.message = message;
            this.contact = contact;
            this.authorImageBitMap = authorImageBitMap;
            this.authorName = authorName;
            this.messageContent = messageContent;
            this.date = date;
            this.stateResource = stateResource;
            this.isEncrypted = isEncrypted;
            this.isMine = isMine;
        }
    }
}
