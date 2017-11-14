package net.sharksystem.sharknet.schnitzeljagd;

import net.sharksystem.api.models.Message;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Yannik on 04.11.2017.
 */

class Schnitzeljagd implements Serializable {
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
}
