package berlin.htw.schneider.viktor.sharknet;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import net.sharkfw.knowledgeBase.TXSemanticTag;

import java.util.List;

/**
 * Created by viktorowich on 20/06/16.
 */
public class InterestsListAdapter extends ArrayAdapter<TXSemanticTag>
{

    private List <TXSemanticTag> txsemantictags;

    public InterestsListAdapter(Context context, int resource, List<TXSemanticTag> txsemantictags)
    {
        super(context, resource, txsemantictags);
        this.txsemantictags = txsemantictags;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.line_item_interest,parent,false);
        }
        TXSemanticTag tx = txsemantictags.get(position);
        TextView t = (TextView) convertView.findViewById(R.id.interest_name);
        t.setText(tx.getName());

        return convertView;
    }
}
