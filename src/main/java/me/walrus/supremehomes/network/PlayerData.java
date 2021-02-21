package me.walrus.supremehomes.network;

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
}
