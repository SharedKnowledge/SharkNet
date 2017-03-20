package net.sharksystem.sharknet.chat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.SharkApp;
import net.sharksystem.sharknet.contact.ContactActivity;
import net.sharksystem.sharknet.contact.ContactsDetailActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChatContactsCheckableListAdapter extends RecyclerView.Adapter<ChatContactsCheckableListAdapter.ContactViewHolder> {

    private final SharkApp mApp;
    private final Context mContext;
    private List<ChatContactsActivity.ContactCheckableDataHolder> mList = new ArrayList<>();

    public ChatContactsCheckableListAdapter(Context context, SharkApp app) {
        mApp = app;
        mContext = context;
    }

    public void setList(List<ChatContactsActivity.ContactCheckableDataHolder> list){
        mList = list;
    }

    public List<ChatContactsActivity.ContactCheckableDataHolder> getCheckedContacts(){
        ArrayList<ChatContactsActivity.ContactCheckableDataHolder> list = new ArrayList<>();
        for (ChatContactsActivity.ContactCheckableDataHolder holder : mList) {
            if(holder.checked){
                list.add(holder);
            }
        }
        return list;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_checkable_line_item, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ContactViewHolder holder, int position) {

        final ChatContactsActivity.ContactCheckableDataHolder item = mList.get(position);

        holder.contactName.setText(item.contactName);
        if (item.contactImage != null) {
            holder.contactImage.setImageBitmap(item.contactImage);
        } else {
            holder.contactImage.setImageResource(R.drawable.ic_person_white_48dp);
            holder.contactImage.setLayoutParams(new FrameLayout.LayoutParams(35, 35));
        }
        holder.checkBox.setChecked(item.checked);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.checkBox.isChecked()){
                    holder.checkBox.setChecked(false);
                    item.checked = false;
                } else {
                    holder.checkBox.setChecked(true);
                    item.checked = true;
                }
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
        CheckBox checkBox;

        public ContactViewHolder(View itemView) {
            super(itemView);
            contactImage = (ImageView) itemView.findViewById(R.id.round_image);
            contactName = (TextView) itemView.findViewById(R.id.contact_nickname);
            checkBox = (CheckBox) itemView.findViewById(R.id.contact_checkable_checkbox);
            checkBox.setClickable(false);
        }
    }
}
