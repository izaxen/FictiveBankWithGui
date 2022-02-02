/**
 * Klass som sköter sparning och laddning utan filerna.
 * @Author Martin Isaksen, marisk-1
 */
package marisk1.utils;

import marisk1.bankLogics.BankLogic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SaveAndLoad implements Serializable {
    private BankLogic bankLogic;

    public SaveAndLoad(BankLogic bankLogic) {
        this.bankLogic = bankLogic;
    }

    /**
     * Metod som sparar ner transaktionerna till en text fil
     * @param fileName filnamn i sträng format
     * @param transactions Stränglista med transaktionerna
     * @param pno sträng med personnr
     * @param accountNr int med kontonr
     * @return Sant eller falskt om sparning lyckats
     */
    public boolean saveTransactions(String fileName, ArrayList<String> transactions, String pno, int accountNr) {
        try {
            FileWriter writer = new FileWriter(fileName);
            if (!transactions.isEmpty()) {
                //Lägger till personnr och kontonr överst i raden.
                writer.write(pno + " " + accountNr + "\n");
                //Loopar igenom transaktionerna och sparar dom. Använder append för att lägga till och inte skriva över
                for (String trans : transactions
                ) {
                    trans = trans.replace("\u00a0", "");
                    writer.append(trans + "\n");
                }
            }
            writer.append("Transactionlog created: " + new Date() + "\n");
            writer.close();
            return true;
        } catch (Exception ignored) {
        }
        return false;
    }

    /**
     * Metod som laddar upp transaktionerna
     * @param fileName sökväg i textformat
     * @return alla inlästa rader i Stränglista
     */
    public List<String> loadTransactions(File fileName) {
        Path path = Paths.get(fileName.toString());
        try {
            return Files.readAllLines(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Mellanmetod för att spara Bankregistret. Skickar med aktuell banklogic objekt som håller i allt.
     * @param file sökväg i File format
     * @return sant eller falskt om sparning lyckats
     */
    public boolean saveBankRegisterToFile(File file) {
        return Serializer.serialize(file.toString(),bankLogic);
    }

    /**
     * Metod som aktiverar inläsning av sparad fil.
     * @param file sökväg för filen
     * @return Nytt Banklogic objekt om inläsning lyckas annars null;
     */
    public BankLogic loadBankRegisterToFile(File file){
        try{
            bankLogic = (BankLogic) Serializer.deserialize(file.toString());
            return bankLogic;
        }
        catch (Exception ignored){
        }
        return null;
    }
}