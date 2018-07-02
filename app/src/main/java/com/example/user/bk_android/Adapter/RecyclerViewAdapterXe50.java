package com.example.user.bk_android.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.bk_android.R;
import com.example.user.bk_android.TableList.Table;
import com.example.user.bk_android.TableList.TableXe50;
import com.google.android.gms.drive.query.Filter;

import java.util.List;

public class RecyclerViewAdapterXe50 extends RecyclerView.Adapter<RecyclerViewAdapterXe50.MyViewHolder> {

    List<TableXe50> tableListXe50;
    //private final View.OnClickListener mOnClickListener = new MyOnClickListener();

    public RecyclerViewAdapterXe50(List<TableXe50> tableListXe50) {
        this.tableListXe50 = tableListXe50;
    }

    @Override
    public RecyclerViewAdapterXe50.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chuyenxe50,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapterXe50.MyViewHolder holder, int position) {
        TableXe50 tableXe50 = tableListXe50.get(position);
        holder.tv_tramchuyenxe50.setText((CharSequence) tableXe50.getString());
    }

    @Override
    public int getItemCount() {
        return tableListXe50.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_tramchuyenxe50;
        public MyViewHolder(View itemView) {
            super(itemView);

            tv_tramchuyenxe50 = itemView.findViewById(R.id.tv_tramchuyenxe50);
        }
    }

    
}
