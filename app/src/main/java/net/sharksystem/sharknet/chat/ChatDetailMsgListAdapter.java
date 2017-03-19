package net.sharksystem.sharknet.chat;

import android.content.Context;
import android.content.Intent;
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
import net.sharkfw.system.L;
import net.sharksystem.api.interfaces.Message;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.SharkApp;
import net.sharksystem.sharknet.contact.ContactsDetailActivity;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by j4rvis on 3/5/17.
 */

public class ChatDetailMsgListAdapter extends RecyclerView.Adapter<ChatDetailMsgListAdapter.ViewHolderBase> {

    private final static int MESSAGE_IS_MINE = 0;
    private final static int MESSAGE_IS_NOT_MINE = 1;
    private final SharkApp application;
    private final Context context;

    private List<Message> messages;

    public ChatDetailMsgListAdapter(Context context, SharkApp application, List<Message> messages) {
        this.messages = messages;
        this.application = application;
        this.context = context;
    }

    public void setMessages(List<Message> messages){
        this.messages = messages;
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

        L.d("bups.", this);

        final Message message = this.messages.get(position);
        try {
            // Message Content
            // TODO add Images etc. Using super.getItemViewType()
            String messageContent = message.getContent().getMessage();
            holder.mMsgView.setText(messageContent);

//            L.d("Message.length: " + messageContent.length(), this);
            if(messageContent.length() < 30){
                holder.mMsgView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            }

            final Context finalContext = this.context;

            holder.mMsgView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    PopupMenu popupMenu = new PopupMenu(finalContext, holder.mMsgView);

                    popupMenu.getMenuInflater().inflate(R.menu.chat_detail_message_menu, popupMenu.getMenu());

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId()){
                                case R.id.message_detail:
                                    Intent intent = new Intent(finalContext, ChatMessageDetailActivity.class);
                                    application.setMessage(message);
                                    finalContext.startActivity(intent);
                                    break;
                                case R.id.message_dislike:
                                    Toast.makeText(finalContext, "You disliked the message", Toast.LENGTH_SHORT).show();
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

            if(!message.isMine()){
                // Author bitmap
                if (message.getSender().getPicture() == null) {
                    holder.mAuthorImageView.setImageResource(R.drawable.ic_person_white_24dp);
                    holder.mAuthorImageView.setLayoutParams(new ViewGroup.LayoutParams(35, 35));
                } else {
                    // Set the bitmap of the author
                    holder.mAuthorImageView.setImageBitmap(BitmapFactory.decodeStream(message.getSender().getPicture().getInputStream()));
                }

                holder.mAuthorImageView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        PopupMenu popupMenu = new PopupMenu(finalContext, holder.mAuthorImageView);

                        popupMenu.getMenuInflater().inflate(R.menu.chat_detail_contact_menu, popupMenu.getMenu());

                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                switch (item.getItemId()){
                                    case R.id.contact_profile:
                                        Intent intent = new Intent(finalContext, ContactsDetailActivity.class);
                                        try {
                                            application.setContact(message.getSender());
                                        } catch (SharkKBException e) {
                                            e.printStackTrace();
                                        }
                                        finalContext.startActivity(intent);
                                        break;
                                    case R.id.contact_block:
                                        Toast.makeText(finalContext, "You blocked the user.", Toast.LENGTH_SHORT).show();
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

                // Message State
                if (message.isVerified()) {
                    holder.mStateView.setImageResource(R.drawable.ic_verified_user_green_24dp);
                } else if (message.isSigned()) {
                    holder.mStateView.setImageResource(R.drawable.ic_warning_dark_grey_24dp);
                } else {
                    holder.mStateView.setImageResource(R.drawable.ic_warning_red_24dp);
                }

                // Encrypted?
                if (message.isEncrypted()) {
                    holder.mEncryptionView.setImageResource(R.drawable.ic_vpn_key_dark_grey_24dp);
                }
                holder.mAuthorTextView.setText(message.getSender().getName());
            }

            // Date
            SimpleDateFormat format = new SimpleDateFormat("d. MMM yyyy, HH:mm");
            holder.mDateView.setText(format.format(message.getDateReceived()));
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemViewType(int position) {

        Message message = this.messages.get(position);
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
        return this.messages.size();
    }

    class ViewHolderBase extends RecyclerView.ViewHolder {

        ImageView mAuthorImageView;
        TextView mAuthorTextView;
        TextView mMsgView;
        TextView mDateView;
        ImageView mStateView;
        ImageView mEncryptionView;

        public ViewHolderBase(View itemView) {
            super(itemView);
            mMsgView = (TextView) itemView.findViewById(R.id.msg_item_content);
            mAuthorImageView = (ImageView) itemView.findViewById(R.id.round_image);
            mAuthorTextView = (TextView) itemView.findViewById(R.id.msg_item_author_name);
            mDateView = (TextView) itemView.findViewById(R.id.msg_item_date);
            mStateView = (ImageView) itemView.findViewById(R.id.msg_item_state);
            mEncryptionView = (ImageView) itemView.findViewById(R.id.msg_item_encryption);
        }
    }
}
