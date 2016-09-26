package berlin.htw.schneider.viktor.sharknet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import berlin.htw.schneider.viktor.sharknet.R;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.interfaces.Contact;


import java.util.List;

/**
 * Created by viktorowich on 08/06/16.
 */
public class ContactsListAdapter extends ArrayAdapter<Contact>
{

    private List<Contact> contacts;

    public ContactsListAdapter(Context context, int resource, List<Contact> objects)
    {
        super(context, resource, objects);
        this.contacts = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.line_item_con,parent,false);
        }

        Contact contact = contacts.get(position);

        //Name
        TextView title = (TextView) convertView.findViewById(R.id.contact_nickname);
        try {
            title.setText(contact.getNickname());
        } catch (SharkKBException e) {
            e.printStackTrace();
        }

        //key
        TextView name = (TextView) convertView.findViewById(R.id.contact_name);
        try {
            name.setText(contact.getPublicKey());
        } catch (SharkKBException e) {
            e.printStackTrace();
        }


        //Image
        ImageView image = (ImageView) convertView.findViewById(R.id.contact_image);
        image.setImageResource(R.drawable.ic_person_pink_700_48dp);
        //TODO: != change to ==  then load image works
        /*if(contact.getPicture() != null)
        {
            image.setImageResource(R.drawable.ic_person_black_24dp);
        }
        else
        {
            //TODO: image.setImageResource(contact.getPicture().);
        }*/

        return convertView;
    }
}
