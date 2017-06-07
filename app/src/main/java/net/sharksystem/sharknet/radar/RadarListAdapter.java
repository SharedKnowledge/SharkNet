package net.sharksystem.sharknet.radar;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.sharksystem.api.shark.peer.NearbyPeer;
import net.sharksystem.sharknet.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.SimpleFormatter;

/**
 * Created by j4rvis on 11/11/16.
 */

public class RadarListAdapter extends BaseAdapter {

    private static boolean mIsTimerRunning;
    private static int mElapsedTime;
    private static Timer mTimer;
    private Context mContext = null;
    private ArrayList<NearbyPeer> mNearbyPeers = new ArrayList<>();

    public RadarListAdapter(Context context) {
        mContext = context;
    }

    public void updateList(ArrayList<NearbyPeer> peers) {
        mNearbyPeers = peers;
        notifyDataSetChanged();
    }

    public void clearList(){
        mNearbyPeers.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mNearbyPeers.size();
    }

    @Override
    public NearbyPeer getItem(int position) {
        return mNearbyPeers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        NearbyPeer peer = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.radar_contact_list_item, parent, false);
        }

        ((TextView) convertView.findViewById(R.id.radar_contact_name)).setText(peer.getSender().getName());
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        ((TextView) convertView.findViewById(R.id.radar_contact_last_seen)).setText(format.format(peer.getLastSeen()));

        return convertView;
    }
}
