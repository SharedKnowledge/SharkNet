package net.sharksystem.sharknet.schnitzeljagd.locator;

import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by emils on 14.11.2017.
 */

public interface GoogleApiConnectionStatusListener {
    void locatorReady();
    void locatorNotReady();
}
