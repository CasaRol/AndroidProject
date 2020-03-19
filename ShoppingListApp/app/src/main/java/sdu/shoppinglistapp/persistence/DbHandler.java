package sdu.shoppinglistapp.persistence;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.*;

import sdu.shoppinglistapp.business.ShopItem;
import sdu.shoppinglistapp.business.ShopList;
import sdu.shoppinglistapp.business.User;

public class DbHandler {
    private static DbHandler instance = null; // instance of singleton class

    // getinstance for singleton instance
    public static DbHandler getInstance() {
        if (instance == null) {
            instance = new DbHandler();
        }
        return instance;
    }

    // private constructor for singleton purposes
    private DbHandler() {
    }


    static String url = "jdbc:postgresql://tek-mmmi-db0a.tek.c.sdu.dk:5432/si3_2018_group_9_db"; //fix to elefant
    static String dbUsername = "si3_2018_group_9"; //fix to elefant
    static String dbPassword = "copt22*viols"; //fix to elefant

    /**
     * A template for a sql querry, change the return type and use of prerared
     * statement as needed
     */
    /*
    private void queryTemplate(){
        try(Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)){
            Class.forName("org.postgresql.Driver");

             PreparedStatement st = conn.prepareStatement("INSERT INTO persons (firstname, lastname) VALUES (?, ?)");
            st.setString(1, person.getFName());
            st.setString(2, person.getLName());
            ResultSet rs = st.executeQuery();

        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DbHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

     */


    public int getUserid(String email) {
        int retID=-1;
        try(Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)){
            Class.forName("org.postgresql.Driver");

            PreparedStatement st = conn.prepareStatement("SELECT users.user_id FROM users WHERE users.email = ?");
            st.setString(1, email);
            ResultSet rs = st.executeQuery();

            while (rs.next()){
                retID = rs.getInt("user_id");
            }

        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DbHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retID;
    }

    public ShopItem addItem(ShopList slist, ShopItem item) {
        try(Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)){
            Class.forName("org.postgresql.Driver");

            PreparedStatement st = conn.prepareStatement("INSERT INTO items(name, added_by, checkmarked, list_id) VALUES(?,?,?,?) RETURNING items.item_id"); //<name>,<added_by>,<checkmarked>,<list_id>)");
            st.setString(1, item.getItemString());
            st.setString(2, item.getAddedBy());
            st.setBoolean(3, item.isCheckmarked());
            st.setInt(4, slist.getId());

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                item.setId(rs.getInt("items.item_id"));
            }


        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DbHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return item;
    }

    public void removeItem(ShopList slist, ShopItem item) {
        try(Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)){
            Class.forName("org.postgresql.Driver");

            PreparedStatement st = conn.prepareStatement("DELETE FROM items WHERE items.item_id = ?");
            st.setInt(1, item.getId());
            st.executeQuery();

        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DbHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void checkmark(ShopList slist, ShopItem item) {
        // TODO query database to set checkmark-boolean to TRUE if currently FALSE, and the other way around
        /*
        UPDATE items
        SET checkmarked = <value>
        WHERE items.item_id = <value>
         */
    }

    public void addUserToList(ShopList slist, int userid) {
        try(Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)){
            Class.forName("org.postgresql.Driver");

            PreparedStatement st = conn.prepareStatement("INSERT INTO lists(list_id, user_id) VALUES(?,?)");
            st.setInt(1, slist.getId());
            st.setInt(2, userid);
            st.executeQuery();

        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DbHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void removeUserFromList(ShopList slist, int userid) {
        try(Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)){
            Class.forName("org.postgresql.Driver");

            PreparedStatement st = conn.prepareStatement("DELETE FROM lists WHERE lists.list_id = ? AND lists.user_id = ?");
            st.setInt(1, slist.getId());
            st.setInt(2, userid);
            st.executeQuery();

        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DbHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public HashMap<Integer, String> getUsername(List<Integer> userid) {
        // TODO query database to get screenname from userids
        // evt lav stringbuilder og kør foreach over listen??
    }

    public ShopList getShopList(int slistid) {
        // TODO query database to get shoplist from id
    }

    public boolean hasShopListChanged(ShopList slist) {
        boolean retBo = false; // prob shuld be true but to test that querry actualy works its set a false
        try(Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)){
            Class.forName("org.postgresql.Driver");

            PreparedStatement st = conn.prepareStatement("SELECT lists.last_updated FROM lists WHERE lists.list_id = ?");
            st.setInt(1, slist.getId());
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                retBo = slist.getTime()!=rs.getLong("last_updated");
            }

        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DbHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retBo;
    }

    public ShopList createShopList(ShopList slist) {
        int userid = slist.getUsers().get(0);
        try(Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)){
            Class.forName("org.postgresql.Driver");

            PreparedStatement st = conn.prepareStatement("INSERT INTO lists(user_id, list_name, last_updated) VALUES(?, ?, ?) RETURNING lists.list_id");
            st.setInt(1, userid);
            st.setString(2, slist.getName());
            st.setLong(3,slist.getTime());
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                slist.setId(rs.getInt(rs.getInt("list_id")));
            }

        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DbHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return slist;
    }

    public User registerUser(User user) {
        try(Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)){
            Class.forName("org.postgresql.Driver");

            PreparedStatement st = conn.prepareStatement("INSERT INTO users(screen_name, email, password) VALUES(?, ?, ?) RETURNING users.user_id");
            st.setString(1, user.getName());
            st.setString(2, user.geteMail());
            st.setString(2, user.getPassword());
            ResultSet rs = st.executeQuery();

            while (rs.next()){
                user.setId(rs.getInt(user_id));
            }

        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DbHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return user;
    }

    public boolean doesEmailExist(String email) {
        boolean retbo = false;
        try(Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)){
            Class.forName("org.postgresql.Driver");

            PreparedStatement st = conn.prepareStatement("SELECT users.email FROM users WHERE users.email = ?");
            st.setString(1, email);
            ResultSet rs = st.executeQuery();

            retbo = rs.next();

        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DbHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retbo;
    }

    public List<ShopList> getShoplistOverview(User user) {
        // TODO ramake this to load everything!!!!
        ArrayList<ShopList> retList = new ArrayList<>();
        try(Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)){
            Class.forName("org.postgresql.Driver");

            PreparedStatement st = conn.prepareStatement("SELECT lists.list_id, lists.list_name FROM lists WHERE lists.user_id = ?");
            st.setInt(1, user.getUserID());
            ResultSet rs = st.executeQuery();

            while (rs.next()){
                retList.add(new ShopList(rs.getString("lists.list_name"), rs.getInt("lists.list_id")));
            }

        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DbHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        // TODO query database to get all shoplists that the User is a part of
        /*
        SELECT lists.list_id, lists.list_name
        FROM lists
        WHERE lists.user_id = <value>
         */
    }

    public User checkCredentials(String email, String pw) {
        // TODO query database to check that given email matches given password. Return the User object if it matches

        // TODO in case of mismatch, return a null object
    }
}
