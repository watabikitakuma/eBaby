package application.auction.closing;

import application.auction.ItemCategory;

public enum ClosingBidType {
    HIGH_BID_AT_CAR_AUCTION,
    LOW_BID_AT_CAR_AUCTION,
    HIGH_BID_AT_OTHERS_AUCTION,
    LOW_BID_AT_OTHERS_AUCTION,
    NO_BID;

    private static final int HIGH_BID_LINE = 10000;

    public static ClosingBidType of(ItemCategory itemCategory, Integer bidAmount) {
        if (bidAmount == null) {
            return NO_BID;
        }

        if (isHighBid(bidAmount)) {
            return highBidType(itemCategory);
        }
        return lowBidType(itemCategory);
    }

    public static boolean isHighBid(Integer bidAmount) {
        return HIGH_BID_LINE <= bidAmount;
    }

    private static ClosingBidType highBidType(ItemCategory itemCategory) {
        switch (itemCategory) {
            case CAR:
                return HIGH_BID_AT_CAR_AUCTION;
            case DOWNLOAD_SOFTWARE:
            case OTHER:
                return HIGH_BID_AT_OTHERS_AUCTION;
            default:
                throw new IllegalArgumentException();
        }
    }

    private static ClosingBidType lowBidType(ItemCategory itemCategory) {
        switch (itemCategory) {
            case CAR:
                return LOW_BID_AT_CAR_AUCTION;
            case DOWNLOAD_SOFTWARE:
            case OTHER:
                return LOW_BID_AT_OTHERS_AUCTION;
            default:
                throw new IllegalArgumentException();
        }
    }
}
