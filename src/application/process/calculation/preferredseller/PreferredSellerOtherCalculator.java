package application.process.calculation.preferredseller;

import application.auction.ItemCategory;
import application.process.calculation.ShippingFeeDiscountCalculator;

public class PreferredSellerOtherCalculator implements ShippingFeeDiscountCalculator {
    @Override
    public Integer calculate(Integer amount) {
        if(amount >= 50){
            return 0;
        }
        return ItemCategory.OTHER.getShippingFee();
    }
}
