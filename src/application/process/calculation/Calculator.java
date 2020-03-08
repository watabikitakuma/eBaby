package application.process.calculation;

import application.auction.closing.ClosingAuction;
import application.auction.closing.SalesAmountAdjustFee;

public interface Calculator {
    SalesAmountAdjustFee calculate(ClosingAuction auction);
}
