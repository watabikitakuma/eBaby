package application.process.notification;

import com.tobeagile.training.ebaby.services.PostOffice;
import application.process.Processor;

abstract class AuctionResultNotifier implements Processor {

    PostOffice office;

    AuctionResultNotifier() {
        office = PostOffice.getInstance();
    }
}
