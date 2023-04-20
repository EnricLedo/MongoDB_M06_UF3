package logica;

import java.util.Scanner;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class Drop {

    public static void eliminarRepositoriRemot(MongoDatabase database, String nomColeccio) {

        // Obtenir col·lecció
        MongoCollection<Document> col = database.getCollection(nomColeccio);

        // Comprovar si la col·lecció té contingut
        try (MongoCursor<Document> cursor = col.find().iterator()) {
            if (cursor.hasNext()) {
                System.out.println("Atenció: la col·lecció té contingut. Si l'elimines, es perdrà tot el contingut.");
                System.out.println("1. Eliminar col·lecció");
                System.out.println("2. Cancel·lar");
                
                Scanner scanner = new Scanner(System.in);
                String opcio = scanner.nextLine();

                if (opcio.equals("1")) {
                    // Eliminar la col·lecció
                    col.drop();
                    System.out.println("La col·lecció ha estat eliminada.");
                } else {
                    System.out.println("L'operació ha estat cancel·lada.");
                }
            } else {
                // Eliminar la col·lecció
                col.drop();
                System.out.println("La col·lecció ha estat eliminada.");
            }
        }
    }
}
