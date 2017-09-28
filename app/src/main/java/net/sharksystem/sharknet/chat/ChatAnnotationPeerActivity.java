package net.sharksystem.sharknet.chat;

import android.content.Intent;
import android.os.Bundle;

import net.sharksystem.sharknet.BaseActivity;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.SharkApp;

/**
 * Created by Dustin Feurich on 28.09.2017.
 */

public class ChatAnnotationPeerActivity extends BaseActivity {

    private SharkApp application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutResource(R.layout.chat_annotation_peer_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();

        //final TextView textView = (TextView) findViewById(R.id.text_view_top);
        //textView.setText(intent.getStringExtra(ChatDetailActivity.EXTRA_MESSAGE));
    }
}
