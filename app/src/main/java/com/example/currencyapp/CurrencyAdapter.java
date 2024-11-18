package com.example.currencyapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CurrencyAdapter extends BaseAdapter {

    private List<Currency> originalList;
    private List<Currency> filteredList;
    private LayoutInflater inflater;

    public CurrencyAdapter(Context context, List<Currency> currencies) {
        this.originalList = currencies;
        this.filteredList = new ArrayList<>(currencies);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.currency_item, parent, false);
        }

        TextView currencyName = convertView.findViewById(R.id.currencyName);
        TextView currencyRate = convertView.findViewById(R.id.currencyRate);

        Currency currency = filteredList.get(position);
        currencyName.setText(currency.getName());
        currencyRate.setText(String.valueOf(currency.getRate()));

        return convertView;
    }

    public void filter(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(originalList);
        } else {
            for (Currency currency : originalList) {
                if (currency.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(currency);
                }
            }
        }
        notifyDataSetChanged();
    }
}
