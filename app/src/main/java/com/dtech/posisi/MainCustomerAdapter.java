package com.dtech.posisi;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dtech.orm.Customer;
import com.dtech.orm.ImageCustomer;

import java.util.List;

/**
 * Created by Administrator on 28/08/2015.
 */
public class MainCustomerAdapter extends RecyclerView.Adapter<MainCustomerAdapter.MainViewHolder> {

    private List<Customer> dataCustomer;
    private Context context;
    String handleLat;
    String handleLong;

    public MainCustomerAdapter(List<Customer> customers){
        this.dataCustomer = customers;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_row, parent,false);
        MainViewHolder holder=new MainViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MainViewHolder holder, int position) {
        Customer customer = dataCustomer.get(position);
        holder.title.setText(customer.getName());
        holder.address.setText(customer.getAddress());
        holder.lLat.setText(customer.getLatTude());
        holder.lLong.setText(customer.getLongTude());
        // show an image
        // edhan 093015.0057 ada 2 cara untuk ambil last image, tinggal pilih mana yang enak
//        String lastImage = ImageCustomer.getLastImageRecord(customer).getImageTest();
        String lastImage = customer.lastImage();
        if (lastImage != null) {
            holder.showUpImageHolder.setImageBitmap(
                    ImageCustomer.decodeImage(lastImage));
        }
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*handleLat = holder.lLat.getText().toString();
                handleLong = holder.lLong.getText().toString();
                Intent gotoMaps = new Intent(context, CekMapsActivity.class);
                gotoMaps.putExtra("intentLat", handleLat);
                gotoMaps.putExtra("intentLong", handleLong);
                context.startActivity(gotoMaps);*/
               /* handleString = holder.title.getText().toString();
                //Toast.makeText(context, "Name Clicked "+handleString, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, ShowLogActivity.class);
                intent.putExtra("stringHandled", handleString);
                context.startActivity(intent);*/
            }
        });

        holder.rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleLat = holder.lLat.getText().toString();
                handleLong = holder.lLong.getText().toString();
                Intent gotoMaps = new Intent(context, CekMapsActivity.class);
                gotoMaps.putExtra("intentLat", handleLat);
                gotoMaps.putExtra("intentLong", handleLong);
                context.startActivity(gotoMaps);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataCustomer.size();
    }

    class MainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title;
        TextView address;
        TextView lLat;
        TextView lLong;
        ImageView showUpImageHolder;
        RelativeLayout rel;

        public MainViewHolder(View itemView) {
            super(itemView);
            rel = (RelativeLayout) itemView.findViewById(R.id.rel);

            context=itemView.getContext();
            title=(TextView)itemView.findViewById(R.id.mlistnama);
            address = (TextView) itemView.findViewById(R.id.mlistaddress);
            lLat = (TextView) itemView.findViewById(R.id.lLat);
            lLong = (TextView) itemView.findViewById(R.id.lLong);
            showUpImageHolder = (ImageView) itemView.findViewById(R.id.showUp);
        }

        @Override
        public void onClick(View view) {
           // Toast.makeText(context,"Clicked",Toast.LENGTH_SHORT).show();
           /* handleLat = lLat.getText().toString();
            handleLong = lLong.getText().toString();
            Intent gotoMaps = new Intent(context, CekMapsActivity.class);
            gotoMaps.putExtra("intentLat", handleLat);
            gotoMaps.putExtra("intentLong", handleLong);
            context.startActivity(gotoMaps);*/
        }
    }
}
