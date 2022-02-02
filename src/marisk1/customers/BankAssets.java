/**
 * Klass som sköter bankens egna tillgångar.
 * Just nu håller den enbart deras kundregister
 *
 * @Author Martin Isaksen, marisk-1
 */

package marisk1.customers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class BankAssets implements Serializable {

    private final HashMap<String, Customer> BANKREGISTER = new HashMap<>();

    public BankAssets() {
    }

    private HashMap<String, Customer> getBANKREGISTER() {
        return BANKREGISTER;
    }

    public ArrayList<String> getAllBankCustomers() {
        ArrayList<String> tempCustomerList = new ArrayList<>();
        /*Kontrollerar om bankens kundlista är större än 0, är den det så loopar den igenom registret och sparar
        varje kund i tempCustomerlist. Strängvärdet som sparas i arraylistan hämtas från seperat metod då det är
        en återkommande utskrift*/
        if (!getBANKREGISTER().isEmpty()) {
            for (String pNo : getBANKREGISTER().keySet()) {
                // Kunder som finns skickas till printout och sparas sedan i temp arraylista för retur
                tempCustomerList.add(getBANKREGISTER().get(pNo).userInfoToString());
            }
        }
        return tempCustomerList;
    }

    /**
     * Metod som hämtar ut aktuell kund från bankens register
     *
     * @param pNo Personnr i strängformat
     * @return kundobjektet
     */
    public Customer getCustomerFromBank(String pNo) {
        return getBANKREGISTER().get(pNo);
    }

    /**
     * Metod som skapar upp en ny kund
     *
     * @param customer Nytt kund objekt
     * @return sant
     */
    public Boolean createCustomer(Customer customer) {
        getBANKREGISTER().put(customer.getPERSON_NUMBER(), customer);
        return true;
    }

    /**
     * Metod som skapar upp en lista med information om kunden och dess konto
     *
     * @param pNo personnr i strängformat
     * @return Stränglista med aktuell information
     */
    public ArrayList<String> deleteCustomer(String pNo) {
        //Skapar upp templistan med informationen innan kunden raderas ur registret
        ArrayList<String> tempCustomerList = getCustomerFromBank(pNo).getCustomerInfoDeletion();
        //Kunden raderas och dess konto
        getBANKREGISTER().remove(pNo);
        return tempCustomerList;
    }

    /**
     * Metod som kontrollerar om sökt kund finns i systemet
     *
     * @param pNo personnr i Sträängformat
     * @return sant om kunden finns annars falskt
     */
    public Boolean isValidCustomer(String pNo) {
        return getBANKREGISTER().containsKey(pNo);
    }

}
