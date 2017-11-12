package net.sharksystem.sharknet.schnitzeljagd;

import net.sharksystem.sharknet.schnitzeljagd.locator.LocatorLocation;

/**
 * Created by Yannik on 12.11.2017.
 */

public class Ort {
    private String name;
    private LocatorLocation location;

    public Ort(String name) {
        this.name = name;
    }

    public Ort(String name, LocatorLocation location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public LocatorLocation getLocation() {
        return location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(LocatorLocation location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return name;
    }
}
