package application.process;

import application.auction.closing.ClosingAuction;

public abstract class ProcessorDecorator implements Processor {
    private Processor processor;

    public ProcessorDecorator(Processor processor) {
        this.processor = processor;
    }

    @Override
    public void process(ClosingAuction auction) {
        if(processor != null) {
            processor.process(auction);
        }
    }
}
