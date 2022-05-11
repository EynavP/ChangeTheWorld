package com.example.changetheworld;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.changetheworld.model.BusinessCurrencyRateForShow;
import com.example.changetheworld.model.business_currency_rate;

import java.text.DecimalFormat;
import java.util.List;

public class AdapterBusinessCurrencyRateForShow extends RecyclerView.Adapter<AdapterBusinessCurrencyRateForShow.ViewHolder>{

    private LayoutInflater layoutInflater;
    private List<BusinessCurrencyRateForShow> data;
    DecimalFormat df = new DecimalFormat("0.000");
    public AdapterBusinessCurrencyRateForShow(Context context, List<BusinessCurrencyRateForShow> data) {
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view=layoutInflater.inflate(R.layout.business_rate_for_show_item_recycler_view,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {

        String symbol=data.get(i).getSymbolId();
        holder.symbolId.setText(symbol);

        String sale_price_value=data.get(i).getSales_price();
        holder.sale_price_value.setText(sale_price_value);

        String currencyName=data.get(i).getCurrencyName();
        holder.currencyName.setText(currencyName);

        String buy_price_value=data.get(i).getBuy_price();
        holder.buy_price_value.setText(buy_price_value);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView sale_price_value, buy_price_value, currencyName,symbolId;

        public ViewHolder(@NonNull View viewItem){
            super(viewItem);
            symbolId=viewItem.findViewById(R.id.symbolCurrency);
            sale_price_value=viewItem.findViewById(R.id.sale_price_value);
            currencyName=viewItem.findViewById(R.id.currency_name);
            buy_price_value=viewItem.findViewById(R.id.buy_price_value);
        }

    }
}

