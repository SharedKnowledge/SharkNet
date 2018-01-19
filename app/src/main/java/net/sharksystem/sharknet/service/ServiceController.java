package net.sharksystem.sharknet.service;

import android.content.Context;
import android.content.Intent;

import net.sharksystem.sharknet.locationprofile.SharkServiceBinder;

/**
 * Created by Max on 15.01.18.
 *
 * @author Max Oehme (546545)
 */

public class ServiceController implements SharkServiceBinder {
    private Intent service;
    private Context mContext;

    public ServiceController(Context context, Class<?> serviceClass) {
        this.mContext = context;
        this.service = new Intent(mContext, serviceClass);
    }

    @Override
    public void start() {
        mContext.startService(service);
    }

    @Override
    public void stop() {
        mContext.stopService(service);
    }
}
