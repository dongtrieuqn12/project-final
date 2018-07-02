package com.example.user.bk_android.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.bk_android.R;
import com.example.user.bk_android.TableList.Table_waiting;

import java.util.List;

public class RecyclerViewAdapter_waitingXe50 extends RecyclerView.Adapter<RecyclerViewAdapter_waitingXe50.MyViewHolder> {

    List<Table_waiting> tableList_waiting;

    public RecyclerViewAdapter_waitingXe50(List<Table_waiting> tableList_waiting) {
        this.tableList_waiting = tableList_waiting;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_layout_waiting,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Table_waiting Table_waiting = tableList_waiting.get(position);
        holder.tv_bienso.setText((CharSequence) Table_waiting.getBienso());
        holder.tv_time.setText(Table_waiting.getTime());
        holder.tv_vantoc.setText(String.valueOf(Table_waiting.getVantoc()));
        holder.tv_khoangcach.setText(String.valueOf(Table_waiting.getKhoangcach()));
    }

    @Override
    public int getItemCount() {
        return tableList_waiting.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_bienso,tv_time,tv_vantoc,tv_khoangcach;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_bienso = itemView.findViewById(R.id.tv_bienso);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_vantoc = itemView.findViewById(R.id.tv_vantoc);
            tv_khoangcach = itemView.findViewById(R.id.tv_khoangcach);
        }
    }
}
