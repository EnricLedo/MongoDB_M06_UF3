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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import org.bson.Document;

public class Clone {

    public static void clonarDirectoriRemot(String nomColeccio, MongoDatabase database) {

        // Obtenir la colección
        MongoCollection<Document> col = database.getCollection(nomColeccio);

        // Obtener fecha límite de modificación
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fechaLimite = null;
        boolean fechaValida = false;
        while (!fechaValida) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Introdueix la data límit de modificació (dd/MM/yyyy): ");
            String fechaStr = scanner.next();
            try {
                fechaLimite = LocalDate.parse(fechaStr, formatter);
                fechaValida = true;
            } catch (Exception e) {
                System.out.println("Data introduïda no vàlida. Torna-ho a intentar.");
            }
        }

        // Iterar sobre los documentos de la colección
        try ( MongoCursor<Document> cursor = col.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                Fichero fichero = Mapeig.mapFromDocument(doc);

                // Obtener la fecha de modificación del fichero
                LocalDateTime fechaModificacion = fichero.getFechaModificacion();

                // Comprobar si el fichero fue modificado antes de la fecha límite
                if (fechaModificacion.toLocalDate().isBefore(fechaLimite)) {
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
}
