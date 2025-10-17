package vn.edu.usth.cryptoapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import vn.edu.usth.cryptoapp.data.CoinData;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.CoinViewHolder> {

    private final Context context;
    private final List<CoinData> coinList;

    public HomeAdapter(Context context, List<CoinData> coinList) {
        this.context = context;
        this.coinList = coinList;
    }

    @NonNull
    @Override
    public CoinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_coin, parent, false);
        return new CoinViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoinViewHolder holder, int position) {
        CoinData coin = coinList.get(position);

        holder.rank.setText(String.valueOf(coin.getRank()));
        holder.name.setText(coin.getName());
        holder.symbol.setText(coin.getSymbol());
        holder.price.setText(String.format("$%,.2f", coin.getPrice()));
        holder.change1h.setText(String.format("%.2f%%", coin.getChange1h()));

        int colorGreen = 0xFF39C874;
        int colorRed = 0xFFF44336;
        holder.change1h.setTextColor(coin.getChange1h() >= 0 ? colorGreen : colorRed);

        Glide.with(context)
                .load(coin.getIconUrl())
                .into(holder.icon);

        SharedPreferences prefs = context.getSharedPreferences("Watchlist", Context.MODE_PRIVATE);
        Set<String> ids = new HashSet<>(prefs.getStringSet("coins", new HashSet<>()));

        boolean isFav = ids.contains(coin.getId());
        holder.starButton.setImageResource(isFav ? R.drawable.ic_watch_fill : R.drawable.ic_watch);

        holder.starButton.setOnClickListener(v -> {
            Set<String> updated = new HashSet<>(prefs.getStringSet("coins", new HashSet<>()));
            SharedPreferences.Editor editor = prefs.edit();

            if (updated.contains(coin.getId())) {
                updated.remove(coin.getId());
                holder.starButton.setImageResource(R.drawable.ic_watch);
                Toast.makeText(context, "Removed from Watchlist", Toast.LENGTH_SHORT).show();
            } else {
                updated.add(coin.getId());
                holder.starButton.setImageResource(R.drawable.ic_watch_fill);
                Toast.makeText(context, "Added to Watchlist", Toast.LENGTH_SHORT).show();
            }

            editor.putStringSet("coins", updated);
            editor.apply();
        });

        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("id", coin.getId());
            bundle.putString("name", coin.getName());
            bundle.putString("symbol", coin.getSymbol());
            bundle.putString("iconUrl", coin.getIconUrl());
            Navigation.findNavController(v).navigate(R.id.nav_overview, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return coinList.size();
    }

    public static class CoinViewHolder extends RecyclerView.ViewHolder {
        ImageButton starButton;
        ImageView icon;
        TextView rank, name, symbol, price, change1h;

        public CoinViewHolder(@NonNull View itemView) {
            super(itemView);
            starButton = itemView.findViewById(R.id.starButton);
            icon = itemView.findViewById(R.id.icon);
            rank = itemView.findViewById(R.id.rank);
            name = itemView.findViewById(R.id.name);
            symbol = itemView.findViewById(R.id.symbol);
            price = itemView.findViewById(R.id.price);
            change1h = itemView.findViewById(R.id.change_1h);
        }
    }
}
