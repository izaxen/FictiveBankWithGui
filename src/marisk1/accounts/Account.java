/**
 * Basklassen för konton, är satt som abstrakt för att endast dela huvudvariablerna som alla konton har
 *
 * @Author Martin Isaksen, marisk-1
 */

package marisk1.accounts;

import marisk1.bankLogics.Transactions;
import marisk1.utils.PrintOutHelper;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;

public abstract class Account extends BankParameters implements Serializable {
    protected final ArrayList<Transactions> TRANSACTIONLOG = new ArrayList<>();
    private final int ACCOUNTNUMBER;
    protected BigDecimal interestRate;
    protected BigDecimal debtInterest;
    protected BigDecimal balance = new BigDecimal("0");

    Account(int newAccountNr) {
        this.ACCOUNTNUMBER = newAccountNr;
    }

    public boolean deposit(int amount) {
        /*Adderar insättningen till nuvarande balans och konverterar den till Bigdecimal
         då kundens konto är uppsatt med det.*/
        setBalance(getBalance().add(BigDecimal.valueOf(amount)));
        //Skickar info till helper för att spara transaktionsloggen
        getTRANSACTIONLOG().add(new Transactions(getBalance(), amount));
        return true;
    }

    /**
     * Metod som sparar ner kontoinformation enligt önskemål i strängformat. Kontrollerar om det är
     * sparränta eller skuldränta
     *
     * @return Sträng med kontoinformation enligt önskemål
     */
    public String endAccountToString() {
        //Kortar ner variabelnamn till en kort variabel för läsligheten

        //Om kontot är på plus kommer det att räkna ut räntan på kontot. Rundar av till 2 decimaler
        //och returnerar önskad information i strängformat
        if (getBalance().intValue() >= 0) {
            //Räknas räntebeloppet ut.
            var interest = (getBalance().multiply(getInterestRate()).divide(BigDecimal.valueOf(100),
                    2, RoundingMode.CEILING));
            return ACCOUNTNUMBER + " " + formatBDToCurrency(getBalance()) + " " +
                    PrintOutHelper.classPrintout(this) + " " + formatBDToCurrency(interest);
        } else {
            //Om konto har negativt saldo räknas skuldränta ut och skickas sedan tillbaka i önskat strängformat.
            var debt = (getBalance().multiply(getDebtInterest()).divide(BigDecimal.valueOf(100),
                    2, RoundingMode.CEILING));

            return ACCOUNTNUMBER + " " + formatBDToCurrency(getBalance()) + " " +
                    //Använder mig av Abs för att konvertera -10 i utskrift till 10 för att ni önskar utskrift i det
                    //formatet.
                    PrintOutHelper.classPrintout(this) + " " + formatBDToCurrency(debt.abs());
        }
    }

    /**
     * Metod som konvertar ner alla transkationsloggar från objektet till en sträng
     */
    public ArrayList<String> convertTransactionLog() {
        ArrayList<String> templist = new ArrayList<>();
        for (Transactions item : getTRANSACTIONLOG()
        ) {
            String trans = item.getLOG_TIME() + " " + formatBDToCurrency(new BigDecimal(item.getACTUAL_AMOUNT())) + " Saldo: " +
                    formatBDToCurrency(item.getACTUAL_BALANCE());
            templist.add(trans);
        }
        return templist;
        //Lägger till info i kundens kontologg som är en arraylista

    }

    public String getAccountForPrintOut() {
        String printOut;
        if (getBalance().intValue() >= 0) {

            printOut = getACCOUNTNUMBER() + " " + formatBDToCurrency(getBalance()) + " " + PrintOutHelper.classPrintout(this) +
                    " " + getInterestRate() + " %";
        } else {
            printOut = getACCOUNTNUMBER() + " " + formatBDToCurrency(getBalance()) + " " + PrintOutHelper.classPrintout(this) +
                    " " + getDebtInterest() + " %";
        }

        return printOut;
    }


    /**
     * Formatera om datan till lokal valuta
     *
     * @param data Big Decimal värde
     * @return Sträng värde med värdet samt valutabenämning efter
     */
    public String formatBDToCurrency(BigDecimal data) {
        return NumberFormat.getCurrencyInstance().format(data);
    }

    //Abstrakta getter & setters,
    // metoder som skickar vidare till subklasserna.

    public abstract void setInterestRate();

    public abstract void setDebtInterest();

    public abstract boolean withdrawl(int withdrawl);


    /**
     * Get och setters för instansvariablerna
     */

    public int getACCOUNTNUMBER() {
        return ACCOUNTNUMBER;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public BigDecimal getDebtInterest() {
        return debtInterest;
    }

    public ArrayList<Transactions> getTRANSACTIONLOG() {
        return TRANSACTIONLOG;
    }


}
