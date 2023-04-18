/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;

/**
 *
 * @author mengu
 */
public class Drop {

    public static void eliminarRepositori(MongoDatabase database, String ruta) {

        List<String> collectionNames = new ArrayList<>();
        for (String name : database.listCollectionNames()) {
            collectionNames.add(name);
        }
        if (collectionNames.contains(ruta)) {
            MongoCollection<Document> collection = database.getCollection(ruta);

            collection.drop();
            System.out.println("La colecció " + ruta + " s'ha eliminat correctament");
        } else {
            System.out.println("La colecció no existeix.");
        }
    }
}
