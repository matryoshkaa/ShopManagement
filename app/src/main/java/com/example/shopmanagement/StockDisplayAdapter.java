package com.example.shopmanagement;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class StockDisplayAdapter extends ArrayAdapter<StockDisplay> {

    private Context mContext;
    private List<StockDisplay> stockList = new ArrayList<>();

    public StockDisplayAdapter(Context context, ArrayList<StockDisplay> list) {

        super(context, 0 , list);
        mContext = context;
        stockList = list;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = convertView;

        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.stockdisplay_layout, parent, false);

        StockDisplay currentProduct = stockList.get(position);

        TextView name = (TextView) listItem.findViewById(R.id.prodName);
        name.setText(currentProduct.getProductName());

        TextView supplier = (TextView) listItem.findViewById(R.id.supplierName);
        supplier.setText(currentProduct.getSupplierName());

        TextView prodId = (TextView) listItem.findViewById(R.id.productId);
        prodId.setText(currentProduct.getProductId());

        TextView unitPrice = (TextView) listItem.findViewById(R.id.unitPrice);
        unitPrice.setText(Double.toString(currentProduct.getUnitPrice()));

        TextView prodStock = (TextView) listItem.findViewById(R.id.numberInStock);
        prodStock.setText(currentProduct.getProductStock());

        return listItem;
    }
}
