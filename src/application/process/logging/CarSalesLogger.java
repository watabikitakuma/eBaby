package application.process.logging;

import application.auction.closing.ClosingAuction;
import application.process.Processor;
import application.auction.Auction;

import java.time.format.DateTimeFormatter;

public class CarSalesLogger extends AuctionSalesLogger {
    private final String CAR_SALES_LOG_FILE = "CAR_SALES_LOG.log";
    private final String LOG_FORMAT = "[date: %s, seller: %s, bidder: %s, bidAmount: %d]";

    public CarSalesLogger(Processor processor) {
        super(processor);
    }

    @Override
    public void process(ClosingAuction auction) {
        log(auction);
        super.process(auction);
    }

    protected void log(ClosingAuction closingAuction) {
        Auction auction = closingAuction.getAuction();
        String messageForHighPrice = String.format(LOG_FORMAT,
                auction.getEndTime().format(DateTimeFormatter.ISO_DATE),
                auction.getSeller().getUserName(),
                auction.getHighAmountBidder().getUserName(),
                auction.getCurrentBidAmount());
        logger.log(CAR_SALES_LOG_FILE, messageForHighPrice);
    }
}
