package net.sharksystem.sharknet.contact;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import net.sharksystem.api.interfaces.Contact;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.SharkApp;

import java.util.ArrayList;
import java.util.List;

public class ContactCheckableListAdapter extends RecyclerView.Adapter<ContactCheckableListAdapter.ContactCheckableViewHolder> {

    private final SharkApp mApp;
    private final Context mContext;
    private List<ContactCheckableListAdapter.ContactCheckableDataHolder> mList = new ArrayList<>();

    public ContactCheckableListAdapter(Context context, SharkApp app) {
        mApp = app;
        mContext = context;
    }

    public void setList(List<ContactCheckableListAdapter.ContactCheckableDataHolder> list){
        mList = list;
    }

    public List<Contact> getCheckedContacts(){
        ArrayList<Contact> list = new ArrayList<>();
        for (ContactCheckableListAdapter.ContactCheckableDataHolder holder : mList) {
            if(holder.checked){
                list.add(holder.contact);
            }
        }
        return list;
    }

    @Override
    public ContactCheckableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_checkable_line_item, parent, false);
        return new ContactCheckableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ContactCheckableViewHolder holder, int position) {

        final ContactCheckableListAdapter.ContactCheckableDataHolder item = mList.get(position);

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

    public static class ContactCheckableDataHolder {
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
