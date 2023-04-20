package logica;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import entidad.Fichero;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.bson.Document;

public class Clone {

    public static void clonarDirectoriRemot(String nomColeccio, MongoDatabase database, String dataTime) {

        MongoCollection<Document> coleccion = database.getCollection(nomColeccio);

        // Crear directori en l'escriptori
        String rutaDirectori = System.getProperty("user.home") + "/Desktop/" + nomColeccio;
        new File(rutaDirectori).mkdirs();

        // Iterar sobre els documents de la colecció
        for (Document doc : coleccion.find()) {

            // Mapejar el document a un objete Fitxer
            Fichero fichero = Mapeig.mapFromDocument(doc);

            // si dataTime no és null i té format de data en string "DDMMAAAA"
            // aquesta expressió regular és una cadena de text que representa un patró de coincidència
            // \\d representa un dígit
            // {2} significa que es requereixen exactament 2 dígits consecutius
            // {4} significa que es requereixen exactament 4 dígits consecutius
            if (dataTime != null && dataTime.matches("\\d{2}\\d{2}\\d{4}")) {
                String dataString = dataTime.substring(0, 2) + "/" + dataTime.substring(2, 4) + "/" + dataTime.substring(4);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDateTime dataLimit = LocalDateTime.parse(dataString, formatter);

                if (fichero.getFechaModificacion().isBefore(dataLimit)) {
                    continue;
                }
            }

            // Determinar l'extensió de l'arxiu
            String nomArxiu = fichero.getRuta().substring(fichero.getRuta().lastIndexOf("/") + 1);
            String extensio = "";
            if (nomArxiu != null) {
                extensio = nomArxiu.substring(nomArxiu.lastIndexOf(".") + 1).toLowerCase();
            } else {
                System.out.println("No hi ha ningun arxiu que clonar");
            }

            // Determinar l'extensio de l'arxiu de desti i el tipus de contingut
            String extensioDesti = "";
            String tipusContingut = "";
            switch (extensio) {
                case "java":
                    extensioDesti = "java";
                    tipusContingut = fichero.getContenido().toString();
                    break;
                case "html":
                    extensioDesti = "html";
                    tipusContingut = fichero.getContenido().toString();
                    break;
                case "xml":
                    extensioDesti = "xml";
                    tipusContingut = fichero.getContenido().toString();
                    break;
                case "txt":
                    extensioDesti = "txt";
                    tipusContingut = fichero.getContenido().toString();
                    break;
                default:
                    continue;
            }

            // Escriure el contingut 
            String rutaArxiu = rutaDirectori + "/" + doc.getObjectId("_id").toString() + "." + extensioDesti;
            try {
                BufferedWriter escritor = new BufferedWriter(new FileWriter(rutaArxiu));
                escritor.write(tipusContingut);
                escritor.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
