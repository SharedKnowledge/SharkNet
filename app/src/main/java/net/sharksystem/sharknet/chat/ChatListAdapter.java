package net.sharksystem.sharknet.chat;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.impl.SharkNetEngine;
import net.sharksystem.api.interfaces.Chat;
import net.sharksystem.api.interfaces.Contact;
import net.sharksystem.api.interfaces.Message;
import net.sharksystem.api.interfaces.Profile;
import net.sharksystem.sharknet.R;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

/**
 * Reads the Information from the ChatActivity-List and
 * fills the List-Items-Layout.
 */
public class ChatListAdapter extends ArrayAdapter<net.sharksystem.api.interfaces.Chat> {

    private Profile myProfile = null;
    private List<net.sharksystem.api.interfaces.Chat> chats;

    public ChatListAdapter(Context context, int resource, List<net.sharksystem.api.interfaces.Chat> objects) {
        super(context, resource, objects);
        this.chats = objects;
        try {
            myProfile = SharkNetEngine.getSharkNet().getMyProfile();
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.chat_line_item, parent, false);
        }

        Chat chat = chats.get(position);

        //Title
        TextView titleView = (TextView) convertView.findViewById(R.id.name);
        try {
            String title = chat.getTitle();
            if(title==null || title.isEmpty()){
                title = "";
                List<Contact> contacts = chat.getContactsWithoutMe();
                Iterator<Contact> iterator = contacts.iterator();
                while (iterator.hasNext()){
                    Contact next = iterator.next();
                    if(!title.isEmpty()){
                        title += ", ";
                    }
                    title += next.getName();
                }

            }
            titleView.setText(title);
        } catch (SharkKBException e) {
            e.printStackTrace();
        }

        //Sender + Lastmessage-Text
        TextView text = (TextView) convertView.findViewById(R.id.msg_text);

        List<Message> msgs = null;
        try {
            msgs = chat.getMessages(false);
//            L.d("Number of Messages for chat " + chat.getTitle() + ": " + msgs.size(), this);
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
        if (msgs.size() > 0) {
            Message last_msg = msgs.get(msgs.size() - 1);
            String content = null;
            try {
                content = last_msg.getContent().getMessage();
            } catch (SharkKBException e) {
                e.printStackTrace();
            }
            String sender = null;
            try {

                sender = last_msg.getSender().getNickname();
            } catch (SharkKBException e) {
                e.printStackTrace();
            }

            String last_msg_content = sender + ":" + content;
            text.setText(last_msg_content);
        }


        //Image
        ImageView image = (ImageView) convertView.findViewById(R.id.chat_image);
        //TODO: != change to ==  then load image works
        //if(chat.getPicture() != null)
        //{
        try {

            if(chat.getPicture() != null){
                image.setImageBitmap(BitmapFactory.decodeStream(chat.getPicture().getInputStream()));
            } else {
                if(chat.getContacts().size() == 1){
                    image.setImageBitmap(BitmapFactory.decodeStream(chat.getContacts().get(0).getPicture().getInputStream()));
                }
            }

            if (chat.getContacts().size() > 1) {
                image.setImageResource(R.drawable.ic_group_accent_24dp);
            } else {
                image.setImageResource(R.drawable.ic_person_accent_24dp);
            }
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
        //}
        // else
        //{
        //TODO: image.setImageResource(chat.getPicture().);
        //‚}

        return convertView;
    }
}
