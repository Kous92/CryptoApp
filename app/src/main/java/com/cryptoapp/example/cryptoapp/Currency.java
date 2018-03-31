package com.cryptoapp.example.cryptoapp;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by ahmed on 09/03/2018.
 */
import java.io.Serializable;

@SuppressWarnings("serial")
@Entity(tableName = "currency")
public class Currency implements Serializable {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "symbol")
    private String symbol;

    @NonNull
    @ColumnInfo(name = "lastPrice")
    private double lastPrice;

    @NonNull
    @ColumnInfo(name = "priceChangePercent")
    private double priceChangePercent;


    public Currency(){}

    /*public Currency(String symbol, double price, double priceChangePercent) {
        this.symbol = symbol;
        this.lastPrice = price;
        this.priceChangePercent = priceChangePercent;
    }
    */



    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(double lastPrice) {
        this.lastPrice = lastPrice;
    }

    public double getPriceChangePercent() {
        return priceChangePercent;
    }

    public void setPriceChangePercent(double priceChangePercent) {
        this.priceChangePercent = priceChangePercent;
    }
}