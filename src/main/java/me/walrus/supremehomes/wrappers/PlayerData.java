package me.walrus.supremehomes.wrappers;

import me.walrus.supremehomes.network.DatabaseManager;
import me.walrus.supremehomes.network.Home;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
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

    public String getHomesAsString(){
        List<String> homeNames = getHomesAsList();
        return String.join("&a, &7", homeNames);
    }

    public List<String> getHomesAsList(){
        List<String> homeNames = new ArrayList<>();
        for(Home home : homes){
            homeNames.add(home.getName());
        }

        return homeNames;
    }

    public void addHome(String name, Location location) throws SQLException {
        Home newHome = new Home();
        newHome.setX(location.getX());
        newHome.setY(location.getY());
        newHome.setZ(location.getZ());
        newHome.setName(name);
        newHome.setOwnerUUID(uuid.toString());
        newHome.setWorld(location.getWorld().getName());
        if(homes.isEmpty()){
            DatabaseManager.createHome(newHome, uuid.toString());
            return;
        }
        for (Home home : homes) {
            if (home.getName().equalsIgnoreCase(name)) {
                DatabaseManager.updateHome(name, uuid.toString(), newHome.getX(), newHome.getY(), newHome.getZ(), location.getWorld().getName());
                homes = DatabaseManager.fetchData(uuid.toString());
                homes.add(newHome);
                return;
            }
        }
        DatabaseManager.createHome(newHome, uuid.toString());
        homes = DatabaseManager.fetchData(uuid.toString());
        homes.add(newHome);
    }

    public boolean deleteHome(String name) throws SQLException {
        return DatabaseManager.deleteHome(uuid.toString(), name);
    }
}
