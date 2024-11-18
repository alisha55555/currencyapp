package com.example.currencyapp;

import android.os.Handler;
import android.os.Looper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DataLoader {

    private static final String API_URL = "https://api.exchangeratesapi.io/latest";

    public void loadCurrencyData(OnCurrenciesLoaded callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(API_URL).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    List<Currency> currencies = parseJson(responseBody);
                    new Handler(Looper.getMainLooper()).post(() -> callback.onLoaded(currencies));
                }
            }
        });
    }

    private List<Currency> parseJson(String json) {
        List<Currency> currencies = new ArrayList<>();
        JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
        JsonObject rates = jsonObject.getAsJsonObject("rates");

        rates.entrySet().forEach(entry -> {
            String name = entry.getKey();
            double rate = entry.getValue().getAsDouble();
            currencies.add(new Currency(name, rate));
        });

        return currencies;
    }

    public interface OnCurrenciesLoaded {
        void onLoaded(List<Currency> currencies);
    }
}
