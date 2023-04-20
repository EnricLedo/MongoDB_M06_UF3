/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import java.io.File;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Taufik
 */
public class Utils {
    
    public static List<File> listFiles(File directorio) {
        List<File> arxius = new ArrayList<>();
        File[] ArrayArxius = directorio.listFiles();
        if (ArrayArxius != null) {
            arxius.addAll(Arrays.asList(ArrayArxius));
            for (File arxiu : ArrayArxius) {
                if (arxiu.isDirectory()) {
                    arxius.addAll(listFiles(arxiu));
                }
            }
        }
        return arxius;
    }
    
    public static ZoneId getZoneId() {
        return ZoneId.systemDefault();
    }
}
