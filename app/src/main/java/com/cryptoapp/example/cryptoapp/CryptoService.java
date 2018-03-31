package com.cryptoapp.example.cryptoapp;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;

public class CryptoService extends Service {

    final static String MY_ACTION = "MY_ACTION";
    String message;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();

        executeManyTime(7000);

        //executeOnce();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void executeOnce() {
        ConnectionTask  task = new ConnectionTask();
        task.setUpdateListener(new OnUpdateListener() {
            @Override
            public void onUpdate(ArrayList<Currency> Currencies, double usd, double eur) {

                Intent intent = new Intent();
                intent.setAction(MY_ACTION);
                intent.putExtra("Currencies", Currencies);
                intent.putExtra("dollar", usd);
                intent.putExtra("euro", eur);
                sendBroadcast(intent);

            }
        });
        task.execute();
    }


    private void executeManyTime(long duration) {
        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
        new Timer().scheduleAtFixedRate(new RepetetifTask(), new Date(), duration);
    }

    private class RepetetifTask extends TimerTask {

        @Override
        public void run() {
            executeOnce();
            //new ConnectionTask().execute();
        }


    }


    class ConnectionTask extends AsyncTask<Void, Void, Void> {

        private String link0 = "https://api.binance.com/api/v1/exchangeInfo";
        private String link1 = "https://api.binance.com/api/v1/ticker/24hr?symbol=";
        private ArrayList<Currency> currencies;
        private double usd;
        private double eur;
        OnUpdateListener listener;
        public void setUpdateListener(OnUpdateListener listener) {
            this.listener = listener;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Toast.makeText(MainActivity.this, "Json Data is downloading", Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            String[] symbols = {"TRXBTC","ETHBTC","ICXBTC","BNBBTC","EOSBTC","XRPBTC","XLMBTC","NEOBTC","ADABTC","LTCBTC","IOTABTC","GVTBTC","QLCBTC","WANBTC","BCCBTC"};//getSymbols();
            if(symbols!=null){
                this.currencies = getAllCurrencies(symbols);
            }else{
                Log.i("next", "fail : Symbols :NULL ");
            }
            usd = 0;
            eur = 0;
            JSONObject j = getJsonObject("https://blockchain.info/ticker");
            try {
                if(j!=null){
                    JSONObject j2 = j.getJSONObject("USD");
                    JSONObject j3 = j.getJSONObject("EUR");
                    if(j2!=null && j3!=null){
                        usd = (double) j2.getDouble("last");
                        eur = (double) j3.getDouble("last");
                    }

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;

        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (listener != null) {
                listener.onUpdate(this.currencies,usd,eur );
            }
        }

        protected String[] getSymbols() {
            Log.i("next", "getSymbols start");
            String symbols = "";
            String[] result = null;
            JSONObject json = getJsonObject(link0);
            if (json != null) {
                JSONArray arrJson = null;
                try {
                    arrJson = json.getJSONArray("symbols");

                    int size = arrJson.length();

                    for (int i = 0; i < size; i++) {
                        JSONObject obj = (JSONObject) arrJson.get(i);
                        symbols += obj.get("symbol").toString() + "#";
                    }
                    result = symbols.split("#");
                } catch (JSONException e) {
                    Log.i("MyApp", "error : " + e.getCause().getMessage());
                    e.printStackTrace();
                }
            }else {
                Log.i("next", "NULL ; fail JSONObject json = getJsonObject(link0);");
            }
            Log.i("next", "getSymbols end");
            return result;

        }
        protected ArrayList<Currency> getAllCurrencies(String[] symbols) {
            Log.i("next", "getAllCurrencies start");

            ArrayList<Currency> Currencies_List = new ArrayList<Currency>();

            for (String symbol : symbols) {
                if (symbol.contains("BNB") || symbol.contains("ETH") || symbol.contains("USDT") || symbol.contains("123456")) {

                } else {


                    Currency currency = null;
                    JSONObject json = getJsonObject(link1 + symbol);
                    if (json == null) {
                        Log.i("error", "JSONObject is null");
                    } else {
                        try {
                            double price = json.getDouble("lastPrice");
                            double priceChangePercent = json.getDouble("priceChangePercent");
                            currency = new Currency();
                            currency.setSymbol(symbol);
                            currency.setLastPrice(price);
                            currency.setPriceChangePercent(priceChangePercent);
                            Currencies_List.add(currency);
                            Log.i("sucess", currency.getSymbol());

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("error", "error parsing : " + e.getCause().getMessage());

                        }
                    }

                }
            }
            Log.i("next", "getAllCurrencies end");
            return Currencies_List;
        }
        protected JSONObject getJsonObject(String url) {
            JSONObject json = null;
            String JsonString = getJsonString(url);
            if (JsonString == null || JsonString.isEmpty()) {
                Log.i("MyAppJson", "JsonString is empty/null");
            } else {
                try {
                    json = new JSONObject(JsonString);
                } catch (JSONException e) {
                    Log.i("MyAppJson", "JSONObject error ");
                    e.printStackTrace();
                }
            }
            return json;

        }
        protected String getJsonString(String reqUrl) {
            String response = null;
            try {
                URL url = new URL(reqUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                // read the response
                InputStream in = new BufferedInputStream(conn.getInputStream());
                response = convertStreamToString(in);
            } catch (MalformedURLException e) {
                Log.e(TAG, "MalformedURLException: " + e.getMessage());
            } catch (ProtocolException e) {
                Log.e(TAG, "ProtocolException: " + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "IOException: " + e.getMessage());
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
            return response;
        }
        protected String convertStreamToString(InputStream is) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return sb.toString();
        }
        private Currency getCurrencyFromJson(String symbol) {

            Currency currency = new Currency();
            currency.setSymbol("test");
            currency.setLastPrice(0.0);
            currency.setPriceChangePercent(0.0);

            JSONObject json = getJsonObject(link1 + symbol);
            if (json != null) {
                Log.i("error", "JSONObject is null");
                try {
                    currency.setSymbol(symbol);
                    currency.setLastPrice(json.getDouble("lastPrice"));
                    currency.setPriceChangePercent(json.getDouble("priceChangePercent"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("error", "error parsing : " + e.getCause().getMessage());
                }
            }
            return currency;
        }


    }
}