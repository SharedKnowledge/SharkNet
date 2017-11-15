package net.sharksystem.sharknet.schnitzeljagd;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import net.sharksystem.sharknet.schnitzeljagd.locator.LocatorLocation;

import java.io.Serializable;

/**
 * Created by Yannik on 04.11.2017.
 */

class Schnitzel implements Comparable<Schnitzel>, Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.message);
        dest.writeParcelable(this.loc, flags);
        dest.writeInt(this.idx);
    }

    protected Schnitzel(Parcel in) {
        this.message = in.readString();
        this.loc = in.readParcelable(LocatorLocation.class.getClassLoader());
        this.idx = in.readInt();
    }

    public static final Parcelable.Creator<Schnitzel> CREATOR = new Parcelable.Creator<Schnitzel>() {
        @Override
        public Schnitzel createFromParcel(Parcel source) {
            return new Schnitzel(source);
        }

        @Override
        public Schnitzel[] newArray(int size) {
            return new Schnitzel[size];
        }
    };
}
