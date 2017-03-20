package net.sharksystem.sharknet.chat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import net.sharksystem.api.impl.SharkNetEngine;
import net.sharksystem.api.interfaces.Chat;
import net.sharksystem.api.interfaces.Contact;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.RxSingleBaseActivity;

import java.util.ArrayList;
import java.util.List;

public class ChatContactsActivity extends RxSingleBaseActivity<List<ChatContactsActivity.ContactCheckableDataHolder>> {


    private ChatContactsCheckableListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureLayout();
        setProgressMessage("Lade Kontakte...");
        startSubscription();
    }

    private void configureLayout() {
        setLayoutResource(R.layout.contact_activity);
        setOptionsMenu(R.menu.chat_add_contact_menu);
        setTitle("Kontakte");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAdapter = new ChatContactsCheckableListAdapter(this, getSharkApp());
        RecyclerView mChatRecyclerView = (RecyclerView) findViewById(R.id.contact_recycler_view);
        mChatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mChatRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected List<ContactCheckableDataHolder> doOnBackgroundThread() throws Exception {
        List<Contact> contacts = SharkNetEngine.getSharkNet().getContacts();
        List<Contact> chatContacts = null;
        if(getSharkApp().getChat() != null){
            Chat chat = getSharkApp().getChat();
            chatContacts = chat.getContacts();
        }
        ArrayList<ContactCheckableDataHolder> list = new ArrayList<>();
        for (Contact contact : contacts) {

            // Image
            Bitmap image = null;
            if (contact.getPicture().getLength() > 0) {
                image = BitmapFactory.decodeStream(contact.getPicture().getInputStream());
            }

            String name = contact.getNickname();

            boolean checked = false;
            if(chatContacts != null && chatContacts.contains(contact)){
                checked = true;
            }

            list.add(new ContactCheckableDataHolder(contact, image, name, checked));
        }
        return list;
    }

    @Override
    protected void doOnUIThread(List<ContactCheckableDataHolder> contactDataHolders) {
        mAdapter.setList(contactDataHolders);
    }

    @Override
    protected void doOnError(Throwable error) {
        error.printStackTrace();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.chat_contact_add:
                getSharkApp().setContactCheckableDataHolderList(mAdapter.getCheckedContacts());
                super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public class ContactCheckableDataHolder {
        Contact contact;
        Bitmap contactImage;
        String contactName;
        boolean checked;

        public ContactCheckableDataHolder(Contact contact, Bitmap contactImage, String contactName, boolean checked) {
            this.contact = contact;
            this.contactImage = contactImage;
            this.contactName = contactName;
            this.checked = checked;
        }
    }
}
