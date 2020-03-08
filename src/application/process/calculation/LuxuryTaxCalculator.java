package application.process.calculation;

import application.auction.closing.AdjustType;
import application.auction.closing.ClosingAuction;
import application.auction.closing.SalesAmountAdjustFee;
import application.process.Processor;

public class LuxuryTaxCalculator extends AuctionFeeCalculator {

    private static final double LUXURY_CAR_TAX_RATE = 0.04;

    public LuxuryTaxCalculator(Processor processor) {
        super(processor);
    }

    @Override
    public SalesAmountAdjustFee calculate(ClosingAuction auction) {
        Integer luxuryTax = (int)(auction.getAuction().getCurrentBidAmount() * LUXURY_CAR_TAX_RATE);
        return new SalesAmountAdjustFee(AdjustType.LUXURY_TAX, luxuryTax);
    }
}
