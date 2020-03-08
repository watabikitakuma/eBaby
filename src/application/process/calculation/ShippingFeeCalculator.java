package application.process.calculation;

import application.auction.*;
import application.auction.closing.AdjustType;
import application.auction.closing.ClosingAuction;
import application.auction.closing.SalesAmountAdjustFee;
import application.process.Processor;

public class ShippingFeeCalculator extends AuctionFeeCalculator {

    public ShippingFeeCalculator(Processor processor) {
        super(processor);
    }

    @Override
    public SalesAmountAdjustFee calculate(ClosingAuction closingAuction) {
        Auction auction = closingAuction.getAuction();
        Integer shippingFee;
        if (auction.getSeller().isPreferred()) {
            ShippingFeeDiscountCalculator calculator = ShippingFeeDiscountCalculatorFactory.getCalculator(auction.getItemCategory());
            shippingFee = calculator.calculate(auction.getCurrentBidAmount());
        } else {
            shippingFee = auction.getItemCategory().getShippingFee();
        }
        return new SalesAmountAdjustFee(AdjustType.SHIPPING_FEE, shippingFee);
    }
}
