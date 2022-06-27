package com.example.hwang.smartcart.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.hwang.smartcart.R;
import com.example.hwang.smartcart.classModels.Setting;

import java.util.List;

public class RecyclerViewAdapterShoppingList extends RecyclerView.Adapter<RecyclerViewAdapterShoppingList.ViewHolder> {

    Context context;

    public List<GetDataAdapter> getDataAdapter;

    ImageLoader imageLoader1;

    public String URL = Setting.URL +"uploads/";

    public RecyclerViewAdapterShoppingList(List<GetDataAdapter> getDataAdapter, Context context){

        super();
        this.getDataAdapter = getDataAdapter;
        this.context = context;
    }

    public void removeItem(int position)
    {

    }

    @Override
    public RecyclerViewAdapterShoppingList.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shoppinglistitem, parent, false);

        //v.setOnClickListener();
        RecyclerViewAdapterShoppingList.ViewHolder viewHolder = new RecyclerViewAdapterShoppingList.ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapterShoppingList.ViewHolder Viewholder, final int position) {


        final GetDataAdapter getDataAdapter1 =  getDataAdapter.get(position);

        imageLoader1 = ServerImageParseAdapter.getInstance(context).getImageLoader();

        imageLoader1.get(URL+getDataAdapter1.getImageServerUrl(),
                ImageLoader.getImageListener(
                        Viewholder.networkImageView,//Server Image
                        R.mipmap.ic_launcher,//Before loading server image the default showing image.
                        android.R.drawable.ic_dialog_alert //Error image if requested image dose not found on server.
                )
        );

        Viewholder.networkImageView.setImageUrl(URL+getDataAdapter1.getImageServerUrl(), imageLoader1);

        Viewholder.ImageTitleNameView.setText(getDataAdapter1.getImageTitleName());

        Viewholder.TextViewPrice.setText(getDataAdapter1.getProductPrice());

        if(getDataAdapter1.getAmount() != null) {
            Viewholder.TextAmount.setText(getDataAdapter1.getAmount() + " 개");

            Viewholder.TextTotal.setText((Integer.parseInt(getDataAdapter1.getProductPrice()) * Integer.parseInt(getDataAdapter1.getAmount())) + " 원");
        }
    }

    @Override
    public int getItemCount() {

        return getDataAdapter.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView ImageTitleNameView;
        public TextView TextViewPrice;
        public TextView TextAmount;
        public TextView TextTotal;
        public NetworkImageView networkImageView ;

        public ViewHolder(View itemView) {

            super(itemView);

            ImageTitleNameView = (TextView) itemView.findViewById(R.id.name) ;

            TextViewPrice = itemView.findViewById(R.id.price);
            TextAmount= itemView.findViewById(R.id.txt_amount);
            TextTotal= itemView.findViewById(R.id.txt_total);

            networkImageView = (NetworkImageView) itemView.findViewById(R.id.image) ;

        }
    }
}
