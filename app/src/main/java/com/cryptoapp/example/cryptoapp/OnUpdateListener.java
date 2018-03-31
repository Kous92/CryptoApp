package com.cryptoapp.example.cryptoapp;

import java.util.ArrayList;

public interface OnUpdateListener {
    public void onUpdate(ArrayList<Currency> obj, double usd, double eur);

}