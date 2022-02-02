/**
 * Subklass för Accounts
 *
 * @Author Martin Isaksen, marisk-1
 */
package marisk1.accounts;

import marisk1.bankLogics.Transactions;

import java.io.Serializable;
import java.math.BigDecimal;

public class CreditAccount extends Account implements Serializable {
    //Kreditgräns för kontot
    private final int creditLimit;

    public CreditAccount(int newAccountNr) {
        //Konstruktorn sätter kreditgräns, sparränta och skuldränta med variabler från BankParameters
        super(newAccountNr);
        creditLimit = creditAccountLimit();
        setDebtInterest();
        setInterestRate();
    }

    @Override
    public boolean withdrawl(int withdrawl) {
        if (withdrawl > 0 && (getBalance()
                .subtract(BigDecimal.valueOf(withdrawl)).compareTo(BigDecimal.valueOf(getCreditLimit()))) >= 0) {
            //om uttag möjligt görs uttag på kontot
            setBalance(getBalance().subtract(BigDecimal.valueOf(withdrawl)));
            // skickar uttaget till transaktionsloggen
            getTRANSACTIONLOG().add(new Transactions(getBalance(), -withdrawl));
            return true;
        }
        return false;
    }

    public int getCreditLimit() {
        return creditLimit;
    }

    @Override
    public void setInterestRate() {
        //Hämtar info från Bankparameters
        interestRate = creditAccountInterest();
    }

    @Override
    public void setDebtInterest() {
        //Hämtar info från Bankparameters
        debtInterest = creditAccountDebtInterest();
    }


}
