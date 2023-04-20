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
    
    public static String getRelativePath(File file, File baseDir) {
        String path = file.getAbsolutePath();
        String basePath = baseDir.getAbsolutePath();
        if (path.startsWith(basePath)) {
            return path.substring(basePath.length() + 1);
        }
        return path;
    }

    public static List<File> listFiles(File dir) {
        List<File> files = new ArrayList<>();
        File[] filesArray = dir.listFiles();
        if (filesArray != null) {
            files.addAll(Arrays.asList(filesArray));
            for (File file : filesArray) {
                if (file.isDirectory()) {
                    files.addAll(listFiles(file));
                }
            }
        }
        return files;
    }
    
    public static ZoneId getZoneId() {
        return ZoneId.systemDefault();
    }
}
