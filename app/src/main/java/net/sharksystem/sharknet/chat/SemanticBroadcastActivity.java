package net.sharksystem.sharknet.chat;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import net.sharkfw.knowledgeBase.SharkCSAlgebra;
import net.sharkfw.knowledgeBase.SharkKB;
import net.sharkfw.knowledgeBase.sync.manager.SyncComponent;
import net.sharkfw.knowledgeBase.sync.manager.port.SyncMergeKP;
import net.sharkfw.system.L;
import net.sharksystem.api.dao_impl.SharkNetApiImpl;
import net.sharksystem.api.models.Chat;
import net.sharksystem.api.models.Contact;
import net.sharksystem.api.models.Message;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.RxSingleBaseActivity;

import java.util.Calendar;
import java.util.List;




/**
 * Created by j4rvis on 3/5/17.
 */

public class SemanticBroadcastActivity extends RxSingleBaseActivity<List<Message>> implements SyncMergeKP.SyncMergeListener {
    public static final String EXTRA_MESSAGE = "";
    private ChatDetailMsgListAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private Chat mChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureLayout();
        if (mChat == null) {
            mChat = getSharkApp().getChat();
        }
        // TODO what if we reload the messages?
        List<Contact> contacts = mChat.getContacts();
//        contacts.remove(getSharkApp().getAccount());

        if (mChat.getTitle() != null) {
            setTitle(mChat.getTitle());
        } else if (contacts.size() == 1) {
            if(contacts.get(0).equals(getSharkApp().getAccount())){
                setTitle(mChat.getOwner().getName());
            } else {
                setTitle(contacts.get(0).getName());
            }
        }
        L.d(mChat.toString(), this);

        setProgressMessage(R.string.chat_progress_load_messages);
    }

    @Override
    public void onBackPressed() {
        getSharkApp().resetChat();
        super.onBackPressed();
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        super.onServiceConnected(name, service);
        mApi.getSharkEngine().getSyncManager().addSyncMergeListener(this);
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
        final LinearLayout mRevealView = (LinearLayout) findViewById(R.id.reveal_items);
        mRevealView.setVisibility(View.GONE);
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
                        Message message = new Message(mApi.getAccount());
                        message.setContent(msg_string);
                        mChat.addMessage(message);
                        mApi.updateChat(mChat);
                        editText.getText().clear();
                        startSubscription();
                        mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
                    }
                }
            }
        });

        final CardView semanticButton = (CardView) findViewById(R.id.semantic_button);
        semanticButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRevealView.getVisibility() == View.GONE) {
                    mRevealView.setVisibility(View.VISIBLE);
                }
                else {
                    mRevealView.setVisibility(View.GONE);
                }
            }
        });


        final ImageButton topicButton = (ImageButton) findViewById(R.id.imageButtonTopic);
        topicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRevealView.setVisibility(View.GONE);
                Intent intent = new Intent(getApplicationContext(), ChatAnnotationActivity.class);
                intent.putExtra(EXTRA_MESSAGE, "Topic");
                startActivity(intent);
            }
        });

        final ImageButton peerButton = (ImageButton) findViewById(R.id.imageButtonPeer);
        peerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRevealView.setVisibility(View.GONE);
                Intent intent = new Intent(getApplicationContext(), ChatAnnotationPeerActivity.class);
                intent.putExtra(EXTRA_MESSAGE, "Peer");
                startActivity(intent);
            }
        });

        final ImageButton timeButton = (ImageButton) findViewById(R.id.imageButtonTime);
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRevealView.setVisibility(View.GONE);
                Intent intent = new Intent(getApplicationContext(), ChatAnnotationTimeActivity.class);
                intent.putExtra(EXTRA_MESSAGE, "Location");
                startActivity(intent);
                //DialogFragment newFragment = new TimePickerFragment();
                //newFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });

        final ImageButton locationButton = (ImageButton) findViewById(R.id.imageButtonLocation);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRevealView.setVisibility(View.GONE);
                Intent intent = new Intent(getApplicationContext(), ChatAnnotationLocationActivity.class);
                intent.putExtra(EXTRA_MESSAGE, "Location");
                startActivity(intent);
            }
        });
    }

    @Override
    protected List<Message> doOnBackgroundThread() throws Exception {
        return mChat.getMessages();
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

    @Override
    public void onNewMerge(SyncComponent component, SharkKB changes) {
        L.d("Received a new message?", this);
        if(SharkCSAlgebra.identical(mChat.getId(), component.getUniqueName())){
            L.d("Yeah it'S the correct chat!", this);
            mChat = mApi.getChat(mChat.getId());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mChat!=null){
                        mAdapter.setMessages(mChat.getMessages());
                        mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
                    }
                }
            });
        }
    }

}
