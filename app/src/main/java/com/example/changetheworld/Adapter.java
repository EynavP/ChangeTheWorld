package com.example.changetheworld;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.changetheworld.model.currency;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<currency> data;

    Adapter(Context context,List<currency> data){
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

        String currency=data.get(i).getCurrencyname();
        viewHolder.currencyName.setText(currency);

        String value=data.get(i).getValue();
        viewHolder.value_name.setText(value);

        String dailyCh=data.get(i).getDailychange();
        viewHolder.daily_change.setText(dailyCh);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView currencyName,value_name,daily_change;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            currencyName=itemView.findViewById(R.id.currency_name);
            value_name=itemView.findViewById(R.id.value);
            daily_change=itemView.findViewById(R.id.daily_change);
        }
    }
}
