package com.sample_project.mitsmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    BluetoothAdapter bluetoothAdapter;
    BluetoothLeScanner bluetoothLeScanner;
    String TAG = getClass().getSimpleName();
BottomNavigationView bottomNavigationView;

HomeFragment homeFragment=new HomeFragment();
NavigationFragment navigationFragment=new NavigationFragment();
AboutFragment aboutFragment=new AboutFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android10BluetoothPermission();
        DatabaseManger dbmanager=new DatabaseManger(MainActivity.this);

        bottomNavigationView=findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.menu_item_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();
                        return true;
                    case R.id.menu_item_navigation:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,navigationFragment).commit();
                        return true;
                    case R.id.menu_item_about:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,aboutFragment).commit();
                        return true;
                }
                return false;
            }
        });
    }

    private void android10BluetoothPermission () {

        boolean isLocationPermissionRequired = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
        boolean isBGLocationAccessNotGranted = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            isBGLocationAccessNotGranted = ContextCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED;
        }
        boolean isLocationAccessNotGranted = ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED;

        if (isLocationPermissionRequired && isBGLocationAccessNotGranted && isLocationAccessNotGranted) {
            requestLocationPermission();
        }
        else {
            setUpBluetooth();
        }
    }
    private void requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder
                        .setTitle("Location Permission")
                        .setMessage("Please give location permission")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                makeLocationRequest();
                                //startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        });
                alertDialogBuilder.show();
            } else {
                makeLocationRequest();
            }
        }
    }

    private void makeLocationRequest() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 101);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode) {
            case 101:
                if (grantResults.length != 0 && grantResults[0] == 0) {
                    setUpBluetooth();
                }
                break;
            default:
                Toast.makeText(this, "on request permission", Toast.LENGTH_LONG).show();
        }
//
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantsValue.REQUEST_ENABLE_BT && resultCode != Activity.RESULT_OK) {
            enableBluetooth(this, BluetoothAdapter.getDefaultAdapter());
        }
    }
    private void setUpBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBluetooth(bluetoothAdapter);
        enableBluetooth(this, bluetoothAdapter);

        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
        //scanBLEDevices(bluetoothLeScanner);
    }

    // SOURCE FOR SET UP BLUETOOTH: https://developer.android.com/guide/topics/connectivity/bluetooth/setup#java

    // GET BLUETOOTH ADAPTER
    private void checkBluetooth (BluetoothAdapter bluetoothAdapter) {
        if (bluetoothAdapter == null) {
            Log.d(TAG, "Bluetooth: " + "bluetooth adapter is null");
        }
        else {
            Log.d(TAG, "Bluetooth: " + "bluetooth is adapter not null");
        }
    }

    // ENABLE BLUETOOTH
    private void enableBluetooth (Activity activity, BluetoothAdapter bluetoothAdapter) {
        Log.d(TAG, "Bluetooth: " + "bluetooth adapter is enabled: " + bluetoothAdapter.isEnabled());
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, ConstantsValue.REQUEST_ENABLE_BT);
            Log.d(TAG, "Bluetooth: " + "bluetooth is enabled");
        }
        else {
            Log.d(TAG, "Bluetooth: " + "bluetooth is already enabled");
        }
    }
}