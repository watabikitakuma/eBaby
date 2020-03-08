package application;

import com.tobeagile.training.ebaby.services.AuctionLogger;
import org.junit.Assert;
import org.junit.Test;

public class LoggerTest{
    private static final String FILE_NAME = "LOG_FILE";
    private static final String MESSAGE = "MESSAGE";

    @Test
    public void returnMessage() {
        AuctionLogger logger = AuctionLogger.getInstance();
        logger.log(FILE_NAME, MESSAGE);
        Assert.assertEquals(logger.returnMessage(FILE_NAME, MESSAGE), MESSAGE);
    }
}
