package logica;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import entidad.Fichero;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.Document;

public class Clone {

    public static void clonarDirectoriRemot(String nomColeccio, MongoDatabase database, String dataTime) {
        //Obtener la colección
        MongoCollection<Document> col = database.getCollection(nomColeccio);

        //Crear la carpeta de destino
        String destFolder = System.getProperty("user.home") + "/Desktop/" + nomColeccio;
        File folder = new File(destFolder);
        if (!folder.exists()) {
            folder.mkdir();
        }

        //Obtener los documentos de la colección
        FindIterable<Document> iterDoc = col.find();
        MongoCursor<Document> cursor = iterDoc.iterator();
        try {
            while (cursor.hasNext()) {
                Document doc = cursor.next();

                //Mapear el documento a un objeto Fichero
                Fichero fichero = Mapeig.mapFromDocument(doc);

                //Crear el archivo en la carpeta de destino
                Path filePath = Paths.get(destFolder, fichero.getRuta());
                Files.createDirectories(filePath.getParent());
                FileWriter writer = new FileWriter(filePath.toFile());

                //Escribir el contenido del archivo
                String contenido = fichero.getContenido();
                if (contenido != null) {
                    byte[] decodedBytes = Base64.getDecoder().decode(contenido);
                    writer.write(new String(decodedBytes));
                }
                writer.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(Clone.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            cursor.close();
        }
    }
}
