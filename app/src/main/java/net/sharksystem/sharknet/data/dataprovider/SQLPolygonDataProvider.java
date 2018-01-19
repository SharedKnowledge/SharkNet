package net.sharksystem.sharknet.data.dataprovider;

import android.content.Context;

import net.sharkfw.knowledgeBase.geom.SharkPoint;
import net.sharksystem.sharknet.data.SharkNetDbHelper;
import net.sharksystem.sharknet.locationprofile.PolygonDataProvider;

import java.util.List;

/**
 * Created by Max on 15.01.18.
 *
 * @author Max Oehme (546545)
 */

public class SQLPolygonDataProvider implements PolygonDataProvider {
    private Context mContext;

    public SQLPolygonDataProvider(Context context) {
        this.mContext = context;
    }

    @Override
    public List<SharkPoint> getPolygonData() {
        return SharkNetDbHelper.getInstance().readSharkPointFromDB(mContext);
    }
}
