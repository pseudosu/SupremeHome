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

    final static String JDBC_URL = "jdbc:mysql://" + config.getMYSQL_HOST() + ":" + config.getMYSQL_PORT() + "/" + config.getMYSQL_DATABASE() + "?useSSL=false&characterEncoding=latin1&useConfigs=maxPerformance&allowPublicKeyRetrieval=true";

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
        String SQL_QUERY = "CREATE TABLE IF NOT EXISTS `" + UUID + "` " +
                "(id int not null auto_increment," +
                " name varchar(255)," +
                " world varchar(255)," +
                " x int(11)," +
                " y int(11)," +
                " z int(255)," +
                "" +
                " primary key (id))";

        Connection con = getConnection();
        PreparedStatement pst = con.prepareStatement(SQL_QUERY);
        int result = pst.executeUpdate();

    }

    public static void updateHome(String homeName, String UUID, double x, double y, double z, String world) throws SQLException {
        List<Home> homes = fetchData(UUID);
        String SQL_QUERY = "update `" + UUID + "` set " +
                "x = ?, y = ?, z = ?, world = ?" +
                " WHERE name = ?";

        System.out.println("Updating home...");
        Connection con = getConnection();
        PreparedStatement pst = con.prepareStatement(SQL_QUERY);
        pst.setDouble(1, x);
        pst.setDouble(2, y);
        pst.setDouble(3, z);
        pst.setString(4, world);
        pst.setString(5, homeName);
        pst.executeUpdate();

    }

    public static void createHome(Home home) throws SQLException {
        String SQL_QUERY = "insert into `" + home.getOwnerUUID() + "`" +
                " values(id, ?, ?, ?, ?, ?)";
        System.out.println(SQL_QUERY);
        Connection con = getConnection();
        PreparedStatement pst = con.prepareStatement(SQL_QUERY);
        pst.setString(1, home.getName());
        pst.setString(2, home.getWorld());
        pst.setDouble(3, home.getX());
        pst.setDouble(4, home.getY());
        pst.setDouble(5, home.getZ());

        pst.executeUpdate();
    }

    public static boolean deleteHome(String UUID, String homeName) throws SQLException {
        String SQL_QUERY = "delete from `" + UUID + "` where name = ?";
        Connection con = getConnection();
        PreparedStatement pst = con.prepareStatement(SQL_QUERY);
        pst.setString(1, homeName);
        int result = pst.executeUpdate();

        return result > 0;
    }

    public static List<Home> fetchData(String UUID) throws SQLException {
        String SQL_QUERY = "select * from `" + UUID + "`";
        List<Home> homes;
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_QUERY);
             ResultSet rs = pst.executeQuery()) {
            homes = new ArrayList<>();
            Home home;
            while (rs.next()) {

                home = new Home();
                home.setX(rs.getDouble("x"));
                home.setY(rs.getDouble("y"));
                home.setZ(rs.getDouble("z"));
                home.setOwnerUUID(UUID);
                home.setName(rs.getString("name"));
                home.setWorld(rs.getString("world"));
                homes.add(home);
            }
        }
        return homes;
    }
}
