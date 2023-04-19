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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.bson.Document;

/**
 *
 * @author joang
 */
public class Pull {

    private static MongoCollection<Document> coleccioFichero;

    public static void pull(String dir_base, boolean force) throws IOException {
        String dirLocal = System.getProperty("user.dir");//Ruta local
        String remoteDir = dir_base.replace("/", ".");

        // Verificar si la ruta local existe
        Path localPath = Paths.get(dirLocal, dir_base);
        if (!Files.exists(localPath)) {
            System.err.println("Error: El directorio no existe.");
            return;
        }

        // Verificar si el archivo local existe
        Path localFile = Paths.get(dirLocal, dir_base);
        if (!Files.exists(localFile)) {
            Files.createDirectories(localFile.getParent());
            Files.createFile(localFile);
        }

        // Obtener la fecha de modificación del archivo local
        long localLastModified = Files.getLastModifiedTime(localFile).toMillis();

        // Obtener el documento del archivo remoto
        Document doc = coleccioFichero.find(new Document("ruta", remoteDir)).first();
        Fichero fichero = Mapeig.mapFromDocument(doc);
        if (fichero == null) {
            System.err.println("Error: El archivo remoto no existe.");
            return;
        }

        // Obtener la fecha de modificación del archivo remoto
        long remoteLastModified = doc.getDate("dataModificacio").getTime();

        if (!force && localLastModified > remoteLastModified) {
            System.err.println("Error: El archivo local es más reciente que el remoto.");
            return;
        }

        // Descargar el contenido del archivo remoto
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
    }
}
