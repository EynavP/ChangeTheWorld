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

import com.example.changetheworld.model.business_currency_rate;

import java.text.DecimalFormat;
import java.util.List;

public class AdapterBusinessCurrencyRate extends RecyclerView.Adapter<AdapterBusinessCurrencyRate.ViewHolder>{

    private LayoutInflater layoutInflater;
    private List<business_currency_rate> data;
    DecimalFormat df = new DecimalFormat("0.000");
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

        String symbol=data.get(i).getSymbolId();
        holder.symbolId.setText(symbol);

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

        TextView Exchangevalue, currencyName,Sales_value,symbolId;
        EditText update_sales_rate;

        public ViewHolder(@NonNull View viewItem){
            super(viewItem);
            symbolId=viewItem.findViewById(R.id.symbolCurrency);
            Exchangevalue=viewItem.findViewById(R.id.sale_price_value);
            currencyName=viewItem.findViewById(R.id.currency_name);
            Sales_value=viewItem.findViewById(R.id.buy_price_value);
            update_sales_rate=viewItem.findViewById(R.id.update_value);
            update_sales_rate.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
                @Override
                public void afterTextChanged(Editable editable) {
                    String current_update_sales = editable.toString();
                    if (!current_update_sales.isEmpty() && !current_update_sales.equals("."))
                        Sales_value.setText(df.format(Float.parseFloat(Sales_value.getText().toString()) + Float.parseFloat(current_update_sales)));
                    else{
                        Sales_value.setText(Exchangevalue.getText().toString());
                    }
                    for (business_currency_rate bcr: data) {
                        if(bcr.getCurrencyName().equals(currencyName.getText().toString())){
                            bcr.setUpdate_sales_rate(update_sales_rate.getText().toString());
                        }
                    }
                }
            });

        }

    }
}
