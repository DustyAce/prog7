package handlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.elements.Route;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.HashSet;
import java.util.Properties;

public class DatabaseHandler {
    private static final Logger logger = LogManager.getLogger("com.github.dustyace.lab6");
    static String url = "jdbc:postgresql://localhost:5432/prog78";
    static String user;
    static String pass;
    static Connection db;
    static Properties props = new Properties();
    static {
        user = System.getenv("psqluser");
        pass = System.getenv("psqlpass");
        if (user == null || pass == null) {
            logger.fatal("Could not get Posgres user/password from enviroment!");
            throw new RuntimeException("Could not get Posgres user/password from enviroment!");
        }
        props.setProperty("user", user);
        props.setProperty("password", pass);
    }

    public static void load() {//gon be used on startup
        try {
            db = DriverManager.getConnection(url, props);
            db.setAutoCommit(false);

            Statement s = db.createStatement();
            ResultSet rs = s.executeQuery("""
                SELECT r.id, r.name, r.coordinates, r.creationdate, r.from, r.to, r.distance, 
                       c.x "c.x", c.y "c.y", 
                       f.x "from.x", f.y "from.y", f.z "from.z", f.name "from.name", 
                       t.x "to.x", t.y "to.y", t.z "to.z", t.name "to.name" 
                FROM route r JOIN coordinates c ON (r.coordinates=c.id)
                LEFT JOIN location f ON (r.from=f.id)
                LEFT JOIN location t ON (r.to=t.id); 
            """);

            HashSet<Route> out = new HashSet<>();
            while (rs.next()) {
                Route r = new Route(rs);
                out.add(r);
            }

            CollectionHandler.setRoutes(out);
            logger.info("Successfully loaded {} objects", out.size());

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            try { db.rollback(); } catch (SQLException ex) {
                throw new RuntimeException(ex); //if this is thrown ur fucked up a lot
            }
            throw new RuntimeException(e);
        }
    }

    public static Long insert(Route r, String owner) {
        try {
            PreparedStatement ps_coords = db.prepareStatement("INSERT INTO coordinates(x,y) VALUES (?, ?)");
            r.getCoordinates().setValuesInStatement(ps_coords);
            ps_coords.execute();


            PreparedStatement ps_route = db.prepareStatement(
                    "INSERT INTO route(name,coordinates,distance,owner) VALUES " +
                    "(?, (SELECT max(id) FROM coordinates), ?, (SELECT id FROM users WHERE name ILIKE ?));");
            ps_route.setString(1,r.getName());
            ps_route.setLong(2,r.getDistance());
            ps_route.setString(3,owner);
            ps_route.execute();

            PreparedStatement insertLocation = db.prepareStatement("INSERT INTO location(x,y,z,name) VALUES (?, ?, ?, ?)");
            if (r.getFrom() != null) {
                r.getFrom().setValuesInStatement(insertLocation);
                insertLocation.execute();
                db.createStatement().execute(
                        "UPDATE route SET \"from\"=(SELECT max(id) FROM location) WHERE id=(SELECT max(id) FROM route);"
                );
            }

            if (r.getTo() != null) {
                r.getTo().setValuesInStatement(insertLocation);
                insertLocation.execute();
                db.createStatement().execute(
                        "UPDATE route SET \"to\"=(SELECT max(id) FROM location) WHERE id=(SELECT max(id) FROM route);"
                );
            }

            db.commit();
            Statement s = db.createStatement();
            s.execute("SELECT max(id) FROM route;");
            ResultSet rs = s.getResultSet();
            rs.next();
            return rs.getLong(1);
        } catch (SQLException e) {
            logger.error("Insertion unsuccessfull!");
            logger.debug(e.getMessage());
            rollback();
            return null;
        }

    }

    public static boolean checkIfMin(Route r) {
        try {
            PreparedStatement ps = db.prepareStatement("SELECT 1 FROM route WHERE distance<?");
            ps.setLong(1, r.getDistance());
            ps.execute();
            return ps.getResultSet().next();
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean remove(Long id, String user) {
        try {
            if (!checkOwnership(id,user)) {
                OutputHandler.message("You do not own this object or no such object exists, cannot remove!");
                return false;
            }

            db.createStatement().execute(String.format(
                            "DELETE FROM route WHERE id=%s", id
                    ));
            db.commit();
            return true;
        } catch (SQLException e) {
            logger.error("Removal unsuccessfull!");
            logger.debug(e.getMessage());
            rollback();
            return false;
        }
    }

    public static HashSet<Long> removeGreater(Route r, String user) {
        try {
            PreparedStatement ret = db.prepareStatement("SELECT id FROM route WHERE owner= ( SELECT id FROM users WHERE name ILIKE ? ) AND distance>?;");
            ret.setString(1,user);
            ret.setLong(2, r.getDistance());
            ret.execute();

            PreparedStatement ps = db.prepareStatement(
                    "DELETE FROM route WHERE owner= ( SELECT id FROM users WHERE name ILIKE ? ) AND distance>?;");
            ps.setString(1,user);
            ps.setLong(2, r.getDistance());
            ps.execute();

            db.commit();

            HashSet<Long> idsToRemove = new HashSet<>();
            ResultSet rs = ret.getResultSet();
            while (rs.next()) {
                idsToRemove.add(rs.getLong(1));
            }
            return idsToRemove;

        } catch (SQLException e) {
            logger.error("GRemoval unsuccessfull!");
            logger.debug(e.getMessage());
            rollback();
            return new HashSet<>();
        }

    }

    public static boolean checkOwnership(Long id, String user) {
        try {
            PreparedStatement ps = db.prepareStatement(
                    "SELECT 1 FROM route WHERE (owner = ( SELECT id FROM users WHERE name=? ) OR owner IS NULL)AND id=?");
            ps.setString(1,user);
            ps.setLong(2, id);
            ps.execute();
            boolean ret= ps.getResultSet().next();
            logger.debug("Checking ownership of {} - {}... Result: {}", id, user, ret);
            return ret;
        } catch ( SQLException e ) {
            logger.error("Something went wrong in checkOwnership :(");
            logger.debug(e.getMessage());
            return false;
        }
    }

    public static HashSet<Long> clear(String user) {
        try {
            logger.debug("Clearing routes with owner '{}'...", user);
            PreparedStatement ret = db.prepareStatement("SELECT id FROM route WHERE owner= ( SELECT id FROM users WHERE name ILIKE ? );");
            ret.setString(1,user);
            ret.execute();
            PreparedStatement ps = db.prepareStatement(
                    "DELETE FROM route WHERE owner= ( SELECT id FROM users WHERE name ILIKE ? );");
            ps.setString(1,user);
            ps.execute();

            db.commit();

            HashSet<Long> idsToRemove = new HashSet<>();
            ResultSet rs = ret.getResultSet();
            while (rs.next()) {
                idsToRemove.add(rs.getLong(1));
            }
            return idsToRemove;
        } catch ( SQLException e ) {
            logger.error("Clear unsuccessfull!");
            logger.debug(e.getMessage());
            rollback();
            return new HashSet<>();
        }
    }

    public static boolean update(Route r, String owner) {
        try{
            if (!checkOwnership(r.getId(), owner)) {
                OutputHandler.message("You are not the owner of this object!");
                return false;
            }
            remove(r.getId(), owner);
            insert(r,owner);
            db.createStatement().execute(String.format(
                    "UPDATE route SET id=%s WHERE id = (SELECT max(id) FROM route)",
                    r.getId()
                    )
            );

            db.commit();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            logger.error("uh-oh :(");
        }
        return false;
    }

    private static void rollback() {
        try {db.rollback();} catch (SQLException ex) { logger.error("Couldn't roll back!"); }
    }

    private static final String dict = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    public static String getRandomString() {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<4; i++) {
            sb.append( dict.charAt(  (int) (Math.random()*dict.length()) ) );
        }
        return sb.toString();

    }


    public static boolean checkUser(String username, String password) {
        try {
            logger.debug("Checking if '{}':'{}' exists", username, password);
            username = username.toLowerCase();

            PreparedStatement selectUserPass = db.prepareStatement("SELECT pass FROM users WHERE name iLIKE ?;");
            selectUserPass.setString(1, username);
            selectUserPass.execute();
            ResultSet rs = selectUserPass.getResultSet();
            rs.next();

            String dbpass = rs.getString(1);
            logger.debug("Found username with hashed password: {}", dbpass);
            String[] passSalt = dbpass.split("\\.");
            String hashedPass = hashPass(password, passSalt[1]);

            if (dbpass.equals(hashedPass)) {
                logger.info("User '{}' with given password found!", username);
                return true;
            }
            throw new SQLException();
        } catch (SQLException e) {
            logger.info("No user '{}' with pass '{}' found.", username, password);
            return false;
        } catch (NullPointerException npe) {
            logger.info("Null pointer or password!");
            return false;
        }
    }

    public static boolean registerUser(String username, String password) {
        try {
            PreparedStatement s = db.prepareStatement("SELECT 1 FROM users WHERE name LIKE ?");
            s.setString(1,username);
            s.execute();

            if (s.getResultSet().next()) {
                OutputHandler.message("User already exists!");
                return false;
            }

            username = username.toLowerCase();
            String hashedPass = hashPass(password, getRandomString());

            PreparedStatement insertUser = db.prepareStatement("INSERT INTO users (name, pass) VALUES (?, ?)");
            insertUser.setString(1, username);
            insertUser.setString(2, hashedPass);
            insertUser.execute();
            db.commit();
            OutputHandler.message("Successfully registered!");
            return true;
        } catch (SQLException e) {
            try {db.rollback();} catch (SQLException ex) { logger.error("Couldn't roll back!"); }
            OutputHandler.message("Something went wrong :(");
            logger.error(e.getMessage());
            return false;
        }
    }

    public static String hashPass(String pass, String salt) {
        try {
            MessageDigest md2 = MessageDigest.getInstance("MD2");
            BigInteger no = new BigInteger(1, md2.digest((salt + pass).getBytes()));
            String ret = String.format("%32s.%s", no.toString(16), salt).replace(" ", "0");
            logger.debug("Hashing '{}' with salt '{}'. Got '{}'", pass, salt, ret);
            return ret;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
