package me.walrus.supremehomes.network;

import org.bukkit.Location;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class PlayerData {
    private UUID uuid;
    private List<Home> homes;

    public PlayerData(UUID uuid) throws SQLException {
        this.uuid = uuid;
        homes = DatabaseManager.fetchData(uuid.toString());
    }

    public List<Home> getHomes() {
        return homes;
    }

    public void addHome(String name, Location location) throws SQLException {
        Home newHome = new Home();
        newHome.setX(location.getX());
        newHome.setY(location.getY());
        newHome.setZ(location.getZ());
        newHome.setName(name);
        newHome.setOwnerUUID(uuid.toString());
        homes.add(newHome);
        for (Home home : homes) {
            if (home.getName().equalsIgnoreCase(name)) {
                DatabaseManager.updateHome(name, uuid.toString(), newHome.getX(), newHome.getY(), newHome.getZ());
            }else{
                DatabaseManager.createHome(newHome);
            }
        }
    }

    public void deleteHome(String name) throws SQLException {
        DatabaseManager.deleteHome(uuid.toString(), name);
    }
}
