package net.sharksystem.sharknet.locationprofile;

import android.location.Location;

/**
 * Created by Max on 13.12.17.
 *
 * @author Max Oehme (546545)
 */

public interface LocationUpdateListener {
    void onLocationUpdate(Location location);
}
