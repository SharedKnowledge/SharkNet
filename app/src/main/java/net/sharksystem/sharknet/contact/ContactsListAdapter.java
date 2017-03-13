package net.sharksystem.sharknet.contact;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.interfaces.Contact;
import net.sharksystem.sharknet.R;

import java.util.List;

/**
 * Created by viktorowich on 08/06/16.
 */
public class ContactsListAdapter extends ArrayAdapter<Contact> {

    private List<Contact> contacts;

    public ContactsListAdapter(Context context, int resource, List<Contact> objects) {
        super(context, resource, objects);
        this.contacts = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.contact_line_item, parent, false);
        }

        Contact contact = contacts.get(position);

        //Name
        TextView title = (TextView) convertView.findViewById(R.id.contact_nickname);
        try {
            title.setText(contact.getNickname());
        } catch (SharkKBException e) {
            e.printStackTrace();
        }

        //Image
        ImageView image = (ImageView) convertView.findViewById(R.id.round_image);

        try {
            if (contact.getPicture() != null) {
                image.setImageBitmap(BitmapFactory.decodeStream(contact.getPicture().getInputStream()));
            } else {
                image.setImageResource(R.drawable.ic_person_white_48dp);
                image.setLayoutParams(new LinearLayout.LayoutParams(35, 35));
            }

        } catch (SharkKBException e) {
            e.printStackTrace();
        }

        return convertView;
    }
}
