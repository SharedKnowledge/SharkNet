package net.sharksystem.sharknet.schnitzeljagd;

import android.support.annotation.NonNull;

import net.sharksystem.sharknet.schnitzeljagd.locator.LocatorLocation;

import java.io.Serializable;

/**
 * Created by Yannik on 04.11.2017.
 */

class Schnitzel implements Comparable<Schnitzel> {
    private String message;
    private LocatorLocation loc;
    private int idx;

    public Schnitzel(int index, String msg, LocatorLocation location){
        this.idx = index;
        this.message = msg;
        this.loc = location;
    }
    public int getIdx() {
        return idx;
    }

    public String getMessage() {
        return message;
    }


    public LocatorLocation getLoc() {
        return loc;
    }

    protected void setIdx(int idx) {
        this.idx = idx;
    }

    protected void setLoc(LocatorLocation loc) {
        this.loc = loc;
    }

    protected void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int compareTo(@NonNull Schnitzel schnitzel) {
        if (idx > schnitzel.getIdx()) {
            return -1;
        } else if (idx < schnitzel.getIdx()) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        if (message.length() <30 ){
            return idx + "  " + message;
        }
        return idx + "  " + message.substring(0,30);
    }
}
