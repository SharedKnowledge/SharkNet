package net.sharksystem.sharknet.contact;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.sharksystem.sharknet.BaseRecyclerViewAdapter;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.SharkApp;
import net.sharksystem.sharknet.chat.ChatListAdapter;

import java.util.ArrayList;
import java.util.List;

public class ContactsListAdapter extends RecyclerView.Adapter<ContactsListAdapter.ContactViewHolder> {

    private final SharkApp mApp;
    private final Context mContext;
    private List<ContactActivity.ContactDataHolder> mList = new ArrayList<>();

    public ContactsListAdapter(Context context, SharkApp app) {
        mApp = app;
        mContext = context;
    }

    public void setList(List<ContactActivity.ContactDataHolder> list){
        mList = list;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_line_item, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {

        final ContactActivity.ContactDataHolder item = mList.get(position);

        holder.contactName.setText(item.contactName);
        if (item.contactImage != null) {
            holder.contactImage.setImageBitmap(item.contactImage);
        } else {
            holder.contactImage.setImageResource(R.drawable.ic_person_white_48dp);
            holder.contactImage.setLayoutParams(new FrameLayout.LayoutParams(35, 35));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mApp.setContact(item.contact);
                mContext.startActivity(new Intent(mContext, ContactsDetailActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {

        ImageView contactImage;
        TextView contactName;

        public ContactViewHolder(View itemView) {
            super(itemView);
            contactImage = (ImageView) itemView.findViewById(R.id.round_image);
            contactName = (TextView) itemView.findViewById(R.id.contact_nickname);
        }
    }
}
