package com.sample_project.mitsmap;

import android.util.Log;

import java.util.Arrays;

public class Path_Result {

    static String value="";
     static int[] myPath;


    public String getPath() {
        Log.i("LOG_GETPATH","getPath= "+value);
        return value;
    }

    public static void setPath(String stringvalue) {
        value = value.concat(stringvalue+"");
        Log.i("LOG_SETPATH","Current= "+value);
    }

    public static int[] getIntegerPath() {
        Log.i("LOG_INTPATH","getPath= "+myPath);
        return myPath;
    }

    public void setIntegerPath(int[] path) {
        myPath= new int[path.length];
        this.myPath=path;
        Log.i("LOG_SETINTPATH","intPath= "+ Arrays.toString(myPath));
    }
    public static void refreshPath() {
        value="";
    }

}
