package application.auction.closing;

import java.util.HashMap;
import java.util.Map;

public class SalesAmountAdjustFees {
    private Map<AdjustType, SalesAmountAdjustFee> values;

    public SalesAmountAdjustFees() {
        values = new HashMap<>();
        initialize();
    }

    public SalesAmountAdjustFees add(SalesAmountAdjustFee adjustFee) {
        SalesAmountAdjustFees newFees = new SalesAmountAdjustFees();
        for (AdjustType type: AdjustType.values()) {
            SalesAmountAdjustFee value = values.get(type);
            newFees.values.put(type, value);
        }
        newFees.values.put(adjustFee.adjustType, adjustFee);
        return newFees;
    }

    Integer getTransactionFee() {
        return values.get(AdjustType.TRANSACTION_FEE).amount;
    }

    Integer getShippingFee() {
        return values.get(AdjustType.SHIPPING_FEE).amount;
    }

    Integer getLuxuryTax() {
        return values.get(AdjustType.LUXURY_TAX).amount;
    }

    private void initialize() {
        for (AdjustType type: AdjustType.values()) {
            values.put(type, type.defaultValue());
        }
    }
}
