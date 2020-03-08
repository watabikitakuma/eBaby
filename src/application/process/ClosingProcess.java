package application.process;

import application.auction.Auction;
import application.auction.closing.ClosingAuction;

public class ClosingProcess {

    public void execute(Auction auction) {
        ClosingAuction closingAuction = new ClosingAuction(auction);
        ClosingProcessFactory factory = ClosingProcessFactory.getInstance();
        Processor processor = factory.create(closingAuction);
        processor.process(closingAuction);
    }
}
