package com.cryptoapp.example.cryptoapp;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;
import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by ahmed on 30/03/2018.
 */

@Dao
public interface CurrencyDao {
    @Query("SELECT * FROM currency")
    List<Currency> getAll();

    @Query("SELECT * FROM currency WHERE symbol LIKE :sym LIMIT 1")
    Currency findBySymbol(String sym);

    @Query("SELECT * FROM currency")
    LiveData<Currency[]> findAll();

    @Insert(onConflict = REPLACE)
    void insertAll(Currency... currencies);

    @Insert(onConflict = REPLACE)
    void insertOne(Currency currency);

    @Update
    void updateOne(Currency currency);

    @Update
    void updateAll(Currency... currencies);


    @Delete
    void delete(Currency currency);


    @Delete
    void deleteAll(Currency... currencies);



}
