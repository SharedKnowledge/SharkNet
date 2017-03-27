package net.sharksystem.sharknet.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import net.sharksystem.api.models.Contact;
import net.sharksystem.sharknet.R;

import java.util.List;

/**
 * Created by viktorowich on 08/06/16.
 */
public class ChatContactsListAdapter extends ArrayAdapter<Contact> {

    private List<Contact> contacts;

    public ChatContactsListAdapter(Context context, int resource, List<Contact> objects) {
        super(context, resource, objects);
        this.contacts = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.chat_new_contact_line_item, parent, false);
        }

        Contact contact = contacts.get(position);

        //Name
        TextView title = (TextView) convertView.findViewById(R.id.contact_nickname);
        title.setText(contact.getName());

        //Image
        ImageView image = (ImageView) convertView.findViewById(R.id.round_image);
        if (contact.getImage() != null) {
            image.setImageBitmap(contact.getImage());
        } else {
            image.setImageResource(R.drawable.ic_person_white_24dp);
            image.setLayoutParams(new ViewGroup.LayoutParams(35, 35));

        }

        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
        checkBox.setChecked(false);

        return convertView;
    }


}
