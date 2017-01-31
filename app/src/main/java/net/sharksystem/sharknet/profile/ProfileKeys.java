package net.sharksystem.sharknet.profile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.impl.SharkNetEngine;
import net.sharksystem.api.interfaces.Contact;
import net.sharksystem.sharknet.R;

import java.util.List;

public class ProfileKeys extends AppCompatActivity {

    private KeyListAdapter keyListAdapter;
    private List<Contact> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_keys);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            contacts = SharkNetEngine.getSharkNet().getContacts();
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
//        TODO: fragen wie am besten eigener Contact/Profile angefügt werden kann
//        try {
////            contacts.add(SharkNetEngine.getSharkNet().getMyProfile());
//        } catch (SharkKBException e) {
//            e.printStackTrace();
//        }
        this.keyListAdapter = new KeyListAdapter(contacts);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.key_recycle_view);

        if (recyclerView != null) {
            LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(llm);
            llm.setStackFromEnd(false);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(keyListAdapter);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
