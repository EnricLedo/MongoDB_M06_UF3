/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.eq;
import entidad.Fichero;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import logica.Mapeig;
import org.bson.Document;

/**
 *
 * @author joang
 */
public class Pull {

    private static MongoCollection<Document> coleccioFichero;

    public static void pull(String rutaRemota) {
      /*  String dirLocal = System.getProperty("user.dir");//Ruta local

        //si existeix la ruta
        if (rutaRemota != null) {
            Document doc = coleccioFichero.find(eq("ruta", rutaRemota)).first();

            Fichero fichero = Mapeig.mapFromDocument(doc);
            StringBuilder contenidoArchivo = fichero.getContenido();
            // Obtener el contenido del archivo del documento y agregarlo al StringBuilder
            contenidoArchivo.append(fichero.getContenido());

            //tractem tots els valors amb un cursor
            MongoCursor<Document> cursorFichero = coleccioFichero.find().cursor();

            //mentre existeixin elements
            while (cursorFichero.hasNext()) {
                Document docFile = cursorFichero.next();
                Fichero f = Mapeig.mapFromDocument(docFile);

                contenidoArchivo.append(f.getContenido());
            }

            // Escriure el contingut del arxiu en un arxiu local
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(dirLocal))) {
                writer.write(contenidoArchivo.toString());
                System.out.println("El contenido del archivo se ha escrito en el archivo local con éxito.");
            } catch (IOException e) {
                System.err.println("Error al escribir el contenido del archivo en el archivo local: " + e.getMessage());
        }
        }*/
    }
}
