package vn.edu.usth.cryptoapp;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

public class OverviewPrice extends MarkerView {

    private final TextView tvPrice;

    public OverviewPrice(Context context, int layoutResource) {
        super(context, layoutResource);
        tvPrice = findViewById(R.id.price1h);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        tvPrice.setText(String.format("$%.2f", e.getY()));
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
