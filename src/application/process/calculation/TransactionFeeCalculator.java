package application.process.calculation;

import application.auction.closing.AdjustType;
import application.auction.Auction;
import application.auction.closing.ClosingAuction;
import application.auction.closing.SalesAmountAdjustFee;
import application.process.Processor;

public class TransactionFeeCalculator extends AuctionFeeCalculator {

    public TransactionFeeCalculator(Processor process) {
        super(process);
    }

    @Override
    public SalesAmountAdjustFee calculate(ClosingAuction auction) {
        double transactionFee = transactionFee(auction.getAuction());
        return new SalesAmountAdjustFee(AdjustType.TRANSACTION_FEE, (int)transactionFee);
    }

    private Integer transactionFee(Auction auction) {
        double transactionFee;
        if(auction.getSeller().isPreferred()) {
            transactionFee = calculate(auction.getCurrentBidAmount(),0.01);
        } else {
            transactionFee = calculate(auction.getCurrentBidAmount(),0.02);
        }
        return (int)transactionFee;
    }

    private double calculate(Integer currentBidAmount, double rate) {
        return currentBidAmount * rate;
    }
}
