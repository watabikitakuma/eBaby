package application.process;

import application.auction.closing.ClosingBidType;
import application.auction.closing.ClosingAuction;
import application.process.calculation.LuxuryTaxCalculator;
import application.process.calculation.ShippingFeeCalculator;
import application.process.calculation.TransactionFeeCalculator;
import application.process.logging.CarSalesLogger;
import application.process.logging.HighPriceLogger;
import application.process.notification.NoBidderNotifier;
import application.process.notification.SoldNotifier;

public class ClosingProcessFactory {
    private static ClosingProcessFactory instance;
    private ClosingProcessFactory() {}

    public static ClosingProcessFactory getInstance() {
        if(instance == null) {
            instance = new ClosingProcessFactory();
        }
        return instance;
    }

    public Processor create(ClosingAuction auction) {
        ClosingBidType closingBidType = auction.getClosingBidType();
        switch (closingBidType) {
            case NO_BID:
                return noBidProcessor();
            case LOW_BID_AT_OTHERS_AUCTION:
                return normalItemProcessor();
            case HIGH_BID_AT_OTHERS_AUCTION:
                return luxuryProcessor();
            case LOW_BID_AT_CAR_AUCTION:
                return carProcessor();
            case HIGH_BID_AT_CAR_AUCTION:
                return luxuryCarProcessor();
            default:
                throw new IllegalArgumentException();
        }
    }

    private Processor carProcessor() {
        return new TransactionFeeCalculator(new ShippingFeeCalculator(new CarSalesLogger(new SoldNotifier())));
    }

    private Processor luxuryCarProcessor() {
        return new TransactionFeeCalculator(new ShippingFeeCalculator(new LuxuryTaxCalculator(new CarSalesLogger(new HighPriceLogger(new SoldNotifier())))));
    }

    private Processor luxuryProcessor() {
        return new TransactionFeeCalculator(new ShippingFeeCalculator(new LuxuryTaxCalculator(new HighPriceLogger(new SoldNotifier()))));
    }

    private Processor normalItemProcessor(){
        return new TransactionFeeCalculator(new ShippingFeeCalculator(new SoldNotifier()));
    }

    private Processor noBidProcessor() {
        return new NoBidderNotifier();
    }
}
