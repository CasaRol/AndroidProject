package sdu.shoppinglistapp.business;

import java.util.ArrayList;
import java.util.List;

import sdu.shoppinglistapp.persistence.DbHandler;

public class User {
    private String name;
    private String eMail;
    private String password = "";
    private int userID;
    private List<ShopList> subscribedShopLists;
    private DbHandler dbh = DbHandler.getInstance();


    /**
     * for creating users that have been given an id from the database
     * @param name
     * @param eMail
     * @param userID
     * @param subscribedShopLists
     */
    public User(String name, String eMail, int userID, List<ShopList> subscribedShopLists) {
        this.name = name;
        this.eMail = eMail;
        this.userID = userID;
        this.subscribedShopLists = subscribedShopLists;
    }

    /**
     * for registering new users in the system
     * @param name
     * @param email
     * @param password
     */
    public User(String name, String email, String password) {
        this.name = name;
        this.eMail = email;
        this.password = password;
        this.userID = -1;
        this.subscribedShopLists = new ArrayList<ShopList>();
    }

    public void addSubscribedShopList(ShopList slist) {
        this.subscribedShopLists.add(slist);
        //dbh.addUserToList(slist, this.userID);  *** it is the responsibility of ShopList to tell the DB what users are connected to it
    }

    public void removeSubscribedShopList(ShopList slist) {
        slist.removeUser(this.userID);
    }

    public void newSList (String givenName) {
        ShopList slist;

        if (givenName.equals("")) {
            slist = new ShopList(userID, this.name);
        } else {
            slist = new ShopList(userID, this.name, givenName);
        }

        slist.setId(dbh.createShopList(slist).getId());
        addSubscribedShopList(slist);
    }

    public void addItem (ShopItem newItem ,ShopList slist) {
        //add item to shoplist
        slist.addItem(newItem);
    }

    public void removeItem (ShopItem newItem, ShopList slist) {
        //remove item to shoplist
        slist.removeItem(newItem);
    }

    public void changeCheckmark(ShopList slist, ShopItem checkedItem) {
        slist.checkmark(checkedItem);
    }

    // *** GETTERS BELOW HERE ***
    public String getName() {
        return name;
    }

    public String geteMail() {
        return eMail;
    }

    public String getPassword() {
        return password;
    }

    public int getUserID() {
        return userID;
    }

    public List<ShopList> getSubscribedShopLists() {
        return subscribedShopLists;
    }

    // *** SETTERS BELOW HERE ***
    public void setId(int id) {
        this.userID = id;
    }
}