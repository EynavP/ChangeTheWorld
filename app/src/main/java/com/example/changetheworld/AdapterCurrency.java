package com.example.changetheworld;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.changetheworld.model.currency;

import java.util.List;

public class AdapterCurrency extends RecyclerView.Adapter<AdapterCurrency.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<currency> data;

    public AdapterCurrency(Context context, List<currency> data){
        this.layoutInflater =LayoutInflater.from(context);
        this.data=data;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.custome_recycler_view,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        int symbol=data.get(i).getSymbolId();
        viewHolder.symbol.setImageResource(symbol);

        String value=data.get(i).getValue();
        viewHolder.value_name.setText(value);

        String dailyCh=data.get(i).getDailyChange();
        viewHolder.daily_change.setText(dailyCh);

        String currencyName=data.get(i).getCurrencyName();
        viewHolder.currency_name.setText(currencyName);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView value_name,daily_change, currency_name;
        ImageView symbol;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            symbol=itemView.findViewById(R.id.symbolCurrency);
            value_name=itemView.findViewById(R.id.currencyName);
            currency_name = itemView.findViewById(R.id.currency_name);
            daily_change=itemView.findViewById(R.id.daily_change);
        }
    }
}
