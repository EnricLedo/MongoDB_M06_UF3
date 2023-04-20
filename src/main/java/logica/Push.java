/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import Utils.Utils;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import entidad.Fichero;
import java.io.BufferedReader;
import org.bson.Document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Taufik
 */
public class Push {

    private final String dirBase;
    private final MongoCollection<Document> collection;

    public Push(String dirBase) {
        this.dirBase = dirBase;
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase database = mongoClient.getDatabase("GETDB");
        if (!database.listCollectionNames().into(new ArrayList<String>()).contains(dirBase)) {
            database.createCollection(dirBase);
        }
        this.collection = database.getCollection(dirBase);
    }

    public void push(Path filePath, boolean force) throws Exception {
        File file = filePath.toFile();
        if (!file.exists()) {
            throw new Exception("El archivo no existe");
        }
        if (file.isDirectory()) {
            pushDirectorio(file, force);
        } else {
            pushFichero(file, force);
        }
    }

    private void pushDirectorio(File dir, boolean force) throws Exception {
        List<File> files = Utils.listFiles(dir);
        for (File file : files) {
            if (file.isFile()) {
                pushFichero(file, force);
            }
        }
    }

    private void pushFichero(File file, boolean force) throws Exception {
        //String ruta = Utils.getRelativePath(file, new File(dirBase));
        String rutaAbsoluta = file.getAbsolutePath();
        Fichero fichero = new Fichero();
        //System.out.println("Ruta absoluta del archivo: " + rutaAbsoluta);
        fichero.setRuta(rutaAbsoluta);
        fichero.setFechaModificacion(Files.getLastModifiedTime(file.toPath()).toInstant().atZone(Utils.getZoneId()).toLocalDateTime());

        StringBuilder sb = new StringBuilder();
        try ( BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                sb.append(linea);
                //sb.append(System.lineSeparator());
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error al abrir el archivo " + file.getName() + ": " + e.getMessage());
            return; // termina la función sin hacer nada más
        }
        fichero.setContenido(sb.toString());

        fichero.setHashMD5(calcularHashMD5(file));
        Document document = collection.find(new Document("_id", rutaAbsoluta)).first();
        if (document == null) {
            Document d = Mapeig.mapToDocument(fichero);
            collection.insertOne(d);
            System.out.println("Archivo añadido al repositorio: " + rutaAbsoluta);
        } else {
            Fichero ficheroRemoto = Mapeig.mapFromDocument(document);
            if(force == false && fichero.getFechaModificacion().isAfter(ficheroRemoto.getFechaModificacion())){
                collection.replaceOne(new Document("_id", rutaAbsoluta), Mapeig.mapToDocument(fichero));
                System.out.println("Archivo actualizado en el repositorio: " + rutaAbsoluta);
            }else if (force == true || fichero.getFechaModificacion().isAfter(ficheroRemoto.getFechaModificacion())) {
                collection.replaceOne(new Document("_id", rutaAbsoluta), Mapeig.mapToDocument(fichero));
                System.out.println("Archivo actualizado en el repositorio: " + rutaAbsoluta);
            } else if (fichero.getFechaModificacion().isEqual(ficheroRemoto.getFechaModificacion())) {
                System.out.println("No se ha modificado, el archivo local tiene la misma fecha de modificación que el remoto: " + rutaAbsoluta);
            } else {
                System.out.println("El archivo local es más antiguo que el remoto: " + rutaAbsoluta);
            }
        }
    }

    private String calcularHashMD5(File file) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(Files.readAllBytes(file.toPath()));
        byte[] digest = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }
}
