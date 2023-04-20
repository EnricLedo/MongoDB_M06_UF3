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

    public static MongoDatabase conexioMongoDB() {
        
        //Conexión a la base de datos
        MongoClient mongoClient = new MongoClient("localhost", 27017);

        MongoDatabase database = mongoClient.getDatabase("GETDB");
        System.out.println("S'ha conectat amb la BD correctament!");

        return database;
    }
}
