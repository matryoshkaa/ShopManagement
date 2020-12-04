package com.example.shopmanagement;
import android.content.Context;
import android.graphics.Color;
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

        TextView id = (TextView) listItem.findViewById(R.id.itemid);
        id.setText(currentProduct.getItemId());

        TextView name = (TextView) listItem.findViewById(R.id.prodNameStore);
        name.setText(currentProduct.getProductName());

        TextView supplier = (TextView) listItem.findViewById(R.id.supplierName);
        supplier.setText(currentProduct.getSupplierName());

        TextView sellingPrice = (TextView) listItem.findViewById(R.id.sellingPrice);
        sellingPrice.setText(Double.toString(currentProduct.getSellingPrice()));

        TextView amt = (TextView) listItem.findViewById(R.id.amt);
        int amtI = currentProduct.getProdAmount();
        if (amtI == 0)
        {
            amt.setTextColor(Color.parseColor("#F0473E"));
            amt.setText("Out of Stock");

        } else {
            amt.setTextColor(Color.parseColor("#5E5D5D"));
            amt.setText(Integer.toString(currentProduct.getProdAmount()));

        }


        return listItem;
    }
}
