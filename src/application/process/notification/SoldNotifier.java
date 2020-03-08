package application.process.notification;

import application.auction.Auction;
import application.auction.closing.ClosingAuction;

public class SoldNotifier extends AuctionResultNotifier {
    private static final String SOLD_MESSAGE = "Your %s auction sold to bidder %s for %d.";
    private static final String HIGH_BIDDER_MESSAGE = "Congratulations! You won an auction for a %s from %s for %d.";

    @Override
    public void process(ClosingAuction closingAuction) {
        Auction auction = closingAuction.getAuction();
        String soldMessage = String.format(SOLD_MESSAGE, auction.getItemDescription(), auction.getHighAmountBidder().getUserEmail(), closingAuction.getSalesAmount());
        office.sendEMail(auction.getSeller().getUserEmail(), soldMessage);
        String bidderMessage = String.format(HIGH_BIDDER_MESSAGE, auction.getItemDescription(), auction.getSeller().getUserEmail(), closingAuction.getPurchaseAmount());
        office.sendEMail(auction.getHighAmountBidder().getUserEmail(), bidderMessage);
    }
}
