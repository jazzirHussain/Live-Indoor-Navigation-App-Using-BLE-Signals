package com.sample_project.mitsmap;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME="MITSMAP.db"; //.db extension must use
    public static final int DB_VERSION=1;
    public static final String building_name = "building_name";
    public static final String building_no = "building_no";

    public static final String table_name3 = "building";
    public static final String FLOOR_ID = "floor_id";
    public static final String FLOOR_NAME ="floor_name";
    public static final String FLOOR_PLAN = "floor_plan";
    public static final String FLOOR_LEVEL ="floor_level";
    public static final String TBL_FLOOR="FLOOR";
    public static final String TBL_ROOM="ROOM";
    public static final String TBL_GRID_ROOM="GRID_ROOM";
    public static final String GRID_ROOM_ID = "grid_room_id";
    public static final String GRID_NO = "grid_no";
    public static final String TBL_WAYPOINTS="WAY_POINTS";
    public static final String TBL_WAYPOINTS_NUMBER="WAY_POINTS_NUMBER";
    private static final String CREATE_TABLE_FLOOR ="CREATE TABLE IF NOT EXISTS "+TBL_FLOOR +"("+FLOOR_ID +" INTEGER PRIMARY KEY AUTOINCREMENT ,"+building_no+" INTEGER not null ,"+FLOOR_LEVEL+" INTEGER not null);"; // ,"+FLOOR_PLAN+" blob not null
    private static final String INSERT_VALUE_FLOOR="INSERT INTO " + TBL_FLOOR+ "(building_no,floor_level) VALUES (1,0),(1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(1,8),(1,9),(1,10);" ;

    private static final String CREATE_TABLE_BUILDING = "CREATE TABLE IF NOT EXISTS " + table_name3 + "(" + building_no + " INTEGER PRIMARY KEY AUTOINCREMENT," + building_name + " VARCHAR(20));";
    public static final String insert_building = "INSERT INTO " + table_name3 + "(building_name) VALUES ('M George Block'), ('Ramanjuan Block'); ";
    public static final String ROOM_ID = "room_id";
    public static final String ROOM_NAME ="room_name";
    public static final String ROOM_NO = "room_no";

    private static final String CREATE_TABLE_ROOM ="CREATE TABLE IF NOT EXISTS "+TBL_ROOM +"("+ROOM_ID +" INTEGER PRIMARY KEY AUTOINCREMENT ,"+ROOM_NAME+" text not null,"+ROOM_NO+" INTEGER NOT NULL ,"+FLOOR_ID+" INTEGER not null );";
    private static final String INSERT_VALUE_ROOM="INSERT INTO "+ TBL_ROOM+ "(room_name,room_no,floor_id) VALUES ('Entrance',000,1),('Reception',001,1)," +
            "('Tagore Library',005,1),('Lift1',099,1),('Lift2',199,2),('Office',106,2)," +
            "('Principal Room',107,2);" ;
    private static final String CREATE_TABLE_GRID_ROOM ="CREATE TABLE IF NOT EXISTS "+TBL_GRID_ROOM +"("+GRID_ROOM_ID +" INTEGER PRIMARY KEY AUTOINCREMENT ,"+ROOM_ID+" INTEGER NOT NULL ,"+GRID_NO+" text not null );";
    public static final String REF_ID = "ref_id";
    public static final String X_VALUE ="x_value";
    public static final String Y_VALUE="y_value";
    private static  final String INSERT_VALUES_GRID_ROOM= "INSERT INTO "+ TBL_GRID_ROOM+ " (room_id,grid_no) VALUES (1,'12,25')," +
            "(2,'11,25'),(2,'10,25'),(4,'8,25')," +
            "(4,'8,24'),(4,'8,23')," +
            "(3,'9,21'),(3,'9,22'),(5,'5,24'),(6,'6,17'),(7,'6,16');" ;
    private static final String CREATE_TABLE_WAYPOINTS ="CREATE TABLE IF NOT EXISTS "+TBL_WAYPOINTS +"("+REF_ID +" INTEGER PRIMARY KEY AUTOINCREMENT ,"+ROOM_NO+" INTEGER NOT NULL ,"+X_VALUE+" INTEGER not null ,"+Y_VALUE+" INTEGER not null );";
    private static final String INSERT_VALUE_WAYPOINTS="INSERT INTO " + TBL_WAYPOINTS+ "(room_no,x_value,y_value) VALUES (000,12,25),(001,11,26),(096,11,25),(097,9,25),(005,9,10),(098,8,25),(099,8,23),(095,9,21),(199,5,24),(198,6,24),(197,6,16),(196,6,9),(107,5,9),(106,3,16);" ;
    public static final String WAY_ID = "way_id";
    public static final String WAY_REF_ID = "way_ref_id";
    private static final String CREATE_TABLE_WAYPOINTS_NUM ="CREATE TABLE IF NOT EXISTS "+TBL_WAYPOINTS_NUMBER +"("+WAY_ID +" INTEGER PRIMARY KEY AUTOINCREMENT ,"+ROOM_NO+" INTEGER NOT NULL ,"+WAY_REF_ID+" INTEGER NOT NULL );";
    private static final String INSERT_VALUE_WAYPOINTS_NUM="INSERT INTO " + TBL_WAYPOINTS_NUMBER+ "(room_no,way_ref_id) VALUES (000,0),(096,1),(001,2),(097,3),(095,4),(005,7),(098,5),(099,6),(199,0),(198,1),(197,2),(196,4),(106,3),(107,5);" ;



    private static final String DROP_TABLE_BUILDING = "DROP TABLE IF EXISTS " + table_name3 + ";";
    public DatabaseHelper(@Nullable Context context) {
        super(context,  DB_NAME, null, DB_VERSION);
        Log.i("db_check","entered in database constructor and created "+DB_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_BUILDING);
        sqLiteDatabase.execSQL(CREATE_TABLE_FLOOR);
        sqLiteDatabase.execSQL(CREATE_TABLE_ROOM);
        sqLiteDatabase.execSQL(CREATE_TABLE_GRID_ROOM);
        sqLiteDatabase.execSQL(CREATE_TABLE_WAYPOINTS);
        sqLiteDatabase.execSQL(INSERT_VALUE_WAYPOINTS);
        sqLiteDatabase.execSQL(CREATE_TABLE_WAYPOINTS_NUM);
        sqLiteDatabase.execSQL(insert_building);
        sqLiteDatabase.execSQL(INSERT_VALUE_ROOM);
        sqLiteDatabase.execSQL(INSERT_VALUE_FLOOR);
        sqLiteDatabase.execSQL(INSERT_VALUES_GRID_ROOM);
        sqLiteDatabase.execSQL(INSERT_VALUE_WAYPOINTS_NUM);

        Log.i("db_check","entered in database oncreate");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.i("db_check","entered in database upgrade");
        sqLiteDatabase.execSQL(DROP_TABLE_BUILDING);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TBL_FLOOR);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TBL_ROOM);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TBL_GRID_ROOM);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TBL_WAYPOINTS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TBL_WAYPOINTS_NUMBER);
        onCreate(sqLiteDatabase);
    }
}
