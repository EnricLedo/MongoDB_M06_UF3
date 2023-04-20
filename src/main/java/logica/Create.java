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
    
    public void crearRepositori(String ruta, MongoDatabase database){
        
        //Agafem el nom del repositori 
        String[] partsRuta = ruta.split("\\\\");
        
        List<String> nomsRepositoris = database.listCollectionNames().into(new ArrayList<>());
        
        //c:\home\\user\getrepo2 = home_user_getrepo2
        String nom_repo = "";
            for (int i = 0; i < partsRuta.length; i++) {
                nom_repo+=partsRuta[i]+"_";
            }
            for (int i = 0; i < nom_repo.length(); i++) {
                if(nom_repo.charAt(i) != '_')
                {
                    nom_repo = nom_repo.substring(1);
                    i--;
                }
                else{
                    nom_repo = nom_repo.substring(1);
                    break;
                }
            }
            if (nom_repo.endsWith("_")) {
                nom_repo = nom_repo.substring(0, nom_repo.length() - 1);
            }
        //Si el repositorni no existeix, el creem
        if(!nomsRepositoris.contains(nom_repo)){
            
            //Per a crearlo hem de asegurar-nos que existeix una carpeta en la direcció "ruta"
            //Si existeix la carpeta el creem
            File file = new File(ruta);
            if (file.exists()) {
                database.createCollection(nom_repo);
            }
            //Si no existeix cap fitxer, no el creem
            else{
                System.out.println("No hi ha cap repositori amb aquest nom, pero no hi ha cap fitxer en la ruta especificada");
            }
        }   
        //Si el repositori existeix, informem a l'usuari
        else{
            System.out.println("El repositori que intentes crear ja existeix.");
        }
    }
}
