package application.auction;

public enum ItemCategory {
    DOWNLOAD_SOFTWARE(0),
    OTHER(10),
    CAR(1000)
    ;

    ItemCategory(Integer shippingFee) {
        this.shippingFee = shippingFee;
    }

    private Integer shippingFee;

    public Integer getShippingFee() {
        return shippingFee;
    }
}
