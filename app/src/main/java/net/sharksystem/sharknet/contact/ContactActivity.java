package net.sharksystem.sharknet.contact;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import net.sharksystem.api.dao_impl.SharkNetApi;
import net.sharksystem.api.models.Contact;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.RxSingleNavigationDrawerActivity;

import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends RxSingleNavigationDrawerActivity<List<Contact>> {

    private ContactListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureLayout();
        setProgressMessage("Lade Kontakte...");
        startSubscription();
    }

    private void configureLayout() {
        setLayoutResource(R.layout.contact_activity);
        setTitle("Kontakte");
        mAdapter = new ContactListAdapter(this, getSharkApp());
        RecyclerView mChatRecyclerView = (RecyclerView) findViewById(R.id.contact_recycler_view);
        mChatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mChatRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected List<Contact> doOnBackgroundThread() throws Exception {
//        List<Contact> contacts = SharkNetEngine.getSharkNet().getContacts();
//        ArrayList<Contact> list = new ArrayList<>();
//        for (Contact contact : contacts) {
//
//            // Image
//            Bitmap image = null;
//            if (contact.getPicture().getLength() > 0) {
//                image = BitmapFactory.decodeStream(contact.getPicture().getInputStream());
//            }
//
//            String name = contact.getNickname();
//
//            list.add(new Contact(contact, image, name));
//        }
//        return list;
        return SharkNetApi.getInstance().getContacts();
    }

    @Override
    protected void doOnUIThread(List<Contact> contactDataHolders) {
        mAdapter.setList(contactDataHolders);
    }

    @Override
    protected void doOnError(Throwable error) {
        error.printStackTrace();
    }


}
