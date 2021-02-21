package me.walrus.supremehomes.network;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.walrus.supremehomes.SupremeHomes;
import me.walrus.supremehomes.util.ConfigManager;

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

    public static void initializePlayer(String UUID) throws SQLException {
        String SQL_QUERY = "CREATE TABLE IF NOT EXISTS byp_userdata" +
                "(id int not null auto_increment," +
                " name varchar(255)," +
                " x int(11)," +
                " y int(11)," +
                " z int(255)," +
                "" +
                " primary key (id))";

        Connection con = getConnection();
        PreparedStatement pst = con.prepareStatement(SQL_QUERY);
        int result = pst.executeUpdate();

    }

    public static void updateHome(String homeName, String UUID, double x, double y, double z) throws SQLException {
        List<Home> homes = fetchData(UUID);
        String SQL_QUERY = "update " + UUID + " set " +
                "x = " + x + ", " +
                "y = " + y + ", " +
                "z = " + z + "" +
                "where name = '" + homeName + "'";

        Connection con = getConnection();
        PreparedStatement pst = con.prepareStatement(SQL_QUERY);
        pst.executeUpdate();

    }

    public static void createHome(Home home) throws SQLException {
        String SQL_QUERY = "insert into " + home.getOwnerUUID() + "" +
                " values(" +
                home.getName() + ", " +
                home.getX() + ", " +
                home.getY() + ", " +
                home.getZ() + ")";
        Connection con = getConnection();
        PreparedStatement pst = con.prepareStatement(SQL_QUERY);
        pst.executeUpdate();
    }

    public static void deleteHome(String UUID, String homeName) throws SQLException {
        String SQL_QUERY = "delete from " + UUID + " where name = " + homeName;
        Connection con = getConnection();
        PreparedStatement pst = con.prepareStatement(SQL_QUERY);
        pst.executeUpdate();
    }

    public static List<Home> fetchData(String UUID) throws SQLException {
        String SQL_QUERY = "select * from " + UUID;
        List<Home> homes;
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_QUERY);
             ResultSet rs = pst.executeQuery()) {
            homes = new ArrayList<>();
            Home home;
            while (rs.next()) {
                if (rs.getString("table_name").equalsIgnoreCase(UUID)) {
                    home = new Home();
                    home.setX(rs.getDouble("x"));
                    home.setY(rs.getDouble("y"));
                    home.setZ(rs.getDouble("z"));
                    home.setOwnerUUID(UUID);
                    home.setName(rs.getString("name"));
                    homes.add(home);
                }
            }
        }
        return homes;
    }
}
