package vn.edu.usth.cryptoapp.main;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.edu.usth.cryptoapp.OverviewPrice;
import vn.edu.usth.cryptoapp.R;

public class Overview extends Fragment {

    private final Map<String, List<Entry>> cachedData = new HashMap<>();
    private LineChart priceChart;
    private ImageView coinIcon;
    private TextView coinName, coinSymbol, coinPrice;
    private Button btn1D, btn7D, btn1M;
    private String coinId = "bitcoin";
    private String coinDisplayName = "Bitcoin";
    private String coinSymbolStr = "BTC";
    private String coinImageUrl = "https://assets.coingecko.com/coins/images/1/large/bitcoin.png";
    private double currentPrice = 0.0;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.overview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        priceChart = view.findViewById(R.id.priceChart);
        coinIcon = view.findViewById(R.id.coinIcon);
        coinName = view.findViewById(R.id.coinName);
        coinSymbol = view.findViewById(R.id.coinSymbol);
        coinPrice = view.findViewById(R.id.coinPrice);
        btn1D = view.findViewById(R.id.btn1D);
        btn7D = view.findViewById(R.id.btn7D);
        btn1M = view.findViewById(R.id.btn1M);

        Bundle args = getArguments();
        if (args != null) {
            coinId = args.getString("id", "bitcoin");
            coinDisplayName = args.getString("name", "Bitcoin");
            coinSymbolStr = args.getString("symbol", "BTC");
            coinImageUrl = args.getString("iconUrl", coinImageUrl);
            currentPrice = args.getDouble("price", 0.0);
        }

        coinName.setText(coinDisplayName);
        coinSymbol.setText(coinSymbolStr);
        coinPrice.setText("$" + String.format("%.2f", currentPrice));
        Glide.with(this).load(coinImageUrl).into(coinIcon);

        setupChart();


        loadChartData("1");

        btn1D.setOnClickListener(v -> loadChartData("1"));
        btn7D.setOnClickListener(v -> loadChartData("7"));
        btn1M.setOnClickListener(v -> loadChartData("30"));

    }


    private void setupChart() {
        priceChart.setBackgroundColor(Color.BLACK);
        priceChart.setNoDataText("Loading...");
        priceChart.getLegend().setEnabled(false);
        priceChart.getDescription().setEnabled(false);
        priceChart.getXAxis().setEnabled(false);
        priceChart.getAxisLeft().setEnabled(false);
        priceChart.getAxisRight().setEnabled(false);
        priceChart.setTouchEnabled(true);
        priceChart.setDragEnabled(true);
        priceChart.setScaleEnabled(false);
        priceChart.setPinchZoom(false);

        OverviewPrice markerView = new OverviewPrice(getContext(), R.layout.overview_price);
        markerView.setChartView(priceChart);
        priceChart.setMarker(markerView);
    }

    private void loadChartData(String days) {
        if (cachedData.containsKey(days)) {
            drawChart(cachedData.get(days));
            return;
        }

        String url = "https://api.coingecko.com/api/v3/coins/" + coinId + "/market_chart?vs_currency=usd&days=" + days;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                JSONArray prices = response.getJSONArray("prices");
                List<Entry> entries = new ArrayList<>();
                for (int i = 0; i < prices.length(); i++) {
                    JSONArray point = prices.getJSONArray(i);
                    float x = i;
                    float y = (float) point.getDouble(1);
                    entries.add(new Entry(x, y));
                }

                cachedData.put(days, entries);
                drawChart(entries);

            } catch (JSONException e) {
                Log.e("CHART_JSON_ERROR", e.toString());
            }
        }, error -> Log.e("CHART_API_ERROR", error.toString()));

        Volley.newRequestQueue(requireContext()).add(request);
    }

    private void drawChart(List<Entry> entries) {
        if (entries.isEmpty()) return;

        float first = entries.get(0).getY();
        float last = entries.get(entries.size() - 1).getY();

        int baseLineColor = (last >= first)
                ? ContextCompat.getColor(requireContext(), R.color.green_up)
                : ContextCompat.getColor(requireContext(), R.color.red_down);

        LineDataSet dataSet = new LineDataSet(entries, "Price");
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setLineWidth(1.8f);
        dataSet.setColor(baseLineColor);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        LineData lineData = new LineData(dataSet);
        priceChart.setData(lineData);
        priceChart.animateX(700);
        priceChart.invalidate();

        float percentChange = ((last - first) / first) * 100f;
        updatePriceText(last, percentChange, baseLineColor);

        priceChart.setOnChartValueSelectedListener(new com.github.mikephil.charting.listener.OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, com.github.mikephil.charting.highlight.Highlight h) {
                float value = e.getY();
                float percent = ((value - first) / first) * 100f;

                int dynamicColor = (value >= first)
                        ? ContextCompat.getColor(requireContext(), R.color.green_up)
                        : ContextCompat.getColor(requireContext(), R.color.red_down);
                dataSet.setColor(dynamicColor);
                priceChart.invalidate();

                updatePriceText(value, percent, dynamicColor);
            }

            @Override
            public void onNothingSelected() {
                dataSet.setColor(baseLineColor);
                priceChart.invalidate();
                updatePriceText(last, percentChange, baseLineColor);
            }
        });
    }

    private void updatePriceText(float price, float percentChange, int color) {
        String priceText = String.format("$%,.2f", price);
        String percentText = String.format(" (%.2f%%)", percentChange);

        coinPrice.setTextColor(color);
        if (percentChange >= 0) coinPrice.setText(priceText + " +" + percentText);
        else coinPrice.setText(priceText + " " + percentText);
    }

}
