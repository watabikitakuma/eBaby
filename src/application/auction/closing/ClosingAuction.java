package application.auction.closing;

import application.auction.Auction;

public class ClosingAuction {
    private Auction auction;
    private SalesAmountAdjustFees adjustFees;
    private ClosingBidType closingBidType;

    public ClosingAuction(Auction auction) {
        this(auction, new SalesAmountAdjustFees());
    }

    public ClosingAuction(Auction auction, SalesAmountAdjustFees adjustFees) {
        this.auction = auction;
        this.adjustFees = adjustFees;
        this.closingBidType = ClosingBidType.of(auction.getItemCategory(), auction.getCurrentBidAmount());
    }

    public ClosingBidType getClosingBidType() {
        return closingBidType;
    }

    public Auction getAuction() {
        return auction;
    }

    public SalesAmountAdjustFees getSalesAmountAdjustFees() {
        return adjustFees;
    }

    public Integer getSalesAmount() {
        return auction.getCurrentBidAmount() - adjustFees.getTransactionFee();
    }

    public Integer getPurchaseAmount() {
        return auction.getCurrentBidAmount() + adjustFees.getShippingFee() + adjustFees.getLuxuryTax();
    }
}
