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
//        editor.putInt("start_location",start_loc_num );
//        editor.putInt("destination_location", dest_loc_num);
//        editor.putInt("Start_floor",floor_number);
//        editor.putInt("Dest_Floor",des_floor_number);
//        editor.putInt("Destination_Room",dest_loc);
//        editor.commit(); // commit changes

     Log.i("Dest_sh1", "start_loc"+start_loc_num+"--dest "+dest_loc_num);

        if (floor_number == des_floor_number){ //same floor
            floor_check = true;

            //Toast.makeText(getApplicationContext(), "Your Source and Destination are on same floor", Toast.LENGTH_SHORT).show();
        }else {
            floor_check = false;
        }
            //Toast.makeText(getApplicationContext(), "Source and Destination on different floor", Toast.LENGTH_SHORT).show();

            int start_floor=1,end_floor;
//        end_floor = Integer.parseInt(room_floor.get(strtext)+"");
        if(floor_check) //same floor
        {
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

        }else{
            // Toast.makeText(getApplicationContext(),"Source and destination on different floor",Toast.LENGTH_SHORT).show();
//                    show the path from source to the shortest lift /stair in the same floor.
//                    1.check for the lifts/stair in the floor.for that retrive from the room table
            ArrayList<String> liftsinfloor=dbmanager.fetchLiftStairinFloor(floor_number);
            int[] floor_dest=new int[liftsinfloor.size()];
            Log.i("floor_lift",liftsinfloor.size()+" is the size");
            for (int i = 0; i < liftsinfloor.size();i++)
            {
                String tosplit=liftsinfloor.get(i);
                Log.i("floor_lift",tosplit+"");
                String[] parts1 = tosplit.split(",");
                Log.i("floor_lift",Arrays.toString(parts1)+" second part after split "+parts1[1]);
                parts1[1]=parts1[1].replace(" ","");
                parts1[1]=parts1[1].trim();
                floor_dest[i]=Integer.parseInt(parts1[1]);
            }

            Log.i("floor_lift",Arrays.toString(floor_dest));
            //get the corresponding waypoint number for floor_dest
            for(int i=0;i<floor_dest.length;i++){
                floor_dest[i]=dbmanager.fetchRoomWayNumber(floor_dest[i]);
            }
            for(int element=0;element<floor_dest.length;element++) {
                if (floor_dest[element] == start_loc_num) {
                    start_at_exit = true;
                    break;
                }
            }

            Log.i("Lift_number", "lift_node_no"+lift_node_no);
            if (start_at_exit) {
                String direction="downwards";
                if(floor_number-des_floor_number>0)
                {
                    direction="downwards";
                    lift_node_no=6;
                }
                else
                {
                    direction="upwards";
                    lift_node_no=0;
                }
                Log.i("START_AT_EXIT", "start_at_exit=You are at exit point of current floor");
                alertDialogBuilder.setTitle("Floor Change");
                alertDialogBuilder.setMessage("Please go "+direction+"  to reach your destination room on floor number " + des_floor_number+". Press on OK Button once you reach your destination floor");
                alertDialogBuilder.setCancelable(true);
                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
//                                                  Log.i("START_AT_EXIT_in", "start_at_exit=" + start_at_exit);
                        DijkstrasAlgorithm.dijkstra(Adjacency_VARIABLE.get_adjacency_matrix(des_floor_number), 0, dest_loc_num);
                        // Path_Result myPath = new Path_Result();
                        selected_pathpoints = Path_Result.myPath;
                        Log.i("selected_path_diff",Arrays.toString(selected_pathpoints)+" in the destination floor "+des_floor_number);
                        Path_Result.refreshPath();
                        pathpointsselected = new int[selected_pathpoints.length][2];
                        roomsinpath=new int[selected_pathpoints.length];
                        for(int i=0;i<selected_pathpoints.length;i++){
                            roomsinpath[i]=dbmanager.fetchRoomNumber(selected_pathpoints[i],des_floor_number);
                            pathpointsselected[i][0]=dbmanager.fetchXWayPointNumber(roomsinpath[i]);
                            pathpointsselected[i][1]=dbmanager.fetchYWayPointNumber(roomsinpath[i]);
                        }
                        Intent in = new Intent(getActivity().getApplicationContext(), DijkstrasActivity.class);
                        in.putExtra("selected_points", selected_pathpoints);
                        in.putExtra("selected_rooms", roomsinpath);
                        in.putExtra("floor_num", des_floor_number);//start_floor
                        in.putExtra("NAV_STATUS", 1);//different floor
                        in.putExtra("start_at_exit", true);// if start at the exit point of source floor=true else false,
                        Bundle mBundle = new Bundle();
                        mBundle.putSerializable("selected_XY_points", pathpointsselected);
                        in.putExtras(mBundle);
                        startActivity(in);
                        Log.i("dif_selected_points", Arrays.toString(selected_pathpoints));
                        Log.i("dif_selected_rooms", Arrays.toString(roomsinpath));
                        selected_pathpoints = null;
                        roomsinpath = null;

                    }

                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


            } else {
                selected_pathpoints = ShortestPath.dijkstra(Adjacency_VARIABLE.get_adjacency_matrix(floor_number), start_loc_num, floor_dest);
                Log.i("selected_path", Arrays.toString(selected_pathpoints));
                Path_Result.refreshPath();
                pathpointsselected = new int[selected_pathpoints.length][2];
                roomsinpath = new int[selected_pathpoints.length];
                for (int i = 0; i < selected_pathpoints.length; i++) {
                    roomsinpath[i] = dbmanager.fetchRoomNumber(selected_pathpoints[i],floor_number);
                    pathpointsselected[i][0] = dbmanager.fetchXWayPointNumber(roomsinpath[i]);
                    pathpointsselected[i][1] = dbmanager.fetchYWayPointNumber(roomsinpath[i]);
                }
                Log.i("dij_selected_points_s3", Arrays.toString(selected_pathpoints));
                Log.i("dij_selected_rooms_s3", Arrays.toString(roomsinpath));

                Log.i("START_AT_EXIT_in", "start_at_exit=" + start_at_exit);
                Intent in = new Intent(getActivity().getApplicationContext(), DijkstrasActivity.class);
                in.putExtra("selected_points", selected_pathpoints);
                in.putExtra("selected_rooms", roomsinpath);
                in.putExtra("floor_num", floor_number);//start_floor
                in.putExtra("NAV_STATUS", 1);//different floor
                in.putExtra("start_at_exit", false);// if start at the exit point of source floor=true else false,
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("selected_XY_points", pathpointsselected);
                in.putExtras(mBundle);
                startActivity(in);
                selected_pathpoints = null;
                roomsinpath = null;

            }
//
        }

   return inflater.inflate(R.layout.fragment_second, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        JSONObject data = null;
//        String src = null;
//        try {
//            data = new JSONObject(getActivity().getIntent().getStringExtra("eventData"));
//
//            String venue = data.getString("building");
//            if(venue.equals("Flea Bazaar")||venue.equals("Food Fest")||venue.equals("Main Entrance")||venue.equals("Fab Hub")||venue.equals("Digital Hub")){
//                src = convertToFormat(data.getString("building"));
//            }else{
//                src = convertToFormat(data.getString("venue"));
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        Log.i("src",src);
//        int markerId = getActivity().getResources().getIdentifier(src, "id", getActivity().getApplicationContext().getPackageName());
////        GifImageView i_but = (GifImageView) view.findViewById(markerId);
////        i_but.setVisibility(View.VISIBLE);
//        ImageView img;
//        //img = view.findViewById(R.id.fragMap);
//
//        int drawableId = getActivity().getResources().getIdentifier(src, "drawable", getActivity().getApplicationContext().getPackageName());
//      //  img.setImageResource(drawableId);
//        JSONObject finalData = data;
////        i_but.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////
////                if(getActivity().getIntent().hasExtra("eventData")){
////
////                    final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
////                    View bottomSheetView = LayoutInflater.from(getActivity().getApplicationContext())
////                            .inflate(
////                                    R.layout.activity_bottomdialog,
////                                    (LinearLayout)view.findViewById(R.id.bottomSheetContainer));
////                    bottomSheetDialog.setContentView( bottomSheetView);
////                    bottomSheetDialog.show( );
////                    TextView name,desc,time,room;
////                    name = bottomSheetDialog.findViewById(R.id.textView2);
////                    desc = bottomSheetDialog.findViewById(R.id.textView3);
////                    time = bottomSheetDialog.findViewById(R.id.textView4);
////                    room = bottomSheetDialog.findViewById(R.id.textView5);
////                    CharSequence c = "sdds";
////                    try {
////                        name.setText(finalData.getString("title"));
////                        desc.setText(finalData.getString("content"));
////                        time.setText(finalData.getString("date"));
////                        room.setText(finalData.getString("venue"));
////                    } catch (JSONException e) {
////                        e.printStackTrace();
////                    }
////
////                }else{
////                    Toast toast= Toast.makeText(getActivity().getApplicationContext(),"Select an Event", Toast.LENGTH_SHORT);
////                }
////            }
////        }
////        );
//
//
//
//

    }
    public String convertToFormat(String src){
        src = src.toLowerCase();
        src = src.replaceAll("-","");
        src = src.replaceAll("\\s","_");
        Log.d("src: ",src);
        return src;

    }
}