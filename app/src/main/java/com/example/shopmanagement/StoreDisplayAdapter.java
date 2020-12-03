package com.example.shopmanagement;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class StoreDisplayAdapter extends ArrayAdapter<StoreDisplay> {

    private Context mContext;
    private ArrayList<StoreDisplay> storeList = new ArrayList<>();

    public StoreDisplayAdapter(Context context, ArrayList<StoreDisplay> list) {

        super(context, 0 , list);
        mContext = context;
        storeList = list;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = convertView;

        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.storedisplay_layout, parent, false);

        StoreDisplay currentProduct = storeList.get(position);

        TextView name = (TextView) listItem.findViewById(R.id.prodNameStore);
        name.setText(currentProduct.getProductName());

        TextView supplier = (TextView) listItem.findViewById(R.id.supplierName);
        supplier.setText(currentProduct.getSupplierName());

        TextView sellingPrice = (TextView) listItem.findViewById(R.id.sellingPrice);
        sellingPrice.setText(Double.toString(currentProduct.getSellingPrice()));

        TextView amt = (TextView) listItem.findViewById(R.id.amt);
        amt.setText(Integer.toString(currentProduct.getProdAmount()));


        return listItem;
    }
}
