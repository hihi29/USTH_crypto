package vn.edu.usth.cryptoapp.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import vn.edu.usth.cryptoapp.R;
import vn.edu.usth.cryptoapp.api.CoinData;
import vn.edu.usth.cryptoapp.HomeAdapter;

public class WatchList extends Fragment {

    private RecyclerView recyclerView;
    private HomeAdapter adapter;
    private final List<CoinData> watchlistCoins = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.watchlist, container, false);
        recyclerView = view.findViewById(R.id.watchlistRecycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new HomeAdapter(getContext(), watchlistCoins);
        recyclerView.setAdapter(adapter);

        loadWatchlistCoins();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadWatchlistCoins();
    }
    public void reloadDataFromPrefs() {
        loadWatchlistCoins();
    }


    private void loadWatchlistCoins() {
        SharedPreferences prefs = requireContext().getSharedPreferences("Watchlist", Context.MODE_PRIVATE);
        Set<String> ids = prefs.getStringSet("coins", new HashSet<>());

        if (ids.isEmpty()) {
            watchlistCoins.clear();
            adapter.notifyDataSetChanged();
            Toast.makeText(getContext(), "No coins in Watchlist", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("WatchlistFragment", "IDs in watchlist: " + ids.toString());

        String idsParam = String.join(",", ids);
        String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&ids=" + idsParam +
                "&price_change_percentage=1h,24h,7d";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    watchlistCoins.clear();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);

                            CoinData coin = new CoinData(
                                    obj.getInt("market_cap_rank"),
                                    obj.getString("name"),
                                    obj.getString("symbol"),
                                    obj.getDouble("current_price"),
                                    obj.optDouble("price_change_percentage_1h_in_currency", 0),
                                    obj.getString("image")
                            );
                            coin.setId(obj.getString("id"));
                            watchlistCoins.add(coin);
                        }
                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("WatchlistFragment", "Error: " + error.getMessage())
        );

        Volley.newRequestQueue(requireContext()).add(request);
    }

}