package berlin.htw.schneider.viktor.sharknet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.interfaces.Comment;
import net.sharksystem.api.interfaces.Feed;

import java.util.List;

/**
 * Created by viktorowich on 29/06/16.
 */
public class TimelineListAdapter extends ArrayAdapter<Feed>
{
    private List<Feed> feeds;


    public TimelineListAdapter(Context context, int resource, List<Feed> objects)
    {
        super(context, resource, objects);
        this.feeds = objects;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.line_item_timeline,parent,false);
        }
        //Typeface type = Typeface.createFromAsset(getContext().getAssets(),"fonts/RockSalt.TTF");
        Feed feed = feeds.get(position);
        //Title
        TextView title = (TextView) convertView.findViewById(R.id.feed_message);
        //title.setTypeface(type);


        String s = null;
        try {
            s = new java.text.SimpleDateFormat("HH:mm").format(feed.getTimestamp());
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
        try {
            title.setText(String.format("%s     %s", feed.getContent().getMessage(), s));
        } catch (SharkKBException e) {
            e.printStackTrace();
        }


        try {
            List<Comment> comments      = feed.getComments(true);
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
        //Log.d("adapter", String.valueOf(msgs.size()));


        //Image
        ImageView image = (ImageView) convertView.findViewById(R.id.feed_image);
        //TODO: not possible to add picture to feed
        /*
        if(feed.getContent().ge != null)
        {
            if(chat.getContacts().size()>1)
            {
                image.setImageResource(R.drawable.ic_group_black_24dp);
            }
            else
            {
                image.setImageResource(R.drawable.ic_person_black_24dp);
            }
        }
        else
        {
            //TODO: image.setImageResource(chat.getPicture().);
        }
*/
        return convertView;
    }
}
