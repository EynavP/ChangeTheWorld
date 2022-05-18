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

import java.util.Date;
import java.util.List;

public class AdapterOrder extends RecyclerView.Adapter<AdapterOrder.ViewHolder> {

        private LayoutInflater layoutInflater;
        private List<Order> data;
        private RecycleSubWalletClickInterface recycleSubWalletClickInterface;
        private String userType = "PrivateClient";


    public AdapterOrder(Context context, List<Order> data, String activity){
            this.layoutInflater =LayoutInflater.from(context);
            this.data=data;
            if (activity.equals("OrdersActivity"))
                this.recycleSubWalletClickInterface = (OrdersActivity)context;
            else {
                this.recycleSubWalletClickInterface = (BusinessOrdersActivity) context;
                userType = "BusinessClient";
            }

    }
        @NonNull
        @Override
        public AdapterOrder.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = layoutInflater.inflate(R.layout.order_recycleview,viewGroup,false);
            return new AdapterOrder.ViewHolder(view,recycleSubWalletClickInterface);
        }

        @Override
        public void onBindViewHolder(@NonNull AdapterOrder.ViewHolder viewHolder, int i) {


            String fromCurrency = data.get(i).getFromCurrency();
            viewHolder.fromCurrency.setText(fromCurrency);

            String date = data.get(i).getPickupDate();
            viewHolder.pickupDate.setText(date);

            String toCurrency = data.get(i).getToCurrency();
            viewHolder.toCurrency.setText(toCurrency);

            String amount = data.get(i).getAmount();
            viewHolder.amount.setText(amount);

            String recevied= data.get(i).getReceived();
            viewHolder.received.setText(recevied);

            String paymentMethod= data.get(i).getPaymentMethod();
            viewHolder.paymentMethod.setText(paymentMethod);

            String status= data.get(i).getStatus();
            viewHolder.status.setText(status);

            String name= data.get(i).getName();
            viewHolder.name.setText(name);

            if (userType.equals("BusinessClient")) {
                String clientName = data.get(i).getClientName();
                viewHolder.name.setText(clientName);
                viewHolder.pickUpFromTitle.setText("Ordered By: ");
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{

            TextView fromCurrency,toCurrency,amount,received,pickupDate,name,status,paymentMethod, id, pickUpFromTitle;


            public ViewHolder(@NonNull View itemView, RecycleSubWalletClickInterface recycleSubWalletClickInterface) {
                super(itemView);
                fromCurrency=itemView.findViewById(R.id.from_currency_value);
                toCurrency=itemView.findViewById(R.id.to_currency_value);
                amount=itemView.findViewById(R.id.amount_value);
                received=itemView.findViewById(R.id.received_value);
                pickupDate=itemView.findViewById(R.id.date_value);
                name=itemView.findViewById(R.id.businessName);
                status=itemView.findViewById(R.id.status_value);
                paymentMethod=itemView.findViewById(R.id.payment_method_value);
                id = itemView.findViewById(R.id.order_id);
                pickUpFromTitle = itemView.findViewById(R.id.businessName_title);

                itemView.setOnClickListener(view -> {
                    if(recycleSubWalletClickInterface != null){
                        int position = getAdapterPosition();

                        if(position != RecyclerView.NO_POSITION){
                            recycleSubWalletClickInterface.onItemClick(position, status.getText().toString());
                        }
                    }
                });
            }
        }
}
