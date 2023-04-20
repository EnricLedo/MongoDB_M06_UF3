/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import Utils.Utils;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import entidad.Fichero;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import org.bson.Document;
/**
 *
 * @author Taufik
 */
public class Compare {

    private final String directorio;  // Ruta del directorio
    private final MongoCollection<Document> collection; // Colección de documentos de MongoDB

    public Compare(String directorio) {
        this.directorio = directorio;
        MongoDatabase database = DBConect.conexioMongoDB();
        this.collection = database.getCollection(directorio);
    }

    // Método para comparar un archivo o un directorio con la base de datos
    public void compare(String fileName) throws Exception {
        File file = new File(directorio, fileName);
        if (!file.exists()) {
            throw new Exception("El archivo no existe");
        }
        if (file.isFile()) {
            compareFile(file);
        } else if (file.isDirectory()) {
            compareDirectory(file);
        }
    }

    // Método auxiliar para comparar todos los archivos dentro de un directorio
    private void compareDirectory(File dir) throws Exception {
        List<File> files = Utils.listFiles(dir);
        for (File file : files) {
            if (file.isDirectory()) {
                compareDirectory(file);
            }
        }
    }

    // Método auxiliar para comparar un archivo con la base de datos
    private void compareFile(File file) throws Exception {
        String rutaAbsoluta = file.getAbsolutePath();
        Document document = collection.find(new Document("_id", rutaAbsoluta)).first();
        if (document == null) {
            System.out.println("El archivo remoto no existe: " + rutaAbsoluta);
        } else {
            Fichero ficheroRemoto = Mapeig.mapFromDocument(document);
            Fichero ficheroLocal = new Fichero();
            ficheroLocal.setRuta(rutaAbsoluta);
            ficheroLocal.setFechaModificacion(LocalDateTime.ofInstant(Instant.ofEpochMilli(file.lastModified()), ZoneId.systemDefault()));
            ficheroLocal.setContenido(new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8));
            if (ficheroLocal.getFechaModificacion().equals(ficheroRemoto.getFechaModificacion())) {
                if ((ficheroLocal.getHashMD5() != null && ficheroLocal.getHashMD5().equals(ficheroRemoto.getHashMD5()))) {
                    System.out.println("El archivo local y remoto tienen exactamente el mismo timestamp y contenido: " + rutaAbsoluta);
                } else {
                    System.out.println("El archivo local y remoto tienen el mismo timestamp, pero difieren en contenido: " + rutaAbsoluta);
                }
            } else {
                System.out.println("El archivo local y remoto tienen distinto timestamp: " + rutaAbsoluta);
            }
        }
    }
}
