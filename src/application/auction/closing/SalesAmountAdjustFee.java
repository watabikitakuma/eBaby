package application.auction.closing;

public class SalesAmountAdjustFee {
    AdjustType adjustType;
    Integer amount;

    public SalesAmountAdjustFee(AdjustType adjustType, Integer amount) {
        this.adjustType = adjustType;
        this.amount = amount;
    }
}
