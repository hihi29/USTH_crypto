package vn.edu.usth.cryptoapp.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import vn.edu.usth.cryptoapp.R;
import vn.edu.usth.cryptoapp.HomeAdapter;
import vn.edu.usth.cryptoapp.api.CoinData;

public class Home extends Fragment {

    private RecyclerView recyclerView;
    private HomeAdapter adapter;
    private final List<CoinData> coinList = new ArrayList<>();

    public Home() {
        // Required empty constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new HomeAdapter(requireContext(), coinList);
        recyclerView.setAdapter(adapter);

        loadData();
    }

    private void loadData() {
        String url = "https://api.coingecko.com/api/v3/coins/markets" +
                "?vs_currency=usd&order=market_cap_desc&per_page=100&page=1&sparkline=false" +
                "&price_change_percentage=1h";

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    coinList.clear();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);

                            CoinData coin = new CoinData(
                                    obj.optInt("market_cap_rank", 0),
                                    obj.optString("name", ""),
                                    obj.optString("symbol", "").toUpperCase(),
                                    obj.optDouble("current_price", 0.0),
                                    obj.optDouble("price_change_percentage_1h_in_currency", 0),
                                    obj.optString("image", "")
                            );

                            coin.setId(obj.optString("id", ""));
                            coinList.add(coin);
                        }

                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        Log.e("API_PARSE", "Error parsing JSON: " + e.getMessage());
                    }
                },
                error -> Log.e("API_VOLLEY", "Error: " + error.getMessage())
        );

        Volley.newRequestQueue(requireContext()).add(request);
    }
}
