package application.user;

import application.auction.Auction;
import application.auction.AuctionCreationException;
import application.auction.ItemCategory;

import java.time.LocalDateTime;

public class User {

    private String firstName;
    private String lastName;
    private String userEmail;
    private String userName;
    private String password;

    private Boolean loggedIn;

    private Boolean isSeller;
    private Boolean isPreferred;

    public User(String firstName, String lastName, String userEmail, String userName, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userEmail = userEmail;
        this.userName = userName;
        this.password = password;
        this.loggedIn = false;
        this.isSeller = false;
        this.isPreferred = false;
    }

    public Auction createAuction(String itemDescription, Integer bidPrice, LocalDateTime startTime, LocalDateTime endTime, ItemCategory itemCategory) {
        if(!this.isSeller){
            throw new AuctionCreationException();
        }
        if(!this.loggedIn) {
            throw new AuctionCreationException();
        }
        return new Auction(this, itemDescription, bidPrice, startTime, endTime, itemCategory);
    }

    public Boolean isLoggedIn() {
       return loggedIn;
    }

    public void bid(Auction auction, Integer amount) {
        auction.bid(amount, this);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setSeller(Boolean seller) {
        isSeller = seller;
    }

    public void setLoggedIn(Boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public void setPreferred(boolean isPreferred) {
        this.isPreferred = isPreferred;
    }

    public boolean isPreferred() {
        return isPreferred;
    }
}
