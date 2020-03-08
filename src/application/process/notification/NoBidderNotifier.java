package application.process.notification;

import application.auction.Auction;
import application.auction.closing.ClosingAuction;

public class NoBidderNotifier extends AuctionResultNotifier {
    private static final String NO_BIDDER_MESSAGE = "Sorry, your auction for %s did not have any bidders.";

    @Override
    public void process(ClosingAuction closingAuction) {
        Auction auction = closingAuction.getAuction();
        String message = String.format(NO_BIDDER_MESSAGE, auction.getItemDescription());
        office.sendEMail(auction.getSeller().getUserEmail(), message);
    }
}
