/**
 * Klass som sköter serialsiering och deserialisering utav filerna
 * @Author Martin Isaksen, marisk-1
 */
package marisk1.utils;

import marisk1.bankLogics.BankLogic;

import java.io.*;

public class Serializer implements Serializable{

    /**
     * Metoden som serialiserar och skapar filen
     * @param filePath sökväg i strängformat
     * @param bankLogic Aktuellt banklogic objekt
     * @return sant om allting gått bra annars false;
     */
    static public boolean serialize(String filePath, BankLogic bankLogic) {

        try {
            var file = new FileOutputStream(filePath);
            var out = new ObjectOutputStream(file);
            out.writeObject(bankLogic);
            out.close();
            file.close();
            return true;
        }
        catch(Exception error){
            return false;
        }
    }

    /**
     * Metod som deserialiserar filen, och läser ut Banklogic objektet och returnerar det.
     * @param filePath sökväg i strängformat
     * @return Nytt Banklogic objekt eller null om fel på inladdning
     */
    static public Object deserialize(String filePath){

        try {
            var file = new FileInputStream(filePath);
            var in = new ObjectInputStream(file);
            BankLogic bankLogic = (BankLogic) in.readObject();
            in.close();
            file.close();
            return bankLogic;
        }
        catch(Exception error){
            return null;
        }
    }
}