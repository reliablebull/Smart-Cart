package com.example.hwang.smartcart.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.hwang.smartcart.R;
import com.example.hwang.smartcart.classModels.Setting;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;

    public List<GetDataAdapter> getDataAdapter;

    ImageLoader imageLoader1;

    public String URL = Setting.URL +"uploads/";

    public RecyclerViewAdapter(List<GetDataAdapter> getDataAdapter, Context context){

        super();
        this.getDataAdapter = getDataAdapter;
        this.context = context;
    }

    public void removeItem(int position)
    {

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem, parent, false);

        //v.setOnClickListener();
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    // google에 검색해서 Recyclerview 동작원리 보시는게 더 빠를겁니다...
    @Override
    public void onBindViewHolder(ViewHolder Viewholder, final int position) {


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

        Viewholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(view.getContext() , getDataAdapter.get(position).ImageTitleName +"" , Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(view.getContext() , ProductInfoActivity.class);
                intent.putExtra("URL" , getDataAdapter.get(position).ImageServerUrl);
                intent.putExtra("NAME" , getDataAdapter.get(position).ImageTitleName);
                intent.putExtra("PRICE" , getDataAdapter.get(position).ProductPrice);

                Log.e("NAME",getDataAdapter.get(position).ImageTitleName);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {

        return getDataAdapter.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView ImageTitleNameView;
        public TextView TextViewPrice;
        public NetworkImageView networkImageView ;

        public ViewHolder(View itemView) {

            super(itemView);

            ImageTitleNameView = (TextView) itemView.findViewById(R.id.name) ;

            TextViewPrice = itemView.findViewById(R.id.price);

            networkImageView = (NetworkImageView) itemView.findViewById(R.id.image) ;

        }
    }
}