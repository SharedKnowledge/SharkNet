package net.sharksystem.sharknet.contact;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import net.sharksystem.api.models.Chat;
import net.sharksystem.api.models.Contact;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.SharkApp;

import java.util.ArrayList;
import java.util.List;

public class ContactCheckableListAdapter extends RecyclerView.Adapter<ContactCheckableListAdapter.ContactCheckableViewHolder> {

    private final SharkApp mApp;
    private Chat mChat;
    private List<Contact> mList = new ArrayList<>();
    private List<Contact> mCheckedContacts = new ArrayList<>();

    public ContactCheckableListAdapter(SharkApp app) {
        mApp = app;
        mChat = mApp.getChat();
        if (mChat != null) {
            mCheckedContacts = mChat.getContacts();
        }
    }

    public void setList(List<Contact> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public List<Contact> getCheckedContacts() {
        return mCheckedContacts;
    }

    @Override
    public ContactCheckableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_checkable_line_item, parent, false);
        return new ContactCheckableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ContactCheckableViewHolder holder, int position) {

        final Contact contact = mList.get(position);

        holder.contactName.setText(contact.getName());
        if (contact.getImage() != null) {
            holder.contactImage.setImageBitmap(contact.getImage());
        } else {
            holder.contactImage.setImageResource(R.drawable.ic_person_white_48dp);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            holder.contactImage.setLayoutParams(params);
            holder.contactImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        Chat chat = mApp.getChat();
        if (chat != null) {
            holder.checkBox.setChecked(chat.getContacts().contains(contact));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox.isChecked()) {
                    holder.checkBox.setChecked(false);
                    mCheckedContacts.remove(contact);
                } else {
                    holder.checkBox.setChecked(true);
                    mCheckedContacts.add(contact);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ContactCheckableViewHolder extends RecyclerView.ViewHolder {

        ImageView contactImage;
        TextView contactName;
        CheckBox checkBox;

        public ContactCheckableViewHolder(View itemView) {
            super(itemView);
            contactImage = (ImageView) itemView.findViewById(R.id.round_image);
            contactName = (TextView) itemView.findViewById(R.id.contact_nickname);
            checkBox = (CheckBox) itemView.findViewById(R.id.contact_checkable_checkbox);
            checkBox.setClickable(false);
        }
    }
}
