package com.flytbasetask;


import java.util.ArrayList;
import java.util.List;

public class Holder
{
    private static Holder instance = null;
    private List<ResultModel> itemArray;
    private Holder(){
        itemArray = new ArrayList<>();
    }

    public static Holder getInstance(){
        if (instance == null)
            instance = new Holder();
        return instance;
    }

    public ArrayList<ResultModel> getItemArray(){
        return new ArrayList<>(this.itemArray);
    }

    public void addItemToArray(ResultModel item){
        this.itemArray.add(item);
    }
    public void clearItemArray(){
        this.itemArray.clear();
    }
}