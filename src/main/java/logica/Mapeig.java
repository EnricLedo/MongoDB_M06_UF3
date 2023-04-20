/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import entidad.Fichero;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import org.bson.Document;

/**
 *
 * @author Taufik
 */
public class Mapeig {

    //De objecte a document
    /**
     * *
     * Passa una classe Fichero a document
     *
     * @param e
     * @return
     */
    public static Document mapToDocument(Fichero fi) {

        Document ret = new Document("_id", fi.getRuta())
                .append("contenido", fi.getContenido())
                .append("fechaModificacion", fi.getFechaModificacion())
                .append("hashMD5", fi.getHashMD5());
        return ret;
    }

    //De document a objecte
    /**
     * *
     * Passa un document a classe Fichero
     *
     * @param d
     * @return
     */
    public static Fichero mapFromDocument(Document d) {

        Fichero ret = new Fichero();

        ret.setRuta(d.getString("_id"));
        //ret.setContenido(new StringBuilder(d.getString("contenido")));
        ret.setContenido(d.getString("contenido"));
        //ret.setFechaModificacion(d.getDate("fechaModificacion").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        Date fechaModificacion = d.getDate("fechaModificacion");
        Instant instant = Instant.ofEpochMilli(fechaModificacion.getTime());
        LocalDateTime fechaModificacionLocal = LocalDateTime.ofInstant(instant, ZoneId.of("UTC"));
        ret.setFechaModificacion(fechaModificacionLocal);

        ret.setHashMD5(d.getString("hashMD5"));

        return ret;
    }
}
