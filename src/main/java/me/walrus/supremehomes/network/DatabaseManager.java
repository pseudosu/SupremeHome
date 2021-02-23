package me.walrus.supremehomes.network;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.walrus.supremehomes.SupremeHomes;
import me.walrus.supremehomes.util.ConfigManager;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

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

    public static void initDatabase() throws SQLException {
        String SQL_QUERY = "create table if not exists home" +
                "(id int not null auto_increment," +
                " user_id char (37)," +
                " home_name varchar (255)," +
                " world_name varchar (255)," +
                " x double (11, 3)," +
                " y double (11, 3)," +
                " z double (11, 3)," +
                " primary key (id)," +
                " unique key idx_user_id_home_name (user_id,home_name))";

        Connection con = getConnection();
        PreparedStatement pst = con.prepareStatement(SQL_QUERY);

        pst.executeUpdate();
        con.close();
        convertDatabase();
    }

    public static void convertDatabase() throws SQLException {
        String SQL_QUERY = "SELECT table_name FROM information_schema.tables " +
                "WHERE not table_name = 'home' and TABLE_SCHEMA=?";

        Connection con = getConnection();
        PreparedStatement pst = con.prepareStatement(SQL_QUERY);
        pst.setString(1, SupremeHomes.getConfigManager().getMYSQL_DATABASE());


        List<String> tableNames = new ArrayList<>();
        ResultSet res = pst.executeQuery();
        while (res.next()) {
            tableNames.add(res.getString("table_name"));
        }

        for (String tableName : tableNames) {
            Bukkit.getLogger().log(Level.INFO, "CONVERTING EXISTING TABLE: " + tableName + " TO NEW FORMAT...");

            String DATA_QUERY = "select * from `" + tableName + "`";
            PreparedStatement ps = con.prepareStatement(DATA_QUERY);
            ResultSet data = ps.executeQuery();
            while (data.next()) {
                String homeName = data.getString("");
                String worldName = data.getString("world");
                double x = data.getDouble("x");
                double y = data.getDouble("y");
                double z = data.getDouble("z");

                String UPDATE_QUERY = "insert into home values(id,?,?,?,?,?,?)";

                PreparedStatement update = con.prepareStatement(UPDATE_QUERY);
                update.setString(1, tableName);
                update.setString(2, homeName);
                update.setString(3, worldName);
                update.setDouble(4, x);
                update.setDouble(5, y);
                update.setDouble(6, z);
                update.executeUpdate();
                Bukkit.getLogger().log(Level.INFO, "DONE");
            }
            String DROP_QUERY = "drop table `" + tableName + "`";
            PreparedStatement drop = con.prepareStatement(DROP_QUERY);
            drop.executeUpdate();

        }
        con.close();
    }

    public static void updateHome(String homeName, String UUID, double x, double y, double z, String world) throws SQLException {
        String SQL_QUERY = "update home set " +
                "x = ?, y = ?, z = ?, world_name = ?" +
                " WHERE home_name = ? and user_id = ?";

        Connection con = getConnection();
        PreparedStatement pst = con.prepareStatement(SQL_QUERY);
        pst.setDouble(1, x);
        pst.setDouble(2, y);
        pst.setDouble(3, z);
        pst.setString(4, world);
        pst.setString(5, homeName);
        pst.setString(6, UUID);
        pst.executeUpdate();
        con.close();
    }

    public static void createHome(Home home, String UUID) throws SQLException {
        String SQL_QUERY = "insert into home" +
                " values(id, ?, ?, ?, ?, ?, ?)";
        Connection con = getConnection();
        PreparedStatement pst = con.prepareStatement(SQL_QUERY);
        pst.setString(1, UUID);
        pst.setString(2, home.getName());
        pst.setString(3, home.getWorld());
        pst.setDouble(4, home.getX());
        pst.setDouble(5, home.getY());
        pst.setDouble(6, home.getZ());

        pst.executeUpdate();
        con.close();
    }

    public static boolean deleteHome(String UUID, String homeName) throws SQLException {
        String SQL_QUERY = "delete from home where home_name = ? and user_id = ?";
        Connection con = getConnection();
        PreparedStatement pst = con.prepareStatement(SQL_QUERY);
        pst.setString(1, homeName);
        pst.setString(2, UUID);
        int result = pst.executeUpdate();
        con.close();
        return result > 0;
    }

    public static List<Home> fetchData(String UUID) {
        String SQL_QUERY = "select * from home where user_id = ?";
        List<Home> homes;
        try {
            Connection con = getConnection();
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);
            pst.setString(1, UUID);
            ResultSet rs = pst.executeQuery();
            homes = new ArrayList<>();
            Home home;
            while (rs.next()) {

                home = new Home();
                home.setX(rs.getDouble("x"));
                home.setY(rs.getDouble("y"));
                home.setZ(rs.getDouble("z"));
                home.setOwnerUUID(UUID);
                home.setName(rs.getString("home_name"));
                home.setWorld(rs.getString("world_name"));
                homes.add(home);
            }
            con.close();
            return homes;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
