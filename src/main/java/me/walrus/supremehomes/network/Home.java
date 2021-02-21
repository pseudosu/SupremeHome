package me.walrus.supremehomes.network;

import java.sql.SQLException;

public class Home {
    private double x, y, z;
    private String name;
    private String ownerUUID;

    public double getX() {
        return x;
    }

    public void updateHome(double x, double y, double z) throws SQLException {
        setX(x);
        setY(y);
        setZ(z);
        DatabaseManager.updateHome(name, ownerUUID, x, y, z);
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

}
