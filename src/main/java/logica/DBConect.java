/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import java.util.Scanner;

/**
 *
 * @author mengu
 */
public class DBConect {

    public static void conexioMongoDB() {

        Scanner scanner = new Scanner(System.in);
        String nomBD;

        //Conexión a la base de datos
        MongoClient mongoClient = new MongoClient("localhost", 27017);

        // Seleccionar la base de datos
        System.out.println("--------------------------------");
        System.out.println("Introdueix el nom de la BD:");
        System.out.println("--------------------------------");

        nomBD = scanner.nextLine();

        MongoDatabase database = mongoClient.getDatabase("nomBD");
        System.out.println("S'ha conectat amb la BD " + nomBD + " correctament!");

    }
}
