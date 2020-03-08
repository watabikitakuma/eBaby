package application.auction;

import application.bid.BidException;
import application.bid.BidLessThanCurrentAmountException;
import application.bid.BidLessThanStartedAmountException;
import application.process.ClosingProcess;
import application.user.User;
import application.user.UserNotLogInException;

import java.time.LocalDateTime;

public class Auction {

    private User seller;
    private String itemDescription;
    private Integer bidPrice;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private Integer currentBidAmount;

    private User highAmountBidder;
    private STATE state;

    private ItemCategory itemCategory;

    public void bid(Integer amount, User user) {

        if(state.equals(STATE.NOT_STARTED)) {
            throw new BidException();
        }

        if(!user.isLoggedIn()) {
            throw new UserNotLogInException();
        }

        if(amount < bidPrice) {
            throw new BidLessThanStartedAmountException();
        }

        if(currentBidAmount != null && amount <= currentBidAmount){
            throw new BidLessThanCurrentAmountException();
        }
        this.highAmountBidder = user;
        this.currentBidAmount = amount;
    }

    private enum STATE{
        NOT_STARTED,
        STARTED,
        CLOSED
    }

    public Auction(User seller, String itemDescription, Integer bidPrice, LocalDateTime startTime, LocalDateTime endTime, ItemCategory itemCategory) {
        if (startTime.isBefore(LocalDateTime.now())) {
            throw new AuctionCreationException();
        }
        if (endTime.isEqual(startTime) || endTime.isBefore(startTime)) {
            throw new AuctionCreationException();
        }
        this.seller = seller;
        this.itemDescription = itemDescription;
        this.bidPrice = bidPrice;
        this.startTime = startTime;
        this.endTime = endTime;
        this.state = STATE.NOT_STARTED;
        this.itemCategory = itemCategory;
    }

    public void onStart() {
        state = STATE.STARTED;
    }

    public void onClose() {
        if(!state.equals(STATE.STARTED)){
            throw new AuctionNotStartedException();
        }
        ClosingProcess process = new ClosingProcess();
        process.execute(this);

        state = STATE.CLOSED;
    }

    public boolean isNotStarted() {
        return state.equals(STATE.NOT_STARTED);
    }

    public boolean isStarted() {
        return state.equals(STATE.STARTED);
    }

    public boolean isClosed() {
        return state.equals(STATE.CLOSED);
    }

    public User getSeller() {
        return seller;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public Integer getBidPrice() {
        return bidPrice;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public User getHighAmountBidder() {
        return highAmountBidder;
    }

    public Integer getCurrentBidAmount() {
        return currentBidAmount;
    }

    public ItemCategory getItemCategory() {
        return itemCategory;
    }
}
