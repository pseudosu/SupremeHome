package me.walrus.supremehomes.network;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.walrus.supremehomes.SupremeHomes;
import me.walrus.supremehomes.util.ConfigManager;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private static HikariConfig databaseConfig = new HikariConfig();
    private static HikariDataSource ds;
    private static ConfigManager config = SupremeHomes.getConfigManager();

    final static String JDBC_URL = "jdbc:mysql://" + config.getMYSQL_HOST() + ":" + config.getMYSQL_PORT() + "/" + config.getMYSQL_DATABASE();

    static {
        databaseConfig.setJdbcUrl(JDBC_URL);
        databaseConfig.setUsername(config.getMYSQL_USERNAME());
        databaseConfig.setPassword(config.getMYSQL_PASSWORD());
        databaseConfig.addDataSourceProperty("cachePrepStmts", "true");
        databaseConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        databaseConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds = new HikariDataSource(databaseConfig);
    }

    private DatabaseManager() {
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public  static List<Home> fetchData(String UUID) throws SQLException {
        String SQL_QUERY = "select * from " + UUID;
        List<Home> homes = null;
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_QUERY);
             ResultSet rs = pst.executeQuery();) {
            homes = new ArrayList<>();
            Home home;
            while (rs.next()) {
                if (rs.getString("table_name").equalsIgnoreCase(UUID)) {
                    home = new Home();
                    home.setX(rs.getDouble("x"));
                    home.setX(rs.getDouble("y"));
                    home.setX(rs.getDouble("z"));
                    home.setOwnerUUID(UUID);
                    home.setName(rs.getString("name"));
                    homes.add(home);
                }
            }
        }
        return homes;
    }
}
