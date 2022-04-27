package com.example.changetheworld;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.changetheworld.model.business_currency_rate;

import java.util.List;

public class AdapterBusinessCurrencyRate extends RecyclerView.Adapter<AdapterBusinessCurrencyRate.ViewHolder>{

    private LayoutInflater layoutInflater;
    private List<business_currency_rate> data;

    public AdapterBusinessCurrencyRate(Context context, List<business_currency_rate> data) {
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view=layoutInflater.inflate(R.layout.bussiness_rate_item_recycler_view,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {

        int symbol=data.get(i).getSymbolId();
        holder.symbolId.setImageResource(symbol);

        String Exchangevalue=data.get(i).getExchangevalue();
        holder.Exchangevalue.setText(Exchangevalue);

        String currencyName=data.get(i).getCurrencyName();
        holder.currencyName.setText(currencyName);

        String Sales_value=data.get(i).getSales_value();
        holder.Sales_value.setText(Sales_value);

        String update_sales_rate=data.get(i).getUpdate_sales_rate();
        holder.update_sales_rate.setText(update_sales_rate);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView symbolId;
        TextView Exchangevalue, currencyName,Sales_value;
        EditText update_sales_rate;

        public ViewHolder(@NonNull View viewItem){
            super(viewItem);
            symbolId=viewItem.findViewById(R.id.symbolCurrency);
            Exchangevalue=viewItem.findViewById(R.id.Exchange_value);
            currencyName=viewItem.findViewById(R.id.currency_name);
            Sales_value=viewItem.findViewById(R.id.Sales_rate_value);
            update_sales_rate=viewItem.findViewById(R.id.update_value);

        }

    }
}
