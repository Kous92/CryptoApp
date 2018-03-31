package com.cryptoapp.example.cryptoapp;

/**
 * Created by ahmed on 30/03/2018.
 */

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyViewAdapter extends RecyclerView.Adapter<MyViewAdapter.MyViewHolder>
{
    private List<Currency> currencyList = new ArrayList<Currency>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView symbol, pricechange, price;

        public MyViewHolder(View view) {
            super(view);
            symbol = (TextView) view.findViewById(R.id.symbol);
            price = (TextView) view.findViewById(R.id.price);
            pricechange = (TextView) view.findViewById(R.id.pricechange);
        }
    }


    public MyViewAdapter(List<Currency> currencyList) {
        this.currencyList = currencyList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.one_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        Currency currency = currencyList.get(position);
        double percentage = currency.getPriceChangePercent();
        holder.symbol.setText(currency.getSymbol().substring(0, 3));
        String final_price_str = String.format("%.8f", currency.getLastPrice());
        holder.price.setText(String.valueOf(final_price_str));
        System.out.println("Prix: " + currency.getLastPrice() + ", changement: " + percentage + "%");

        if (percentage > 0)
        {
            String final_change_str = String.format("%.3f", percentage);
            String change = "+" + final_change_str + "%";
            holder.pricechange.setText(change);
            holder.pricechange.setTextColor(Color.parseColor("#228b22"));
        }
        else
        {
            String final_change_str = String.format("%.3f", percentage);
            String change = final_change_str + "%";
            holder.pricechange.setText(change);
            holder.pricechange.setTextColor(Color.parseColor("#ff0000"));
        }

    }

    @Override
    public int getItemCount() {
        return currencyList.size();
    }

    public void clear() {
        final int size = currencyList.size();
        currencyList.clear();
        notifyItemRangeRemoved(0, size);
    }
}

