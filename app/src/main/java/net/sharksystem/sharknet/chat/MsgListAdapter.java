package net.sharksystem.sharknet.chat;

import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import net.sharksystem.sharknet.R;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.interfaces.Message;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by viktorowich on 01/06/16.
 */
public class MsgListAdapter extends RecyclerView.Adapter<MsgListAdapter.ViewHolderBase>
{
    private List<Message> msgs;
    //Drawable check,cross;

    public MsgListAdapter(List<Message> objects) {
        msgs = objects;
    }

    /**
     * Check if i am the creator of the message.
     *
     * @param position Position of the Message from the List of all Messages
     * @return int viewType: 0 not my Message
     * 1 my Message
     */
    @Override
    public int getItemViewType(int position) {
        Message message = msgs.get(position);
        try {
            if (message.isMine()) {
                return 1;
            } else {
                return 0;
            }

            // TODO: soll schauen ob content leer ist oder nicht
        } catch (SharkKBException e) {
            e.printStackTrace();
        }


        return 0;
    }

    @Override
    public ViewHolderBase onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType) {
            case 0:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.line_item_msg, parent, false);
                return new ViewHolderMsg(itemView);
            case 1:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.line_item_msg_my, parent, false);
                return new ViewHolderMsgMy(itemView);

        }
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.line_item_msg, parent, false);
        return new ViewHolderMsg(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolderBase holder, int position) {
        Message message = msgs.get(position);
        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableStringBuilder builder_msg_inf = new SpannableStringBuilder();
        String s = null;
        try {
            s = new java.text.SimpleDateFormat("HH:mm dd.MM.yyyy").format(message.getDateReceived());
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
        try {
            builder.append(message.getContent().getMessage()).append(" ");
        } catch (SharkKBException e) {
            e.printStackTrace();
        }

        // TODO: Ausrufezeichen soll später weg
        // if(!message.isdisliked())
        //{
        // TODO: sollte vielleicht besser über die Bubble farbe angezeigt werden
        // holder.msg.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_warning_lime_800_18dp, 0);
        //}

        try {
            if (message.getContent().getInputStream() != null) {
                switch (message.getContent().getMimeType()) {
                    case "image/png":
                        holder.image_capture.setImageBitmap(BitmapFactory.decodeStream(message.getContent().getInputStream()));
//                        Drawable d = Drawable.createFromStream(message.getContent().getInputStream(),"Image");
//                        holder.msg.setCompoundDrawablesWithIntrinsicBounds(null,null,null,d);
                        break;
                    case "image/jpg":
                        holder.image_capture.setImageBitmap(BitmapFactory.decodeStream(message.getContent().getInputStream()));
                        break;
                    case "sound/mp3":
                        Log.d("MimeType", message.getContent().getMimeType());
                        break;
                    case "video/avi":
                        Log.d("MimeType", message.getContent().getMimeType());
                        break;
                    default:
                        //TODO:

                }
            }
            if (message.isMine()) {
                s = "Gesendet am " + s;
            } else {
                try {
                    if (message.isDisliked()) {
                        holder.msg.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_send_pink_700_18dp, 0);

                    } else {
                        holder.msg.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_send_black_18dp, 0);

                    }
                } catch (SharkKBException e) {
                    e.printStackTrace();
                }
                // TODO: Ausrufezeigen soll später hin
                try {
                    if (message.getSender().getPublicKey() != null && message.getSender().getPublicKey().isEmpty()) {
                        ImageView key;
                        if (message.getSender().getPublicKeyExpiration() != null && message.getSender().getPublicKeyExpiration().before(new Timestamp(System.currentTimeMillis()))) {
                            key = (ImageView) holder.itemView.findViewById(R.id.msg_key);

                        } else {
                            //TODO: soll ein grauer Schlüssel angezeigt werden
                            key = (ImageView) holder.itemView.findViewById(R.id.msg_key_grey);
                        }
                        key.setVisibility(View.VISIBLE);
                    }
                } catch (SharkKBException e) {
                    e.printStackTrace();
                }
                s = "Empfangen am " + s;
                holder.timestamp.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.cardview_dark_background));
            }
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
        builder_msg_inf.append(s);
        // builder.setSpan(new ImageSpan(holder.itemView.getResources().getDrawable(R.drawable.ic_warning_lime_800_18dp)),builder.length() - 1,builder.length(),0);
        //builder_msg_inf.setSpan(new ImageSpan(holder.itemView.getResources().getDrawable(R.drawable.circle_orange)),builder_msg_inf.length() - 2,builder_msg_inf.length(),0);
        //builder_msg_inf.setSpan(new ImageSpan(holder.itemView.getResources().getDrawable(R.drawable.circle_red)),builder_msg_inf.length() - 3,builder_msg_inf.length(),0);
/*
        assert check != null;
        check.setBounds(0, 0, holder.msg.getLineHeight(),holder.msg.getLineHeight());
        assert cross != null;
        cross.setBounds(0, 0, holder.msg.getLineHeight(),holder.msg.getLineHeight());
        if(message.isMine())
        {
            //holder.msg.setTextAlignment();
            holder.msg.setBackgroundColor(Color.parseColor("#FFC2185B"));
            holder.msg.setTextColor(Color.WHITE);

            Log.d("00000000"," ist meine ");
        }

        if(message.isEncrypted())
        {
            builder.setSpan(new ImageSpan(check),builder.length() - 1,builder.length(),0);
            Log.d("Viktor","isEncrypted");
        }
        else
        {
            builder.setSpan(new ImageSpan(cross),builder.length() - 1,builder.length(),0);
            Log.d("Viktor","not isEncrypted");

        }
        if (message.isSigned())
        {
            builder.setSpan(new ImageSpan(check),builder.length() - 2,builder.length(),0);
            Log.d("Viktor","isSigned");

        }
        else
        {
            builder.setSpan(new ImageSpan(cross),builder.length() - 2,builder.length(),0);
            Log.d("Viktor","not isSigned");

        }

        if (message.isVerified())
        {
            builder.setSpan(new ImageSpan(check),builder.length() - 3,builder.length(),0);
            Log.d("Viktor","isVerified");

        }
        else
        {
            Log.d("Viktor","not isVerified");
            builder.setSpan(new ImageSpan(cross),builder.length() - 3,builder.length(),0);

        }
        */
        holder.msg.setText(builder);
        holder.timestamp.setText(builder_msg_inf);
    }

    @Override
    public int getItemCount() {
        return msgs.size();
    }



    public class ViewHolderBase extends RecyclerView.ViewHolder {
        public TextView msg, timestamp;
        public ImageView image_capture;

        public ViewHolderBase(View itemView)
        {
            super(itemView);
            msg = (TextView) itemView.findViewById(R.id.msg);
            timestamp = (TextView) itemView.findViewById(R.id.timestamp);
            image_capture = (ImageView) itemView.findViewById(R.id.image_capture);
        }
    }

    public class ViewHolderMsg extends ViewHolderBase {
        public ViewHolderMsg(View itemView) {
            super(itemView);
        }
    }

    public class ViewHolderMsgMy extends ViewHolderBase {
        public ViewHolderMsgMy(View itemView) {
            super(itemView);
        }
    }
}
