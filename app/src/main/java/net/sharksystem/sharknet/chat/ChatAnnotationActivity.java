package net.sharksystem.sharknet.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import net.sharksystem.api.models.Message;
import net.sharksystem.sharknet.BaseActivity;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.SharkApp;
import net.sharksystem.sharknet.broadcast.BroadcastActivity;

/**
 * Created by Dustin Feurich on 28.09.2017.
 */

public class ChatAnnotationActivity extends BaseActivity {

    private SharkApp application;
    private EditText textSI;
    private EditText textName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutResource(R.layout.chat_annotation_activity);
        textSI = (EditText) findViewById(R.id.annotation_si);
        textName = (EditText) findViewById(R.id.annotation_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            textSI.setText(extra.getString(BroadcastActivity.EXTRA_MESSAGE));
        }

        final ImageButton saveButton = (ImageButton) findViewById(R.id.imageButtonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                if (!TextUtils.isEmpty(textSI.getText().toString())) {
                    returnIntent.putExtra("result", textSI.getText().toString());
                    setResult(Activity.RESULT_OK, returnIntent);
                    System.out.println("_________ " + textSI.getText() + " ___________BBBB");
                    finish();
                }
                else {
                    setResult(Activity.RESULT_CANCELED, returnIntent);
                    finish();
                }

            }
        });

        //final TextView textView = (TextView) findViewById(R.id.text_view_top);
        //textView.setText(intent.getStringExtra(ChatDetailActivity.EXTRA_MESSAGE));
    }
}
