package com.sample_project.mitsmap;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.SerializationHelper;

public class DijkstrasActivity extends AppCompatActivity implements SensorEventListener {
    private static int floor_num;
    static int[] selected_points,selected_rooms;
    int[][] selected_XY_points;
    static int[][] arrayReceived;
    static int[] selected_pathpoints,selected_pathpoints_new;
    static int[][] pathpointsselected ,pathpointsselected_new;
    static int[] roomsinpath,roomsinpath_new;
static  boolean startscan=false;
    // Class members for each sensor reading of interest
    private static float accelerometerX;
    private static float accelerometerY;
    private static float accelerometerZ;
    private static float magneticX;
    private static float magneticY;
    private static float magneticZ;
    private static float light;
    private static float rotationX;
    private static float rotationY;
    private static float rotationZ;
    private static float[] rotation;
    private static float[] inclination;
    private static float[] orientation;
    private List<Sensor> sensorList;
    private String display_msg="";
//file
private static final String FILE_NAME = "error_analysis.txt";
private static int datacollector=1;
    FileOutputStream fos = null;
    //weka
    private static final String WEKA_TEST = "WekaTest";
    static ArrayList<String> classVal = new ArrayList<String>();
    DecimalFormat decimalFormatter;
    private long pressedTime;
    static String className=null;
    static int nav_status;
    private Handler handler = new Handler();
    static ArrayList<Attribute> attributeList = new ArrayList<Attribute>();
    private static int collectionCount=0;
    SharedPreferences preferences ;
    BluetoothManager btManager;
    BluetoothAdapter btAdapter;
    BluetoothLeScanner btScanner;
    boolean scanning = false;
    private SensorManager sensorManager;
    private DatabaseManger dbmanager;
    ArrayList<String> listBluetoothDevice;
    Map<String, String> device_rssi_dynamic ;
    Map<String, Integer> device_scan_no;
    static String rssi_old;
    int x_flag = 1,scan_val = 0;
    static int button_status=0;
    HashMap<String, Integer> fixedMACBLE = new HashMap<String, Integer>();
    DijkstrasLiveView gridRoom;
   static int start_floor ;
    static int dest_floor;
    static int source_floor;
    static int floor;
    static int inital=0;
    static int stop_room, dest_room_no;
    static boolean start_at_exit_point;
    boolean navigation_status=false;
    AlertDialog.Builder alertDialogBuilder;
    HashMap<String, Integer> BLE_FLOOR = new HashMap<String, Integer>();
    private void resetBLEReadings() {
        fixedMACBLE.clear();
        fixedMACBLE.put("AC:67:B2:3C:C6:46",0);//esp32 1
        fixedMACBLE.put("3C:61:05:14:A7:C2",0);//esp32 2
        fixedMACBLE.put("3C:61:05:14:B1:BA",0);//esp32 10
        fixedMACBLE.put("3C:61:05:14:B5:0A",0);//esp32 14
        fixedMACBLE.put("3C:61:05:14:A7:72",0);//esp32 20
        fixedMACBLE.put("E0:E2:E6:0D:49:76",0); //	-ESP21 -[05]
        fixedMACBLE.put("E0:E2:E6:0D:39:FA",0);	//-ESP23 -[06]
        fixedMACBLE.put("E0:E2:E6:0B:7D:7A",0);	//-ESP24 -[07]
    }

    private void resetBLEFLOORReadings() {
        BLE_FLOOR.clear();
        BLE_FLOOR.put("AC:67:B2:3C:C6:46",231);//esp32 10
        BLE_FLOOR.put("3C:61:05:14:A7:C2",202);//esp32 2
        BLE_FLOOR.put("3C:61:05:14:B1:BA",207);//esp32 8
        BLE_FLOOR.put("3C:61:05:14:B5:0A",204);//esp32 14
        BLE_FLOOR.put("3C:61:05:14:A7:72",000);//esp32 20
        BLE_FLOOR.put("E0:E2:E6:0D:49:76",201); //	-ESP21 -[05]
        BLE_FLOOR.put("E0:E2:E6:0D:39:FA",205);	//-ESP23 -[06]
        BLE_FLOOR.put("E0:E2:E6:0B:7D:7A",200);	//-ESP24 -[24]
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dijkstras);
        try {
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Intent intent=getIntent();
        alertDialogBuilder= new AlertDialog.Builder(this);
        start_at_exit_point=intent.getBooleanExtra("start_at_exit",true);
        floor=intent.getIntExtra("floor_num",0);
        Log.i("Strt_point",start_at_exit_point+"");
        selected_points= intent.getIntArrayExtra("selected_points");
        selected_rooms=intent.getIntArrayExtra("selected_rooms");
        nav_status=intent.getIntExtra("NAV_STATUS",0);
        setFloorNum(floor);
       // preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        preferences = getSharedPreferences("Nav_Location", MODE_PRIVATE);
        start_floor = preferences.getInt("start_location",0);
        source_floor=preferences.getInt("Start_floor",0);
        dest_floor=preferences.getInt("Dest_Floor",0);
        stop_room=preferences.getInt("destination_location",0);
        dest_room_no=preferences.getInt("Destination_Room",0);
        Log.i("Dest_sh_result", "source floor="+source_floor+"des_floor" + dest_floor+" stop room = "+stop_room);

        dbmanager=new DatabaseManger(this);
        decimalFormatter = new DecimalFormat("#");
        decimalFormatter.setMaximumFractionDigits(8);

        btManager = (BluetoothManager) getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE);
        btAdapter = btManager.getAdapter();
        btScanner = btAdapter.getBluetoothLeScanner();

        sensorManager = (SensorManager)this.getSystemService(SENSOR_SERVICE);
        listBluetoothDevice = new ArrayList<>();
        device_rssi_dynamic = new HashMap<String, String>();
        device_scan_no = new HashMap<String, Integer>();
        resetBLEReadings();
        resetBLEFLOORReadings();
        rotation = new float[9];
        inclination = new float[9];
        orientation = new float[3];
        // pixelGrid = new MyGridLocationView(this);
        Log.d(WEKA_TEST,  "before button");
        gridRoom= new DijkstrasLiveView(this);
       // gridRoom.setGridValue(intent.getStringExtra("initalXY"));
        gridRoom.setNumColumns(16);
        gridRoom.setNumRows(35);

        Log.i("dij_selected_points", Arrays.toString(selected_points));
        Log.i("dij_selected_rooms", Arrays.toString(selected_rooms));

        arrayReceived= new int[selected_points.length][2];
        Object[] objectArray = (Object[]) getIntent().getExtras().getSerializable("selected_XY_points");
        if(objectArray!=null){
            arrayReceived = new int[objectArray.length][];
            for(int i=0;i<objectArray.length;i++){
                arrayReceived[i]=(int[]) objectArray[i];
            }
        }

        Log.i("dij_selected_XY_points", Arrays.deepToString(arrayReceived));

        setContentView(gridRoom);
        navigation_status=true;
        scanBLE();
        gridRoom.invalidate();
        gridRoom.requestLayout();

    }



    public void setFloorNum(int floornum){
        this.floor_num = floornum;

    }
    public static int getFloorNum() {
        return floor_num;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        switch (sensorEvent.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                accelerometerX = sensorEvent.values[0];
                accelerometerY = sensorEvent.values[1];
                accelerometerZ= sensorEvent.values[2];
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                magneticX = sensorEvent.values[0];
                magneticY = sensorEvent.values[1];
                magneticZ = sensorEvent.values[2];
                break;
            case Sensor.TYPE_LIGHT:
                light = sensorEvent.values[0];
                break;
            case Sensor.TYPE_ROTATION_VECTOR:
                rotationX = sensorEvent.values[0];
                rotationY = sensorEvent.values[1];
                rotationZ = sensorEvent.values[2];
                break;
            default:
                break;
        }

        SensorManager.getRotationMatrix(rotation, inclination,
                new float[] {accelerometerX, accelerometerY, accelerometerZ},
                new float[] {magneticX, magneticY, magneticZ});
        orientation = SensorManager.getOrientation(rotation, orientation);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    @Override
    protected void onResume()
    {
        super.onResume();
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
        for (Sensor sensor : sensorList) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
        }

    }
    @Override
    protected void onStop()
    {
        // Unregister the listener
        super.onStop();
        sensorManager.unregisterListener(this);
        btScanner.stopScan(leScanCallback);
        btScanner.flushPendingScanResults(leScanCallback);
    }

    @Override
    protected void onPause() {
        super.onPause();
        btScanner.stopScan(leScanCallback);

        sensorManager.unregisterListener(this);
    }
    private void scanBLE() {
        System.out.println("start scanning");
        if (!scanning && navigation_status ) {

            // Stops scanning after a predefined scan period.
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    scanning = false;
                    //btScanner.stopScan(leScanCallback);
                    // Log.i("Log_scan_stop", "Arraylist " + rssiArrayList);
//

                    ArrayList resultBle =  getListBluetoothDevice();

                        Map<String, String> resultRssiMap = getDeviceRssiDynamic();
                        Map<String, Integer> resultScanNumMap = getDeviceScanNo();

                        Log.i("My_final_cal_dij", "scan finished--" + getListBluetoothDevice() + "*" +
                                accelerometerX + "," + accelerometerY + "," + accelerometerZ +
                                "*" + magneticX + "," + magneticY + "," + magneticZ + "*" + light +
                                "*" + rotationX + "," + rotationY + "," + rotationZ + "*" +
                                orientation[0] + "," + orientation[1] + "," + orientation[2]
                        );
                        Log.i("BLE_RSSI", resultRssiMap+"");
                        Log.i("BLE_list",resultBle+"");

                        if(resultBle!= null && !resultBle.isEmpty()){
                            collectionCount++;
                            Log.i("BLE_size","resultBle="+resultBle.size()+
                                    "  resultRssiMap"+resultRssiMap.size()

                            );

                            if (resultBle.size() != 0) {
                                resetBLEReadings();
                                for (int i = 0; i < resultBle.size(); i++) {
                                    Log.i("ble" + i, resultBle.get(i) + " ");
                                    String myRssi = resultRssiMap.get(resultBle.get(i));
                                    Log.i("ble" + i, myRssi);
                                    //split the value into an array
                                    String[] arrOfStr = myRssi.split(",");
                                    int[] int_rssi_array = new int[arrOfStr.length];
                                    for (int j = 0; j < arrOfStr.length; j++) {
                                        int_rssi_array[j] = Integer.parseInt(arrOfStr[j]);
                                    }

                                    //find average of rssi or apply the filter

                                    double total = 0;
                                    for (int k = 0; k < int_rssi_array.length; k++) {
                                        total = total + int_rssi_array[k];
                                    }
                                    double average = total / int_rssi_array.length;
                                    Log.i("ble" + i, (int) average+"");
                                    Log.i("ble" + i, "--------------------------------");
                                    fixedMACBLE.put(resultBle.get(i) + "", (int) average);

                                }
                                Log.i("ble_final_dij", fixedMACBLE + "");
                                //check the ble node in corresponding floor
                                Log.i("Dfloor_Predicit",getFloorNum()+"");
                                int current_floor=getFloorNum();
                                predictTheModelF1(current_floor);

                                // predictTheModel();
                                resultBle.clear();
                                resultRssiMap.clear();
                            }
                            // resultBle.clear();
                        }
                    scanning = true;


//                        no_of_scans = 1;
                    // Repeat.
                    handler.postDelayed(this, 1500);
                }
            }, 2000);//earlier it is 2500

            scanning = true;
            Log.i("Log_scan_3", "scan finished" + scanning);
            try {
                btScanner.startScan(leScanCallback);
                //
                // ble_class.listBluetoothDevice.clear();
            } catch (Exception e) {
                Log.i("Log_scan_4", "Exception" + e);
            }


        } else {

            scanning = false;
            Log.i("Log_scan_5", "scan finished" + scanning);
            btScanner.stopScan(leScanCallback);

        }

    }

    private void predictTheModelF1(int current_floor) {
        try {
            int r_no = checkTheFloor();


            int room_id_no=dbmanager.fetchNearByRoom(r_no,floor_num);
            int room_no=dbmanager.fetchMyRoom(room_id_no);
            int x_point = dbmanager.fetchXWayPointNumber(room_no);
            int y_point = dbmanager.fetchYWayPointNumber(room_no);
            gridRoom.setGridValue(x_point,y_point);

            Log.d("last_room",  selected_rooms[selected_rooms.length-1]+ "and room_no= "+room_no);
            Log.i("if_case_dij","Selected rooms on path"+ Arrays.toString(selected_rooms)+" Room no="+room_no+" Dest_room_no="+dest_room_no+" Stop room="+stop_room+" inital="+inital);

            if(selected_rooms[selected_rooms.length-1]==room_no){

                if(room_no==dest_room_no ){
                    Log.i("nav_complete"," Room no="+room_no+" Dest_room_no="+dest_room_no+" inital="+inital+"start_scan="+startscan);
                    setContentView(gridRoom);
                   // Toast.makeText(this,"Hurry, you reached the destination!!!",Toast.LENGTH_SHORT).show();
                    navigation_status=false;
                   // startscan=false;
                    if(dest_room_no==5)
                        display_msg=ConstantsValue.proom_msg;
                    btScanner.stopScan(leScanCallback);
                    alertDialogBuilder.setTitle("Navigation Completed");
                    alertDialogBuilder.setMessage("You reached at the destination"+display_msg);
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }else if(room_no!=dest_room_no) {
                    Log.i("inside_if3"," Room no="+room_no+" Dest_room_no="+dest_room_no+" inital="+inital+"start_scan="+startscan);
                    //startscan=false;
                    String direction="downwards";
                    if(source_floor-dest_floor>0)
                        direction="downwards";
                    else
                        direction="upwards";


                    btScanner.stopScan(leScanCallback);
                    setContentView(gridRoom);
                    alertDialogBuilder.setTitle("Floor Change");
                    alertDialogBuilder.setMessage("You reached the exit point of current floor .Please go "+direction+"  to reach your destination room on floor number "+dest_floor+".Press OK once you reach your destination floor.");
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            //Toast.makeText(getApplicationContext(),"You clicked over OK",Toast.LENGTH_SHORT).show();
                            //load the dest floor map.
                            //start vertex is the current entry at destination floor 1/ from left or from right
                            //destination vertex is the destinastion room.
                            //dest_floor=5;

                            ArrayList<String> liftsinfloor=dbmanager.fetchLiftStairinFloor(dest_floor);
                            int[] floor_dest=new int[liftsinfloor.size()];
                            Log.i("floor_lift_dij",liftsinfloor.size()+" is the size");
                            for (int i = 0; i < liftsinfloor.size();i++)
                            {
                                String tosplit=liftsinfloor.get(i);
                                Log.i("floor_lift_dij",tosplit+"");
                                String[] parts = tosplit.split(",");
                                Log.i("floor_lift_dij", Arrays.toString(parts)+" second part after split "+parts[1]);
                                parts[1]=parts[1].replace(" ","");
                                parts[1]=parts[1].trim();
                                floor_dest[i]= Integer.parseInt(parts[1]);
                            }

                            Log.i("floor_lift_dij","Exit points in floor = " + Arrays.toString(floor_dest));
                            int start_vertex=0;
                            //if user is at any of the exit point
                            for(int element=0;element<floor_dest.length;element++){
                                if(floor_dest[element]==room_no ){
                                    start_vertex=dbmanager.fetchRoomWayNumber(room_no);
                                }
//                                else if(room_no==0)
//                                    start_vertex=1;
                            }
                            //get the corresponding waypoint number for floor_dest
                            for(int i=0;i<floor_dest.length;i++){
                                floor_dest[i]=dbmanager.fetchRoomWayNumber(floor_dest[i]);
                            }
                            Log.i("floor_lift_dij","floor_dest="+ Arrays.toString(floor_dest));

                            //cheeck if reached at any exist of destination floor

                            Log.i("floor_lift_dij","Start_vertex= "+start_vertex +" stop_vertex ="+stop_room);
                            DijkstrasAlgorithm.dijkstra(Adjacency_VARIABLE.get_adjacency_matrix(dest_floor), start_vertex, stop_room);
                            // Path_Result myPath = new Path_Result();
                            selected_pathpoints_new = Path_Result.myPath;
                            Log.i("param_selected_path", Arrays.toString(selected_pathpoints_new)+" floor number="+dest_floor);
                            Path_Result.refreshPath();
                            pathpointsselected_new = new int[selected_pathpoints_new.length][2];
                            roomsinpath_new=new int[selected_pathpoints_new.length];

                            for(int i=0;i<selected_pathpoints_new.length;i++){
                                roomsinpath_new[i]=dbmanager.fetchRoomNumber(selected_pathpoints_new[i],dest_floor);
                                pathpointsselected_new[i][0]=dbmanager.fetchXWayPointNumber(roomsinpath_new[i]);
                                pathpointsselected_new[i][1]=dbmanager.fetchYWayPointNumber(roomsinpath_new[i]);
                            }
                            navigation_status=true;
                            setFloorNum(dest_floor);
                            Intent in=new Intent(DijkstrasActivity.this, DijkstrasActivity.class);

                            in.putExtra("selected_points",selected_pathpoints_new);
                            in.putExtra("selected_rooms",roomsinpath_new);
                            in.putExtra("floor_num",dest_floor);
                            in.putExtra("NAV_STATUS",0);//same floor
                            Bundle mBundle = new Bundle();
                            mBundle.putSerializable("selected_XY_points", pathpointsselected_new);
                            in.putExtras(mBundle);
                            startActivity(in);
                            Log.i("dij_selected_points_n", Arrays.toString(selected_pathpoints_new));
                            Log.i("dij_selected_rooms_n", Arrays.toString(roomsinpath_new));
                            finish();

                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }

            else{

                setContentView(gridRoom);
            }


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        attributeList.clear();
        classVal.clear();
        className=null;
    }
    private int checkTheFloor() {
        // Create a list from elements of HashMap
        String selected_ble = null;
        Integer value = 0;
        Map<String, Integer> map = new HashMap<String, Integer>();
        for(Map.Entry<String, Integer> entry: fixedMACBLE.entrySet()) {

            // if give value is equal to value from entry
            // print the corresponding key
            if(entry.getValue() != value) {
                map.put(entry.getKey(),entry.getValue());
            }
        }
        int maxValueInMap=(Collections.max(map.values()));  // This will return max value in the HashMap
        Log.i("c_floor_check", "Maximum value : "+maxValueInMap);

        for (Map.Entry<String, Integer> entry : fixedMACBLE.entrySet()) {  // Iterate through HashMap
            if (entry.getValue()==maxValueInMap) {
                selected_ble=entry.getKey();
                break;// Print the key with max value
            }
        }

        Log.i("c_floor_check", "Selected ble : "+selected_ble);
        return BLE_FLOOR.get(selected_ble);
    }


    private ScanCallback leScanCallback = new ScanCallback() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            BluetoothDevice myDevice = result.getDevice();
            Method getUuidsMethod = null;

            Log.i("lescan", "txpower" + myDevice.getType() + "Name: " + result.getDevice().getName() + " --rssi:   " + result.getRssi());

            myDevice.getUuids();
            myDevice.getBluetoothClass();
            myDevice.getAddress();

            int rssi = result.getRssi();
            String resultname = result.getDevice().getAddress();
////
            if (!(resultname == null)) {
                // tv_scan.append("Name: " + result.getDevice().getName() + " --rssi:  " + rssi + "\n");
                Log.i("SCAN_DA", "txpower" + myDevice.getType() + "Name: " + resultname + " --rssi:   " + rssi);
                addBluetoothDevice(myDevice,resultname, rssi);
                //  Log.i("add_device", "Status: " + add_status);
            }
        }
        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
//                    Toast.makeText(MainActivity.this,
//                            "onScanFailed: " + String.valueOf(errorCode),
//                            Toast.LENGTH_LONG).show();
            Log.e("Log_scan_failed", "Result=" + errorCode);
        }

        private void addBluetoothDevice(BluetoothDevice device, String myDeviceAddress, int rssi) {
            if(fixedMACBLE.containsKey(device.toString())){
                if (!listBluetoothDevice.contains(device.toString())) {

                    listBluetoothDevice.add(device.toString());
                    Log.i("Log_scan_add_0.1", device.toString() + "---" + rssi);
                    device_rssi_dynamic.put(device.toString(), Integer.toString(rssi));
                    device_scan_no.put(device.toString(), 1);


                    Log.i("Log_scan_add_1", "" + device_rssi_dynamic);

                    // Toast.makeText(getApplicationContext(),"New device added to the list"+device_rssi_dynamic,Toast.LENGTH_SHORT).show();
                    // listViewLE.invalidateViews();
                } else {
                    //update the value with new value.
                    // setNoofScan();
                    //no_of_scans++;
                    Log.i("Log_scan_1_ELSE", listBluetoothDevice + "--" + device_rssi_dynamic);
                    if (device_rssi_dynamic.containsKey(device.toString())) {
                        rssi_old = device_rssi_dynamic.get(device.toString());

                        String rssi_concat = rssi_old.concat(",");
                        rssi_concat += Integer.toString(rssi);
//                            int rssi_new_avg= (int) ((0.75*rssi)+((1-0.75)*rssi_old));
//                            int rssi_new=rssi_new_avg+rssi_old;
                        device_rssi_dynamic.put(device.toString(), rssi_concat);
                        scan_val = device_scan_no.get(device.toString());
                        scan_val++;
                        device_scan_no.put(device.toString(), scan_val);
                        Log.i("Log_scan_LEUSER", device_scan_no.get(device.toString()) + "--" + rssi + "---->>>" + device_rssi_dynamic);

                        //Toast.makeText(getApplicationContext(),"Device already in the list"+device_rssi_dynamic,Toast.LENGTH_SHORT).show();
                    } else {
                        Log.i("Log_scan_", "no device found");

                    }


                }
            }


        }
    };
    public Map<String, String> getDeviceRssiDynamic(){
        if(!device_rssi_dynamic.isEmpty()){
            Log.i("getDeviceRssiDynamic",""+device_rssi_dynamic);
            return device_rssi_dynamic;
        }else{
            return null;
        }

    }
    public Map<String, Integer> getDeviceScanNo(){
        if(!device_rssi_dynamic.isEmpty()){
            Log.i("getDeviceScanNo",""+device_scan_no);
            return device_scan_no;
        }else{
            return null;
        }

    }
    public ArrayList<String> getListBluetoothDevice(){
        if(!listBluetoothDevice.isEmpty()){
            Log.i("getListBluetoothDevice",""+listBluetoothDevice);
            return listBluetoothDevice;
        }else{
            return null;
        }

    }
    public float getmagneticX(){
        return magneticX;
    }
    public float getmagneticY(){
        return magneticY;
    }
    public float getmagneticZ(){
        return magneticZ;
    }

}