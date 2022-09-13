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
    private void resetBLEReadings() {
        fixedMACBLE.clear();
        fixedMACBLE.put("AC:67:B2:3C:C6:46",0);//esp32 1
        fixedMACBLE.put("3C:61:05:14:A7:C2",0);//esp32 2
        fixedMACBLE.put("3C:61:05:11:C6:02",0);//esp32 3
        fixedMACBLE.put("3C:61:05:11:D0:02",0);//esp32 4
        fixedMACBLE.put("3C:61:05:11:CE:82",0);//esp32 5
        fixedMACBLE.put("3C:61:05:11:D0:EE",0);//esp32 6
        fixedMACBLE.put("AC:67:B2:3C:CC:1A",0);//esp32 7
        fixedMACBLE.put("3C:61:05:11:C7:FE",0);//esp32 8

        fixedMACBLE.put("3C:61:05:11:B5:EA",0);//esp32 9

        fixedMACBLE.put("3C:61:05:14:B1:BA",0);//esp32 10
        fixedMACBLE.put("3C:61:05:11:D1:D2",0);//esp32 11
        fixedMACBLE.put("3C:61:05:14:AF:12",0);//esp32 12
        fixedMACBLE.put("3C:61:05:11:CD:4E",0);//esp32 13
        fixedMACBLE.put("3C:61:05:14:B5:0A",0);//esp32 14


        fixedMACBLE.put("3C:61:05:11:D0:36",0);//esp32 17
        fixedMACBLE.put("AC:67:B2:3C:CB:FA",0);//esp32 18
        fixedMACBLE.put("3C:61:05:11:C8:8A",0);//esp32 19
        fixedMACBLE.put("3C:61:05:14:A7:72",0);//esp32 20
        fixedMACBLE.put("E0:E2:E6:0D:49:76",0); //	-ESP21 -[05]
        fixedMACBLE.put("E0:E2:E6:0D:39:FA",0);	//-ESP23 -[06]
        fixedMACBLE.put("E0:E2:E6:0B:7D:7A",0);	//-ESP24 -[07]
        // fixedMACBLE.put("D0:5F:64:52:12:BB",0);//NODE1
    }
//    private void resetBLEFLOORReadings() {
//        BLE_FLOOR.clear();
//        BLE_FLOOR.put("AC:67:B2:3C:C6:46",1);//esp32 1
//        BLE_FLOOR.put("3C:61:05:14:A7:C2",1);//esp32 2
//        BLE_FLOOR.put("3C:61:05:11:C6:02",0);//esp32 3
//        BLE_FLOOR.put("3C:61:05:11:D0:02",0);//esp32 4
//        BLE_FLOOR.put("3C:61:05:11:CE:82",0);//esp32 5
//        BLE_FLOOR.put("3C:61:05:11:D0:EE",0);//esp32 6
//        BLE_FLOOR.put("AC:67:B2:3C:CC:1A",0);//esp32 7
//        BLE_FLOOR.put("3C:61:05:11:C7:FE",0);//esp32 8
//
//        BLE_FLOOR.put("3C:61:05:11:B5:EA",0);//esp32 9
//
//        BLE_FLOOR.put("3C:61:05:14:B1:BA",0);//esp32 10
//        BLE_FLOOR.put("3C:61:05:11:D1:D2",0);//esp32 11
//        BLE_FLOOR.put("3C:61:05:14:AF:12",0);//esp32 12
//        BLE_FLOOR.put("3C:61:05:11:CD:4E",0);//esp32 13
//        BLE_FLOOR.put("3C:61:05:14:B5:0A",1);//esp32 14
//
//
//        BLE_FLOOR.put("3C:61:05:11:D0:36",0);//esp32 17
//        BLE_FLOOR.put("AC:67:B2:3C:CB:FA",0);//esp32 18
//        BLE_FLOOR.put("3C:61:05:11:C8:8A",0);//esp32 19
//        BLE_FLOOR.put("3C:61:05:14:A7:72",2);//esp32 20
//        BLE_FLOOR.put("E0:E2:E6:0D:49:76",2); //	-ESP21 -[05]
//        BLE_FLOOR.put("E0:E2:E6:0D:39:FA",2);	//-ESP23 -[06]
//        BLE_FLOOR.put("E0:E2:E6:0B:7D:7A",2);	//-ESP24 -[07]
////        BLE_FLOOR.put("D0:5F:64:52:12:BB",4);//NODE1
//    }
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
        rotation = new float[9];
        inclination = new float[9];
        orientation = new float[3];
        // pixelGrid = new MyGridLocationView(this);
        Log.d(WEKA_TEST,  "before button");
        gridRoom= new DijkstrasLiveView(this);
       // gridRoom.setGridValue(intent.getStringExtra("initalXY"));
        if(floor==1){
            gridRoom.setNumColumns(15);
            gridRoom.setNumRows(31);
        }else if(floor==2){
            gridRoom.setNumColumns(10);
            gridRoom.setNumRows(30);
        }

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
//        // Register this class as a listener for the accelerometer sensor
////        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
////                SensorManager.SENSOR_DELAY_NORMAL);
////        // ...and the orientation sensor
////        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
////                SensorManager.SENSOR_DELAY_NORMAL);
////        // Get a reference to the magnetometer
////        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),SensorManager.SENSOR_DELAY_NORMAL);
////        //sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);
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
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
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
                                switch (current_floor){
                                    case 1:predictTheModelF1(current_floor);break;
                                    case 2:predictTheModelF2(current_floor);break;
                                }

                                // predictTheModel();
                                resultBle.clear();
                                resultRssiMap.clear();
                            }
                            // resultBle.clear();
                        }
                    scanning = true;


//                        no_of_scans = 1;
                    // Repeat.
                    handler.postDelayed(this, 1000);
                }
            }, 2500);

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


            //Toast.makeText(getApplicationContext(), "["+ attributeList+"]", Toast.LENGTH_SHORT).show();
            attributeList.clear();

            Attribute mac1 = new Attribute("mac1");
            Attribute mac2 = new Attribute("mac2");
            Attribute mac3 = new Attribute("mac3");
            Attribute mac4 = new Attribute("mac4");
            Attribute mac5 = new Attribute("mac5");
            Attribute mac6 = new Attribute("mac6");
            Attribute mac7 = new Attribute("mac7");

            Attribute magx = new Attribute("magx");
            Attribute magy = new Attribute("magy");
            Attribute magz = new Attribute("magz");
            Attribute orix = new Attribute("orix");
            Attribute oriy = new Attribute("oriy");
            Attribute oriz = new Attribute("oriz");

//                    @attribute grid {'15,1','15,0','14,1','13,1','13,0','12,0','11,0','11,1','12,1','12,2','12,3','12,4','12,5','12,6','12,7','11,7','11,6','11,5','11,4','11,3','11,2'}
            classVal.add("12,25");
            classVal.add("11,25");
            classVal.add("10,25");
            classVal.add("9,25");
            classVal.add("8,25");
            classVal.add("8,24");
            classVal.add("9,24");
            classVal.add("9,23");
            classVal.add("9,22");
            classVal.add("9,21");
            classVal.add("8,23");

            // Toast.makeText(getApplicationContext(), "["+ classVal+"]", Toast.LENGTH_SHORT).show();
            attributeList.add(mac1);
            attributeList.add(mac2);
            attributeList.add(mac3);
            attributeList.add(mac4);
            attributeList.add(mac5);
            attributeList.add(mac6);
            attributeList.add(mac7);
            attributeList.add(magx);
            attributeList.add(magy);
            attributeList.add(magz);
            attributeList.add(orix);
            attributeList.add(oriy);
            attributeList.add(oriz);
            attributeList.add(new Attribute("gridValue", classVal));
            //Toast.makeText(getApplicationContext(), "["+ attributeList+"]", Toast.LENGTH_SHORT).show();
            Instances data = new Instances("TestInstances", attributeList, 0);

            data.setClassIndex(data.numAttributes() - 1);
            DenseInstance inst_co = new DenseInstance(data.numAttributes()) {
                {
                    //-82,-78,-82,-67,-55,-31.637161,9.728535,-18.218353,0.500622,-0.512263,9.255263,-0.030765,-0.004506,-0.605709,1.296407,0.055211,-0.054038,'15,1'

                    setValue(mac1, fixedMACBLE.get("AC:67:B2:3C:C6:46"));
                    setValue(mac2, fixedMACBLE.get("3C:61:05:14:A7:C2"));
                    setValue(mac3, fixedMACBLE.get("3C:61:05:14:B5:0A"));
                    setValue(mac4, fixedMACBLE.get("3C:61:05:14:A7:72"));
                    setValue(mac5, fixedMACBLE.get("E0:E2:E6:0D:49:76"));
                    setValue(mac6, fixedMACBLE.get("E0:E2:E6:0D:39:FA"));
                    setValue(mac7, fixedMACBLE.get("E0:E2:E6:0B:7D:7A"));
                    setValue(magx, magneticX);
                    setValue(magy, magneticY);
                    setValue(magz, magneticZ);
                    setValue(orix, orientation[0]);
                    setValue(oriy, orientation[1]);
                    setValue(oriz, orientation[2]);
                }
            };
            // reference to dataset
            inst_co.setDataset(data);
            Log.d(WEKA_TEST, data + "");
            // Set instance's values for the attributes "latitude", "longitude", and
            // "pollutant concentration"

            // inst_co.setMissing(cluster);
            //  inst_co.setDataset(data);
            AssetManager assetManager = getAssets();
            //previous model=updated_model4(ibk7).model
            J48 cls_co = (J48) SerializationHelper.read(assetManager.open("mgeorgeblock_floor1_j48.model"));

//            if (cls_co == null) {
//                Toast.makeText(getApplicationContext(), "Model not loaded!", Toast.LENGTH_SHORT).show();
//                return;
//            } else {
//                Toast.makeText(getApplicationContext(), "Model loaded!", Toast.LENGTH_SHORT).show();
//
////                            // load classifier from file
//            }
            double result = 0;
            Log.d(WEKA_TEST, cls_co + "");
            Log.d(WEKA_TEST, inst_co + "");
            result = cls_co.classifyInstance(inst_co);
            className = classVal.get(Double.valueOf(result).intValue());
            Log.d(WEKA_TEST, result + "   " + className);

            // pixelGrid.setGridValue(className);
            gridRoom.setGridValue(className);
            Log.d(WEKA_TEST, result + "  --2-- " + className);
            Log.i("dijkstars_activity", "dest_floor" +floor_num);

            int room_id_no=dbmanager.fetchNearByRoom(className,floor_num);
            int room_no=dbmanager.fetchMyRoom(room_id_no);
           // Toast.makeText(getApplicationContext(),"ClassName="+room_no,Toast.LENGTH_SHORT).show();
           // inital++;
            //if room_no is not in the current floor plot the previous position.
            className=null;
            Log.d("last_room",  selected_rooms[selected_rooms.length-1]+ "and room_no= "+room_no);
            Log.i("if_case_dij","Selected rooms on path"+ Arrays.toString(selected_rooms)+" Room no="+room_no+" Dest_room_no="+dest_room_no+" Stop room="+stop_room+" inital="+inital);

            if(selected_rooms[selected_rooms.length-1]==room_no){

//                if(room_no==dest_room_no && inital==1){
//                    startscan=true;
//                    Log.i("inside_if1"," Room no="+room_no+" Dest_room_no="+dest_room_no+" inital="+inital+"start_scan="+startscan);
//                }

//                else if(room_no==dest_room_no && inital!=1){
//                    startscan=false;
//                    Log.i("inside_if2"," Room no="+room_no+" Dest_room_no="+dest_room_no+" inital="+inital+"start_scan="+startscan);
//                }

//                if(room_no==dest_room_no || !startscan && inital>1  ){
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
                   // Toast.makeText(this,"You reached at the lift . Please go "+direction+"  to reach your destination room on floor number " +dest_floor,Toast.LENGTH_SHORT).show();
                    //navigation_status=false;
                    //setContentView(gridRoom);

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

    private void predictTheModelF2(int current_floor){
        try {


            //Toast.makeText(getApplicationContext(), "["+ attributeList+"]", Toast.LENGTH_SHORT).show();
            attributeList.clear();
            classVal.clear();
//            Attribute mac4 = new Attribute("mac4");
//            Attribute mac5 = new Attribute("mac5");
            Attribute mac1 = new Attribute("mac1");
            Attribute mac2 = new Attribute("mac2");
            Attribute mac3 = new Attribute("mac3");
            Attribute mac4 = new Attribute("mac4");
            Attribute mac5 = new Attribute("mac5");
            Attribute mac6 = new Attribute("mac6");
            Attribute mac7 = new Attribute("mac7");

            Attribute magx = new Attribute("magx");
            Attribute magy = new Attribute("magy");
            Attribute magz = new Attribute("magz");

            Attribute orix = new Attribute("orix");
            Attribute oriy = new Attribute("oriy");
            Attribute oriz = new Attribute("oriz");

//                    @attribute grid {'15,1','15,0','14,1','13,1','13,0','12,0','11,0','11,1','12,1','12,2','12,3','12,4','12,5','12,6','12,7','11,7','11,6','11,5','11,4','11,3','11,2'}
            classVal.add("5,24");
            classVal.add("6,24");
            classVal.add("6,23");
            classVal.add("6,22");
            classVal.add("6,21");
            classVal.add("6,20");
            classVal.add("6,19");
            classVal.add("6,18");
            classVal.add("6,17");
            classVal.add("6,16");


            // Toast.makeText(getApplicationContext(), "["+ classVal+"]", Toast.LENGTH_SHORT).show();
//            attributeList.add(mac4);
//            attributeList.add(mac5);
            attributeList.add(mac1);
            attributeList.add(mac2);
            attributeList.add(mac3);
            attributeList.add(mac4);
            attributeList.add(mac5);
            attributeList.add(mac6);
            attributeList.add(mac7);
            attributeList.add(magx);
            attributeList.add(magy);
            attributeList.add(magz);
//            attributeList.add(accx);
//            attributeList.add(accy);
//            attributeList.add(accz);
//            attributeList.add(rotx);
//            attributeList.add(roty);
//            attributeList.add(rotz);
            attributeList.add(orix);
            attributeList.add(oriy);
            attributeList.add(oriz);
            attributeList.add(new Attribute("gridValue", classVal));

            Instances data = new Instances("TestInstances", attributeList, 0);

            data.setClassIndex(data.numAttributes() - 1);




            //Toast.makeText(getApplicationContext(), "my magn["+ Arrays.toString(myValue)+"]", Toast.LENGTH_SHORT).show();
            //  0.656281,37.701157,-11.826157,'3,2'
            //  Toast.makeText(getApplicationContext(), "["+ Arrays.toString(myValue)+"]", Toast.LENGTH_SHORT).show();

            DenseInstance inst_co = new DenseInstance(data.numAttributes()) {
                {
                    //-82,-78,-82,-67,-55,-31.637161,9.728535,-18.218353,0.500622,-0.512263,9.255263,-0.030765,-0.004506,-0.605709,1.296407,0.055211,-0.054038,'15,1'
//                    setValue(mac4, fixedMACBLE.get("3C:61:05:11:D1:D2"));//11
//                    setValue(mac5, fixedMACBLE.get("3C:61:05:14:AF:12"));//12

                    setValue(mac1, fixedMACBLE.get("AC:67:B2:3C:C6:46"));
                    setValue(mac2, fixedMACBLE.get("3C:61:05:14:A7:C2"));
                    setValue(mac3, fixedMACBLE.get("3C:61:05:14:B5:0A"));
                    setValue(mac4, fixedMACBLE.get("3C:61:05:14:A7:72"));
                    setValue(mac5, fixedMACBLE.get("E0:E2:E6:0D:49:76"));
                    setValue(mac6, fixedMACBLE.get("E0:E2:E6:0D:39:FA"));
                    setValue(mac7, fixedMACBLE.get("E0:E2:E6:0B:7D:7A"));
                    setValue(magx, magneticX);
                    setValue(magy, magneticY);
                    setValue(magz, magneticZ);
//                    setValue(accx, accelerometerX);
//                    setValue(accy, accelerometerY);
//                    setValue(accz, accelerometerZ);
//                    setValue(rotx, rotationX);
//                    setValue(roty, rotationY);
//                    setValue(rotz, rotationZ);
                    setValue(orix, orientation[0]);
                    setValue(oriy, orientation[1]);
                    setValue(oriz, orientation[2]);
                }
            };
            // reference to dataset
            inst_co.setDataset(data);
            Log.d(WEKA_TEST, data + "");
            // Set instance's values for the attributes "latitude", "longitude", and
            // "pollutant concentration"

            // inst_co.setMissing(cluster);
            //  inst_co.setDataset(data);
            AssetManager assetManager = getAssets();
            //previous model=fifthmodelJuly7.model
            J48 cls_co = (J48) SerializationHelper.read(assetManager.open("mgeorgeblock_floor2_j48.model"));

//            if (cls_co == null) {
//                Toast.makeText(getApplicationContext(), "Model not loaded!", Toast.LENGTH_SHORT).show();
//                return;
//            } else {
//                Toast.makeText(getApplicationContext(), "Model loaded!", Toast.LENGTH_SHORT).show();
//
////                            // load classifier from file
//            }
            double result = 0;
            Log.d(WEKA_TEST, cls_co + "");
            Log.d(WEKA_TEST, inst_co + "");
            result = cls_co.classifyInstance(inst_co);
            className = classVal.get(Double.valueOf(result).intValue());
            Log.d(WEKA_TEST, result + "   " + className);

//            fos.write(String.valueOf(datacollector).getBytes());
//            datacollector++;
//            fos.write((".----->"+className).getBytes());
//            fos.write(("\n").getBytes());
//            if(datacollector>20)
//            {
//                //datacollector=1;
//                Toast.makeText(this, "Collected 20 values" + FILE_NAME,Toast.LENGTH_LONG).show();
//                fos.flush();
//                fos.close();
//            }


            // pixelGrid.setGridValue(className);
            gridRoom.setGridValue(className);
            Log.d(WEKA_TEST, result + "  --2-- " + className);
            Log.i("model","entered in predictTheModelF2 and predicted "+className);
           // Toast.makeText(this, "Saved to " + getFilesDir() + "/" + FILE_NAME,Toast.LENGTH_LONG).show();
            Log.i("floor2_status", "dest_floor" +floor_num);
            int room_id_no=dbmanager.fetchNearByRoom(className,floor_num);
            int room_no=dbmanager.fetchMyRoom(room_id_no);

            Log.i("floor2_status_room", "room_id_no= " +room_id_no+" and room_no= "+room_no);
            //if room_no is not in the current floor plot the previous position.
            className=null;
            Log.d("last_room_F2",  selected_rooms[selected_rooms.length-1]+ "and room_no= "+room_no);
            Log.i("if_case_floor2","Selected rooms on path"+ Arrays.toString(selected_rooms)+" Room no="+room_no+" Dest_room_no="+dest_room_no+" Stop room="+stop_room);
            if(selected_rooms[selected_rooms.length-1]==room_no){
                if(room_no==dest_room_no){
                    setContentView(gridRoom);
                    Log.i("nav_complete_f2"," Room no="+room_no+" Dest_room_no="+dest_room_no+" inital="+inital+"start_scan="+startscan);

                    // Toast.makeText(this,"Hurry, you reached the destination!!!", Toast.LENGTH_SHORT).show();
                    navigation_status=false;
                    btScanner.stopScan(leScanCallback);
//                    alertDialogBuilder.setTitle("Floor Change");
                    if(dest_room_no==107)
                        display_msg=ConstantsValue.proom_msg;
                    else if(dest_room_no==106)
                        display_msg=ConstantsValue.officeroom_msg;
                    alertDialogBuilder.setTitle("Navigation Completed");
                    alertDialogBuilder.setMessage("You reached at the destination."+display_msg);
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
                   String dir;
                    if(source_floor-dest_floor>0)
                        dir="downwards";
                    else
                        dir="upwards";
                   Toast.makeText(this,"You reached at the lift .Please go to floor number "+dest_floor, Toast.LENGTH_SHORT).show();
                    //navigation_status=false;
                    setContentView(gridRoom);
                    btScanner.stopScan(leScanCallback);
                    alertDialogBuilder.setTitle("Floor Change");
                    alertDialogBuilder.setMessage("You reached the exit point of current floor .Please go "+dir+"  to reach your destination room on floor number "+dest_floor+". Press ok once reach your destination floor");
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                           // Toast.makeText(getApplicationContext(),"You clicked over OK",Toast.LENGTH_SHORT).show();
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

                            Log.i("floor_lift_dij","Start_vertex= "+start_vertex);
                            DijkstrasAlgorithm.dijkstra(Adjacency_VARIABLE.get_adjacency_matrix(dest_floor), start_vertex, stop_room);
                            // Path_Result myPath = new Path_Result();
                            selected_pathpoints_new = Path_Result.myPath;
                            Log.i("selected_path", Arrays.toString(selected_pathpoints_new));
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
//            if(616==room_no){
//                Log.i("if_case","reached at destination-same floor"+Arrays.toString(selected_rooms));
//                  setContentView(gridRoom);
//                if(nav_status==0){
//                    Toast.makeText(this,"Hurry, you reached the destination!!!",Toast.LENGTH_SHORT).show();
//                    navigation_status=false;
//                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                }else if(className.equals("4,36")){
//                    Toast.makeText(this,"Reached at destination lift!!!",Toast.LENGTH_SHORT).show();
//
//                }else {
//                    Toast.makeText(this,"You reached the at lift .Please go to floor number "+dest_floor,Toast.LENGTH_SHORT).show();
//                    navigation_status=false;
//                    btScanner.stopScan(leScanCallback);
//                    alertDialogBuilder.setTitle("Floor Change");
//                    alertDialogBuilder.setMessage("You reached the exit point of current floor .Please go to floor number "+dest_floor+".Do you want to continue?");
//                    alertDialogBuilder.setCancelable(false);
//                    alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface arg0, int arg1) {
//                            Toast.makeText(getApplicationContext(),"You clicked over YES",Toast.LENGTH_SHORT).show();
//                            //load the dest floor map.
//                            //start vertex is the current entry at destination floor 1/ from left or from right
//                            //destination vertex is the destinastion room
//
//                            DijkstrasAlgorithm.dijkstra(Adjacency_VARIABLE.get_adjacency_matrix(), 1, stop_room, getApplicationContext());
//                            // Path_Result myPath = new Path_Result();
//                            selected_pathpoints_new = Path_Result.myPath;
//                            Log.i("selected_path",Arrays.toString(selected_pathpoints_new));
//                            Path_Result.refreshPath();
//                            pathpointsselected_new = new int[selected_pathpoints_new.length][2];
//                            roomsinpath_new=new int[selected_pathpoints_new.length];
//
//                            for(int i=0;i<selected_pathpoints_new.length;i++){
//                                roomsinpath_new[i]=dbmanager.fetchRoomNumber(selected_pathpoints_new[i]);
//                                pathpointsselected_new[i][0]=dbmanager.fetchXWayPointNumber(roomsinpath_new[i]);
//                                pathpointsselected_new[i][1]=dbmanager.fetchYWayPointNumber(roomsinpath_new[i]);
//                            }
//                            navigation_status=true;
//                            setFloorNum(dest_floor);
//                            Intent in=new Intent(DijkstrasActivity.this,DijkstrasActivity.class);
//
//                            in.putExtra("selected_points",selected_pathpoints_new);
//                            in.putExtra("selected_rooms",roomsinpath_new);
//                            in.putExtra("floor_num",dest_floor);
//                            in.putExtra("NAV_STATUS",0);//same floor
//                            Bundle mBundle = new Bundle();
//                            mBundle.putSerializable("selected_XY_points", pathpointsselected_new);
//                            in.putExtras(mBundle);
//                            startActivity(in);
//                            Log.i("dij_selected_points_n", Arrays.toString(selected_pathpoints_new));
//                            Log.i("dij_selected_rooms_n", Arrays.toString(roomsinpath_new));
//                            finish();
//
//                         }
//                    });
//
////                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
////                        @Override
////                        public void onClick(DialogInterface dialog, int which) {
////                            Toast.makeText(getApplicationContext(),"You clicked over No",Toast.LENGTH_SHORT).show();
////
////                        }
////                    });
//                    AlertDialog alertDialog = alertDialogBuilder.create();
//                    alertDialog.show();
//                }
//


   //         }
             else{
// Toast.makeText(this,"You are near "+room_id_no+"\n Current location: "+room_no,Toast.LENGTH_SHORT).show();
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