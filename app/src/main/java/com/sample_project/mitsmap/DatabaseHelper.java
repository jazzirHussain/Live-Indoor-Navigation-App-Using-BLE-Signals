package com.sample_project.mitsmap;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME="ML_INDOOR_MAP.DB"; //.db extension must use
    public static final int DB_VERSION=3;
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
    private static final String INSERT_VALUE_ROOM="INSERT INTO "+ TBL_ROOM+ "(room_name,room_no,floor_id) VALUES ('Power Electronics Lab',608,5),('Lift1/Stair1',650,5)," +
            "('Gents Washroom',651,5),('Dst R&D Lab',609,5),('Jarvis Lab',610,5),('Library (Book Bank Section)',607,5)," +
            "('Classroom (M.Tech CyberSecurity S3/S4)',611,5),('Classroom',612,5)," +
            "('Classroom (M.Tech AI&DS S3/S4)',606,5),('Elective Room',613,5)," +
            "('EC Faculty Room 2',605,5),('Classroom',604,5)," +
            "('Lift2/Stair2',652,5),('RF Lab',603,5),('Signal Processing Lab',601,5)," +
            "('Ladies Toilet',600,5),('MP & MC Lab',507,4),('Lift1/Stair1',550,4),('Gents Washroom',551,4)," +
            "('Digital Electronics Lab',508,4),('Analog Circuits Lab',509,4),('Analog Communication Lab',510,4)," +
            "('Electronics Workshop',511,4),('Project Lab',512,4),('EC Faculty Room 1',504,4),('Classroom',505,4)," +
            "('Shannon Hall',506,4),('Classroom',503,4),('Lift2/Stair2',552,4),('Classroom( MHRD Innovation Cell)',502,4)," +
            "('VLSI / Embedded Lab',501,4),('Ladies Washroom',500,4);" ;
    private static final String CREATE_TABLE_BT_DEVICE ="CREATE TABLE IF NOT EXISTS "+TBL_BT_DEVICE +"("+BT_DEVICE_ID +" INTEGER PRIMARY KEY AUTOINCREMENT ,"+BT_DEVICE_NAME+" text not null ,"+BT_DEVICE_MAC+" text not null ,"+ROOM_ID+" INTEGER not null );";
    public static final String GRID_ROOM_ID = "grid_room_id";
   // public static final String ROOM_NAME ="room_name";
    public static final String GRID_NO = "grid_no";
//
    private static final String CREATE_TABLE_GRID_ROOM ="CREATE TABLE IF NOT EXISTS "+TBL_GRID_ROOM +"("+GRID_ROOM_ID +" INTEGER PRIMARY KEY AUTOINCREMENT ,"+ROOM_ID+" INTEGER NOT NULL ,"+GRID_NO+" text not null );";
    public static final String REF_ID = "ref_id";
    public static final String X_VALUE ="x_value";
    public static final String Y_VALUE="y_value";
private static  final String INSERT_VALUES_GRID_ROOM= "INSERT INTO "+ TBL_GRID_ROOM+ " (room_id,grid_no) VALUES (1,'7,54')," +
            "(1,'7,55'),(1,'8,54'),(1,'8,55'),(2,'9,54')," +
            "(2,'9,55'),(2,'10,54'),(2,'10,55'),(3,'11,54'),(3,'11,55'),(3,'12,54'),(3,'12,55'),(4,'11,48')," +
            "(4,'11,49'),(4,'11,50'),(4,'11,51'),(4,'11,52'),(4,'11,53'),(4,'12,48'),(4,'12,49'),(4,'12,50')," +
            "(4,'12,51'),(4,'12,52'),(4,'12,53'),(5,'11,44'),(5,'11,45'),(5,'11,46'),(5,'11,47'),(5,'12,44')," +
            "(5,'12,45'),(5,'12,46'),(5,'12,47'),(6,'8,44'),(6,'8,45'),(6,'8,46'),(6,'8,47'),(6,'9,44'),(6,'9,45')," +
            "(6,'9,46'),(6,'9,47'),(6,'10,44'),(6,'10,45'),(6,'10,46'),(6,'10,47'),(7,'11,37'),(7,'11,38'),(7,'11,39')," +
            "(7,'11,40'),(7,'11,41'),(7,'11,42'),(7,'11,43'),(7,'12,37'),(7,'12,38'),(7,'12,39'),(7,'12,40'),(7,'12,41')," +
            "(7,'12,42'),(7,'12,43'),(8,'11,31'),(8,'11,32'),(8,'11,33'),(8,'11,34'),(8,'11,35'),(8,'11,36'),(8,'12,31');" ;
private  static  final String INSERT_VALUES_GRID_ROOM1= "INSERT INTO "+ TBL_GRID_ROOM+ " (room_id,grid_no) VALUES (8,'12,32'),(8,'12,33'),(8,'12,34'),(8,'12,35'),(8,'12,36'),(9,'8,34'),(9,'8,35'),(9,'8,36'),(9,'8,37'),(9,'9,34')," +
            "(9,'9,35'),(9,'9,36'),(9,'9,37'),(9,'10,34'),(9,'10,35'),(9,'10,36'),(9,'10,37'),(10,'11,28'),(10,'11,29'),(10,'11,30')," +
            "(10,'12,28'),(10,'12,29'),(10,'12,30'),(11,'8,26'),(11,'8,27'),(11,'9,26'),(11,'9,27'),(11,'10,26'),(11,'10,27'),(11,'11,26')," +
            "(11,'11,27'),(11,'12,26'),(11,'12,27'),(12,'9,23'),(12,'9,24'),(12,'9,25'),(12,'10,23'),(12,'10,24'),(12,'10,25'),(12,'11,23')," +
            "(12,'11,24'),(12,'11,25'),(12,'12,23'),(12,'12,24'),(12,'12,25'),(12,'13,25'),(12,'14,25'),(12,'15,25'),(13,'9,18'),(13,'9,19')," +
            "(13,'9,20'),(13,'9,21'),(13,'9,22'),(13,'10,18'),(13,'10,19'),(13,'10,20'),(13,'10,21'),(13,'10,22'),(13,'11,18'),(13,'11,19')," +
            "(13,'11,20'),(13,'11,21'),(13,'11,22'),(13,'12,18'),(13,'12,19'),(13,'12,20'),(13,'12,21'),(13,'12,22'),(14,'14,8'),(14,'14,9')," +
            "(14,'14,10'),(14,'14,11'),(14,'14,12'),(14,'14,13'),(14,'14,14'),(14,'14,15'),(14,'14,16'),(14,'14,17'),(14,'14,18'),(14,'14,19')," +
            "(14,'15,8'),(14,'15,9'),(14,'15,10'),(14,'15,11'),(14,'15,12'),(14,'15,13'),(14,'15,14'),(14,'15,15'),(14,'15,16'),(14,'15,17'),(14,'15,18')," +
            "(14,'15,19'),(15,'14,3'),(15,'14,4'),(15,'14,5'),(15,'14,6'),(15,'14,7'),(15,'15,3'),(15,'15,4'),(15,'15,5'),(15,'15,6'),(15,'15,7'),(16,'14,0')," +
            "(16,'14,1'),(16,'14,2'),(16,'15,0'),(16,'15,1'),(16,'15,2'),(17,'7,53'),(17,'7,54'),(17,'8,53'),(17,'8,54'),(17,'9,53'),(17,'9,54'),(18,'10,52')," +
            "(18,'10,53'),(18,'10,54'),(18,'11,52'),(18,'11,53'),(18,'11,54'),(19,'12,52'),(19,'12,53'),(20,'13,52'),(20,'13,53'),(20,'12,47'),(20,'12,48')," +
            "(20,'12,49'),(20,'12,50'),(20,'12,51'),(20,'12,52'),(20,'13,47'),(20,'13,48'),(20,'13,49'),(20,'13,50'),(20,'13,51'),(20,'13,52'),(21,'12,41')," +
            "(21,'12,42'),(21,'12,43'),(21,'12,44'),(21,'12,45'),(21,'12,46'),(21,'13,41'),(21,'13,42'),(21,'13,43'),(21,'13,44'),(21,'13,45'),(21,'13,46')," +
            "(22,'12,36'),(22,'12,37'),(22,'12,38'),(22,'12,39'),(22,'12,40'),(22,'13,36'),(22,'13,37'),(22,'13,38'),(22,'13,39'),(22,'13,40')," +
            "(23,'12,32'),(23,'12,33'),(23,'12,34'),(23,'12,35'),(23,'13,32'),(23,'13,33'),(23,'13,34'),(23,'13,35'),(24,'12,28'),(24,'12,29')," +
            "(24,'12,30'),(24,'12,31'),(24,'13,28'),(24,'13,29'),(24,'13,30'),(24,'13,31'),(25,'9,26'),(25,'9,27'),(25,'10,26'),(25,'10,27')," +
            "(25,'11,26'),(25,'11,27'),(25,'12,26'),(25,'12,27'),(25,'13,26'),(25,'13,27'),(26,'9,33'),(26,'9,34'),(26,'9,35'),(26,'9,36')," +
            "(26,'10,33'),(26,'10,34'),(26,'10,35'),(26,'10,36'),(26,'11,33'),(26,'11,34'),(26,'11,35'),(26,'11,36'),(27,'9,42'),(27,'9,43')," +
            "(27,'9,44'),(27,'9,45'),(27,'10,42'),(27,'10,43'),(27,'10,44'),(27,'10,45'),(27,'11,42'),(27,'11,43'),(27,'11,44'),(27,'11,45')," +
            "(28,'9,23'),(28,'9,24'),(28,'9,25'),(28,'10,23'),(28,'10,24'),(28,'10,25'),(28,'11,23'),(28,'11,24'),(28,'11,25'),(28,'12,23')," +
            "(28,'12,24'),(28,'12,25'),(28,'13,23'),(28,'13,24'),(28,'13,25'),(28,'14,25'),(28,'15,25'),(29,'9,18'),(29,'9,19'),(29,'9,20')," +
            "(29,'10,18'),(29,'10,19'),(29,'10,20'),(29,'10,22'),(29,'11,18'),(29,'11,19'),(29,'11,20'),(29,'11,21'),(29,'11,22'),(29,'12,20')," +
            "(29,'12,21'),(29,'12,22'),(29,'13,20'),(29,'13,21'),(29,'13,22'),(30,'14,15'),(30,'14,16'),(30,'14,17'),(30,'14,18'),(30,'14,19')," +
            "(30,'14,20'),(30,'14,21'),(30,'15,15'),(30,'15,16'),(30,'15,17'),(30,'15,18'),(30,'15,19'),(30,'15,20'),(30,'15,21'),(31,'13,8')," +
            "(31,'13,9'),(31,'13,10'),(31,'13,11'),(31,'14,4'),(31,'14,5'),(31,'14,6'),(31,'14,7'),(31,'14,8'),(31,'14,9'),(31,'14,10'),(31,'14,11')," +
            "(31,'14,12'),(31,'14,13'),(31,'14,14'),(31,'15,4'),(31,'15,5'),(31,'15,6'),(31,'15,7'),(31,'15,8'),(31,'15,9'),(31,'15,10'),(31,'15,11')," +
            "(31,'15,12'),(31,'15,13'),(31,'15,14'),(32,'14,2'),(32,'14,3'),(32,'15,2'),(32,'15,3');" ;
    private static final String CREATE_TABLE_WAYPOINTS ="CREATE TABLE IF NOT EXISTS "+TBL_WAYPOINTS +"("+REF_ID +" INTEGER PRIMARY KEY AUTOINCREMENT ,"+ROOM_NO+" INTEGER NOT NULL ,"+X_VALUE+" INTEGER not null ,"+Y_VALUE+" INTEGER not null );";
    private static final String INSERT_VALUE_WAYPOINTS="INSERT INTO " + TBL_WAYPOINTS+ "(room_no,x_value,y_value) VALUES (650,9,55),(651,12,55),(609,12,53),(610,12,47),(607,9,47),(611,12,38),(612,12,37),(613,12,30),(606,9,37),(605,9,27),(690,12,27),(604,12,26),(617,12,21),(618,12,19),(619,15,19),(603,15,17),(601,15,7),(600,15,2),(608,8,55)," +
            "(507,8,54),(550,10,54),(551,13,54),(552,11,21),(508,13,52),(509,13,45),(510,13,37),(511,13,34),(512,13,31),(504,10,27),(505,10,34),(506,10,45),(503,13,26),(502,15,16),(501,15,9),(506,10,45),(503,13,26),(502,15,16),(518,13,21),(519,15,21),(500,15,3),(590,13,27);" ;

    public static final String WAY_ID = "way_id";
    public static final String WAY_REF_ID = "way_ref_id";
    private static final String CREATE_TABLE_WAYPOINTS_NUM ="CREATE TABLE IF NOT EXISTS "+TBL_WAYPOINTS_NUMBER +"("+WAY_ID +" INTEGER PRIMARY KEY AUTOINCREMENT ,"+ROOM_NO+" INTEGER NOT NULL ,"+WAY_REF_ID+" INTEGER NOT NULL );";
    private static final String INSERT_VALUE_WAYPOINTS_NUM="INSERT INTO " + TBL_WAYPOINTS_NUMBER+ "(room_no,way_ref_id) VALUES (650,1),(651,2),(609,3),(610,4),(607,5),(611,6),(612,7),(613,9),(606,8),(605,11),(690,10),(604,12),(617,13),(618,14),(619,15),(603,16),(601,17)," +
            "(600,18),(608,0),(507,0),(550,1),(551,2),(552,14),(508,3),(509,4),(510,6),(511,7),(512,9),(504,11),(505,8),(506,5),(503,12),(518,13),(519,15),(502,16),(501,17),(500,18),(590,10);" ;




    public DatabaseHelper(@Nullable Context context) {

        super(context, DB_NAME, null, DB_VERSION);
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
        db.execSQL(INSERT_VALUES_GRID_ROOM1);

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
