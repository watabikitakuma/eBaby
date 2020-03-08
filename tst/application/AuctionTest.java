package application;

import com.tobeagile.training.ebaby.services.AuctionLogger;
import com.tobeagile.training.ebaby.services.PostOffice;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import application.auction.Auction;
import application.auction.AuctionNotStartedException;
import application.auction.ItemCategory;
import application.user.User;
import application.user.Users;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuctionTest {

    private User seller;
    private User bidder;
    private Auction auction;
    private String sellerEmail = "ichiro.suzuki@gmail.com";
    private String itemDescription = "Text book";
    private Integer highBidAmount = 5000;
    private Integer sellerAmount = 4900;
    private Integer otherItemAmount = 5010;
    private Integer CAR_AMOUNT = 6000;
    private Integer BID_AMOUNT_FOR_LOGGER = 10000;
    private String HIGH_PRICE_LOG_FILE_NAME = "HIGH_PRICE_LOG.log";
    private String CAR_SALES_LOG = "CAR_SALES_LOG.log";

    private final String SOLD_MESSAGE = "Your %s auction sold to bidder %s for %d.";
    private final String BIDDER_MESSAGE = "Congratulations! You won an auction for a %s from %s for %d.";

    private final Integer FIRST_BID_PRICE = 0;
    private final LocalDateTime START_TIME = LocalDateTime.now().plusHours(1);
    private final LocalDateTime END_TIME = START_TIME.plusHours(10);

    @Before
    public void setup() {
        String firstName = "ichiro";
        String lastName = "suzuki";

        String userName = "ICHIRO";
        String password = "SUZUKI";
        seller = new User(firstName, lastName, sellerEmail, userName, password);
        seller.setSeller(true);

        Users users = new Users();
        users.register(seller);
        users.logIn(seller.getUserName(), seller.getPassword());

        String bidderFirstName = "bidder";
        String bidderLastName = "bidder";
        String bidderUserEmail = "bidder@gmail.com";
        String bidderUserName = "BIDDER";
        String bidderPassword = "BIDDER";
        bidder = new User(bidderFirstName, bidderLastName, bidderUserEmail, bidderUserName, bidderPassword);
        users.register(bidder);
        users.logIn(bidder.getUserName(), bidder.getPassword());




        PostOffice postOffice = PostOffice.getInstance();
        postOffice.clear();

        AuctionLogger logger = AuctionLogger.getInstance();
        logger.clearLog(CAR_SALES_LOG);
        logger.clearLog(HIGH_PRICE_LOG_FILE_NAME);
    }

    @Test
    public void as_an_auction_I_want_to_be_started_so_that_I_can_accept_bits(){

        auction = seller.createAuction(itemDescription, FIRST_BID_PRICE, START_TIME, END_TIME, ItemCategory.OTHER);
        Assert.assertTrue(auction.isNotStarted());
        Assert.assertFalse(auction.isStarted());
        auction.onStart();
        Assert.assertTrue(auction.isStarted());
        Assert.assertFalse(auction.isNotStarted());
    }

    @Test
    public void story_7_NO_BID_NOTIFY_THE_SELLER() {

        auction = seller.createAuction(itemDescription, FIRST_BID_PRICE, START_TIME, END_TIME, ItemCategory.OTHER);
        String message = String.format("Sorry, your auction for %s did not have any bidders.", itemDescription);
        auction.onStart();
        auction.onClose();
        PostOffice postOffice = PostOffice.getInstance();
        String log = postOffice.findEmail(sellerEmail, message);
        Assert.assertEquals(String.format("<sendEMail address=\"%s\" >%s</sendEmail>\n", sellerEmail, message), log);
    }

    @Test
    public void story_7_SELL_NOTIFY_THE_SELLER(){

        auction = seller.createAuction(itemDescription, FIRST_BID_PRICE, START_TIME, END_TIME, ItemCategory.OTHER);
        String bidderEmail = "bidder@gmail.com";

        String message = String.format("Your %s auction sold to bidder %s for %d.", itemDescription, bidderEmail, sellerAmount);
        auction.onStart();
        bidder.bid(auction, highBidAmount);
        auction.onClose();
        PostOffice postOffice = PostOffice.getInstance();
        String log = postOffice.findEmail(sellerEmail, message);
        Assert.assertEquals(String.format("<sendEMail address=\"%s\" >%s</sendEmail>\n", sellerEmail, message), log);
    }

    @Test
    public void story_7_SELL_NOTIFY_THE_BIDDER(){
        auction = seller.createAuction(itemDescription, FIRST_BID_PRICE, START_TIME, END_TIME, ItemCategory.OTHER);
        Integer highBidAmount = 5000;
        String message = String.format("Congratulations! You won an auction for a %s from %s for %d.", itemDescription, sellerEmail, otherItemAmount);
        auction.onStart();
        bidder.bid(auction, highBidAmount);
        auction.onClose();
        PostOffice postOffice = PostOffice.getInstance();
        String log = postOffice.findEmail(bidder.getUserEmail(), message);
        Assert.assertEquals(String.format("<sendEMail address=\"%s\" >%s</sendEmail>\n", bidder.getUserEmail(), message), log);
    }

    @Test
    public void story_7_CLOSE_AUCTION() {

        auction = seller.createAuction(itemDescription, FIRST_BID_PRICE, START_TIME, END_TIME, ItemCategory.OTHER);
        auction.onStart();
        Assert.assertFalse(auction.isClosed());
        auction.onClose();
        Assert.assertTrue(auction.isClosed());
    }

    @Test
    public void story_7_CLOSE_AUCTION_WHEN_NOT_STARTED(){

        auction = seller.createAuction(itemDescription, FIRST_BID_PRICE, START_TIME, END_TIME, ItemCategory.OTHER);
        try{
            auction.onClose();
            Assert.fail("No exception thrown for close no started auction");
        } catch (AuctionNotStartedException e){}
    }

    @Test
    public void story8_DOWNLOAD_SOFTWARE(){
        auction = seller.createAuction(itemDescription, FIRST_BID_PRICE, START_TIME, END_TIME, ItemCategory.DOWNLOAD_SOFTWARE);
        String bidderEmail = "bidder@gmail.com";
        String sellerMessage = String.format(SOLD_MESSAGE, itemDescription, bidderEmail, sellerAmount);
        String bidderMessage = String.format(BIDDER_MESSAGE, itemDescription, sellerEmail, highBidAmount);
        auction.onStart();
        bidder.bid(auction, highBidAmount);
        auction.onClose();
        PostOffice postOffice = PostOffice.getInstance();
        String sellerLog = postOffice.findEmail(sellerEmail, sellerMessage);
        Assert.assertEquals(String.format("<sendEMail address=\"%s\" >%s</sendEmail>\n", sellerEmail, sellerMessage), sellerLog);
        String bidderLog = postOffice.findEmail(bidderEmail,"");
        Assert.assertEquals(String.format("<sendEMail address=\"%s\" >%s</sendEmail>\n", bidderEmail, bidderMessage), bidderLog);
    }

    @Test
    public void story8_OTHER_ITEM() {
        auction = seller.createAuction(itemDescription, FIRST_BID_PRICE, START_TIME, END_TIME, ItemCategory.OTHER);
        String bidderEmail = "bidder@gmail.com";
        String sellerMessage = String.format(SOLD_MESSAGE, itemDescription, bidderEmail, sellerAmount);
        String bidderMessage = String.format(BIDDER_MESSAGE, itemDescription, sellerEmail, otherItemAmount);
        auction.onStart();
        bidder.bid(auction, highBidAmount);
        auction.onClose();
        PostOffice postOffice = PostOffice.getInstance();
        String sellerLog = postOffice.findEmail(sellerEmail, sellerMessage);
        Assert.assertEquals(String.format("<sendEMail address=\"%s\" >%s</sendEmail>\n", sellerEmail, sellerMessage), sellerLog);
        String bidderLog = postOffice.findEmail(bidderEmail,"");
        Assert.assertEquals(String.format("<sendEMail address=\"%s\" >%s</sendEmail>\n", bidderEmail, bidderMessage), bidderLog);
    }

    @Test
    public void story8_CAR() {
        auction = seller.createAuction(itemDescription, FIRST_BID_PRICE, START_TIME, END_TIME, ItemCategory.CAR);
        String bidderEmail = "bidder@gmail.com";
        String sellerMessage = String.format(SOLD_MESSAGE, itemDescription, bidderEmail, sellerAmount);
        String bidderMessage = String.format(BIDDER_MESSAGE, itemDescription, sellerEmail, CAR_AMOUNT);
        auction.onStart();
        bidder.bid(auction, highBidAmount);
        auction.onClose();
        PostOffice postOffice = PostOffice.getInstance();
        String sellerLog = postOffice.findEmail(sellerEmail, sellerMessage);
        Assert.assertEquals(String.format("<sendEMail address=\"%s\" >%s</sendEmail>\n", sellerEmail, sellerMessage), sellerLog);
        String bidderLog = postOffice.findEmail(bidderEmail,"");
        Assert.assertEquals(String.format("<sendEMail address=\"%s\" >%s</sendEmail>\n", bidderEmail, bidderMessage), bidderLog);
    }

    @Test
    public void story8_LUXURY_CAR() {
        auction = seller.createAuction(itemDescription, FIRST_BID_PRICE, START_TIME, END_TIME, ItemCategory.CAR);
        String bidderEmail = "bidder@gmail.com";
        Integer luxuryCarAmount = 50000;
        double transactionFee = luxuryCarAmount * 0.02;
        Integer sellerCarAmount = luxuryCarAmount - (int)transactionFee;
        double luxuryTax = luxuryCarAmount * 0.04;
        Integer carShippingFee = 1000;
        Integer soldCarAmount = luxuryCarAmount + (int)luxuryTax + carShippingFee;

        String sellerMessage = String.format(SOLD_MESSAGE, itemDescription, bidderEmail, sellerCarAmount);
        String bidderMessage = String.format(BIDDER_MESSAGE, itemDescription, sellerEmail, soldCarAmount);
        auction.onStart();
        bidder.bid(auction, 50000);
        auction.onClose();
        PostOffice postOffice = PostOffice.getInstance();
        String sellerLog = postOffice.findEmail(sellerEmail, sellerMessage);
        Assert.assertEquals(String.format("<sendEMail address=\"%s\" >%s</sendEmail>\n", sellerEmail, sellerMessage), sellerLog);
        String bidderLog = postOffice.findEmail(bidderEmail,"");
        Assert.assertEquals(String.format("<sendEMail address=\"%s\" >%s</sendEmail>\n", bidderEmail, bidderMessage), bidderLog);
    }

    @Test
    public void story9_LOG_ALL_CAR_SALES() {
        auction = seller.createAuction(itemDescription, FIRST_BID_PRICE, START_TIME, END_TIME, ItemCategory.CAR);
        auction.onStart();
        bidder.bid(auction, highBidAmount);
        auction.onClose();
        AuctionLogger logger = AuctionLogger.getInstance();
        String expectedMessage = String.format("[date: %s, seller: %s, bidder: %s, bidAmount: %d]",
                auction.getEndTime().format(DateTimeFormatter.ISO_DATE), seller.getUserName(), bidder.getUserName(), highBidAmount);
        String actualMessage = logger.returnMessage("CAR_SALES_LOG.log", expectedMessage);
        Assert.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void story9_LOG_ALL_CAR_SALES_HIGH_PRICE() {
        auction = seller.createAuction(itemDescription, FIRST_BID_PRICE, START_TIME, END_TIME, ItemCategory.CAR);
        auction.onStart();
        bidder.bid(auction, BID_AMOUNT_FOR_LOGGER);
        auction.onClose();
        AuctionLogger logger = AuctionLogger.getInstance();
        String expectedMessage = String.format("[date: %s, seller: %s, bidder: %s, bidAmount: %d]",
                auction.getEndTime().format(DateTimeFormatter.ISO_DATE), seller.getUserName(), bidder.getUserName(), BID_AMOUNT_FOR_LOGGER);
        String actualMessage = logger.returnMessage(CAR_SALES_LOG, expectedMessage);
        Assert.assertEquals(expectedMessage, actualMessage);
        String actualMessageForHighPriceLog = logger.returnMessage(HIGH_PRICE_LOG_FILE_NAME, expectedMessage);
        Assert.assertEquals(expectedMessage, actualMessageForHighPriceLog);
    }

    @Test
    public void story9_LOG_GREATER_10K() {
        auction = seller.createAuction(itemDescription, FIRST_BID_PRICE, START_TIME, END_TIME, ItemCategory.CAR);
        auction.onStart();
        bidder.bid(auction, BID_AMOUNT_FOR_LOGGER);
        auction.onClose();
        AuctionLogger logger = AuctionLogger.getInstance();
        String expectedMessage = String.format("[date: %s, seller: %s, bidder: %s, bidAmount: %d]",
                auction.getEndTime().format(DateTimeFormatter.ISO_DATE), seller.getUserName(), bidder.getUserName(), BID_AMOUNT_FOR_LOGGER);
        String actualMessage = logger.returnMessage(HIGH_PRICE_LOG_FILE_NAME, expectedMessage);
        Assert.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void story9_NO_LOGGING() {
        auction = seller.createAuction(itemDescription, FIRST_BID_PRICE, START_TIME, END_TIME, ItemCategory.OTHER);
        auction.onStart();
        bidder.bid(auction, highBidAmount);
        auction.onClose();
        AuctionLogger logger = AuctionLogger.getInstance();
        String message = String.format("[date: %s, seller: %s, bidder: %s, bidAmount: %d]",
                auction.getEndTime().format(DateTimeFormatter.ISO_DATE), seller.getUserName(), bidder.getUserName(), highBidAmount);
        Assert.assertFalse(logger.findMessage(CAR_SALES_LOG, message));
        Assert.assertFalse(logger.findMessage(HIGH_PRICE_LOG_FILE_NAME, message));
    }

    @Test
    public void story10_transactionFeeDiscount() {
        seller.setPreferred(true);
        auction = seller.createAuction(itemDescription, FIRST_BID_PRICE, START_TIME, END_TIME, ItemCategory.OTHER);
        String message = String.format("Congratulations! You won an auction for a %s from %s for %d.", itemDescription, sellerEmail, highBidAmount);
        auction.onStart();
        bidder.bid(auction, highBidAmount);
        auction.onClose();
        String log = getEmail(bidder.getUserEmail(), message);
        Assert.assertEquals(String.format("<sendEMail address=\"%s\" >%s</sendEmail>\n", bidder.getUserEmail(), message), log);
    }

    @Test
    public void story10_halfPriceShippingCar(){
        seller.setPreferred(true);
        auction = seller.createAuction(itemDescription, FIRST_BID_PRICE, START_TIME, END_TIME, ItemCategory.CAR);
        Integer highBidAmount = 5000;
        Integer bidderAmount = 5500;
        String message = String.format("Congratulations! You won an auction for a %s from %s for %d.", itemDescription, sellerEmail, bidderAmount);
        auction.onStart();
        bidder.bid(auction, highBidAmount);
        auction.onClose();
        String log = getEmail(bidder.getUserEmail(), message);
        Assert.assertEquals(String.format("<sendEMail address=\"%s\" >%s</sendEmail>\n", bidder.getUserEmail(), message), log);
    }

    @Test
    public void story10_onlyCharge1PercentTransactionFee() {
        seller.setPreferred(true);
        auction = seller.createAuction(itemDescription, FIRST_BID_PRICE, START_TIME, END_TIME, ItemCategory.CAR);
        Integer highBidAmount = 5000;
        Integer sellerCarAmount = 4950;
        String message = String.format("Congratulations! You won an auction for a %s from %s for %d.", itemDescription, sellerEmail, sellerCarAmount);
        auction.onStart();
        bidder.bid(auction, highBidAmount);
        auction.onClose();
        String sellerMessage = String.format(SOLD_MESSAGE, itemDescription, bidder.getUserEmail(), sellerCarAmount);
        String sellerLog = getEmail(sellerEmail, sellerMessage);
        Assert.assertEquals(String.format("<sendEMail address=\"%s\" >%s</sendEmail>\n", sellerEmail, sellerMessage), sellerLog);
    }

    private String getEmail(String email, String message) {
        PostOffice postOffice = PostOffice.getInstance();
        return postOffice.findEmail(email, message);
    }
}