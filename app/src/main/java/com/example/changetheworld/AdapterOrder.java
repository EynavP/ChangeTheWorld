package com.example.changetheworld;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.changetheworld.model.Order;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

public class AdapterOrder extends RecyclerView.Adapter<AdapterOrder.ViewHolder> {

        private LayoutInflater layoutInflater;
        private List<Order> data;

        AdapterOrder(Context context, List<Order> data){
            this.layoutInflater =LayoutInflater.from(context);
            this.data=data;

        }
        @NonNull
        @Override
        public AdapterOrder.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = layoutInflater.inflate(R.layout.business_recycler_view,viewGroup,false);
            return new AdapterOrder.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AdapterOrder.ViewHolder viewHolder, int i) {

            String fromCurrency = data.get(i).getFromCurrency();
            viewHolder.fromCurrency.setText(fromCurrency);

            Date date = data.get(i).getPickupDate();
            viewHolder.editDate.setText(date.toString());

            String toCurrency = data.get(i).getToCurrency();
            viewHolder.toCurrency.setText(toCurrency);

            float amount = data.get(i).getAmount();
            viewHolder.fromAmount.setText(Float.toString(amount));

            float recevied= data.get(i).getReceived();
            viewHolder.toAmount.setText(Float.toString(recevied));

            String paymentMethod= data.get(i).getPaymentMethod();
            viewHolder.paymentMethod.setText(paymentMethod);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{

            TextView editDate,editPickTime,fromAmount,fromCurrency,toAmount,toCurrency,paymentMethod;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                editDate=itemView.findViewById(R.id.editDate);
                editPickTime=itemView.findViewById(R.id.EditPickUpTime);
                fromAmount=itemView.findViewById(R.id.FromAmount);
                fromCurrency=itemView.findViewById(R.id.FromCurrency);
                toAmount=itemView.findViewById(R.id.ToAmount);
                toCurrency=itemView.findViewById(R.id.ToCurrency);
                paymentMethod=itemView.findViewById(R.id.PaymentMethod);
            }
        }
}
