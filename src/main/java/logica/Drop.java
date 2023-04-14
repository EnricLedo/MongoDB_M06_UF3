/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 *
 * @author mengu
 */
public class Drop {
    public static void eliminarRepositori(MongoDatabase database, String ruta) {

    MongoCollection<Document> collection = database.getCollection("ruta");

    if (collection != null) {
        collection.drop();
        System.out.println("La colección ha sido eliminada correctamente.");
    } else {
        System.out.println("La colección no existe.");
    }
}

}
