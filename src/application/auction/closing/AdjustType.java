package application.auction.closing;

public enum AdjustType {
    TRANSACTION_FEE,
    SHIPPING_FEE,
    LUXURY_TAX;

    SalesAmountAdjustFee defaultValue() {
        return new SalesAmountAdjustFee(this, 0);
    }
}
