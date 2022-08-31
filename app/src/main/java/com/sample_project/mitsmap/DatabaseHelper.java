package com.sample_project.mitsmap;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME="MITS_INDOOR_MAP.DB"; //.db extension must use
    public static final int DB_VERSION=1;
    public static final String TBL_BULDING="BUILDING";
    public static final String TBL_FLOOR="FLOOR";
    public static final String TBL_ROOM="ROOM";
    public static final String TBL_GRID_ROOM="GRID_ROOM";
    public static final String TBL_WAYPOINTS="WAY_POINTS";
    public static final String TBL_WAYPOINTS_NUMBER="WAY_POINTS_NUMBER";
    public static final String TBL_BT_DEVICE ="BLUETOOTH_DEVICE";
    public static final String TBL_WIFI_DEVICE ="WIFI_DEVICE";

    //Table columns
    public static final String B_ID = "b_id";
    public static final String B_NAME ="bulding_name";
    public static final String B_ADDRESS="address";

    private static final String CREATE_TABLE_BULDING = "CREATE TABLE IF NOT EXISTS " + TBL_BULDING + "(" + B_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + B_NAME + " TEXT NOT NULL, " + B_ADDRESS + " TEXT);";
    private static final String INSERT_VALUE_BULDING="INSERT INTO " + TBL_BULDING+ "(bulding_name,address) VALUES ('Muthoot Institute of Technology & Science',' Arakkunnam, Kochi, Kerala 682313');";
    private static final String INSERT_VALUE_BULDING2="INSERT INTO " + TBL_BULDING+ "(bulding_name,address) VALUES ('TocH','Kochi-Madurai-Tondi Point Rd, Puthenkurish, Varikoli, Kerala 682308');";

    public static final String FLOOR_ID = "floor_id";
    public static final String FLOOR_NAME ="floor_name";
    public static final String FLOOR_PLAN = "floor_plan";
    public static final String FLOOR_LEVEL ="floor_level";

    private static final String CREATE_TABLE_FLOOR ="CREATE TABLE IF NOT EXISTS "+TBL_FLOOR +"("+FLOOR_ID +" INTEGER PRIMARY KEY AUTOINCREMENT ,"+B_ID+" INTEGER not null ,"+FLOOR_LEVEL+" INTEGER not null);"; // ,"+FLOOR_PLAN+" blob not null
    private static final String INSERT_VALUE_FLOOR="INSERT INTO " + TBL_FLOOR+ "(b_id,floor_level) VALUES (1,0),(1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(1,8),(1,9),(1,10);" ;

    public static final String ROOM_ID = "room_id";
   public static final String ROOM_NAME ="room_name";
    public static final String ROOM_NO = "room_no";

    private static final String CREATE_TABLE_ROOM ="CREATE TABLE IF NOT EXISTS "+TBL_ROOM +"("+ROOM_ID +" INTEGER PRIMARY KEY AUTOINCREMENT ,"+ROOM_NAME+" text not null,"+ROOM_NO+" INTEGER NOT NULL ,"+FLOOR_ID+" INTEGER not null );";
    public static final String BT_DEVICE_ID = "bt_device_id";
    public static final String BT_DEVICE_NAME ="bt_device_name";
    public static final String BT_DEVICE_MAC = "bt_device_mac";
    private static final String INSERT_VALUE_ROOM="INSERT INTO "+ TBL_ROOM+ "(room_name,room_no,floor_id) VALUES ('Entrance',000,1),('Lift1/Stair1',099,1)," +
            "('Reception',001,1),('Tagore Library',005,1)," +
           "('Lift2/Stair2',199,2),('Office',106,2),('Principal Room',107,2);" ;
    private static final String CREATE_TABLE_BT_DEVICE ="CREATE TABLE IF NOT EXISTS "+TBL_BT_DEVICE +"("+BT_DEVICE_ID +" INTEGER PRIMARY KEY AUTOINCREMENT ,"+BT_DEVICE_NAME+" text not null ,"+BT_DEVICE_MAC+" text not null ,"+ROOM_ID+" INTEGER not null );";
    public static final String GRID_ROOM_ID = "grid_room_id";
   // public static final String ROOM_NAME ="room_name";
    public static final String GRID_NO = "grid_no";
//
    private static final String CREATE_TABLE_GRID_ROOM ="CREATE TABLE IF NOT EXISTS "+TBL_GRID_ROOM +"("+GRID_ROOM_ID +" INTEGER PRIMARY KEY AUTOINCREMENT ,"+ROOM_ID+" INTEGER NOT NULL ,"+GRID_NO+" text not null );";
    public static final String REF_ID = "ref_id";
    public static final String X_VALUE ="x_value";
    public static final String Y_VALUE="y_value";
private static  final String INSERT_VALUES_GRID_ROOM= "INSERT INTO "+ TBL_GRID_ROOM+ " (room_id,grid_no) VALUES (1,'12,25')," +
            "(2,'10,24'),(2,'10,23'),(3,'11,25'),(3,'10,25')," +
            "(4,'9,21'),(5,'5,24'),(6,'6,17'),(7,'6,16');" ;
private static final String CREATE_TABLE_WAYPOINTS ="CREATE TABLE IF NOT EXISTS "+TBL_WAYPOINTS +"("+REF_ID +" INTEGER PRIMARY KEY AUTOINCREMENT ,"+ROOM_NO+" INTEGER NOT NULL ,"+X_VALUE+" INTEGER not null ,"+Y_VALUE+" INTEGER not null );";
    private static final String INSERT_VALUE_WAYPOINTS="INSERT INTO " + TBL_WAYPOINTS+ "(room_no,x_value,y_value) VALUES (000,12,25),(001,11,25),(005,9,21),(099,10,23),(199,5,24),(106,6,17),(107,6,16);" ;

    public static final String WAY_ID = "way_id";
    public static final String WAY_REF_ID = "way_ref_id";
    private static final String CREATE_TABLE_WAYPOINTS_NUM ="CREATE TABLE IF NOT EXISTS "+TBL_WAYPOINTS_NUMBER +"("+WAY_ID +" INTEGER PRIMARY KEY AUTOINCREMENT ,"+ROOM_NO+" INTEGER NOT NULL ,"+WAY_REF_ID+" INTEGER NOT NULL );";
    private static final String INSERT_VALUE_WAYPOINTS_NUM="INSERT INTO " + TBL_WAYPOINTS_NUMBER+ "(room_no,way_ref_id) VALUES (000,1),(001,2),(005,3),(099,4),(199,5),(106,6),(107,7);" ;




    public DatabaseHelper(@Nullable Context context) {

        super(context, DB_NAME, null, DB_VERSION);
        Log.i("db_con",  "inside databse helper");

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       // db.execSQL("CREATE TABLE "+tbl_name+"("+userName+" TEXT primary key,"+ age+" NUMBER)");
        db.execSQL(CREATE_TABLE_BULDING);
        db.execSQL(CREATE_TABLE_FLOOR);
        db.execSQL(CREATE_TABLE_ROOM);
        db.execSQL(CREATE_TABLE_GRID_ROOM);
        db.execSQL(CREATE_TABLE_WAYPOINTS);
        db.execSQL(CREATE_TABLE_WAYPOINTS_NUM);

        db.execSQL(INSERT_VALUE_BULDING);
        db.execSQL(INSERT_VALUE_WAYPOINTS_NUM);
        db.execSQL(INSERT_VALUE_ROOM);
        db.execSQL(INSERT_VALUE_BULDING2);
        db.execSQL(INSERT_VALUE_FLOOR);
        db.execSQL(INSERT_VALUE_WAYPOINTS);
        db.execSQL(INSERT_VALUES_GRID_ROOM);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TBL_BULDING);
        db.execSQL("DROP TABLE IF EXISTS "+ TBL_FLOOR);
        db.execSQL("DROP TABLE IF EXISTS "+ TBL_ROOM);
        db.execSQL("DROP TABLE IF EXISTS "+ TBL_GRID_ROOM);
        db.execSQL("DROP TABLE IF EXISTS "+ TBL_WAYPOINTS);
        db.execSQL("DROP TABLE IF EXISTS "+ TBL_WAYPOINTS_NUMBER);
    }

}
