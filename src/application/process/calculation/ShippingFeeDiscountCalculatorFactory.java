package application.process.calculation;

import application.auction.ItemCategory;
import application.process.calculation.preferredseller.PreferredSellerCarCalculator;
import application.process.calculation.preferredseller.PreferredSellerOtherCalculator;

public class ShippingFeeDiscountCalculatorFactory {

    public static ShippingFeeDiscountCalculator getCalculator(ItemCategory itemCategory) {
        if(itemCategory.equals(ItemCategory.OTHER)) {
            return new PreferredSellerOtherCalculator();
        } else if(itemCategory.equals(ItemCategory.CAR)) {
            return new PreferredSellerCarCalculator();
        }
        return new ShippingFeeDiscountCalculator() {};
    }
}
