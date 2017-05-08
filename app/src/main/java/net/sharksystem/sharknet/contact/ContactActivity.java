package net.sharksystem.sharknet.contact;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import net.sharkfw.system.L;
import net.sharksystem.api.dao_impl.SharkNetApiImpl;
import net.sharksystem.api.models.Contact;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.RxSingleNavigationDrawerActivity;

import java.util.List;

public class ContactActivity extends RxSingleNavigationDrawerActivity<List<Contact>> {

    private ContactListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureLayout();
        setProgressMessage(R.string.chat_progress_load_contacts);
    }

    private void configureLayout() {
        setLayoutResource(R.layout.contact_activity);
        setTitle(R.string.activity_title_contacts);
        mAdapter = new ContactListAdapter(this, getSharkApp());
        RecyclerView mChatRecyclerView = (RecyclerView) findViewById(R.id.contact_recycler_view);
        mChatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mChatRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected List<Contact> doOnBackgroundThread() throws Exception {
        return mApi.getContacts();
    }

    @Override
    protected void doOnUIThread(List<Contact> contacts) {
        mAdapter.setList(contacts);
    }

    @Override
    protected void doOnError(Throwable error) {
        error.printStackTrace();
    }


}
