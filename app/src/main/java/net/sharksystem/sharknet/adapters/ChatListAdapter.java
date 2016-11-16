package net.sharksystem.sharknet.adapters;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.sharksystem.sharknet.R;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.interfaces.Message;

import java.util.List;

/**
 * Reads the Information from the ChatActivity-List and
 * fills the List-Items-Layout.
 */
public class ChatListAdapter extends ArrayAdapter<net.sharksystem.api.interfaces.Chat> {

    private List<net.sharksystem.api.interfaces.Chat> chats;

    public ChatListAdapter(Context context, int resource, List<net.sharksystem.api.interfaces.Chat> objects) {
        super(context, resource, objects);
        this.chats = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.line_item_chat, parent, false);
        }

        net.sharksystem.api.interfaces.Chat chat = chats.get(position);
        //Title
        TextView title = (TextView) convertView.findViewById(R.id.name);
        try {
            title.setText(chat.getTitle());
        } catch (SharkKBException e) {
            e.printStackTrace();
        }

        //Sender + Lastmessage-Text
        TextView text = (TextView) convertView.findViewById(R.id.msg_text);

        List<Message> msgs = null;
        try {
            msgs = chat.getMessages(false);
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
        Log.d("adapter", String.valueOf(msgs.size()));
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


        //Image
        ImageView image = (ImageView) convertView.findViewById(R.id.chat_image);
        //TODO: != change to ==  then load image works
        //if(chat.getPicture() != null)
        //{
        try {
            if (chat.getContacts().size() > 1) {
                image.setImageResource(R.drawable.ic_group_pink_600_24dp);
            } else {
                image.setImageResource(R.drawable.ic_person_pink_600_24dp);
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
