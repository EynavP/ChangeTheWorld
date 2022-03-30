package com.example.changetheworld;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.changetheworld.model.Order;
import com.example.changetheworld.model.Transaction;

import java.util.List;

public class AdapterTransaction extends RecyclerView.Adapter<AdapterTransaction.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<Transaction> data;

    public AdapterTransaction(Context layoutInflater, List<Transaction> data) {
        this.layoutInflater =  LayoutInflater.from(layoutInflater);
        this.data = data;
    }

    @NonNull
    @Override
    public AdapterTransaction.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.subwallet_recycler_view,viewGroup,false);
        return new AdapterTransaction.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTransaction.ViewHolder viewHolder, int i) {

        String date = data.get(i).getCreate_time().toString();
        viewHolder.date.setText(date);

        String amount = data.get(i).getAmount();
        viewHolder.amount.setText(amount);

        String action = data.get(i).getAction();
        viewHolder.action.setText(action);

    }

    @Override
    public int getItemCount() {return data.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView date,amount,action;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.dateTransaction);
            amount=itemView.findViewById(R.id.amountTransaction);
            action=itemView.findViewById(R.id.actionTransaction);
        }
    }
}
