package logica;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import entidad.Fichero;
import logica.Mapeig;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.bson.Document;

public class Clone {

    public static void clonarDirectoriRemot(String nomColeccio, MongoDatabase database) {

        // Obtener la colección
        MongoCollection<Document> col = database.getCollection(nomColeccio);

        // Iterar sobre los documentos de la colección
        try ( MongoCursor<Document> cursor = col.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                Fichero fichero = Mapeig.mapFromDocument(doc);

                // Obtener la ruta del fichero
                String ruta = fichero.getRuta();

                // Crear el directorio de destino
                File dirDest = new File(ruta).getParentFile();
                if (!dirDest.exists()) {
                    dirDest.mkdirs();
                }

                // Escribir el contenido del fichero en el archivo de destino
                try ( FileWriter fw = new FileWriter(ruta);  BufferedWriter bw = new BufferedWriter(fw)) {
                    bw.write(fichero.getContenido());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
