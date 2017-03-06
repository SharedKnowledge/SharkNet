package net.sharksystem.sharknet.chat;

import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.system.L;
import net.sharksystem.api.interfaces.Message;
import net.sharksystem.sharknet.R;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by j4rvis on 3/5/17.
 */

public class ChatDetailMsgListAdapter extends RecyclerView.Adapter<ChatDetailMsgListAdapter.ViewHolderBase> {

    private final List<Message> messages;

    public ChatDetailMsgListAdapter(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public ChatDetailMsgListAdapter.ViewHolderBase onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = 0;

        L.d("viewType: " + viewType, this);

        switch (viewType) {
            case 0:
                layout = R.layout.chat_detail_msg_line_item;
                break;
            case 1:
                layout = R.layout.chat_detail_msg_line_item;
                break;
            default:
                break;
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolderBase(view);
    }

    @Override
    public void onBindViewHolder(ChatDetailMsgListAdapter.ViewHolderBase holder, int position) {

        Message message = this.messages.get(position);

        try {
            // Message Content
            // TODO add Images etc. Using super.getItemViewType()
            String messageContent = message.getContent().getMessage();
            holder.mMsgView.setText(messageContent);

//            L.d("Message.length: " + messageContent.length(), this);
            if(messageContent.length() < 30){
                holder.mMsgView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            }

            // Author image
            if (message.getSender().getPicture() == null) {
                holder.mAuthorImageView.setImageResource(R.drawable.ic_person_accent_24dp);
//                holder.mAuthorImageView.setVisibility(View.GONE);
            } else {
                // Set the image of the author
                holder.mAuthorImageView.setImageResource(R.drawable.ic_person_accent_24dp);
            }

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
            // Date
            SimpleDateFormat format = new SimpleDateFormat("d. MMM yyyy, HH:mm");
            holder.mDateView.setText(format.format(message.getDateReceived()));
            holder.mAuthorTextView.setText(message.getSender().getName());
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemViewType(int position) {

        Message message = this.messages.get(position);
        try {
            if (message.isMine()) {
                return 0;
            } else {
                return 1;
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
            mAuthorImageView = (ImageView) itemView.findViewById(R.id.msg_item_author_image);
            mAuthorTextView = (TextView) itemView.findViewById(R.id.msg_item_author_name);
            mDateView = (TextView) itemView.findViewById(R.id.msg_item_date);
            mStateView = (ImageView) itemView.findViewById(R.id.msg_item_state);
            mEncryptionView = (ImageView) itemView.findViewById(R.id.msg_item_encryption);
        }
    }
}
