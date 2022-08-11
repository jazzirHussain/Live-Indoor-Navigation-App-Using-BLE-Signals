package com.sample_project.mitsmap;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;


public class HomeFragment extends Fragment {

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
//        PhotoView photoView = (PhotoView)getView().findViewById(R.id.photo_view);
//        photoView.setZoomable(true);
//        photoView.setImageResource(R.drawable.fifth_floor_ramanujan_2);
        PhotoView photoView  = view. findViewById(R.id.photo_view);
        Button btn_floor1=view.findViewById(R.id.button1);
        Button btn_floor2=view.findViewById(R.id.button2);
        btn_floor1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoView.setImageResource(R.drawable.reception_edited);
            }
        });
        btn_floor2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoView.setImageResource(R.drawable.second_floor_edited);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}