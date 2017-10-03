package net.sharksystem.sharknet.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import net.sharksystem.api.models.Message;
import net.sharksystem.sharknet.BaseActivity;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.SharkApp;

/**
 * Created by Dustin Feurich on 28.09.2017.
 */

public class ChatAnnotationActivity extends BaseActivity {

    private SharkApp application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutResource(R.layout.chat_annotation_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();

        final ImageButton saveButton = (ImageButton) findViewById(R.id.imageButtonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChatDetailActivity.class);
                startActivity(intent);
            }
        });

        //final TextView textView = (TextView) findViewById(R.id.text_view_top);
        //textView.setText(intent.getStringExtra(ChatDetailActivity.EXTRA_MESSAGE));
    }
}
