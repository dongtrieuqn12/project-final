package com.example.user.bk_android.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.bk_android.R;
import com.example.user.bk_android.TableList.Table;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    List<Table> tableList;
    //private final View.OnClickListener mOnClickListener = new MyOnClickListener();

    public RecyclerViewAdapter(List<Table> tableList) {
        this.tableList = tableList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recyclerview_tuyenxe,parent,false);
        //itemView.setOnClickListener(mOnClickListener);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Table table = tableList.get(position);
        holder.im_tentuyenxe.setImageResource(table.getImage());
        holder.tvtuyenxe.setText(table.getString1());
        holder.tvTramDauCuoi.setText(table.getString2());


    }

    @Override
    public int getItemCount() {
        return tableList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView im_tentuyenxe;
        TextView tvtuyenxe,tvTramDauCuoi;
        public MyViewHolder(View itemView) {
            super(itemView);
            im_tentuyenxe = itemView.findViewById(R.id.im_tentuyenxe);
            tvtuyenxe = itemView.findViewById(R.id.tvtuyenxe);
            tvTramDauCuoi = itemView.findViewById(R.id.tvTramDauCuoi);
        }
    }
}
