package net.sharksystem.sharknet.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.interfaces.Chat;
import net.sharksystem.api.interfaces.Contact;
import net.sharksystem.api.interfaces.Message;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.SharkApp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReentrantLock;

import rx.Scheduler;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    private final Context mContext;
    private final ReentrantLock lock = new ReentrantLock();
    private final Scheduler mScheduler;
    private final SharkApp mApp;
    public Subscription mSubscription;
    private List<Chat> mChats = new ArrayList<>();


    public ChatListAdapter(Context context, SharkApp app) {
        mContext = context;
        mApp = app;
        mScheduler = Schedulers.newThread();
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
        lock.lock();

        Single<ChatDataHolder> single = Single.fromCallable(new Callable<ChatDataHolder>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public ChatDataHolder call() throws Exception {
                final Chat chat = mChats.get(holder.getAdapterPosition());
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
                Bitmap image = null;
                int imageId = 0;
                if (chat.getPicture().getLength() > 0) {
                    image = BitmapFactory.decodeStream(chat.getPicture().getInputStream());
                } else {
                    imageId = R.drawable.ic_group_white_24dp;
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mApp.setChat(chat);
                        mContext.startActivity(new Intent(mContext, ChatDetailActivity.class));
                    }
                });

                return new ChatDataHolder(image, imageId, name, message);
            }
        });

        try {
            mSubscription = single.subscribeOn(mScheduler).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleSubscriber<ChatDataHolder>() {

                @Override
                public void onSuccess(ChatDataHolder value) {
                    if (value.image != null) {
                        holder.chatImage.setImageBitmap(value.image);
                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                        holder.chatImage.setLayoutParams(params);
                        holder.chatImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    } else if (value.imageId != 0) {
                        holder.chatImage.setImageResource(value.imageId);
                    }
                    holder.chatLastMessage.setText(value.message);
                    holder.chatName.setText(value.name);
                }

                @Override
                public void onError(Throwable error) {
                    error.printStackTrace();
                }
            });
        } finally {
            lock.unlock();
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

    private class ChatDataHolder {
        Bitmap image;
        int imageId;
        String message;
        String name;

        ChatDataHolder(Bitmap image, int imageId, String name, String message) {
            this.image = image;
            this.imageId = imageId;
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
