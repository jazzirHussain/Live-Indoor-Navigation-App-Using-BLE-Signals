package com.sample_project.mitsmap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SecondFragment extends Fragment {

    ImageView imageView;
    ImageView imageView2;
    int floor_number,des_floor_number;
    int lift_node_no=0;
    private DatabaseManger dbmanager;
    static boolean floor_check=false;
    static int[] selected_pathpoints;
    static int[][] pathpointsselected ;
    static int[] roomsinpath;
    boolean start_at_exit=false;
    SharedPreferences sharedpreference_location ;
    SharedPreferences.Editor editor ;
    AlertDialog.Builder alertDialogBuilder;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        dbmanager=new DatabaseManger(getActivity().getApplicationContext());
        BottomNavigationView navBar = getActivity().findViewById(R.id.bottom_navigation);
        navBar.setVisibility(View.GONE);
        sharedpreference_location = getActivity().getApplicationContext().getSharedPreferences("Nav_Location", Context.MODE_PRIVATE);
        editor = sharedpreference_location.edit();
        alertDialogBuilder= new AlertDialog.Builder(getActivity());
        String strtext = getArguments().getString("spinnerValue");
        Log.i("Data",strtext);

        int start_room=0,end_room;
        start_room=getArguments().getInt("start_point");

//        end_room = Integer.parseInt(room_vertices.get(strtext)+"");

        String[] parts = strtext.split(" - ", 2);
        String r_no = parts[0];
        String r_name = parts[1];
        Log.i("Data_SF","Start Location is "+start_room+" and Destination Location is "+r_no);
        end_room = Integer.parseInt(r_no);
        floor_number = dbmanager.getFloorNumber(start_room);
        Log.i("For_Dij_floor", "start_floor_num" + floor_number);
        des_floor_number = dbmanager.getFloorNumber(end_room);
        // des_floor_number=4;
        Log.i("For_Dij_floor", "des_floor_num" + des_floor_number);
        int start_loc_num=dbmanager.fetchRoomWayNumber(start_room);
        int dest_loc_num=dbmanager.fetchRoomWayNumber(end_room);
        editor.putInt("start_location",start_room );

        editor.putInt("Start_floor",floor_number);
        editor.putInt("destination_location", dest_loc_num);
        editor.putInt("Dest_Floor",des_floor_number);
        editor.putInt("Destination_Room",end_room);
        editor.commit();

        Log.i("Dest_sh1", "start_loc"+start_loc_num+"--dest "+dest_loc_num);

        if(start_loc_num!=dest_loc_num){
            DijkstrasAlgorithm.dijkstra(Adjacency_VARIABLE.get_adjacency_matrix(des_floor_number), start_loc_num, dest_loc_num);
            // Path_Result myPath = new Path_Result();
            selected_pathpoints = Path_Result.myPath;
            Log.i("selected_path_SF", Arrays.toString(selected_pathpoints));
            Path_Result.refreshPath();
            pathpointsselected = new int[selected_pathpoints.length][2];
            roomsinpath=new int[selected_pathpoints.length];
            for(int i=0;i<selected_pathpoints.length;i++){
                roomsinpath[i]=dbmanager.fetchRoomNumber(selected_pathpoints[i],des_floor_number);
                pathpointsselected[i][0]=dbmanager.fetchXWayPointNumber(roomsinpath[i]);
                pathpointsselected[i][1]=dbmanager.fetchYWayPointNumber(roomsinpath[i]);
            }
            Intent in=new Intent(getActivity().getApplicationContext(),DijkstrasActivity.class);
            in.putExtra("selected_points",selected_pathpoints);
            in.putExtra("selected_rooms",roomsinpath);
            in.putExtra("floor_num",floor_number);
            in.putExtra("NAV_STATUS",0);//same floor
            Bundle mBundle = new Bundle();
            mBundle.putSerializable("selected_XY_points", pathpointsselected);
            in.putExtras(mBundle);
            startActivity(in);
            Log.i("dij_selected_points_s1", Arrays.toString(selected_pathpoints));
            Log.i("dij_selected_rooms_s1", Arrays.toString(roomsinpath));
//                finish();
        }else{
            Toast.makeText(getActivity().getApplicationContext(),"Same start and stop location" , Toast.LENGTH_SHORT).show();
        }

        return inflater.inflate(R.layout.fragment_second, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    public String convertToFormat(String src){
        src = src.toLowerCase();
        src = src.replaceAll("-","");
        src = src.replaceAll("\\s","_");
        Log.d("src: ",src);
        return src;

    }
}