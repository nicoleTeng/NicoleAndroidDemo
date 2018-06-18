package com.example.util;

import android.content.Context;

public class DBHelper {

    public static String getDbDir(Context context) {
        //String sqliteDir = context.getDatabasePath("MyDb").getPath();
        String sqliteDir = "/data/data/" + context.getPackageName() + "/databases";
        return sqliteDir;
    }

}
