package com.cryptoapp.example.cryptoapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class BTC extends AppCompatActivity {



    //private List<Currency> CurrencyList ;
    private RecyclerView recyclerView;
    private MyViewAdapter mAdapter;
    private AppDatabase db;

   ServiceReceiver myReceiver;
    private ViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btc);

        myReceiver = new ServiceReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CryptoService.MY_ACTION);
        registerReceiver(myReceiver, intentFilter);

        db = AppDatabase.getAppDatabase(this.getApplication());
        viewModel = ViewModelProviders.of(this).get(ViewModel.class);


        subscribeToViewModel();

    }


    @Override
    protected void onResume() {
        super.onResume();
        StartService_Connection_GetDataFromSite();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopCryptoService();

    }

    public void btc(View view) {

    }


    public void doller(View view) {
        Intent intent = new Intent(this, Dollar.class);
        startActivity(intent);
    }

    public void euro(View view) {
        Intent intent = new Intent(this, Euro.class);
        startActivity(intent);
    }

    public void main(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        //db.currencyDao().deleteAll();
        //printCryptoToScreen(new ArrayList<Currency>());
    }


    public void StartService_Connection_GetDataFromSite() {
        startService(new Intent(getBaseContext(), CryptoService.class));
    }

    public void stopCryptoService() {
        stopService(new Intent(getBaseContext(), CryptoService.class));
    }

    public void printCryptoToScreen(ArrayList<Currency>  currencies){
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.removeAllViewsInLayout();


        mAdapter = new MyViewAdapter(currencies);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        //prepareData(currencies);
        mAdapter.notifyDataSetChanged();
    }

    private void prepareData(ArrayList<Currency>  currencies) {

        if (currencies == null||currencies.isEmpty()||currencies.size()==0) {
            Toast.makeText(this,
                    "Please wait, The Service is downloading data ...", Toast.LENGTH_SHORT).show();
        } else {
            ArrayList<Currency> Currencies = currencies;
            if (Currencies.size() > 0) {
                /*for (Currency currency : Currencies) {
                    currencies.add(currency);
                }*/
                mAdapter.notifyDataSetChanged();
            }
        }

    }

private class ServiceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final ArrayList<Currency>  currencies = (ArrayList<Currency>)intent.getSerializableExtra("Currencies");

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                db.currencyDao().insertAll(currencies.toArray(new Currency[currencies.size()]));
                currencies.clear();
                return null;
            }

        }.execute();


    }
}


private class DBReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final ArrayList<Currency>  currencies = (ArrayList<Currency>)intent.getSerializableExtra("Currencies");

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                db.currencyDao().insertAll(currencies.toArray(new Currency[currencies.size()]));
                currencies.clear();
                return null;
            }

        }.execute();


    }
}

    @Override
    protected void onStop() {
        //unregisterReceiver(myReceiver);
        super.onStop();
    }



    private void subscribeToViewModel() {
        viewModel.currencies.observe(this, new Observer<Currency[]>() {
            @Override
            public void onChanged(@Nullable Currency[] currencies) {
                updateAfterSubscribe(currencies);
            }
        });
    }

    private void updateAfterSubscribe(final @NonNull Currency[] currencies) {

        printCryptoToScreen(new ArrayList<Currency>(Arrays.asList(currencies)));
    }




}




