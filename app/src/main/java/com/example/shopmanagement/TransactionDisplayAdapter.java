package com.example.shopmanagement;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TransactionDisplayAdapter extends ArrayAdapter<TransactionDisplay> {

    private Context mContext;
    private ArrayList<TransactionDisplay> transactionList = new ArrayList<>();

    public TransactionDisplayAdapter(Context context, ArrayList<TransactionDisplay> list) {

        super(context, 0 , list);
        mContext = context;
        transactionList = list;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = convertView;

        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.transactiondisplay_layout, parent, false);

        TransactionDisplay currentProduct = transactionList.get(position);

        TextView id = (TextView) listItem.findViewById(R.id.transactionId);
        id.setText(currentProduct.getId());


        TextView name = (TextView) listItem.findViewById(R.id.productName);
        name.setText(currentProduct.getItemName());

        TextView date = (TextView) listItem.findViewById(R.id.date);
        date.setText(currentProduct.getDate());

        TextView sellingPrice = (TextView) listItem.findViewById(R.id.price);
        sellingPrice.setText(Double.toString(currentProduct.getAmountPaidD()));

        TextView amt = (TextView) listItem.findViewById(R.id.amtSold);
        amt.setText(Integer.toString(currentProduct.getAmountPurchasedI()));


        return listItem;
    }
}
