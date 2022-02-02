package marisk1.bankLogics;

import marisk1.utils.PrintOutHelper;

import java.io.Serializable;
import java.math.BigDecimal;

public class Transactions implements Serializable {

    private final String LOG_TIME;
    private final BigDecimal ACTUAL_BALANCE;
    private final int ACTUAL_AMOUNT;

    public Transactions(BigDecimal actualBalance, int actualAmount) {
        this.LOG_TIME = PrintOutHelper.getSimpleDate();
        this.ACTUAL_BALANCE = actualBalance;
        this.ACTUAL_AMOUNT = actualAmount;

    }

    public String getLOG_TIME() {
        return LOG_TIME;
    }

    public BigDecimal getACTUAL_BALANCE() {
        return ACTUAL_BALANCE;
    }

    public int getACTUAL_AMOUNT() {
        return ACTUAL_AMOUNT;
    }
}
