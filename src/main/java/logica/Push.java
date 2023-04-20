/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import Utils.Utils;
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

    private final String directorio;// Directorio en el que se realizará el push
    private final MongoCollection<Document> collection;// Colección de MongoDB en la que se almacenarán los archivos

    public Push(String directorio) {
        this.directorio = directorio;
        // Conexión a la base de datos MongoDB  
        MongoDatabase database = DBConect.conexioMongoDB();
         // Si la colección con el nombre del directorio no existe, se crea
        if (!database.listCollectionNames().into(new ArrayList<String>()).contains(directorio)) {
            database.createCollection(directorio);
            System.out.println("");
            System.out.println("Se ha creado la coleccion " + directorio + "en la base de datos GETBD.");
            System.out.println("");
        }
         // Se obtiene la colección a partir del nombre del directorio
        this.collection = database.getCollection(directorio);
    }

    // Método para realizar el push de un archivo o un directorio
    public void push(Path rutaCompleta, boolean force) throws Exception {
        // Se obtiene el archivo o directorio a partir de la ruta
        File file = rutaCompleta.toFile();
        // Si el archivo o directorio no existe, se lanza una excepción
        if (!file.exists()) {
            throw new Exception("El archivo no existe");
        }
        // Si es un directorio, se realiza el push de los archivos que contiene
        if (file.isDirectory()) {
            pushDirectorio(file, force);
        } else {    // Si es un archivo, se realiza el push del mismo
            pushFichero(file, force);
        }
    }

     // Método para realizar el push de los archivos contenidos en un directorio
    private void pushDirectorio(File directorio, boolean force) throws Exception {
        List<File> arxius = Utils.listFiles(directorio);
        for (File arxiu : arxius) {
            if (arxiu.isFile()) {
                pushFichero(arxiu, force);
            }
        }
    }

    // Método para realizar el push de un archivo
    private void pushFichero(File arxiu, boolean force) throws Exception {
        //String ruta = Utils.getRelativePath(file, new File(dirBase));
        String rutaAbsoluta = arxiu.getAbsolutePath();
        Fichero fichero = new Fichero();
        //System.out.println("Ruta absoluta del archivo: " + rutaAbsoluta);
        fichero.setRuta(rutaAbsoluta);
        fichero.setFechaModificacion(Files.getLastModifiedTime(arxiu.toPath()).toInstant().atZone(Utils.getZoneId()).toLocalDateTime());

        StringBuilder sb = new StringBuilder();
        try ( BufferedReader br = new BufferedReader(new FileReader(arxiu))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                sb.append(linea);
                //sb.append(System.lineSeparator());
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error al abrir el archivo " + arxiu.getName() + ": " + e.getMessage());
            return; // termina la función sin hacer nada más
        }
        fichero.setContenido(sb.toString());

        fichero.setHashMD5(calcularHashMD5(arxiu));
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

    //Metodo para calular el HasMD5
    private String calcularHashMD5(File arxiu) throws Exception {
        MessageDigest algoritmo = MessageDigest.getInstance("MD5");
        algoritmo.update(Files.readAllBytes(arxiu.toPath()));
        byte[] resultado = algoritmo.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : resultado) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }
}
