package application.process.calculation.preferredseller;

import application.auction.ItemCategory;
import application.process.calculation.ShippingFeeDiscountCalculator;

public class PreferredSellerCarCalculator implements ShippingFeeDiscountCalculator {
    @Override
    public Integer calculate(Integer amount) {
        return ItemCategory.CAR.getShippingFee() / 2;
    }
}
