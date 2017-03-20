package net.sharksystem.sharknet.chat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.SharkApp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import rx.Scheduler;
import rx.Subscription;
import rx.schedulers.Schedulers;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    private final Context mContext;
    private final SharkApp mApp;
    private List<ChatActivity.ChatDataHolder> mChats = new ArrayList<>();


    public ChatListAdapter(Context context, SharkApp app) {
        mContext = context;
        mApp = app;
    }

    public void setChats(List<ChatActivity.ChatDataHolder> chats) {
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

        final ChatActivity.ChatDataHolder chatDataHolder = mChats.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mApp.setChat(chatDataHolder.chat);
                mContext.startActivity(new Intent(mContext, ChatDetailActivity.class));
            }
        });
        if (chatDataHolder.image != null) {
            holder.chatImage.setImageBitmap(chatDataHolder.image);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            holder.chatImage.setLayoutParams(params);
            holder.chatImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else if (chatDataHolder.imageId != 0) {
            holder.chatImage.setImageResource(chatDataHolder.imageId);
        }
        holder.chatLastMessage.setText(chatDataHolder.message);
        holder.chatName.setText(chatDataHolder.name);

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
