package net.sharksystem.sharknet.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.interfaces.Message;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.SharkApp;
import net.sharksystem.sharknet.contact.ContactsDetailActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Single;
import rx.SingleSubscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by j4rvis on 3/5/17.
 */

public class ChatDetailMsgListAdapter extends RecyclerView.Adapter<ChatDetailMsgListAdapter.ViewHolderBase> {

    private final static int MESSAGE_IS_MINE = 0;
    private final static int MESSAGE_IS_NOT_MINE = 1;
    private final SharkApp mApp;
    private final Context mContext;

    private List<Message> mMessages = new ArrayList<>();
    public Subscription mSubscription;

    public ChatDetailMsgListAdapter(Context context, SharkApp application) {
        mApp = application;
        mContext = context;
    }

    public void setMessages(List<Message> messages){
        mMessages = messages;
        notifyDataSetChanged();
    }

    @Override
    public ChatDetailMsgListAdapter.ViewHolderBase onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = 0;

        switch (viewType) {
            case MESSAGE_IS_MINE:
                layout = R.layout.chat_detail_my_msg_line_item;
                break;
            case MESSAGE_IS_NOT_MINE:
                layout = R.layout.chat_detail_msg_line_item;
                break;
            default:
                break;
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolderBase(view);
    }

    @Override
    public void onBindViewHolder(final ChatDetailMsgListAdapter.ViewHolderBase holder, int position) {

        final Message message = mMessages.get(position);

        Single<MessageDataHolder> single = Single.fromCallable(new Callable<MessageDataHolder>() {
            @Override
            public MessageDataHolder call() throws Exception {

                String messageContent = message.getContent().getMessage();
                Bitmap authorImage = null;
                if(message.getSender().getPicture() != null) {
                    authorImage = BitmapFactory.decodeStream(message.getSender().getPicture().getInputStream());
                }
                int stateResource;
                if (message.isVerified()) {
                    stateResource = R.drawable.ic_verified_user_green_24dp;
                } else if (message.isSigned()) {
                    stateResource = R.drawable.ic_warning_dark_grey_24dp;
                } else {
                    stateResource = R.drawable.ic_warning_red_24dp;
                }
                String authorName = message.getSender().getName();
                SimpleDateFormat format = new SimpleDateFormat("d. MMM yyyy, HH:mm");
                String date = format.format(message.getDateReceived());

                boolean isEncrypted = message.isEncrypted();
                boolean isMine = message.isMine();

                return new MessageDataHolder(authorImage, authorName, messageContent, date, stateResource, isEncrypted, isMine);
            }
        });

        mSubscription = single.subscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread()).subscribe(new SingleSubscriber<MessageDataHolder>() {
            @Override
            public void onSuccess(MessageDataHolder value) {
                // message
                holder.msgView.setText(value.messageContent);
                if (value.messageContent.length() < 30) {
                    holder.msgView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                }
                holder.msgView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        PopupMenu popupMenu = new PopupMenu(mContext, holder.msgView);
                        popupMenu.getMenuInflater().inflate(R.menu.chat_detail_message_menu, popupMenu.getMenu());
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                switch (item.getItemId()) {
                                    case R.id.message_detail:
                                        Intent intent = new Intent(mContext, ChatMessageDetailActivity.class);
                                        mApp.setMessage(message);
                                        mContext.startActivity(intent);
                                        break;
                                    case R.id.message_dislike:
                                        Toast.makeText(mContext, "You disliked the message", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        break;
                                }

                                return false;
                            }
                        });

                        popupMenu.show();

                        return true;
                    }
                });
                // date
                holder.dateView.setText(value.date);
                if (!value.isMine) {
                    // image
                    if (value.authorImageBitMap == null) {
                        holder.authorImageView.setImageResource(R.drawable.ic_person_white_24dp);
                        holder.authorImageView.setLayoutParams(new ViewGroup.LayoutParams(35, 35));
                    } else {
                        holder.authorImageView.setImageBitmap(value.authorImageBitMap);
                    }
                    holder.authorImageView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {

                            PopupMenu popupMenu = new PopupMenu(mContext, holder.authorImageView);

                            popupMenu.getMenuInflater().inflate(R.menu.chat_detail_contact_menu, popupMenu.getMenu());

                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {

                                    switch (item.getItemId()) {
                                        case R.id.contact_profile:
                                            Intent intent = new Intent(mContext, ContactsDetailActivity.class);
                                            try {
                                                mApp.setContact(message.getSender());
                                            } catch (SharkKBException e) {
                                                e.printStackTrace();
                                            }
                                            mContext.startActivity(intent);
                                            break;
                                        case R.id.contact_block:
                                            Toast.makeText(mContext, "You blocked the user.", Toast.LENGTH_SHORT).show();
                                            break;
                                        default:
                                            break;
                                    }

                                    return false;
                                }
                            });

                            popupMenu.show();

                            return true;
                        }
                    });
                    // authorName
                    holder.authorTextView.setText(value.authorName);
                    //encrypted
                    if (value.isEncrypted) {
                        holder.encryptionView.setImageResource(R.drawable.ic_vpn_key_dark_grey_24dp);
                    }
                    //state
                    holder.stateView.setImageResource(value.stateResource);
                }
            }

            @Override
            public void onError(Throwable error) {

            }
        });
    }

    @Override
    public int getItemViewType(int position) {

        Message message = this.mMessages.get(position);
        try {
            if (message.isMine()) {
                return MESSAGE_IS_MINE;
            } else {
                return MESSAGE_IS_NOT_MINE;
            }
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return this.mMessages.size();
    }

    private class MessageDataHolder{
        Bitmap authorImageBitMap;
        String authorName;
        String messageContent;
        String date;
        int stateResource;
        boolean isEncrypted;
        boolean isMine;

        private MessageDataHolder(Bitmap authorImageBitMap, String authorName, String messageContent, String date, int stateResource, boolean isEncrypted, boolean isMine) {
            this.authorImageBitMap = authorImageBitMap;
            this.authorName = authorName;
            this.messageContent = messageContent;
            this.date = date;
            this.stateResource = stateResource;
            this.isEncrypted = isEncrypted;
            this.isMine = isMine;
        }
    }

    class ViewHolderBase extends RecyclerView.ViewHolder {

        ImageView authorImageView;
        TextView authorTextView;
        TextView msgView;
        TextView dateView;
        ImageView stateView;
        ImageView encryptionView;

        public ViewHolderBase(View itemView) {
            super(itemView);
            msgView = (TextView) itemView.findViewById(R.id.msg_item_content);
            authorImageView = (ImageView) itemView.findViewById(R.id.round_image);
            authorTextView = (TextView) itemView.findViewById(R.id.msg_item_author_name);
            dateView = (TextView) itemView.findViewById(R.id.msg_item_date);
            stateView = (ImageView) itemView.findViewById(R.id.msg_item_state);
            encryptionView = (ImageView) itemView.findViewById(R.id.msg_item_encryption);
        }
    }
}
