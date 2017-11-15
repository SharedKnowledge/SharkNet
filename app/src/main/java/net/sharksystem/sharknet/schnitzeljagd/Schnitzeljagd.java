package net.sharksystem.sharknet.schnitzeljagd;

import android.os.Parcel;
import android.os.Parcelable;

import net.sharksystem.api.models.Message;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Yannik on 04.11.2017.
 */

class Schnitzeljagd implements Parcelable {
    private ArrayList<Schnitzel> schnitzelList = new ArrayList<>();
    private String description;

    public Schnitzeljagd(String description){
        this.description = description;
    }
    public Schnitzeljagd(ArrayList<Schnitzel> schnitzel, String description) {
        this.schnitzelList = schnitzel;
        this.description = description;
    }

    public ArrayList<Schnitzel> getSchnitzel() {
        return schnitzelList;
    }
    public void addSchnitzel(Schnitzel schnitzel){
        schnitzelList.add(schnitzel);
    }
    public String getDescription() {
        return description;
    }

    protected void setSchnitzel(ArrayList<Schnitzel> schnitzel){
        this.schnitzelList = schnitzel;
    }

    protected void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.schnitzelList);
        dest.writeString(this.description);
    }

    protected Schnitzeljagd(Parcel in) {
        this.schnitzelList = in.createTypedArrayList(Schnitzel.CREATOR);
        this.description = in.readString();
    }

    public static final Parcelable.Creator<Schnitzeljagd> CREATOR = new Parcelable.Creator<Schnitzeljagd>() {
        @Override
        public Schnitzeljagd createFromParcel(Parcel source) {
            return new Schnitzeljagd(source);
        }

        @Override
        public Schnitzeljagd[] newArray(int size) {
            return new Schnitzeljagd[size];
        }
    };
}
