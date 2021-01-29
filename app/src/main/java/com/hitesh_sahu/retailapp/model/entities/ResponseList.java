package com.hitesh_sahu.retailapp.model.entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ResponseList<T> {
    @SerializedName("statusCode")
    private int status;
    @SerializedName("data")
    private ArrayList<T> data;

    public ResponseList(int status, ArrayList<T> data) {
        this.status = status;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<T> getData() {
        return data;
    }

    public void setData(ArrayList<T> data) {
        this.data = data;
    }
}
