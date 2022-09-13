package com.sample_project.mitsmap;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.InputStream;
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

import static android.content.Context.SENSOR_SERVICE;

public class NavigationFragment extends Fragment implements SensorEventListener {
    HashMap<String, Integer> fixedMACBLE = new HashMap<String, Integer>();
    HashMap<String, Integer> BLE_FLOOR = new HashMap<String, Integer>();
    Button btn_location,btn_viewmaps;
    // Storage for Sensor readings
    private static float[] mGeomagnetic = null;
    private long pressedTime;
    static String className=null;
    private Handler handler = new Handler();
    static ArrayList<Attribute> attributeList = new ArrayList<Attribute>();
    public static int x;
    public static int y;
    static int GridX,GridY;
    static boolean floor_check=false;
    //weka
    private static final String WEKA_TEST = "WekaTest";
    static ArrayList<String> classVal = new ArrayList<String>();
    DecimalFormat decimalFormatter;
    BluetoothManager btManager;
    BluetoothAdapter btAdapter;
    BluetoothLeScanner btScanner;
    boolean scanning = false;
    private SensorManager sensorManager;
    private DatabaseManger dbmanager;
    ArrayList<String> listBluetoothDevice;
    Map<String, String> device_rssi_dynamic ;
    Map<String, Integer> device_scan_no;

    //file
    private static final String FILE_NAME = "walk_analysis.txt";
    private static int datacollector=1;
    FileOutputStream fos;
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


    private static int collectionCount=0;
    private Context mContext;
    static String rssi_old;
    TextView textView,textView1;
    Spinner dropdown;
    Button btn_nav;
    String[] floor = new String[] {
            "Entrance", "Reception", "Library", "Lift 1","Lift 2", "Office", "Principal Room"
    };
    int x_flag = 1,scan_val = 0;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       this.mContext = context;
     //   dbmanager=new DatabaseManger(getActivity());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {


        decimalFormatter = new DecimalFormat("#");
        decimalFormatter.setMaximumFractionDigits(8);
        btManager = (BluetoothManager) getActivity().getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE);
        btAdapter = btManager.getAdapter();
        btScanner = btAdapter.getBluetoothLeScanner();

        dbmanager=new DatabaseManger(getActivity().getApplicationContext());
        sensorManager = (SensorManager) getActivity().getApplicationContext().getSystemService(SENSOR_SERVICE);
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
        for (Sensor sensor : sensorList) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
        }
        listBluetoothDevice = new ArrayList<>();
        device_rssi_dynamic = new HashMap<String, String>();
        device_scan_no = new HashMap<String, Integer>();
        resetBLEReadings();
        resetBLEFLOORReadings();
        rotation = new float[9];
        inclination = new float[9];
        orientation = new float[3];

        View view = inflater.inflate(R.layout.fragment_navigation,
                container, false);

//        btn_location = view.findViewById(R.id.btn_my_location);
//        btn_viewmaps=view.findViewById(R.id.btn_view_maps);
        textView=view.findViewById(R.id.tv_start);
        textView1=view.findViewById(R.id.textView4);
        dropdown=view.findViewById(R.id.spinner_waypoints);
        btn_nav=view.findViewById(R.id.btn_nav);
        Log.i(WEKA_TEST,  "clicked on button");

        boolean stat=scanBLE(container,view);
        //scanBLE();

//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            public void run() {
//                if(this!=null ){
//                    Log.i("Values3", "Message");
//                    textView.setText("Entrance");
//        if(stat) {
//            textView1.setVisibility(View.VISIBLE);
//            dropdown.setVisibility(View.VISIBLE);
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(container.getContext(), android.R.layout.simple_spinner_item, floor);
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            dropdown.setAdapter(adapter);
//            Button button = (Button) view.findViewById(R.id.button1);
//
//            button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    FragmentManager fm = getFragmentManager();
//                    FragmentTransaction ft = fm.beginTransaction();
//                    SecondFragment llf = new SecondFragment();
//                    Bundle args = new Bundle();
//                    args.putString("spinnerValue", dropdown.getSelectedItem().toString());
//
//                    llf.setArguments(args);
//                    ft.replace(((ViewGroup) getView().getParent()).getId(), llf);
//                    ft.commit();
//                }
//            });
////                }
////            }
////        }, 2000);
//        }else{
//            Log.i(WEKA_TEST,  "unscussful scan");
//        }
        return view;

    }
    private boolean scanBLE(ViewGroup container,View view) {
        System.out.println("start scanning");
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
        if (!scanning) {

            // Stops scanning after a predefined scan period.
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    scanning = false;
                    btScanner.stopScan(leScanCallback);
                    // Log.i("Log_scan_stop", "Arraylist " + rssiArrayList);
//

                    ArrayList resultBle =  getListBluetoothDevice();
                    if(resultBle!=null && !resultBle.isEmpty()){
                        Map<String, String> resultRssiMap = getDeviceRssiDynamic();
                        Map<String, Integer> resultScanNumMap = getDeviceScanNo();

                        Log.i("My_final_call_7_p", "scan finished--" + getListBluetoothDevice() + "*" +
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
                                Log.i("c_floor_check", fixedMACBLE + "");

                                int current_floor=checkTheFloor();
                                Log.i("c_floor_check", "You are on floor : "+current_floor);

                                switch (current_floor){
                                    case 1:predictTheModelF1(current_floor,container,view);break;
                                    case 2:predictTheModelF2(current_floor,container,view);break;
                                }

                                resultBle.clear();
                                resultRssiMap.clear();
                            }
                            // resultBle.clear();
                        }
                    }else{
                        textView.setText("Searching for nearbydevice");

                        scanBLE(container, view);
                    }

                    // Repeat.
                    // scanning = true;

                    //  handler.postDelayed(this, ConstantsValue.SCAN_PERIOD);
//                    else {
//                        Log.i("log_ble", "inital run");
//                        scanBLE();
//                    }
                }
            }, 2000);

            scanning = true;
            Log.i("Log_scan_3", "scan finished" + scanning);
            try {
                btScanner.startScan(leScanCallback);
                //
                // ble_class.listBluetoothDevice.clear();
            } catch (Exception e) {
                Log.i("Log_scan_4", "Exception" + e);
            }


        return true;
        } else {

            scanning = false;
            Log.i("Log_scan_5", "scan finished" + scanning);
            btScanner.stopScan(leScanCallback);
            return false;
        }
        //   handler.postDelayed((Runnable) this, 3000);

//            }
//        },3000);
        //Start the same Activity
    }
    private ScanCallback leScanCallback = new ScanCallback() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            BluetoothDevice myDevice = result.getDevice();
            Method getUuidsMethod = null;

            Log.i("lescan", "txpower" + myDevice.getType() + "Name: " + result.getDevice().getAddress() + " --rssi:   " + result.getRssi());

            myDevice.getUuids();
            myDevice.getBluetoothClass();
            myDevice.getAddress();

            int rssi = result.getRssi();
            String resultname = result.getDevice().getAddress();
////
            if (!(resultname == null)) {
                // tv_scan.append("Name: " + result.getDevice().getName() + " --rssi:  " + rssi + "\n");
                Log.i("SCAN", "txpower" + myDevice.getType() + "Name: " + result.getDevice().getName() + " --rssi:   " + rssi);
                addBluetoothDevice(myDevice,resultname, rssi);
                //  Log.i("add_device", "Status: " + add_status);
            }

//            myblelist.add("Device Name: " + result.getDevice().getName() + " rssi: " + result.getRssi() + "\n");
//            lv.setAdapter(adapter);
            // auto scroll for text view
//            final int scrollAmount = tv_scan.getLayout().getLineTop(tv_scan.getLineCount()) - tv_scan.getHeight();
////            // if there is no need to scroll, scrollAmount will be <=0
//            if (scrollAmount > 0)
//                tv_scan.scrollTo(0, scrollAmount);


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
                        /*int rssi_old=device_rssi_dynamic.get(device.toString());
                        int rssi_new=rssi+rssi_old;
                        device_rssi_dynamic.put(device.toString(),rssi_new);
                        Log.i("Log_scan_",getNoofScan()+"--"+rssi_old+"====>"+rssi_new+"%%%"+device.toString());*/
                    //Toast.makeText(getApplicationContext(),"Device already in the list"+device_rssi_dynamic,Toast.LENGTH_SHORT).show();


                }
            }


        }
    };
    public Map<String,String> getDeviceRssiDynamic(){
        if(!device_rssi_dynamic.isEmpty()){
            Log.i("getDeviceRssiDynamic",""+device_rssi_dynamic);
            return device_rssi_dynamic;
        }else{
            return null;
        }

    }
    public Map<String,Integer> getDeviceScanNo(){
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
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       // return inflater.inflate(R.layout.fragment_navigation, container, false);
    }
    private void predictTheModelF1(int current_floor, ViewGroup container, View view) {
        try {


            //Toast.makeText(getApplicationContext(), "["+ attributeList+"]", Toast.LENGTH_SHORT).show();
            attributeList.clear();
            classVal.clear();
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
//            Attribute accx = new Attribute("accx");
//            Attribute accy = new Attribute("accy");
//            Attribute accz = new Attribute("accz");
//            Attribute rotx = new Attribute("rotx");
//            Attribute roty = new Attribute("roty");
//            Attribute rotz = new Attribute("rotz");
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
            //Toast.makeText(getApplicationContext(), "["+ attributeList+"]", Toast.LENGTH_SHORT).show();
            Instances data = new Instances("TestInstances", attributeList, 0);

            data.setClassIndex(data.numAttributes() - 1);


            //Toast.makeText(getApplicationContext(), "my magn["+ Arrays.toString(myValue)+"]", Toast.LENGTH_SHORT).show();
            //  0.656281,37.701157,-11.826157,'3,2'
            //  Toast.makeText(getApplicationContext(), "["+ Arrays.toString(myValue)+"]", Toast.LENGTH_SHORT).show();

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
            //Classifier cls_co;
            AssetManager assetManager = getContext().getAssets();
            InputStream is = assetManager.open("mgeorgeblock_floor1_j48.model");
            //previous model=updated_model4(j48).model
            J48 cls_co = (J48) weka.core.SerializationHelper.read(is);

            // IBk cls_co = (IBk) SerializationHelper.read(assetManager.open("iBK_combined_data.model"));

            if (cls_co == null) {
                Toast.makeText(getActivity(), "Model not loaded!", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Toast.makeText(getActivity(), "Model loaded!", Toast.LENGTH_SHORT).show();

//                            // load classifier from file
            }
            double result = 0;
            Log.d(WEKA_TEST, cls_co + "");
            Log.d(WEKA_TEST, inst_co + "");
            result = cls_co.classifyInstance(inst_co);
            className = classVal.get(Double.valueOf(result).intValue());
            Log.d(WEKA_TEST, result + "   " + className);

            // pixelGrid.setGridValue(className);
            Log.i("c_floor", result + "  --2-- " + className);
            //need to find the floor also
            // int current_floor=checkTheFloor();
            Log.i("c_floor", "current_floor" +current_floor);
//            int room_id_no=dbmanager.fetchNearByRoom(className,current_floor);
//            int room_no=dbmanager.fetchMyRoom(room_id_no);
          //  Log.i("c_floor", "You are near "+room_id_no+"\n Current location: "+room_no);
            //Toast.makeText(this,"You are near "+room_id_no+"\n Current location: "+room_no,Toast.LENGTH_SHORT).show();
            //setContentView(pixelGrid);
            btScanner.stopScan(leScanCallback);
//            Intent intent_explore = new Intent(getApplicationContext(), StartNavigation.class);
//            intent_explore.putExtra("cur_room_no", room_no);
//            // intent_explore.putExtra("current_loc",location_name);
//
//            startActivity(intent_explore);
//            finish();
            int room_id_no=dbmanager.fetchNearByRoom(className,current_floor);
            int room_no=dbmanager.fetchMyRoom(room_id_no);
            textView.setText(room_no+" - "+dbmanager.fetchRoomName(room_no));
            textView1.setVisibility(View.VISIBLE);
            dropdown.setVisibility(View.VISIBLE);
            btn_nav.setVisibility(View.VISIBLE);
            int[] pointsName=dbmanager.allRooms();
            Log.i("All_rooms", Arrays.toString(pointsName));
            Integer[] newArray = new Integer[pointsName.length];
            ArrayList ae = new ArrayList<>();;
            String[] rooms_with_name=new String[pointsName.length];
            int i = 0;
            for (int value : pointsName) {
                newArray[i++] = Integer.valueOf(value);
                ae.add(Integer.valueOf(value)+" - " +dbmanager.fetchRoomName(Integer.valueOf(value)));

            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(container.getContext(), android.R.layout.simple_spinner_item, ae);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dropdown.setAdapter(adapter);

            btn_nav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    SecondFragment llf = new SecondFragment();
                    Bundle args = new Bundle();
                    args.putString("spinnerValue", dropdown.getSelectedItem().toString());
                    args.putInt("start_point",room_no);

                    llf.setArguments(args);
                    ft.replace(((ViewGroup) getView().getParent()).getId(), llf);
                    ft.commit();
                }
            });

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        attributeList.clear();
        classVal.clear();
        className=null;
        current_floor=0;

    }

    private void predictTheModelF2(int current_floor, ViewGroup container, View view) {
        try {


            //Toast.makeText(getApplicationContext(), "["+ attributeList+"]", Toast.LENGTH_SHORT).show();
            attributeList.clear();
            classVal.clear();
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
//            Attribute accx = new Attribute("accx");
//            Attribute accy = new Attribute("accy");
//            Attribute accz = new Attribute("accz");
//            Attribute rotx = new Attribute("rotx");
//            Attribute roty = new Attribute("roty");
//            Attribute rotz = new Attribute("rotz");
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
            //Toast.makeText(getApplicationContext(), "["+ attributeList+"]", Toast.LENGTH_SHORT).show();
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
            //Classifier cls_co;
            AssetManager assetManager = getContext().getAssets();
            InputStream is = assetManager.open("mgeorgeblock_floor2_j48.model");
            //previous model=fifthmodelJuly7.model
            J48 cls_co = (J48) weka.core.SerializationHelper.read(is);

            // IBk cls_co = (IBk) SerializationHelper.read(assetManager.open("iBK_combined_data.model"));

            if (cls_co == null) {
                Toast.makeText(getActivity(), "Model not loaded!", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Toast.makeText(getActivity(), "Model loaded!", Toast.LENGTH_SHORT).show();

//                            // load classifier from file
            }
            double result = 0;
            Log.d(WEKA_TEST, cls_co + "");
            Log.d(WEKA_TEST, inst_co + "");
            result = cls_co.classifyInstance(inst_co);
            className = classVal.get(Double.valueOf(result).intValue());
            Log.d(WEKA_TEST, result + "   " + className);
////            fos.write(String.valueOf(datacollector).getBytes());
////            Toast.makeText(getActivity(), "Data" + datacollector,Toast.LENGTH_LONG).show();
////            datacollector++;
//            fos.write((".----->"+className).getBytes());
//            fos.write(("\n").getBytes());
//            if(datacollector>7)
//            {
//                //datacollector=1;
//                Toast.makeText(getActivity(), "Collected 7 values" + FILE_NAME,Toast.LENGTH_LONG).show();
//                fos.flush();
//                fos.close();
//            }
            // pixelGrid.setGridValue(className);
            Log.i("c_floor", result + "  --2-- " + className);
            //need to find the floor also
            // int current_floor=checkTheFloor();
            Log.i("c_floor", "current_floor" +className);

            int room_id_no=dbmanager.fetchNearByRoom(className,current_floor);
            int room_no=dbmanager.fetchMyRoom(room_id_no);
            Log.i("c_floor", "You are near "+room_id_no+" ==>>Current location: "+room_no);
            textView.setText(room_no+" - "+dbmanager.fetchRoomName(room_no));



            // Toast.makeText(this,"You are near "+room_id_no+"\n Current location: "+room_no,Toast.LENGTH_SHORT).show();
            //setContentView(pixelGrid);
            btScanner.stopScan(leScanCallback);
            textView1.setVisibility(View.VISIBLE);
            dropdown.setVisibility(View.VISIBLE);
            btn_nav.setVisibility(View.VISIBLE);
            int[] pointsName=dbmanager.allRooms();
            Log.i("All_rooms", Arrays.toString(pointsName));
            Integer[] newArray = new Integer[pointsName.length];
            ArrayList ae = new ArrayList<>();;
            String[] rooms_with_name=new String[pointsName.length];
            int i = 0;
            for (int value : pointsName) {
                newArray[i++] = Integer.valueOf(value);
                ae.add(Integer.valueOf(value)+" - " +dbmanager.fetchRoomName(Integer.valueOf(value)));

            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(container.getContext(), android.R.layout.simple_spinner_item, ae);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dropdown.setAdapter(adapter);

            btn_nav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    SecondFragment llf = new SecondFragment();
                    Bundle args = new Bundle();
                    args.putString("spinnerValue", dropdown.getSelectedItem().toString());
                    args.putInt("start_point",room_no);

                    llf.setArguments(args);
                    ft.replace(((ViewGroup) getView().getParent()).getId(), llf);
                    ft.commit();
                }
            });
//            Intent intent_explore = new Intent(getActivity().getApplicationContext(), StartNavigation.class);
//            intent_explore.putExtra("initalXY",className);
//            intent_explore.putExtra("cur_room_no", room_no);
//            // intent_explore.putExtra("current_loc",location_name);
//
//            startActivity(intent_explore);
//            finish();


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        attributeList.clear();
        classVal.clear();
        className=null;
    }

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
    private void resetBLEFLOORReadings() {
        BLE_FLOOR.clear();
        BLE_FLOOR.put("AC:67:B2:3C:C6:46",1);//esp32 1
        BLE_FLOOR.put("3C:61:05:14:A7:C2",1);//esp32 2
        BLE_FLOOR.put("3C:61:05:11:C6:02",0);//esp32 3
        BLE_FLOOR.put("3C:61:05:11:D0:02",0);//esp32 4
        BLE_FLOOR.put("3C:61:05:11:CE:82",0);//esp32 5
        BLE_FLOOR.put("3C:61:05:11:D0:EE",0);//esp32 6
        BLE_FLOOR.put("AC:67:B2:3C:CC:1A",0);//esp32 7
        BLE_FLOOR.put("3C:61:05:11:C7:FE",0);//esp32 8

        BLE_FLOOR.put("3C:61:05:11:B5:EA",0);//esp32 9

        BLE_FLOOR.put("3C:61:05:14:B1:BA",0);//esp32 10
        BLE_FLOOR.put("3C:61:05:11:D1:D2",0);//esp32 11
        BLE_FLOOR.put("3C:61:05:14:AF:12",0);//esp32 12
        BLE_FLOOR.put("3C:61:05:11:CD:4E",0);//esp32 13
        BLE_FLOOR.put("3C:61:05:14:B5:0A",1);//esp32 14


        BLE_FLOOR.put("3C:61:05:11:D0:36",0);//esp32 17
        BLE_FLOOR.put("AC:67:B2:3C:CB:FA",0);//esp32 18
        BLE_FLOOR.put("3C:61:05:11:C8:8A",0);//esp32 19
        BLE_FLOOR.put("3C:61:05:14:A7:72",2);//esp32 20
        BLE_FLOOR.put("E0:E2:E6:0D:49:76",2); //	-ESP21 -[05]
        BLE_FLOOR.put("E0:E2:E6:0D:39:FA",2);	//-ESP23 -[06]
        BLE_FLOOR.put("E0:E2:E6:0B:7D:7A",2);	//-ESP24 -[07]
//        BLE_FLOOR.put("D0:5F:64:52:12:BB",4);//NODE1
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
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}