package com.cryptoapp.example.cryptoapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by ahmed on 30/03/2018.
 */

public class ViewModel extends AndroidViewModel  {

    //public final LiveData<ArrayList<Currency>> currencies;

    private AppDatabase db;
    public LiveData<Currency[]> currencies;

    public ViewModel(Application application) {
        super(application);
        db = AppDatabase.getAppDatabase(this.getApplication());
        currencies =  db.currencyDao().findAll();

    }



    public void initialiseDb(ArrayList<Currency> currencies) {
        //db = AppDatabase.getAppDatabase(this.getApplication());
        //DBHandle.initialiseData(db,currencies);
    }



}