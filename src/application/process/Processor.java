package application.process;

import application.auction.closing.ClosingAuction;

public interface Processor {
    void process(ClosingAuction auction);
}
