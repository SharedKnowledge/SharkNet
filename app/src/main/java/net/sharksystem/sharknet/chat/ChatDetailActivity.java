package net.sharksystem.sharknet.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import net.sharksystem.api.dao_impl.SharkNetApi;
import net.sharksystem.api.models.Chat;
import net.sharksystem.api.models.Message;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.RxSingleBaseActivity;

import java.util.List;

/**
 * Created by j4rvis on 3/5/17.
 */

public class ChatDetailActivity extends RxSingleBaseActivity<List<Message>> {
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
        mAdapter = new ChatDetailMsgListAdapter(this, getSharkApp());
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
                        Message message = new Message(SharkNetApi.getInstance().getAccount());
                        message.setContent(msg_string);
                        mChat.addMessage(message);
                        // TODO Save message
                        SharkNetApi.getInstance().updateChat(mChat);
                        editText.getText().clear();
                        startSubscription();
                        mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
                    }
                }
            }
        });
    }

    @Override
    protected List<Message> doOnBackgroundThread() throws Exception {
        if (mChat == null) {
            mChat = getSharkApp().getChat();
        }
        // TODO what if we reload the messages?
        setTitle(mChat.getTitle());

        return mChat.getMessages();

//        List<Message> messages = mChat.getMessages(false);
//        ArrayList<MessageDataHolder> list = new ArrayList<>();
//        for (Message message : messages) {
//            String messageContent = message.getContent().getMessage();
//            Bitmap authorImage = null;
//            if (message.getSender().getPicture() != null) {
//                authorImage = BitmapFactory.decodeStream(message.getSender().getPicture().getInputStream());
//            }
//            int stateResource;
//            if (message.isVerified()) {
//                stateResource = R.drawable.ic_verified_user_green_24dp;
//            } else if (message.isSigned()) {
//                stateResource = R.drawable.ic_warning_dark_grey_24dp;
//            } else {
//                stateResource = R.drawable.ic_warning_red_24dp;
//            }
//            Contact contact = message.getSender();
//            String authorName = contact.getName();
//            SimpleDateFormat format = new SimpleDateFormat("d. MMM yyyy, HH:mm");
//            String date = format.format(message.getDateReceived());
//
//            boolean isEncrypted = message.isEncrypted();
//            boolean isMine = message.isMine();
//
//            list.add(new MessageDataHolder(message, contact, authorImage, authorName, messageContent, date, stateResource, isEncrypted, isMine));
//        }
//        return list;
    }

    @Override
    protected void doOnUIThread(List<Message> list) {
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
}
