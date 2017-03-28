package net.sharksystem.sharknet.contact;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import net.sharksystem.api.models.Contact;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.SharkApp;

import java.util.ArrayList;
import java.util.List;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactViewHolder> {

    private final SharkApp mApp;
    private final Context mContext;
    private List<Contact> mList = new ArrayList<>();

    public ContactListAdapter(Context context, SharkApp app) {
        mApp = app;
        mContext = context;
    }

    public void setList(List<Contact> list){
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_line_item, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {

        final Contact contact = mList.get(position);

        holder.contactName.setText(contact.getName());
        if (contact.getImage() != null) {
            holder.contactImage.setImageBitmap(contact.getImage());
        } else {
            holder.contactImage.setImageResource(R.drawable.ic_person_white_48dp);
            holder.contactImage.setLayoutParams(new FrameLayout.LayoutParams(35, 35));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mApp.setContact(contact);
                mContext.startActivity(new Intent(mContext, ContactDetailActivity.class));
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
