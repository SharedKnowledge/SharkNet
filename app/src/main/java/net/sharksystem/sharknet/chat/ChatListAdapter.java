package net.sharksystem.sharknet.chat;

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

import net.sharksystem.api.dao_impl.SharkNetApiImpl;
import net.sharksystem.api.dao_interfaces.SharkNetApi;
import net.sharksystem.api.models.Chat;
import net.sharksystem.api.models.Contact;
import net.sharksystem.api.models.Message;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.SharkApp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    private final Context mContext;
    private final SharkApp mApp;
    private List<Chat> mChats = new ArrayList<>();
    private Contact mAccount;


    public ChatListAdapter(Context context, SharkApp app) {
        mContext = context;
        mApp = app;
        mAccount = mApp.getAccount();
    }

    public void setChats(List<Chat> chats) {
        mChats = chats;
//        Iterator<Chat> iterator = mChats.iterator();
//        while(iterator.hasNext()) {
//            Chat next = iterator.next();
//            if(next.getMessages().isEmpty()) iterator.remove();
//        }
        notifyDataSetChanged();
    }

    @Override
    public ChatListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_line_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ChatListAdapter.ViewHolder holder, final int position) {

        final Chat chat = mChats.get(position);
        List<Contact> contacts = chat.getContacts();
        contacts.remove(mAccount);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mApp.setChat(chat);
                mContext.startActivity(new Intent(mContext, ChatDetailActivity.class));
            }
        });
        if (chat.getImage() != null) {
            holder.chatImage.setImageBitmap(chat.getImage());
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            holder.chatImage.setLayoutParams(params);
            holder.chatImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else if (contacts.size() > 1) {
            holder.chatImage.setImageResource(R.drawable.ic_group_white_24dp);
        } else if(contacts.size()==1){
            if(contacts.get(0).getImage()!=null){
                holder.chatImage.setImageBitmap(contacts.get(0).getImage());
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                holder.chatImage.setLayoutParams(params);
                holder.chatImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else {
                holder.chatImage.setImageResource(R.drawable.ic_person_white_24dp);
            }
        }
        List<Message> messages = chat.getMessages();
        if(!messages.isEmpty()){
            Message lastMessage = messages.get(messages.size() - 1);
            String messageText;
            if (lastMessage.getSender().equals(mAccount)) {
                messageText = "Me: " + lastMessage.getContent();
            } else {
                messageText = lastMessage.getSender().getName() + ": " + lastMessage.getContent();
            }
            holder.chatLastMessage.setText(messageText);
        }

        if (chat.getTitle() != null) {
            holder.chatName.setText(chat.getTitle());
        } else if (contacts.size() == 1) {
            holder.chatName.setText(contacts.get(0).getName());
        }


    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView chatImage;
        TextView chatName;
        TextView chatLastMessage;

        ViewHolder(View itemView) {
            super(itemView);
            chatImage = (ImageView) itemView.findViewById(R.id.chat_line_item_image);
            chatName = (TextView) itemView.findViewById(R.id.chat_line_item_name);
            chatLastMessage = (TextView) itemView.findViewById(R.id.chat_line_item_last_message);
        }
    }

}
