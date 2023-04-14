/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import entidad.Fichero;
import java.time.ZoneId;
import org.bson.Document;

/**
 *
 * @author Taufik
 */
public class Mapeig {
    
    //De objecte a document
    /***
     * Passa una classe Fichero a document
     * @param e
     * @return 
     */
    
    public static Document mapToDocument(Fichero fi){
        
        Document ret = new Document("ruta", fi.getRuta())
                .append("contenido", fi.getContenido())
                .append("fechaCreacion", fi.getFechaCreacion())
                .append("fechaModificacion", fi.getFechaModificacion())
                .append("hashMD5", fi.getHashMD5());
        return ret; 
    }
    
    //De document a objecte
    /***
     * Passa un document a classe Fichero
     * @param d
     * @return 
     */
    public static Fichero mapFromDocument(Document d){
        
        Fichero ret = new Fichero();
        
        ret.setRuta(d.getString("ruta"));
        ret.setContenido(new StringBuilder(d.getString("contenido")));
        ret.setFechaCreacion(d.getDate("fechaCreacion").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        ret.setFechaModificacion(d.getDate("fechaModificacion").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        ret.setHashMD5(d.getString("hashMD5"));
        
        return ret;
    }
}
