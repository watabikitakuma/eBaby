package application.process.calculation;

import application.auction.closing.ClosingAuction;
import application.auction.closing.SalesAmountAdjustFee;
import application.auction.closing.SalesAmountAdjustFees;
import application.process.Processor;
import application.process.ProcessorDecorator;

public abstract class AuctionFeeCalculator extends ProcessorDecorator implements Calculator {

    public AuctionFeeCalculator(Processor processor) {
        super(processor);
    }

    @Override
    public void process(ClosingAuction auction) {
        SalesAmountAdjustFee fee = calculate(auction);
        SalesAmountAdjustFees fees = auction.getSalesAmountAdjustFees();
        ClosingAuction closingAuction = new ClosingAuction(auction.getAuction(), fees.add(fee));
        super.process(closingAuction);
    }
}
