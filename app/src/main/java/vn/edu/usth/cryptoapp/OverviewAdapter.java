package vn.edu.usth.cryptoapp;

import android.content.Context;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import vn.edu.usth.cryptoapp.api.CoinData;

public class OverviewAdapter extends RecyclerView.Adapter<OverviewAdapter.ViewHolder> {

    private final Context context;
    private final List<CoinData> coinList;

    public OverviewAdapter(Context context, List<CoinData> coinList) {
        this.context = context;
        this.coinList = coinList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_coin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CoinData coin = coinList.get(position);

        holder.rank.setText(String.valueOf(coin.getRank()));
        holder.name.setText(coin.getName());
        holder.symbol.setText(coin.getSymbol());
        holder.price.setText(String.format("$%,.2f", coin.getPrice()));

        Glide.with(context).load(coin.getIconUrl()).into(holder.icon);
    }

    @Override
    public int getItemCount() {
        return coinList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView rank, name, price, symbol;
        ImageView icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rank = itemView.findViewById(R.id.rank);
            icon = itemView.findViewById(R.id.icon);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            symbol = itemView.findViewById(R.id.symbol);


        }
    }
}
