package me.walrus.supremehomes.network;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.SQLException;

public class Home {
    private double x, y, z;
    private String name;
    private String ownerUUID;
    private String world;

    public double getX() {
        return x;
    }

    public void updateHome(double x, double y, double z, String world) throws SQLException {
        setX(x);
        setY(y);
        setZ(z);
        setWorld(world);;
        DatabaseManager.updateHome(name, ownerUUID, x, y, z, world);
    }

    public Location getLocation() {
        return new Location(Bukkit.getWorld(world), getX(), getY(), getZ());
    }


    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerUUID() {
        return ownerUUID;
    }

    public void setOwnerUUID(String ownerUUID) {
        this.ownerUUID = ownerUUID;
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }
}
