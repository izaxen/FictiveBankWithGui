/**
 * Subklass till Accounts
 *
 * @Author Martin Isaksen, marisk-1
 */

package marisk1.accounts;

import marisk1.bankLogics.Transactions;

import java.io.Serializable;
import java.math.BigDecimal;

public class SavingsAccount extends Account implements Serializable {
    //Boolean för kontroll av fritt uttag.
    private boolean freeWithdrawl = true;

    public SavingsAccount(int newAccountNr) {
        super(newAccountNr);
        //Konstruktorn sätter aktuella räntesatser för skuld- och sparränta från BankParameters
        setDebtInterest();
        setInterestRate();

    }

    @Override
    public boolean withdrawl(int withdrawl) {
        if (withdrawl > 0 && (getBalance()
                .compareTo(BigDecimal.valueOf(withdrawl)) >= 0)) {

            if (isFreeWithdrawl()) {
                //Sätter den nya balansen genom att ta nuvarande balans minus uttaget
                setBalance(getBalance().subtract(BigDecimal.valueOf(withdrawl)));
                setFreeWithdrawl(false);
            } else {
                //Om fritt uttag utförts beräknas sedan summan av uttaget med straffränta för att kontrollera om uttag
                // är möjligt. Lägger till 1 på slutet för att minska kod och slippa plussa räntan på aktuellt uttag
                double amountWithInterest = withdrawl * ((getDebtInterest().doubleValue() / 100) + 1);
                int i = (new BigDecimal("0").compareTo(getBalance().subtract(BigDecimal.valueOf(amountWithInterest))));
                if (i >= 0) {
                    return false;
                }
                //om uttag möjligt görs uttag på kontot
                setBalance(getBalance().subtract(BigDecimal.valueOf(amountWithInterest)));
            }
            getTRANSACTIONLOG().add(new Transactions(getBalance(), -withdrawl));
            return true;
        }
        return false;
    }

    public boolean isFreeWithdrawl() {
        return freeWithdrawl;
    }

    public void setFreeWithdrawl(boolean freeWithdrawl) {
        this.freeWithdrawl = freeWithdrawl;
    }

    @Override
    public void setInterestRate() {
        //Hämtar info från Bankparameters
        interestRate = savingsAccountInterest();
    }

    @Override
    public void setDebtInterest() {
        //Hämtar info från Bankparameters
        debtInterest = savingsAccountDebtInterest();
    }

}

