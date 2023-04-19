/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author Enric
 */
public class Create {
    
    public void crearRepositori(String ruta) throws IOException{
        
        //Mirem si hi ha un directori a la ruta especificada
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
    }
}
