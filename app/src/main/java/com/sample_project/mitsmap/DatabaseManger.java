package com.sample_project.mitsmap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseManger {
    private DatabaseHelper dbHelper;
    private Context context;

    private SQLiteDatabase database;
    private SQLiteDatabase database_insert;

    public DatabaseManger(Context c) {
        this.context = c;
        dbHelper = new DatabaseHelper(c);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Log.i("db_check",  "inside databse constructor"+context);

    }


//    public void close() {
//        dbHelper.close();
//    }
//
//    //to fetch the detaild from building table to the spinner
//    public List<BuildingSpinner> fetch() {
//        List<BuildingSpinner> list = new ArrayList<BuildingSpinner>();
//        dbHelper = new DatabaseHelper(context);
//        database = dbHelper.getReadableDatabase();
//        String selectQuery = "SELECT  * FROM " + DatabaseHelper.TBL_BULDING;
//        Cursor cr = database.rawQuery(selectQuery, null);
//        while (cr.moveToNext()) {
//            list.add(new BuildingSpinner(cr.getInt(0), cr.getString(1)));
//        }
//        // closing connection
//        cr.close();
//        //  database.close();
//        // returning lables
//
//        return list;
//    }
//
//    //insert the room details to the table
//    public boolean insertDataRoom(String room_name, int room_no, int floor_id) {
//        dbHelper = new DatabaseHelper(context);
//        database_insert = dbHelper.getWritableDatabase();
//        ContentValues contentValues=new ContentValues();
//        contentValues.put(DatabaseHelper.ROOM_NAME,room_name);
//
//        contentValues.put(DatabaseHelper.ROOM_NO,room_no);
//        contentValues.put(DatabaseHelper.FLOOR_ID,floor_id);
//
//        Long result=database_insert.insert(DatabaseHelper.TBL_ROOM,null,contentValues);
//        if(result==-1 ){
//            Log.i("LOG_ROOM","Unscucessfully inserted");
//            return false;
//        }else{
//            Log.i("LOG_ROOM","Succesfully inserted");
//            return true;
//        }
//    }
//    //check for the room id based on the room number
//    public ArrayList<Integer> fetchRoomId(int room_num) {
//        int room_id=0;
//        ArrayList<Integer> arList = new ArrayList<>();
//        boolean flag=false;
//        //List<BuildingSpinner> list = new ArrayList<BuildingSpinner>();
//        dbHelper = new DatabaseHelper(context);
//        database = dbHelper.getReadableDatabase();
//        String selectQuery =  "SELECT "+DatabaseHelper.ROOM_ID+" FROM "
//                + DatabaseHelper.TBL_ROOM +" WHERE "+DatabaseHelper.ROOM_NO+" = "
//                + room_num ;
//        Cursor cr = database.rawQuery(selectQuery, null);
//        while (cr.moveToNext()) {
//
//            int index1=cr.getColumnIndex(DatabaseHelper.ROOM_ID);
//            arList.add(cr.getInt(index1));
//        }
//        // closing connection
//        cr.close();
//        //  database.close();
//        // returning lables
//     return  arList;
//    }
//
//    public boolean deleteGridData(int room_id) {
//        dbHelper = new DatabaseHelper(context);
//        database = dbHelper.getWritableDatabase();
//        int i=database.delete(DatabaseHelper.TBL_GRID_ROOM, DatabaseHelper.ROOM_ID + "=" + room_id, null);
//        // close();
//        if(i==-1){
//            return false;
//        }else{
//            return true;
//        }
//    }
//    public boolean deleteRoomData(int room_num) {
//        dbHelper = new DatabaseHelper(context);
//        database = dbHelper.getWritableDatabase();
//        int i=database.delete(DatabaseHelper.TBL_ROOM, DatabaseHelper.ROOM_ID + "=" + room_num, null);
//        // close();
//        if(i==-1){
//            return false;
//        }else{
//            return true;
//        }
//    }
//    //fetch last room id
//    public int fetchLastRoomId() {
//        int room_id=0;
//        dbHelper = new DatabaseHelper(context);
//        database = dbHelper.getReadableDatabase();
//        String selectQuery=   "SELECT "+DatabaseHelper.ROOM_ID+" FROM "
//                + DatabaseHelper.TBL_ROOM +" ORDER BY "+DatabaseHelper.ROOM_ID+" DESC LIMIT 1 " ;
//        Cursor cr=database.rawQuery(selectQuery, null);
//        if (cr.moveToNext()) {
//
//            int index1=cr.getColumnIndex(DatabaseHelper.ROOM_ID);
//            room_id=cr.getInt(index1);
//
//
//        }
//        cr.close();
//
//        return room_id;
//    }
//
//    //insert the room-grids mapping
//    public boolean insertGridRoomMapping(int room_no, String grid_no) {
//        dbHelper = new DatabaseHelper(context);
//        database_insert = dbHelper.getWritableDatabase();
//        ContentValues contentValues=new ContentValues();
//        contentValues.put(DatabaseHelper.ROOM_ID,room_no);
//        contentValues.put(DatabaseHelper.GRID_NO,grid_no);
//
//        Long result=database_insert.insert(DatabaseHelper.TBL_GRID_ROOM,null,contentValues);
//        if(result==-1 ){
//            Log.i("LOG_ROOM","Unscucessfully inserted");
//            return false;
//        }else{
//            Log.i("LOG_ROOM","Succesfully inserted");
//            return true;
//        }
//    }
//
//
//    public ArrayList fetchXYWaypoints(int floor) {
//        floor=floor+1;
//        String search= String.valueOf(floor)+'%';
//        Log.i("WAY_ARRAY_SE", search);
//        ArrayList<String> arList = new ArrayList<>();
//        boolean flag=false;
//        //List<BuildingSpinner> list = new ArrayList<BuildingSpinner>();
//        dbHelper = new DatabaseHelper(context);
//        database = dbHelper.getReadableDatabase();
//        String selectQuery =  "SELECT * FROM "
//                + DatabaseHelper.TBL_WAYPOINTS +" WHERE "+DatabaseHelper.ROOM_NO
//                + " LIKE '" + floor + "%'" ;
//        Cursor cr = database.rawQuery(selectQuery, null);
//        while (cr.moveToNext()) {
//
//            int index1=cr.getColumnIndex(DatabaseHelper.X_VALUE);
//            int index2=cr.getColumnIndex(DatabaseHelper.Y_VALUE);
//           String x_y_value=cr.getInt(index1)+","+cr.getInt(index2);
//           arList.add(x_y_value);
//        }
//        // closing connection
//        cr.close();
//        //  database.close();
//        // returning lables
//        Log.i("WAY_ARRAY", Arrays.toString(arList.toArray()));
//        return  arList;
//    }
//
//    public Point fetchRoomWayPoint(int room_num) {
//        Point xy_value = null;
//        int room_id=0;
//        dbHelper = new DatabaseHelper(context);
//        database = dbHelper.getReadableDatabase();
//        String selectQuery =  "SELECT * FROM "
//                + DatabaseHelper.TBL_WAYPOINTS +" WHERE "+DatabaseHelper.ROOM_NO+" = "
//                + room_num ;
//        Cursor cr = database.rawQuery(selectQuery, null);
//        while (cr.moveToNext()) {
//
//            int index1=cr.getColumnIndex(DatabaseHelper.X_VALUE);
//            int index2=cr.getColumnIndex(DatabaseHelper.Y_VALUE);
//            xy_value=new Point(cr.getInt(index1),cr.getInt(index2));
//        }
//        // closing connection
//        cr.close();
//        return xy_value;
//    }
//
    public int fetchXWayPointNumber(int room_no) {
        int xy_value = 0;
        int room_id=0;
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT * FROM "
                + DatabaseHelper.TBL_WAYPOINTS +" WHERE "+DatabaseHelper.ROOM_NO+" = "
                + room_no ;
        Cursor cr = database.rawQuery(selectQuery, null);
        while (cr.moveToNext()) {

            int index1=cr.getColumnIndex(DatabaseHelper.X_VALUE);
            xy_value=cr.getInt(index1);

        }
        // closing connection
        cr.close();
        return xy_value;
    }

    public int fetchRoomNumber(int selected_pathpoint,int floor) {

        String search= String.valueOf(floor)+'%';
        int room_value = 0;
        int room_id=0;
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT * FROM "
                + DatabaseHelper.TBL_WAYPOINTS_NUMBER +" WHERE "+DatabaseHelper.WAY_REF_ID+" = "
                + selected_pathpoint +" AND "+DatabaseHelper.FLOOR_ID+" = "+ floor;

        Cursor cr = database.rawQuery(selectQuery, null);
        while (cr.moveToNext()) {

            int index1=cr.getColumnIndex(DatabaseHelper.ROOM_NO);
            room_value=cr.getInt(index1);
        }
        Log.i("roomno_floor", "You are near "+room_value+" in floor "+floor);
        // closing connection
        cr.close();
        return room_value;
    }

    public int fetchYWayPointNumber(int i) {
        int xy_value = 0;
        int room_id=0;
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT * FROM "
                + DatabaseHelper.TBL_WAYPOINTS +" WHERE "+DatabaseHelper.ROOM_NO+" = "
                + i ;
        Cursor cr = database.rawQuery(selectQuery, null);
        while (cr.moveToNext()) {

            int index1=cr.getColumnIndex(DatabaseHelper.Y_VALUE);
            xy_value=cr.getInt(index1);
        }
        // closing connection
        cr.close();
        return xy_value;
    }

    public int fetchNearByRoom(int r_no, int floor) {

        int xy_value = -1;

        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT * FROM "
                + DatabaseHelper.TBL_GRID_ROOM  +" a INNER JOIN "+DatabaseHelper.TBL_ROOM+" b ON a."+DatabaseHelper.ROOM_ID+ "= b."+DatabaseHelper.ROOM_ID+
                " WHERE "+DatabaseHelper.ROOM_NO+ " = '" + r_no  + "'" +
                " AND "+DatabaseHelper.FLOOR_ID+" = "+ floor ;

        Cursor cr=database.rawQuery(selectQuery, null);
        while (cr.moveToNext()) {

            int index1=cr.getColumnIndex(DatabaseHelper.ROOM_ID);
            xy_value=cr.getInt(index1);


        }
        Log.i("c_floor", "You are near "+xy_value);
        // closing connection
        cr.close();
        return xy_value;
    }

    public int fetchMyRoom(int room_id_no) {
        int xy_value = -1;
        int room_id=0;
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT * FROM "
                + DatabaseHelper.TBL_ROOM +" WHERE "+DatabaseHelper.ROOM_ID+" = "
                + room_id_no ;
        Cursor cr = database.rawQuery(selectQuery, null);
        while (cr.moveToNext()) {

            int index1=cr.getColumnIndex(DatabaseHelper.ROOM_NO);
            xy_value=cr.getInt(index1);
        }
        // closing connection
        cr.close();
        return xy_value;
    }
//
//    public int[] allWayPoints() {
//        int[] list = new int[200];
//        int i=0;
//        dbHelper = new DatabaseHelper(context);
//        database = dbHelper.getReadableDatabase();
//        String selectQuery = "SELECT  * FROM " + DatabaseHelper.TBL_WAYPOINTS;
//        Cursor cr = database.rawQuery(selectQuery, null);
//        while (cr.moveToNext()) {
//
//            int index1=cr.getColumnIndex(DatabaseHelper.ROOM_NO);
//            list[i]=cr.getInt(index1);
//            i++;
//        }
//        cr.close();
//        return list;
//    }
    public int[] allRooms() {

        int i=0;
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + DatabaseHelper.TBL_ROOM;
        Cursor cr = database.rawQuery(selectQuery, null);
        int[] list = new int[cr.getCount()];
        while (cr.moveToNext()) {

            int index1=cr.getColumnIndex(DatabaseHelper.ROOM_NO);
            list[i]=cr.getInt(index1);
            i++;
        }
        cr.close();
        return list;
    }
    public int fetchRoomWayNumber(int start_loc) {
        int xy_value = 0;
        int room_id=0;
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT * FROM "
                + DatabaseHelper.TBL_WAYPOINTS_NUMBER +" WHERE "+DatabaseHelper.ROOM_NO+" = "
                + start_loc ;
        Cursor cr = database.rawQuery(selectQuery, null);
        while (cr.moveToNext()) {

            int index1=cr.getColumnIndex(DatabaseHelper.WAY_REF_ID);
            xy_value=cr.getInt(index1);
        }
        // closing connection
        cr.close();
        return xy_value;
    }

    public String fetchRoomName(int room_num) {
        String room_name = null;
        int room_id=0;
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT * FROM "
                + DatabaseHelper.TBL_ROOM +" WHERE "+DatabaseHelper.ROOM_NO+" = "
                + room_num ;
        Cursor cr = database.rawQuery(selectQuery, null);
        while (cr.moveToNext()) {

            int index1=cr.getColumnIndex(DatabaseHelper.ROOM_NAME);
            room_name=cr.getString(index1);
        }
        // closing connection
        cr.close();
        return room_name;
    }
    public int getFloorNumber(int start_loc) {
        int xy_value = 0;
        int room_id=0;
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT * FROM "
                + DatabaseHelper.TBL_ROOM +" WHERE "+DatabaseHelper.ROOM_NO+" = "
                + start_loc ;
        Cursor cr = database.rawQuery(selectQuery, null);
        while (cr.moveToNext()) {

            int index1=cr.getColumnIndex(DatabaseHelper.FLOOR_ID);
            xy_value=cr.getInt(index1);
        }
        // closing connection
        cr.close();
        return xy_value;
    }
    public ArrayList<String> fetchLiftStairinFloor(int floor_number) {
        int room_id=0;

        String search="lift";
        ArrayList<String> arList = new ArrayList<>();
        boolean flag=false;
        //List<BuildingSpinner> list = new ArrayList<BuildingSpinner>();
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT * FROM "
                + DatabaseHelper.TBL_ROOM +" WHERE "+DatabaseHelper.ROOM_NAME+" LIKE 'lift%'" +" AND "+DatabaseHelper.FLOOR_ID+" = "
                + floor_number ;
        Cursor cr = database.rawQuery(selectQuery, null);
        while (cr.moveToNext()) {

            int index1=cr.getColumnIndex(DatabaseHelper.ROOM_NAME);
            int index2=cr.getColumnIndex(DatabaseHelper.ROOM_NO);
            String x_y_value=cr.getString(index1)+","+cr.getInt(index2);
            Log.i("String_lift",x_y_value);
            arList.add(x_y_value);
        }

        // closing connection
        cr.close();
        //  database.close();
        // returning lables
        return  arList;
    }
}
