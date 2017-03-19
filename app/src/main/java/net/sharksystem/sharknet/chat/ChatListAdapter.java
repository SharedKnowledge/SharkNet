package net.sharksystem.sharknet.chat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.system.L;
import net.sharksystem.api.interfaces.Chat;
import net.sharksystem.api.interfaces.Contact;
import net.sharksystem.api.interfaces.Message;
import net.sharksystem.sharknet.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Single;
import rx.SingleSubscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Reads the Information from the ChatActivity-List and
 * fills the List-Items-Layout.
 */
public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    private final Context mContext;
    private List<Chat> mChats = new ArrayList<>();
    private Subscription mSubscription;

    public ChatListAdapter(Context context) {
        mContext = context;
    }

    public void setChats(List<Chat> chats) {
        mChats = chats;
        notifyDataSetChanged();
    }

    @Override
    public ChatListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_line_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ChatListAdapter.ViewHolder holder, final int position) {
        Single<ChatDataHolder> single = Single.fromCallable(new Callable<ChatDataHolder>() {
            @Override
            public ChatDataHolder call() throws Exception {

                Chat chat = mChats.get(position);

                // name
                String name = chat.getTitle();
                if (name == null || name.isEmpty()) {
                    name = "";
                    List<Contact> contacts = chat.getContactsWithoutMe();
                    for (Contact next : contacts) {
                        if (!name.isEmpty()) {
                            name += ", ";
                        }
                        name += next.getName();
                    }
                }

                // message
                String message = "";
                try {
                    List<Message> messages = chat.getMessages(false);
                    if (messages.size() > 0) {
                        Message last_msg = messages.get(messages.size() - 1);
                        String content = last_msg.getContent().getMessage();
                        String sender = last_msg.getSender().getNickname();
                        message = sender + ":" + content;
                    }
                } catch (SharkKBException e) {
                    e.printStackTrace();
                }

                // Image
                Bitmap image;
                if (chat.getPicture().getLength() > 0) {
                    image = BitmapFactory.decodeStream(chat.getPicture().getInputStream());
                } else {
                    if (chat.getContacts().size() == 1) {
                        image = BitmapFactory.decodeStream(chat.getContacts().get(0).getPicture().getInputStream());
                    } else {
                        if (chat.getContacts().size() > 1) {
                            image = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_group_white_24dp);
                        } else {
                            image = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_person_white_24dp);
                        }
                    }
                }

                return new ChatDataHolder(image, name, message);
            }
        });

        mSubscription = single
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<ChatDataHolder>() {

                    @Override
                    public void onSuccess(ChatDataHolder value) {
                        holder.chatImage.setImageBitmap(value.image);
                        holder.chatLastMessage.setText(value.message);
                        holder.chatName.setText(value.name);
                    }

                    @Override
                    public void onError(Throwable error) {
                        L.e(error.getMessage(), this);
                    }
        });
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    private class ChatDataHolder {
        String message;
        String name;
        Bitmap image;

        ChatDataHolder(Bitmap image, String name, String message) {
            this.image = image;
            this.name = name;
            this.message = message;
        }
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
