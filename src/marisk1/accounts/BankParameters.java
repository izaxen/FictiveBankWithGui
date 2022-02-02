/**
 * Bankens huvudklass för parametersättning. Satt som en helt abstrakt klass för den skall finns tillgänglig för
 * accounten. Vill ha bankens parametrar samlade på ett ställe
 *
 * @Author Martin Isaksen, marisk-1
 */

package marisk1.accounts;

import java.io.Serializable;
import java.math.BigDecimal;

abstract class BankParameters implements Serializable {
    //Bankens huvudkontonr, används endast för att hålla reda på vilket nytt konotonr som skall genereras.

    public BankParameters() {
    }
    //Ställer in sparränta på sparkonto
    protected BigDecimal savingsAccountInterest() {
        return new BigDecimal("1.2");
    }

    //Ställer in skuldränta på sparkonto
    protected BigDecimal savingsAccountDebtInterest() {
        return new BigDecimal("2.0");
    }

    //Ställer in sparränta på kreditkonto
    protected BigDecimal creditAccountInterest() {
        return new BigDecimal("0.5");
    }

    //Ställer skuldränta på kreditkonto
    protected BigDecimal creditAccountDebtInterest() {
        return new BigDecimal("7.0");
    }

    protected int creditAccountLimit() {
        return -5000;
    }
}
