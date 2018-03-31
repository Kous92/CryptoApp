package com.cryptoapp.example.cryptoapp;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by ahmed on 30/03/2018.
 */

@Database(entities = {Currency.class}, version = 1 , exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CurrencyDao currencyDao();
    private static AppDatabase INSTANCE;
    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "currency-database")
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }
    public static void destroyInstance() {
        INSTANCE = null;
    }
}