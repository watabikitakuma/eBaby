package application.process.calculation;

public interface ShippingFeeDiscountCalculator {
    default Integer calculate(Integer amount) {
        return 0;
    }
}
