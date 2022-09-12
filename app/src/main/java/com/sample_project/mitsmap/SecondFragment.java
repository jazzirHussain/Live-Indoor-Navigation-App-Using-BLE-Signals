package com.sample_project.mitsmap;

import android.content.Context;
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

import java.util.Arrays;
import java.util.HashMap;

public class SecondFragment extends Fragment {

    ImageView imageView;
    ImageView imageView2;
    int floor_number,des_floor_number;
    private DatabaseManger dbmanager;
    static boolean floor_check=false;
    static int[] selected_pathpoints;
    static int[][] pathpointsselected ;
    static int[] roomsinpath;
    SharedPreferences sharedpreference_location ;
    SharedPreferences.Editor editor ;
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
        String strtext = getArguments().getString("spinnerValue");
        Log.i("Data",strtext);
//        HashMap<String,Integer> room_vertices=new HashMap<String,Integer>();
//        room_vertices.put("Entrance",0);
//        room_vertices.put("Reception",2);
//        room_vertices.put("Library",4);
//        room_vertices.put("Lift 1",6);
//        room_vertices.put("Lift 2",0);
//        room_vertices.put("Office",3);
//        room_vertices.put("Principal Room",5);
//        HashMap<String,Integer> room_floor=new HashMap<String,Integer>();
//        room_floor.put("Entrance",1);
//        room_floor.put("Reception",1);
//        room_floor.put("Library",1);
//        room_floor.put("Lift 1",1);
//        room_floor.put("Lift 2",2);
//        room_floor.put("Office",2);
//        room_floor.put("Principal Room",2);

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

        }
//        CustomView customView= null;
//        try {
//            customView = new CustomView(getActivity().getApplicationContext());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        customView.setStartFloorNum(floor_number);
//        customView.setDestFloorNum(des_floor_number);
//        customView.setStartRoomVertex(start_loc_num);
//        customView.setDestRoomVertex(dest_loc_num);
//        customView.postInvalidate();
//        customView.requestLayout();
//
//        return customView;


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