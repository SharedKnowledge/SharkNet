package net.sharksystem.sharknet.locationprofile.data;

import net.sharkfw.knowledgeBase.spatial.SpatialInformation;

/**
 * Created by Max on 13.01.18.
 *
 * @author Max Oehme (546545)
 */

public class SpatialInformationImpl implements SpatialInformation {


    @Override
    public double getSourceToProfileDistance() {
        return 0;
    }

    @Override
    public double getEntranceExitInProfileDistance() {
        return 0;
    }

    @Override
    public double getDestinationToProfileDistance() {
        return 0;
    }

    @Override
    public int getProfileEntrancePointWeight() {
        return 0;
    }

    @Override
    public int getProfileExitPointWeight() {
        return 0;
    }
}
