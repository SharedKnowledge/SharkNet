package berlin.htw.schneider.viktor.sharknet;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.sharknet_api_android.interfaces.Message;

import java.util.List;

/**
 * Created by viktorowich on 01/06/16.
 */
public class MsgListAdapter extends RecyclerView.Adapter<MsgListAdapter.ViewHolderBase>
{
    private List<Message> msgs;
    //Drawable check,cross;

    public MsgListAdapter( List<Message> objects)
    {
        msgs = objects;
    }

    public class ViewHolderBase extends RecyclerView.ViewHolder
    {
        public TextView msg,timestamp;

        public ViewHolderBase(View itemView)
        {
            super(itemView);
            msg = (TextView) itemView.findViewById(R.id.msg);
            timestamp = (TextView) itemView.findViewById(R.id.timestamp);
            //check = itemView.getResources().getDrawable(R.drawable.ic_check_green_600_18dp);
           // cross = itemView.getResources().getDrawable(R.drawable.ic_close_red_300_18dp);
        }
    }

    public class ViewHolderMsg extends ViewHolderBase
    {
        public TextView msg;

        public ViewHolderMsg(View itemView)
        {
            super(itemView);
            msg = (TextView) itemView.findViewById(R.id.msg);
            //check = itemView.getResources().getDrawable(R.drawable.ic_check_green_600_18dp);
            //cross = itemView.getResources().getDrawable(R.drawable.ic_close_red_300_18dp);
        }
    }

    public class ViewHolderMsgMy extends ViewHolderBase
    {
        public TextView msg;

        public ViewHolderMsgMy(View itemView)
        {
            super(itemView);
            msg = (TextView) itemView.findViewById(R.id.msg);
            //check = itemView.getResources().getDrawable(R.drawable.ic_check_green_600_18dp);
            //cross = itemView.getResources().getDrawable(R.drawable.ic_close_red_300_18dp);
        }
    }

    /**
     * Check if i am the creator of the message.
     *
     * @param position  Position of the Message from the List of all Messages
     *
     * @return int viewType: 0 not my Message
     *                       1 my Message
     */
    @Override
    public int getItemViewType(int position)
    {
        Message message = msgs.get(position);
        if(message.isMine())
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }

    @Override
    public ViewHolderBase onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView;
        switch (viewType)
        {
            case 0:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.line_item_msg,parent,false);
                return new ViewHolderMsg(itemView);
            case 1:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.line_item_msg_my,parent,false);
                return new ViewHolderMsgMy(itemView);

        }
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.line_item_msg,parent,false);
        return new ViewHolderMsg(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolderBase holder, int position)
    {
        Message message = msgs.get(position);
        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableStringBuilder builder_msg_inf = new SpannableStringBuilder();
        String s = null;
        try {
            s = new java.text.SimpleDateFormat("HH:mm dd.MM.yyyy").format(message.getTimestamp());
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
        try {
            builder.append(message.getContent().getMessage());
        } catch (SharkKBException e) {
            e.printStackTrace();
        }

        if(message.isMine())
        {
            s = "Gesendet am "+s;
            //holder.msg.setTextAlignment();
           // holder.msg.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.sharknet));
            holder.timestamp.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.sharknet));
        }
        else
        {
            s = "Empfangen am "+s;
            holder.timestamp.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.cardview_dark_background));
        }
        builder_msg_inf.append(s);
       // builder_msg_inf.setSpan(new ImageSpan(holder.itemView.getResources().getDrawable(R.drawable.circle_green)),builder_msg_inf.length() - 1,builder_msg_inf.length(),0);
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
    public int getItemCount()
    {
        return msgs.size();
    }
}
