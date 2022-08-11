package com.sample_project.mitsmap;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SecondFragment extends Fragment {

    ImageView imageView;
    ImageView imageView2;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {

        BottomNavigationView navBar = getActivity().findViewById(R.id.bottom_navigation);
        navBar.setVisibility(View.GONE);
        String strtext = getArguments().getString("spinnerValue");
        Log.i("Data",strtext);
        HashMap<String,Integer> room_vertices=new HashMap<String,Integer>();
        room_vertices.put("Entrance",0);
        room_vertices.put("Reception",2);
        room_vertices.put("Library",4);
        room_vertices.put("Lift 1",6);
        room_vertices.put("Lift 2",0);
        room_vertices.put("Office",3);
        room_vertices.put("Principal Room",4);
        HashMap<String,Integer> room_floor=new HashMap<String,Integer>();
        room_floor.put("Entrance",1);
        room_floor.put("Reception",1);
        room_floor.put("Library",1);
        room_floor.put("Lift 1",1);
        room_floor.put("Lift 2",2);
        room_floor.put("Office",2);
        room_floor.put("Principal Room",2);

        int start_room=0,end_room;

        end_room = Integer.parseInt(room_vertices.get(strtext)+"");
        Log.i("Data","end_room="+end_room);
        int start_floor=1,end_floor;
        end_floor = Integer.parseInt(room_floor.get(strtext)+"");

        CustomView customView= null;
        try {
            customView = new CustomView(getContext());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        customView.setStartFloorNum(start_floor);
        customView.setDestFloorNum(end_floor);
        customView.setStartRoomVertex(start_room);
        customView.setDestRoomVertex(end_room);
        customView.postInvalidate();
        customView.requestLayout();

        return customView;


     //return inflater.inflate(R.layout.fragment_second, container, false);
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