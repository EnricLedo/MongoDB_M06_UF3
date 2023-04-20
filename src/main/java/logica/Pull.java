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
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;

/**
 * Esta clase se encarga de descargar archivos de un repositorio MongoDB a un
 * directorio local.
 *
 * @author joang
 */
public class Pull {

    private final String dirBase;
    private final MongoCollection<Document> collection;

    /**
     *
     * Constructor de la clase Pull.
     *
     * @param dirBase Directorio base donde se van a descargar los archivos.
     * @throws Exception Si el directorio no existe o si hay problemas de
     * conexión con MongoDB.
     */
    public Pull(String dirBase) throws Exception {
        this.dirBase = dirBase;
        File dir = new File(dirBase);
        if (!dir.exists()) {
            throw new Exception("El directorio no existe");
        }
        MongoClientURI clientURI = new MongoClientURI("mongodb://localhost:27017");
        MongoClient mongoClient = new MongoClient(clientURI);
        MongoDatabase database = mongoClient.getDatabase("GETDB");
        if (!database.listCollectionNames().into(new ArrayList<String>()).contains("ficheros")) {
            database.createCollection("ficheros");
        }
        this.collection = database.getCollection("ficheros");
    }

    /**
     *
     * Descarga un archivo o directorio desde el repositorio a un directorio
     * local.
     *
     * @param filePath Ruta del archivo o directorio a descargar.
     * @param force Indica si se va a sobrescribir el archivo local aunque sea
     * más reciente que el archivo remoto.
     * @throws Exception Si hay algún problema al descargar el archivo o si el
     * archivo local es más antiguo que el archivo remoto.
     */
    public void pull(Path filePath, boolean force) throws Exception {
        File file = filePath.toFile();
        if (!file.exists()) {
            file.createNewFile();
        }
        if (file.isDirectory()) {
            pullDirectorio(file, force);
        } else {
            pullFichero(file, force);
        }
    }

    /**
     *
     * Descarga los archivos de un directorio desde el repositorio a un
     * directorio local.
     *
     * @param dir Directorio a descargar.
     * @param force Indica si se van a sobrescribir los archivos locales aunque
     * sean más recientes que los archivos remotos.
     * @throws Exception Si hay algún problema al descargar los archivos o si
     * algún archivo local es más antiguo que su equivalente remoto.
     */
    private void pullDirectorio(File dir, boolean force) throws Exception {
        List<File> files = Utils.listFiles(dir);
        for (File file : files) {
            if (file.isFile()) {
                pullFichero(file, force);
            }
        }
    }

    /**
     *
     * Descarga un archivo desde el repositorio a un archivo local.
     *
     * @param file Archivo a descargar.
     *
     * @param force Indica si se va a sobrescribir el archivo local aunque sea
     * más reciente que el archivo remoto.
     *
     * @throws Exception Si hay algún problema al descargar el archivo o si el
     * archivo local es más antiguo que su equivalente remoto.
     */
    private void pullFichero(File file, boolean force) throws Exception {
        String rutaAbsoluta = file.getAbsolutePath();
        // Obtener la fecha de modificación del archivo local y remoto
        BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        LocalDateTime localFileModifiedTime = LocalDateTime.ofInstant(attr.lastModifiedTime().toInstant(), ZoneId.systemDefault());
        Document document = collection.find(new Document("ruta", rutaAbsoluta)).first();
        if (document == null) {
            throw new Exception("El archivo no existe en el repositorio");
        } else {
            Fichero fichero = Mapeig.mapFromDocument(document);

            // Comprobar si se ha especificado el modificador "force" o si el archivo local es más reciente
            if (!force && fichero.getFechaModificacion().compareTo(localFileModifiedTime) > 0) {
                throw new Exception("El archivo local es más antiguo que su equivalente remoto");
            }

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
