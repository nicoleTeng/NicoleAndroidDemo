package com.example.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class FileUtils {
    private static final String JPG_HEX = "ff";  
    private static final String PNG_HEX = "89";  
    private static final String JPG_EXT = "jpg";  
    private static final String PNG_EXT = "png";

    public static String getFileExtension(Context context, String filePath) {  
        FileInputStream fis = null;  
        String extension = StringUtils.getExtension(filePath);  
        try {  
            fis = new FileInputStream(new File(filePath)); 
            byte[] bs = new byte[1];  
            fis.read(bs);  
            String type = Integer.toHexString(bs[0]&0xFF);
            Toast.makeText(context, type, Toast.LENGTH_LONG).show();
            if (JPG_HEX.equals(type)) {
                extension = JPG_EXT;
            }
            if (PNG_HEX.equals(type)) {
                extension = PNG_EXT;
            }
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                if (fis != null) {
                    fis.close();  
                }
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        return extension;  
    }

    /*
     * dirPath = Environment.getExternalStorageDirectory().getPath();
     */
    public static void createFile(String dirPath, String name) {
        String path = dirPath + "/Pictures/Screenshots";
        File file = new File(path, name);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
