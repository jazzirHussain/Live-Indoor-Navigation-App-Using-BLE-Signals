package com.sample_project.mitsmap;

import androidx.annotation.NonNull;

public class BuildingSpinner {
    int _BID;
    String _BNAME;

    public BuildingSpinner(int anInt, String string) {
        this._BID=anInt;
        this._BNAME=string;
    }

    public BuildingSpinner(String string) {
        this._BNAME=string;
    }

    public int get_BID() {
        return _BID;
    }

    public String get_Bulding_name() {
        return _BNAME;
    }

    @NonNull
    @Override
    public String toString() {
        return _BNAME;
    }
}
