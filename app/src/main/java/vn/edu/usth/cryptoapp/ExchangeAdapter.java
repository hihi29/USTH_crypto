package vn.edu.usth.cryptoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import vn.edu.usth.cryptoapp.api.ExchangeData;

public class ExchangeAdapter extends RecyclerView.Adapter<ExchangeAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(ExchangeData exchange);
    }

    private final Context context;
    private final List<ExchangeData> exchanges;
    private final OnItemClickListener listener;

    public ExchangeAdapter(Context context, List<ExchangeData> exchanges, OnItemClickListener listener) {
        this.context = context;
        this.exchanges = exchanges;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_exchange, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExchangeData ex = exchanges.get(position);

        holder.rank.setText(String.valueOf(ex.getRank()));
        holder.name.setText(ex.getName());
        holder.country.setText(ex.getCountry().isEmpty() ? "—" : ex.getCountry());
        holder.volume.setText(String.format("%.2f BTC", ex.getVolume24h()));

        Glide.with(context)
                .load(ex.getImageUrl())
                .into(holder.logo);

        holder.itemView.setOnClickListener(v -> listener.onItemClick(ex));
    }

    @Override
    public int getItemCount() {
        return exchanges.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView rank, name, country, volume;
        ImageView logo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rank = itemView.findViewById(R.id.exchangeRank);
            name = itemView.findViewById(R.id.exchangeName);
            country = itemView.findViewById(R.id.exchangeCountry);
            volume = itemView.findViewById(R.id.exchangeVolume);
            logo = itemView.findViewById(R.id.exchangeLogo);
        }
    }
}
