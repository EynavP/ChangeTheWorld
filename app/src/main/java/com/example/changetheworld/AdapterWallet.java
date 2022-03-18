package com.example.changetheworld;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.changetheworld.model.Wallet;

import java.util.List;

public class AdapterWallet extends RecyclerView.Adapter<AdapterWallet.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<Wallet> data;

    AdapterWallet(Context context, List<Wallet> data){
        this.layoutInflater =LayoutInflater.from(context);
        this.data=data;

    }
    @NonNull
    @Override
    public AdapterWallet.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.wallet_recycler_view,viewGroup,false);
        return new AdapterWallet.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterWallet.ViewHolder viewHolder, int i) {

        String symbol = data.get(i).getSymbol();
        viewHolder.symbol.setText(symbol);

        String currencyName = data.get(i).getCurrency();
        viewHolder.currencyName.setText(currencyName);

        float amount = data.get(i).getBalance();
        viewHolder.balance.setText(Float.toString(amount));

        float value = data.get(i).getValueLocalCurrency();
        viewHolder.value.setText(Float.toString(value));

        String symbolLocalCurrency = data.get(i).getSymbolLocalCurrency();
        viewHolder.symbolLocalCurrency.setText(symbolLocalCurrency);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView symbol,currencyName,balance,value,symbolLocalCurrency;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            symbol=itemView.findViewById(R.id.symbol_recycler);
            currencyName=itemView.findViewById(R.id.currency_name);
            balance=itemView.findViewById(R.id.balance);
            value=itemView.findViewById(R.id.value_amonut);
            symbolLocalCurrency=itemView.findViewById(R.id.symbolLocalCurrency);
        }
    }


}