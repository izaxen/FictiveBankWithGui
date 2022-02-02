/**
 * Klassen håller kundens information samt konto. Samlar alla kundens konto i en hashmap
 * för att alla kontos keyvalue är kontonr således enklare att söka på.
 *
 * @Author Martin Isaksen, marisk-1
 */
package marisk1.customers;

import marisk1.accounts.Account;
import marisk1.accounts.CreditAccount;
import marisk1.accounts.SavingsAccount;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Customer implements Serializable {
    private final String PERSON_NUMBER;
    private final HashMap<Integer, Account> OWNEDACCOUNTS = new HashMap<>();
    private String name;
    private String surName;

    public Customer(String name, String surName, String pNo) {

        this.name = name;
        this.surName = surName;
        this.PERSON_NUMBER = pNo;
    }

    /**
     * Returnerar aktuell kundinfo
     *
     * @return Sträng med önskad info
     */
    public String userInfoToString() {

        return getPERSON_NUMBER() + " " + getName() + " " + getSurName();
    }

    /**
     * Metod som hämtar kundens aktuella konto
     *
     * @param accountnr Kontonummer i int
     * @return Aktuella kontot
     */
    public Account getCustomerAccount(int accountnr) {
        return this.getOWNEDACCOUNTS().get(accountnr);
    }

    /**
     * Metod som kontrollera om kunden äger ett specifikt konto
     *
     * @param accountnr kontonummer i int
     * @return Sant om konto finns, falskt om det inte finns
     */
    public Boolean isAccountValid(int accountnr) {
        return this.getOWNEDACCOUNTS().containsKey(accountnr);
    }

    /**
     * Metod som lägger till nytt sparkonto
     *
     * @param savingsAccount Objekt
     * @return kontonummer i int
     */
    public int addNewAccount(SavingsAccount savingsAccount) {
        this.getOWNEDACCOUNTS().put(savingsAccount.getACCOUNTNUMBER(), savingsAccount);
        return savingsAccount.getACCOUNTNUMBER();
    }

    /**
     * Metod som lägger till nytt kreditkonto
     *
     * @param creditAccount Objekt
     * @return kontonummer i int
     */
    public int addNewAccount(CreditAccount creditAccount) {
        this.getOWNEDACCOUNTS().put(creditAccount.getACCOUNTNUMBER(), creditAccount);
        return creditAccount.getACCOUNTNUMBER();
    }

    /**
     * Metod som sparar ner kontoinformation för utskrift samt raderar konto när utskriften är sparad
     *
     * @param accountNr Kontonummer i int
     * @return Sträng på önskad kontoinformation
     */
    public String closeAccount(int accountNr) {
        /*Den visella kontoinformationen hämtas från metod endAccountToString för att kunna återanvända
        denna i flera metoder efter informationen lagts i strängen close stängs kontot ned*/
        String close = getCustomerAccount(accountNr).endAccountToString();
        this.getOWNEDACCOUNTS().remove(accountNr);
        return close;
    }

    /**
     * Metod som samlar information om kundens konto inför avslut av kund
     *
     * @return Sträng array med önskad information
     */
    public ArrayList<String> getCustomerInfoDeletion() {
        ArrayList<String> tempCustomerList = new ArrayList<>();
        //Adderar kundinfo till strängen

        tempCustomerList.add(userInfoToString());
            /*Loopar igenom kundens alla accounts och sparar ner information från dom i en sträng med
            hjälp av extrametoden endAccountToString*/
        for (int acc : getOWNEDACCOUNTS().keySet()) {
            tempCustomerList.add(getOWNEDACCOUNTS().get(acc).endAccountToString());
        }
        return tempCustomerList;
    }

    public ArrayList<String> getCustomerAccounts() {
        ArrayList<String> tempList = new ArrayList<>();

        for (int account : getOWNEDACCOUNTS().keySet()) {
            tempList.add(getOWNEDACCOUNTS().get(account).getAccountForPrintOut());
        }
        return tempList;
    }

    public String getBasicCustomerInfo() {
        return PERSON_NUMBER + " " + getName() + " " + getSurName();
    }

    /**
     * Getters och setter för instansvariablerna
     */

    //Satt som private så man inte når den utanför Customer.
    private HashMap<Integer, Account> getOWNEDACCOUNTS() {
        return OWNEDACCOUNTS;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getPERSON_NUMBER() {
        return PERSON_NUMBER;
    }
}
