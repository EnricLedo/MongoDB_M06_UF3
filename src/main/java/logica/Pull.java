/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import Utils.Utils;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import entidad.Fichero;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;

/**
 *
 * @author joang
 */
public class Pull {

    private final String dirBase;
    private final MongoCollection<Document> collection;

    public Pull(String dirBase) {
        this.dirBase = dirBase;
        MongoClientURI clientURI = new MongoClientURI("mongodb://localhost:27017");
        MongoClient mongoClient = new MongoClient(clientURI);
        MongoDatabase database = mongoClient.getDatabase("GETDB");
        if (!database.listCollectionNames().into(new ArrayList<String>()).contains("ficheros")) {
            database.createCollection("ficheros");
        }
        this.collection = database.getCollection("ficheros");
    }
    
    public void pull(Path filePath) throws Exception {
        File file = filePath.toFile();
        if (!file.exists()) {
            file.createNewFile();
        }
        if (file.isDirectory()) {
            pullDirectorio(file);
        } else {
            pullFichero(file);
        }
    }

    private void pullDirectorio(File dir) throws Exception {
        List<File> files = Utils.listFiles(dir);
        for (File file : files) {
            if (file.isFile()) {
                pullFichero(file);
            }
        }
    }
    
    private void pullFichero(File file) throws Exception {
        String rutaAbsoluta = file.getAbsolutePath();
        Document document = collection.find(new Document("ruta", rutaAbsoluta)).first();
        if (document == null) {
            throw new Exception("El archivo no existe en el repositorio");
        } else {
            Fichero fichero = Mapeig.mapFromDocument(document);
            try (PrintWriter out = new PrintWriter(file)) {
                out.println(fichero.getContenido());
                System.out.println("Archivo descargado: " + rutaAbsoluta);
            } catch (FileNotFoundException e) {
                System.err.println("Error al guardar el archivo " + file.getName() + ": " + e.getMessage());
                return; // termina la función sin hacer nada más
            }
        }
    }
}
