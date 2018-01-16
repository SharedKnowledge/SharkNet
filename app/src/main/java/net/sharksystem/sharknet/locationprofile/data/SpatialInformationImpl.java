package net.sharksystem.sharknet.locationprofile.data;

import net.sharkfw.knowledgeBase.spatial.SpatialInformation;

/**
 * Created by Max on 13.01.18.
 *
 * @author Max Oehme (546545)
 */

public class SpatialInformationImpl implements SpatialInformation {

    private double sourceToProfile;
    private double entrenceToExit;
    private double destinationToProfile;
    private int weightEntrance;
    private int weightExit;

    public SpatialInformationImpl(double sourceToProfile, double entrenceToExit, double destinationToProfile, int weightEntrance, int weightExit) {
        this.sourceToProfile = sourceToProfile;
        this.entrenceToExit = entrenceToExit;
        this.destinationToProfile = destinationToProfile;
        this.weightEntrance = weightEntrance;
        this.weightExit = weightExit;
    }

    @Override
    public double getSourceToProfileDistance() {
        return this.sourceToProfile;
    }

    @Override
    public double getEntranceExitInProfileDistance() {
        return this.entrenceToExit;
    }

    @Override
    public double getDestinationToProfileDistance() {
        return this.destinationToProfile;
    }

    @Override
    public int getProfileEntrancePointWeight() {
        return this.weightEntrance;
    }

    @Override
    public int getProfileExitPointWeight() {
        return this.weightExit;
    }

    public void setSourceToProfileDistance(double sourceToProfile) {
        this.sourceToProfile = sourceToProfile;
    }

    public void setEntranceExitInProfileDistance(double entrenceToExit) {
        this.entrenceToExit = entrenceToExit;
    }

    public void setDestinationToProfileDistance(double destinationToProfile) {
        this.destinationToProfile = destinationToProfile;
    }

    public void setProfileEntrancePointWeight(int weightEntrance) {
        this.weightEntrance = weightEntrance;
    }

    public void setProfileExitPointWeight(int weightExit) {
        this.weightExit = weightExit;
    }

    @Override
    public String toString() {
        return "SpatialInformationImpl{" +
                "sourceToProfile=" + sourceToProfile +
                ", entrenceToExit=" + entrenceToExit +
                ", destinationToProfile=" + destinationToProfile +
                ", weightEntrance=" + weightEntrance +
                ", weightExit=" + weightExit +
                '}';
    }
}
