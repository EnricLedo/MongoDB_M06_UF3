package logica;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.bson.Document;

public class Clone {

    public static void clonarDirectoriRemot(String rutaColeccio, MongoDatabase database) {

        //Obtener coleccion
        MongoCollection<Document> coleccio = database.getCollection(rutaColeccio);

        // Crear directorio en el escritorio
        String rutaDirectorio = System.getProperty("user.home") + "/Desktop/" + rutaColeccio;
        new File(rutaDirectorio).mkdirs();

        // Iterar sobre los documentos de la colección
        for (Document documento : coleccio.find()) {
            // Obtener la extensión del archivo original
            String nombreArchivo = documento.getString("nombreArchivo");
            String extension = nombreArchivo.substring(nombreArchivo.lastIndexOf(".") + 1).toLowerCase();

            // Determinar la extensión del archivo de destino y el tipo de contenido
            String extensionDestino = "";
            String tipoContenido = "";
            switch (extension) {
                case "java":
                    extensionDestino = "java";
                    tipoContenido = documento.getString("contenido");
                    break;
                case "html":
                    extensionDestino = "html";
                    tipoContenido = documento.getString("contenidoHtml");
                    break;
                case "xml":
                    extensionDestino = "xml";
                    tipoContenido = documento.getString("contenidoXml");
                    break;
                case "txt":
                    extensionDestino = "txt";
                    tipoContenido = documento.getString("contenidoTxt");
                    break;
                default:
                    // Si la extensión no está soportada, saltar este documento
                    continue;
            }

            // Escribir el contenido en un archivo dentro de la carpeta
            String rutaArchivo = rutaDirectorio + "/" + documento.getObjectId("_id").toString() + "." + extensionDestino;
            try {
                BufferedWriter escritor = new BufferedWriter(new FileWriter(rutaArchivo));
                escritor.write(tipoContenido);
                escritor.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
