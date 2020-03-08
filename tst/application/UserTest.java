package application;

import application.user.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import application.auction.Auction;
import application.auction.AuctionCreationException;
import application.auction.ItemCategory;
import application.bid.BidException;
import application.bid.BidLessThanCurrentAmountException;
import application.bid.BidLessThanStartedAmountException;

import java.time.LocalDateTime;

public class UserTest {

    private User seller;

    @Before
    public void setup(){
        String firstName = "ichiro";
        String lastName = "suzuki";
        String userEmail = "ichiro.suzuki@gmail.com";
        String userName = "ICHIRO";
        String password = "SUZUKI";
        seller = new User(firstName, lastName, userEmail, userName, password);
        seller.setSeller(true);
    }

    @Test
    public void As_a_user_I_want_to_register_so_that_I_can_log_in(){
        String firstName = "ichiro";
        String lastName = "suzuki";
        String userEmail = "ichiro.suzuki@gmail.com";
        String userName = "ICHIRO";
        String password = "SUZUKI";
        User user = new User(firstName, lastName, userEmail, userName, password);

        Assert.assertEquals(user.getFirstName(), firstName);
        Assert.assertEquals(user.getLastName(), lastName);
        Assert.assertEquals(user.getUserEmail(), userEmail);
        Assert.assertEquals(user.getUserName(), userName);
        Assert.assertEquals(user.getPassword(), password);
    }



    @Test
    public void As_a_registered_user_I_want_to_log_in_so_that_I_can_be_authenticated() {
        String firstName = "ichiro";
        String lastName = "suzuki";
        String userEmail = "ichiro.suzuki@gmail.com";
        String userName = "ICHIRO";
        String password = "SUZUKI";
        User user = new User(firstName, lastName, userEmail, userName, password);

        Users users = new Users();
        users.register(user);
        users.logIn(userName, password);
        Assert.assertTrue(user.isLoggedIn());
    }

    @Test
    public void As_a_registered_user_I_want_to_log_in_so_that_I_can_be_authenticated_SAME_USER_REGISTER(){
        String firstName = "ichiro";
        String lastName = "suzuki";
        String userEmail = "ichiro.suzuki@gmail.com";
        String userName = "ICHIRO";
        String password = "SUZUKI";
        User user = new User(firstName, lastName, userEmail, userName, password);

        String firstName2 = "ichiro";
        String lastName2 = "suzuki";
        String userEmail2 = "ichiro.suzuki@gmail.com";
        String userName2 = "ICHIRO";
        String password2 = "SUZUKI";
        User user2 = new User(firstName2, lastName2, userEmail2, userName2, password2);

        Users users = new Users();
        users.register(user);
        try {
            users.register(user2);
            Assert.fail("No exception thrown for duplicate user");
        } catch(UserDuplicateException e){}

    }

    @Test
    public void As_a_registered_user_I_want_to_log_in_so_that_I_can_be_authenticated_CANT_FIND_USERNAME() {
        String username = "ichiro";
        String password = "suzuki";

        Users users = new Users();

        try {
            users.logIn(username, password);
            Assert.fail("No exception thrown for cannot find username");
        } catch (LogInErrorException e) {}
    }

    @Test
    public void As_a_registered_user_I_want_to_log_in_so_that_I_can_be_authenticated_INCORRECT_PASSWORD(){
        String firstName = "ichiro";
        String lastName = "suzuki";
        String userEmail = "ichiro.suzuki@gmail.com";
        String userName = "ICHIRO";
        String password = "SUZUKI";
        User user = new User(firstName, lastName, userEmail, userName, password);

        Users users = new Users();
        users.register(user);

        try {
            users.logIn(userName, "INCORRECT");
            Assert.fail("No exception thrown for incorrect password");
        } catch (LogInErrorException e) {}
    }

    @Test
    public void As_an_authenticated_user_I_want_to_log_out_so_that_I_can_be_unauthenticated() {
        String firstName = "ichiro";
        String lastName = "suzuki";
        String userEmail = "ichiro.suzuki@gmail.com";
        String userName = "ICHIRO";
        String password = "SUZUKI";
        User user = new User(firstName, lastName, userEmail, userName, password);

        Users users = new Users();
        users.register(user);

        users.logIn(userName, password);

        users.logOut(user.getUserName());
        Assert.assertFalse(user.isLoggedIn());
    }

    @Test
    public void As_an_authenticated_user_I_want_to_log_out_so_that_I_can_be_unauthenticated_CANT_FIND_USERNAME() {

        String userName = "ICHIRO";

        Users users = new Users();
        try {
            users.logOut(userName);
            Assert.fail("No exception thrown for cannot find username");
        } catch (LogOutErrorException e) {}
    }

    @Test
    public void As_an_authenticated_user_I_want_to_log_out_so_that_I_can_be_unauthenticated_DID_NOT_LOG_IN() {
        String firstName = "ichiro";
        String lastName = "suzuki";
        String userEmail = "ichiro.suzuki@gmail.com";
        String userName = "ICHIRO";
        String password = "SUZUKI";
        User user = new User(firstName, lastName, userEmail, userName, password);

        Users users = new Users();
        users.register(user);
        try {
            users.logOut(userName);
            Assert.fail("No exception thrown for did not log in");
        } catch (LogOutErrorException e) {}
    }

    @Test
    public void As_an_authenticated_seller_I_want_to_create_an_auction_so_I_can_sell_stuff() {
        String firstName = "ichiro";
        String lastName = "suzuki";
        String userEmail = "ichiro.suzuki@gmail.com";
        String userName = "ICHIRO";
        String password = "SUZUKI";
        User seller = new User(firstName, lastName, userEmail, userName, password);
        seller.setSeller(true);

        Users users = new Users();
        users.register(seller);
        users.logIn(userName, password);

        String itemDescription = "";
        Integer bidPrice = 0;
        LocalDateTime startTime = LocalDateTime.now().plusHours(1);
        LocalDateTime endTime = startTime.plusHours(10);

        Auction auction = seller.createAuction(itemDescription, bidPrice, startTime, endTime, ItemCategory.OTHER);

        Assert.assertEquals(auction.getSeller(), seller);
        Assert.assertEquals(auction.getItemDescription(), itemDescription);
        Assert.assertEquals(auction.getBidPrice(), bidPrice);
        Assert.assertEquals(auction.getStartTime(), startTime);
        Assert.assertEquals(auction.getEndTime(), endTime);
    }

    @Test
    public void As_an_authenticated_seller_I_want_to_create_an_auction_so_I_can_sell_stuff_NO_SELLER_CANNOT_CREATE_AUCTION() {
        String firstName = "ichiro";
        String lastName = "suzuki";
        String userEmail = "ichiro.suzuki@gmail.com";
        String userName = "ICHIRO";
        String password = "SUZUKI";
        User noSeller = new User(firstName, lastName, userEmail, userName, password);

        String itemDescription = "";
        Integer bidPrice = 0;
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusHours(1);

        try {
            Auction auction = noSeller.createAuction(itemDescription, bidPrice, startTime, endTime, ItemCategory.OTHER);
            Assert.fail("No exception thrown for no seller create auction");
        } catch (AuctionCreationException e) { }

    }

    @Test
    public void As_an_authenticated_seller_I_want_to_create_an_auction_so_I_can_sell_stuff_SELLER_DIDNT_LOGIN() {
        String firstName = "ichiro";
        String lastName = "suzuki";
        String userEmail = "ichiro.suzuki@gmail.com";
        String userName = "ICHIRO";
        String password = "SUZUKI";
        User nologInUser = new User(firstName, lastName, userEmail, userName, password);
        nologInUser.setSeller(true);

        String itemDescription = "";
        Integer bidPrice = 0;
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusHours(1);

        try {
            Auction auction = nologInUser.createAuction(itemDescription, bidPrice, startTime, endTime, ItemCategory.OTHER);
            Assert.fail("No exception thrown for no seller create auction");
        } catch (AuctionCreationException e) { }
    }

    @Test
    public void As_an_authenticated_seller_I_want_to_create_an_auction_so_I_can_sell_stuff_STARTTIME_GREATER_THAN_NOW() {
        String firstName = "ichiro";
        String lastName = "suzuki";
        String userEmail = "ichiro.suzuki@gmail.com";
        String userName = "ICHIRO";
        String password = "SUZUKI";
        User seller = new User(firstName, lastName, userEmail, userName, password);
        seller.setSeller(true);

        Users users = new Users();
        users.register(seller);
        users.logIn(userName, password);

        String itemDescription = "";
        Integer bidPrice = 0;
        LocalDateTime startTime = LocalDateTime.now().minusHours(1);
        LocalDateTime endTime = startTime.plusHours(10);

        try {
            Auction auction = seller.createAuction(itemDescription, bidPrice, startTime, endTime, ItemCategory.OTHER);
            Assert.fail("No exception thrown for start time greater than now");
        } catch (AuctionCreationException e) { }
    }

    @Test
    public void As_an_authenticated_seller_I_want_to_create_an_auction_so_I_can_sell_stuff_ENDTIME_GREATER_THAN_STARTTIME(){
        String firstName = "ichiro";
        String lastName = "suzuki";
        String userEmail = "ichiro.suzuki@gmail.com";
        String userName = "ICHIRO";
        String password = "SUZUKI";
        User seller = new User(firstName, lastName, userEmail, userName, password);
        seller.setSeller(true);

        Users users = new Users();
        users.register(seller);
        users.logIn(userName, password);

        String itemDescription = "";
        Integer bidPrice = 0;
        LocalDateTime startTime = LocalDateTime.now().plusHours(1);
        LocalDateTime endTime = startTime;

        try {
            Auction auction = seller.createAuction(itemDescription, bidPrice, startTime, endTime, ItemCategory.OTHER);
            Assert.fail("No exception thrown for end time greater than start time");
        } catch (AuctionCreationException e) { }
    }

    @Test
    public void As_an_authenticated_bidder_I_want_to_bid_on_a_started_auction_so_that_I_can_become_the_highest_bidder() {
        String firstName = "ichiro";
        String lastName = "suzuki";
        String userEmail = "ichiro.suzuki@gmail.com";
        String userName = "ICHIRO";
        String password = "SUZUKI";
        User seller = new User(firstName, lastName, userEmail, userName, password);
        seller.setSeller(true);

        Users users = new Users();
        users.register(seller);
        users.logIn(userName, password);

        String itemDescription = "";
        Integer bidPrice = 1000;
        LocalDateTime startTime = LocalDateTime.now().plusHours(1);
        LocalDateTime endTime = startTime.plusHours(10);

        Auction auction = seller.createAuction(itemDescription, bidPrice, startTime, endTime, ItemCategory.OTHER);
        auction.onStart();

        String bidderFirstName = "ichiro";
        String bidderLastName = "suzuki";
        String bidderUserEmail = "ichiro.suzuki@gmail.com";
        String bidderUserName = "bidder";
        String bidderPassword = "bidder";
        User bidder = new User(bidderFirstName, bidderLastName, bidderUserEmail, bidderUserName, bidderPassword);
        users.register(bidder);
        users.logIn(bidderUserName, bidderPassword);

        Integer amount = 1000;
        bidder.bid(auction, amount);
    }

    @Test
    public void As_an_authenticated_bidder_I_want_to_bid_on_a_started_auction_so_that_I_can_become_the_highest_bidder_AUCTION_NOT_STARTED() {
        String userName = "ICHIRO";
        String password = "SUZUKI";

        Users users = new Users();
        users.register(seller);
        users.logIn(userName, password);

        String itemDescription = "";
        Integer bidPrice = 0;
        LocalDateTime startTime = LocalDateTime.now().plusHours(1);
        LocalDateTime endTime = startTime.plusHours(10);

        Auction auction = seller.createAuction(itemDescription, bidPrice, startTime, endTime, ItemCategory.OTHER);

        String bidderFirstName = "ichiro";
        String bidderLastName = "suzuki";
        String bidderUserEmail = "ichiro.suzuki@gmail.com";
        String bidderUserName = "ICHIRO";
        String bidderPassword = "SUZUKI";
        User bidder = new User(bidderFirstName, bidderLastName, bidderUserEmail, bidderUserName, bidderPassword);

        Integer amount = 1000;
        try {
            bidder.bid(auction, amount);
            Assert.fail("No exception thrown for auction not started");
        } catch (BidException e) {}
    }

    @Test
    public void As_an_authenticated_bidder_I_want_to_bid_on_a_started_auction_so_that_I_can_become_the_highest_bidder_BIDDER_NOT_LOG_IN() {
        Users users = new Users();
        users.register(seller);
        users.logIn(seller.getUserName(), seller.getPassword());

        String itemDescription = "";
        Integer bidPrice = 0;
        LocalDateTime startTime = LocalDateTime.now().plusHours(1);
        LocalDateTime endTime = startTime.plusHours(10);

        Auction auction = seller.createAuction(itemDescription, bidPrice, startTime, endTime, ItemCategory.OTHER);
        auction.onStart();

        String bidderFirstName = "ichiro";
        String bidderLastName = "suzuki";
        String bidderUserEmail = "ichiro.suzuki@gmail.com";
        String bidderUserName = "ICHIRO";
        String bidderPassword = "SUZUKI";
        User bidder = new User(bidderFirstName, bidderLastName, bidderUserEmail, bidderUserName, bidderPassword);

        Integer amount = 1000;
        try {
            bidder.bid(auction, amount);
            Assert.fail("No exception thrown for bidder not log in");
        } catch (UserNotLogInException e) {}
    }

    @Test
    public void As_an_authenticated_bidder_I_want_to_bid_on_a_started_auction_so_that_I_can_become_the_highest_bidder_First_bid_is_greater_than_started() {

        Users users = new Users();
        users.register(seller);
        users.logIn(seller.getUserName(), seller.getPassword());

        String itemDescription = "";
        Integer bidPrice = 2000;
        LocalDateTime startTime = LocalDateTime.now().plusHours(1);
        LocalDateTime endTime = startTime.plusHours(10);

        Auction auction = seller.createAuction(itemDescription, bidPrice, startTime, endTime, ItemCategory.OTHER);
        auction.onStart();

        String bidderFirstName = "ichiro";
        String bidderLastName = "suzuki";
        String bidderUserEmail = "ichiro.suzuki@gmail.com";
        String bidderUserName = "BIDDER";
        String bidderPassword = "BIDDER";
        User bidder = new User(bidderFirstName, bidderLastName, bidderUserEmail, bidderUserName, bidderPassword);
        users.register(bidder);
        users.logIn(bidderUserName, bidderPassword);

        Integer amount = 1999;
        try {
            bidder.bid(auction, amount);
            Assert.fail("No exception thrown for amount is less than started amount");
        } catch (BidLessThanStartedAmountException e) {}
    }

    @Test
    public void As_an_authenticated_bidder_I_want_to_bid_on_a_started_auction_so_that_I_can_become_the_highest_bidder_bid_is_greater_than_current() {
        Users users = new Users();
        users.register(seller);
        users.logIn(seller.getUserName(), seller.getPassword());

        String itemDescription = "";
        Integer bidPrice = 2000;
        LocalDateTime startTime = LocalDateTime.now().plusHours(1);
        LocalDateTime endTime = startTime.plusHours(10);

        Auction auction = seller.createAuction(itemDescription, bidPrice, startTime, endTime, ItemCategory.OTHER);
        auction.onStart();

        String bidderFirstName = "ichiro";
        String bidderLastName = "suzuki";
        String bidderUserEmail = "ichiro.suzuki@gmail.com";
        String bidderUserName = "BIDDER";
        String bidderPassword = "BIDDER";
        User bidder = new User(bidderFirstName, bidderLastName, bidderUserEmail, bidderUserName, bidderPassword);
        users.register(bidder);
        users.logIn(bidderUserName, bidderPassword);

        Integer amount = 2100;
        bidder.bid(auction, amount);

        Integer nextAmount = 2100;
        try {
            bidder.bid(auction, nextAmount);
            Assert.fail("No exception thrown for amount is less than current amount");
        } catch (BidLessThanCurrentAmountException e) {}
    }
}
