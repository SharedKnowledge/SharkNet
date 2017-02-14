package net.sharksystem.sharknet.pki;

import android.graphics.Color;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.security.SharkPublicKey;
import net.sharkfw.system.SharkPKVerifyException;
import net.sharksystem.api.impl.InterestImpl;
import net.sharksystem.sharknet.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static net.sharksystem.api.shark.Application.getContext;

/**
 * Created by j4rvis on 2/11/17.
 */
public class PublicKeyListAdapter extends BaseAdapter {

    private List<SharkPublicKey> items = new ArrayList<>();

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Object getItem(int position) {
        return this.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void updateItems(List<SharkPublicKey> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.pki_public_key_line_item, parent, false);
        }

//        final SharkPublicKey item = (SharkPublicKey) this.getItem(position);
//
//        TextView owner = (TextView) convertView.findViewById(R.id.text_view_owner);
//        TextView isValid = (TextView) convertView.findViewById(R.id.text_view_is_valid);
//
//        owner.setText(item.getOwner().getName());
//
//        long validity = item.getValidity();
//        Date date = new Date(validity);
//        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy 'at' HH:mm:ss", Locale.GERMANY);
//        String s = format.format(date);
//
//        isValid.setText(s);
//        if(date.after(new Date(System.currentTimeMillis()))){
//            isValid.setTextColor(Color.GREEN);
//        } else {
//            isValid.setTextColor(Color.RED);
//        }
//
//        ImageButton imageButton = (ImageButton) convertView.findViewById(R.id.imageButton);
//        imageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                item.delete();
//                items.remove(item);
//                notifyDataSetChanged();
//            }
//        });

        return convertView;
    }
}
