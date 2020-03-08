package application.process.logging;

import application.auction.closing.ClosingAuction;
import application.process.Processor;
import application.process.ProcessorDecorator;
import com.tobeagile.training.ebaby.services.AuctionLogger;

public abstract class AuctionSalesLogger extends ProcessorDecorator {

    protected AuctionLogger logger;

    public AuctionSalesLogger(Processor processor) {
        super(processor);
        logger = AuctionLogger.getInstance();
    }

    protected abstract void log(ClosingAuction auction);
}
