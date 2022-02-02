/**
 * Bankens huvudlogik, här sker bankens alla uträkningar och transaktioner
 *
 * @author Martin Isaksen, marisk-1
 */

package marisk1.bankLogics;

import marisk1.accounts.CreditAccount;
import marisk1.accounts.SavingsAccount;
import marisk1.customers.BankAssets;
import marisk1.customers.Customer;
import marisk1.utils.SaveAndLoad;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class BankLogic implements Serializable {
    private final BankAssets BANK = new BankAssets();
    private final SaveAndLoad SAVE_AND_LOAD = new SaveAndLoad(this);
    private int lastAccount = 1000;

    /**
     * Metod som loopar igenom alla bankens kunder med deras uppgifter
     *
     * @return En arraylista med presentation av bankens kunder
     */
    public ArrayList<String> getAllCustomers() {
        return BANK.getAllBankCustomers();
    }

    /**
     * Skapar upp en ny kund i bankens register efter kontroll gjord om kunden redan finns
     *
     * @param name    Förnamn i strängformat
     * @param surName efternamn i strängformat
     * @param pNo     personnnr i strängformat
     * @return True om ny kund skapats, false om kund redan fanns
     */
    public boolean createCustomer(String name, String surName, String pNo) {
        //Kontrollera att pNo är unikt genom att göra en reverse kontroll, om unikt skapa ny kund
        if (!isPNo(pNo)) {
            return BANK.createCustomer(new Customer(name, surName, pNo));
        }
        return false;
    }

    /**
     * Metod som kontrollerar om kunden finns och sedan om någon av inkommande värde är blankt
     *
     * @param name    Förnamn i strängformat
     * @param surName efternamn i strängformat
     * @param pNo     personnnr i strängformat
     * @return True om något av kundens namn ändrats false om inget gjorts
     */
    public boolean changeCustomerName(String name, String surName, String pNo) {
        //Kontrollerar att kunden finns och att det minst finns ett värde att ändra
        if (isPNo(pNo) && (!(name.equals("") && surName.equals("")))) {
            var c = BANK.getCustomerFromBank(pNo);
            //Använder mig av 2st IF satser för att den skall kontrollera båda oavsett.
            if (!name.equals("")) {
                c.setName(name);
            }
            if (!surName.equals("")) {
                c.setSurName(surName);
            }
            return true;
        }
        return false;
    }

    /**
     * Kontrollerar om kunden finns och sätter då upp ett nytt sparkonto
     *
     * @param pNo personnr i strängformat
     * @return Kundens nya konto nummer vid skapande -1 om inget nytt skapats
     */
    public int createSavingsAccount(String pNo) {
        if (isPNo(pNo)) {
            //Skapar nytt konto till kunden
            SavingsAccount createdAccount = new SavingsAccount(setLastAccount());
            return BANK.getCustomerFromBank(pNo).addNewAccount(createdAccount);
        }
        return -1;
    }

    /**
     * Kontrollerar om kunden finns och sätter då upp ett nytt kreditkonto
     *
     * @param pNo personnr i strängformat
     * @return Kundens nya konto nummer vid skapande -1 om inget nytt skapats
     */
    public int createCreditAccount(String pNo) {
        if (isPNo(pNo)) {
            //Skapar nytt konto till kunden
            CreditAccount createdAccount = new CreditAccount(setLastAccount());
            return BANK.getCustomerFromBank(pNo).addNewAccount(createdAccount);
        }
        return -1;
    }

    /**
     * Metod som hämtar kundens specifika konto med info om det.
     *
     * @param pNo       personnr i strängformat
     * @param accountId konto nummer i heltal
     * @return Sträng med önskad information, om kund eller konto inte finns returneras null
     */
    public String getAccount(String pNo, int accountId) {
        if (isAccount(pNo, accountId)) {

            //Skickar konto till printout och returnerar sedan kontoinformation som sträng
            return BANK.getCustomerFromBank(pNo).getCustomerAccount(accountId).getAccountForPrintOut();
        }
        return null;
    }

    /**
     * Gör en insättning på konto med kontonummer accountId som tillhör kunden med personnummer pNo.
     *
     * @param pNo       personnr i strängformat
     * @param accountId Kontonr i heltal
     * @param amount    insättningssumma i heltal
     * @return True om insättning gjorts False om ingen insättning gjorts
     */
    public boolean deposit(String pNo, int accountId, int amount) {
        if (isAccount(pNo, accountId) && amount > 0) {
            return BANK.getCustomerFromBank(pNo).getCustomerAccount(accountId).deposit(amount);
        }
        return false;
    }

    /**
     * Gör ett uttag på konto med kontonummer accountId som tillhör kunden med personnummer pNo.
     *
     * @param pNo       personnr i strängformat
     * @param accountId kontonummer i heltal
     * @param amount    uttagssumma i heltal
     * @return True om uttag utförst, false om inget uttag gjorts
     */
    public boolean withdraw(String pNo, int accountId, int amount) {
        //Kontrollerar att kunden med account finns,
        if (isAccount(pNo, accountId)) {
            return BANK.getCustomerFromBank(pNo).getCustomerAccount(accountId).withdrawl(amount);
        }
        return false;
    }

    /**
     * Avslutar ett konto med kontonummer accountId som tillhör kunden med personnummer pNo.
     * När konto avslutas beräknas äben räntan ut.
     *
     * @param pNo       Personnr i strängformat
     * @param accountId Kontonr i heltal
     * @return Sträng med kontoinformation enligt önskemål och NULL om inget konto tagits bort
     */
    public ArrayList<String> closeAccount(String pNo, int accountId) {
        if (isPNo(pNo) && isAccount(pNo, accountId)) {
            ArrayList<String> templist = new ArrayList<>();
            templist.add("Account deleted: " + BANK.getCustomerFromBank(pNo).closeAccount(accountId));
            return templist;
        }
        return null;
    }

    /**
     * Tar bort en kund med personnummer pNo ur banken, alla kundens eventuella konton tas också bort och
     * resultatet returneras.
     *
     * @param pNo personnr i strängformat
     * @return Arraylist med önskade information om kunden togs bort annars retuneras null
     */
    public ArrayList<String> deleteCustomer(String pNo) {
        if (isPNo(pNo)) {
            return BANK.deleteCustomer(pNo);
        }
        return new ArrayList<>();
    }

    /**
     * Metod för att plocka ut transaktionslogg för specifikt konto
     *
     * @param pNo       personnr i sträng format
     * @param accountId kontonummer i integer format
     * @return Stränglista med alla transaktioner. Om transaktioner saknas retuneras null
     */
    public ArrayList<String> getTransactions(String pNo, int accountId) {
        if (isAccount(pNo, accountId)) {
            return BANK.getCustomerFromBank(pNo).getCustomerAccount(accountId).convertTransactionLog();
        }
        return new ArrayList<>();
    }

    /**
     * Kontrollerar om kundens personnr finns med i banken.
     *
     * @param pNo personnr i strängformat som skall kontrolleras
     * @return om kundens finns med eller ej
     */
    private boolean isPNo(String pNo) {
        return BANK.isValidCustomer(pNo);
    }

    /**
     * Kontrollerar om kunden har specifika kontot
     *
     * @param pNo       personnr i strängformat för att ha rätt kund
     * @param accountId inkommande heltal som är kontotnr
     * @return om kunden äger kontot eller ej
     */
    private boolean isAccount(String pNo, int accountId) {
        return BANK.getCustomerFromBank(pNo).isAccountValid(accountId);
    }

    /**
     * En metod som hämtar ut alla kundens konto utan extra information
     *
     * @param pNo Personnr i Strängformat
     * @return Kundens konto i Arraylista
     */
    public ArrayList<String> getCustomerAccounts(String pNo) {
        return BANK.getCustomerFromBank(pNo).getCustomerAccounts();

    }

    /**
     * Hämtar enbart kunduppgifterna i strängformat
     *
     * @param pNo Personnr i Strängformat
     * @return Kundens uppgifter i strängformat
     */
    public String getBasicCustomerInfo(String pNo) {
        //Kontrollerar så att kundens personnr finns i systemet.
        if (isPNo(pNo)) {
            return BANK.getCustomerFromBank(pNo).getBasicCustomerInfo();
        }
        return null;
    }

    /**
     * Metod som först validerar att kundnr och kontonr stämmer överens med bankens register innan
     * kontoutdraget renderas upp
     *
     * @param loadedFile Arraylista med kontohändelser samt personnr och kontonr
     * @return Sant eller falskt för utrendeirng av felhanteringen
     */
    public boolean validateLoadedTransactionFile(ArrayList loadedFile) {
        //Kontrollerar att filen inte är tom.
        if (!loadedFile.isEmpty()) {
            //Plockar ut personnr och kontonr som är lagrade på forsta.ser raden.
            //Sedan kontroll emot banken, om allt ok renderas utdraget uppi modalen
            String[] splittPnoAccount = loadedFile.get(0).toString().split("\\s");
            String validPno = splittPnoAccount[0];
            try{ int validAccountNr = Integer.parseInt(splittPnoAccount[1]);
            return isAccount(validPno, validAccountNr);}
            catch (Exception ignore){
                return false;
            }
        } else return false;
    }

    /**
     * Metoden skickar vidare vald inofrmation för sparning till fil
     *
     * @param file                  filname
     * @param customerPno           Personnr
     * @param customerAccountNumber kontonr
     * @return sant eller falsk om det sparats till filen
     */
    public boolean saveTransactionsToFile(File file, String customerPno, int customerAccountNumber) {

        return SAVE_AND_LOAD.saveTransactions(file.toString(),
                getTransactions(customerPno, customerAccountNumber), customerPno,
                customerAccountNumber);
    }

    /**
     * Metod som skickar vidare för att spara bankregistret
     *
     * @param file sökvägen till filen
     * @return ett komplett Banklogic objekt
     */
    public boolean saveBankRegisterToFile(File file) {
        return SAVE_AND_LOAD.saveBankRegisterToFile(file);
    }

    /**
     * Metod som sätter nytt senaste accountnr
     *
     * @return Nytt unik kontonr
     */
    private int setLastAccount() {
        this.lastAccount = lastAccount + 1;
        return this.lastAccount;
    }
}