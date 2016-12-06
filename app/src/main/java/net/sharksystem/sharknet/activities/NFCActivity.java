package net.sharksystem.sharknet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.sharknet.R;

public class NFCActivity extends AppCompatActivity implements View.OnClickListener{

    private String con_nickname;
    private static final String CONTACT_NICKNAME = "CONTACT_NICKNAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.con_nickname = getIntent().getStringExtra("CONTACT_NICKNAME");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NFCActivity.this, ContactsDetailViewActivity.class);
                //identifies the chat
                intent.putExtra(CONTACT_NICKNAME, con_nickname);
                startActivity(intent);
            }
        });

    }

    /**
     * click Back-Button on Phone
     * */
    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(NFCActivity.this, ContactsDetailViewActivity.class);
        //identifies the chat
        intent.putExtra(CONTACT_NICKNAME, con_nickname);
        startActivity(intent);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId()) {
            case R.id.home:
                onBackPressed();
                break;
        }
    }
}
