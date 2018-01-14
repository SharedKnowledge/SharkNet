package net.sharksystem.sharknet.locationprofile.geometry;

import net.sharkfw.knowledgeBase.geom.PointGeometry;

/**
 * Created by Max on 13.01.18.
 *
 * @author Max Oehme (546545)
 */

public interface ProfileGeometry {
    double distanceTo(PointGeometry location);
}
