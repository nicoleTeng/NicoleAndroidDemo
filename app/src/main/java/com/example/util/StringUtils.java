package com.example.util;

import android.util.Log;

public class StringUtils {
    private static final String TAG = "StringUtils";

    public static String mergeString(String[] wordA, String[] wordB) {
        String sentence = "";
        for (String w : wordA) {
            sentence += w;
        }
        for (String w : wordB) {
            sentence += w;
        }
        return sentence;
    }
    
    public static String mergeStringWithStringBuilder(String[] wordA, String[] wordB) {
        StringBuilder sentence = new StringBuilder();
        for (String w : wordA) {
            sentence.append(w);
        }
        for (String w : wordB) {
            sentence.append(w);
        }
        return sentence.toString();
    }
    
    public static String getExtension(String filePath) {
        if (filePath == null) {
            return null;
        }
        String extension = null;
        int beginIndex = filePath.lastIndexOf(".");
        extension = filePath.substring(beginIndex + 1, filePath.length());
        Log.v(TAG, "txh getExtension = " + extension);
        return extension;
    }
}
