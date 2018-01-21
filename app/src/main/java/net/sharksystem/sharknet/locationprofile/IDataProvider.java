package net.sharksystem.sharknet.locationprofile;

import net.sharkfw.knowledgeBase.geom.SharkPoint;

import java.util.List;

/**
 * Created by Max on 15.01.18.
 *
 * @author Max Oehme (546545)
 */

public interface IDataProvider {
    List<SharkPoint> getAllPointData();

    void putAllData(List<SharkPoint> points);
}
