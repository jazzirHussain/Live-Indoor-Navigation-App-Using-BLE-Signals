package com.sample_project.mitsmap;

import android.os.Bundle;

import androidx.annotation.NonNull;
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

import org.json.JSONException;

public class NavigationFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        String[] floor = new String[] {
                "Entrance", "Reception", "Library", "Lift 1","Lift 2", "Office", "Principal Room"
        };

        View view = inflater.inflate(R.layout.fragment_navigation,
                container, false);
        TextView textView=view.findViewById(R.id.tv_start);
        TextView textView1=view.findViewById(R.id.textView4);
        Spinner dropdown=view.findViewById(R.id.spinner_waypoints);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if(this!=null ){
                    Log.i("Values3", "Message");
                    textView.setText("Entrance");
                    textView1.setVisibility(View.VISIBLE);
                    dropdown.setVisibility(View.VISIBLE);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(container.getContext(), android.R.layout.simple_spinner_item, floor);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dropdown.setAdapter(adapter);
                    Button button = (Button) view.findViewById(R.id.button1);

                    button.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            FragmentManager fm = getFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();
                            SecondFragment llf = new SecondFragment();
                            Bundle args = new Bundle();
                            args.putString("spinnerValue",dropdown.getSelectedItem().toString());

                            llf.setArguments(args);
                            ft.replace(((ViewGroup)getView().getParent()).getId(), llf);
                            ft.commit();
                        }
                    });
                }
            }
        }, 2000);

        return view;

    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       // return inflater.inflate(R.layout.fragment_navigation, container, false);
    }
}