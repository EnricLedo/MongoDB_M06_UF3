/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import com.mongodb.client.MongoDatabase;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Enric
 */
public class Create {
    
    public void crearRepositori(String ruta, MongoDatabase database) throws IOException{
        
        //Agafem el nom del repositori 
        String[] partsRuta = ruta.split("\\\\");
        String nom_repo = partsRuta[partsRuta.length-1];
        
        List<String> nomsRepositoris = database.listCollectionNames().into(new ArrayList<>());
        
        //Si el repositorni no existeix, el creem
        if(!nomsRepositoris.contains(nom_repo)){
            database.createCollection(nom_repo);
        }
        //Si existeix, informem a l'usuari
        else{
            System.out.println("El repositori que intentes crear ja existeix.");
        }
        
        /*
        Així és com hauria de quedar el Main
        System.out.println("-----------------------------------------------------");
                                System.out.println("S'ha selecionat Create");
                                System.out.println("-----------------------------------------------------");

                                System.out.println("Introdueix la ruta del repositori a Crear: ");
                                System.out.println("Exemple: C:\\Users\\Enric\\OneDrive\\Desktop\\Nom_Del_Repo");
                                rutaRemota = scanner.next();
                                System.out.println("-----------------------------------------------------");
                                
                                Create creator = new Create();
                                creator.crearRepositori(rutaRemota, database);
                                System.out.println("S'ha creat amb exit");
                                System.out.println("-----------------------------------------------------");
        */
        
        /*
        //Mirem si hi ha un repositori a la ruta especificada
        File f = new File(ruta);
        if(!f.exists()){
            System.out.println("No hi ha cap document en la ruta especificada");
        }
        else{
            //Busquem tots els noms de documents que hi hagi per penjar-los
            Files.walk(Paths.get(ruta)).forEach(r-> {
                if (Files.isRegularFile(r)) {
                    //Per a cada fitxer he l'he de convertir a Document i pujar-lo
                    System.out.println(r);
                }
            });
        }
        
        database.createCollection("mycollection");
        try {
            String ruta = "/ruta/filename.txt";
            String contenido = "Contenido de ejemplo";
            File file = new File(ruta);
            // Si el archivo no existe es creado
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(contenido);
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
    }
}
