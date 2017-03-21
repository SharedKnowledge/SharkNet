package net.sharksystem.sharknet.chat;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import net.sharkfw.system.L;
import net.sharksystem.api.impl.SharkNetEngine;
import net.sharksystem.api.interfaces.Chat;
import net.sharksystem.api.interfaces.Contact;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.RxSingleBaseActivity;
import net.sharksystem.sharknet.contact.ContactCheckableListAdapter;
import net.sharksystem.sharknet.contact.ContactListAdapter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Single;
import rx.SingleSubscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChatSettingsActivity extends RxSingleBaseActivity<List<ContactListAdapter.ContactDataHolder>> {

    private static final int PICK_IMAGE_REQUEST = 1777;
    private ContactListAdapter mAdapter;
    private ImageView mChatImage;
    private EditText mChatTitle;
    private Subscription subscription;
    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutResource(R.layout.chat_new_activity);
        setOptionsMenu(R.menu.chat_new_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setProgressMessage("Lade Kontakte...");
        startSubscription();

        mAdapter = new ContactListAdapter(this, getSharkApp());
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.contact_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);

        mChatImage = (ImageView) findViewById(R.id.chat_new_image);
        mChatTitle = (EditText) findViewById(R.id.chat_new_title);

        mChatImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                // Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.chat_new_create_button:
                if(mChatTitle.getText().toString().isEmpty()){
                    Toast.makeText(this, "Kein Titel ausgewählt." , Toast.LENGTH_SHORT).show();
                } else {

                    Single<Chat> single = Single.fromCallable(new Callable<Chat>() {
                        @Override
                        public Chat call() throws Exception {
                            Chat chat = getSharkApp().getChat();
                            chat.setTitle(mChatTitle.getText().toString());
                            if(mBitmap != null){
                                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                mBitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                                byte[] bitmapdata = bos.toByteArray();
                                ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
                                chat.setPicture(bs, "image/png");
                            }

                            return chat;
                        }
                    });

                    final Context that = this;

                    subscription = single.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleSubscriber<Chat>() {
                        @Override
                        public void onSuccess(Chat value) {
                            getSharkApp().setChat(value);
                            startActivity(new Intent(that, ChatDetailActivity.class));
                        }

                        @Override
                        public void onError(Throwable error) {
                            error.printStackTrace();
                        }
                    });
                }
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected List<ContactListAdapter.ContactDataHolder> doOnBackgroundThread() throws Exception {
        List<Contact> contacts = getSharkApp().getChat().getContacts();
        ArrayList<ContactListAdapter.ContactDataHolder> list = new ArrayList<>();
        for (Contact contact : contacts) {
            Bitmap image = null;
            if (contact.getPicture().getLength() > 0) {
                image = BitmapFactory.decodeStream(contact.getPicture().getInputStream());
            }
            String name = contact.getNickname();
            list.add(new ContactListAdapter.ContactDataHolder(contact, image, name));
        }
        return list;
    }

    @Override
    protected void doOnUIThread(List<ContactListAdapter.ContactDataHolder> contactDataHolders) {
        mAdapter.setList(contactDataHolders);
    }

    @Override
    protected void doOnError(Throwable error) {
        error.printStackTrace();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {

            Uri uri = data.getData();

            L.d(data.getType(), this);

            try {
                mBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                mChatImage.setImageBitmap(mBitmap);
                findViewById(R.id.chat_new_image_edit_view).setVisibility(View.GONE);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(subscription!=null && !subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
    }
}
