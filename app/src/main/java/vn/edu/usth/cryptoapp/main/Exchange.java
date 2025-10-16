package vn.edu.usth.cryptoapp.main;

import android.content.Intent;
import android.net.Uri;
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
import vn.edu.usth.cryptoapp.ExchangeAdapter;
import vn.edu.usth.cryptoapp.api.ExchangeData;

public class Exchange extends Fragment {

    private RecyclerView recyclerView;
    private ExchangeAdapter adapter;
    private final List<ExchangeData> exchangeList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exchange, container, false);

        recyclerView = view.findViewById(R.id.newsRecycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new ExchangeAdapter(requireContext(), exchangeList, exchange -> {

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(exchange.getUrl()));
            startActivity(browserIntent);
        });

        recyclerView.setAdapter(adapter);
        loadExchanges();

        return view;
    }

    private void loadExchanges() {
        String url = "https://api.coingecko.com/api/v3/exchanges?per_page=50&page=1";

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET, url, null,
                response -> {
                    exchangeList.clear();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);

                            ExchangeData exchange = new ExchangeData(
                                    i + 1,
                                    obj.optString("name", ""),
                                    obj.optString("country", ""),
                                    obj.optDouble("trade_volume_24h_btc", 0),
                                    obj.optString("url", ""),
                                    obj.optString("image", "")
                            );
                            exchangeList.add(exchange);
                        }

                        adapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        Log.e("EXCHANGE_PARSE", "Error: " + e.getMessage());
                    }
                },
                error -> Log.e("EXCHANGE_API", "Error: " + error.getMessage())
        );

        Volley.newRequestQueue(requireContext()).add(request);
    }
}
