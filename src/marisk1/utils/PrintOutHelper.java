/**
 * Klass som är till för att sköta olika printouts och returnera rätt värde.
 *
 * @Author Martin Isaksen, marisk-1
 */
package marisk1.utils;

import marisk1.accounts.Account;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PrintOutHelper {

    /**
     * Metod som enbart skriver om klassnamn till svenska
     *
     * @param acc Aktuella kontoobjektet
     * @return Sträng med info
     */
    public static String classPrintout(Account acc) {
        return acc.getClass().getName().contains("SavingsAccount") ? "Sparkonto" : "Kreditkonto";
    }

    /**
     * Metod som enbart returnerar datum i format enligt önskemål
     *
     * @return datum i strängformat
     */
    public static String getSimpleDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }


}
