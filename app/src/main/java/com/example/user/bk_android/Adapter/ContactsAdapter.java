package com.example.user.bk_android.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.bk_android.R;
import com.example.user.bk_android.TableList.Table;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<Table> tableList;
    private List<Table> tableListFiltered;
    private ContactsAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvtuyenxe, tvTramDauCuoi;

        public ImageView im_tentuyenxe;

        public MyViewHolder(View view) {
            super(view);
            tvtuyenxe = view.findViewById(R.id.tvtuyenxe);
            tvTramDauCuoi = view.findViewById(R.id.tvTramDauCuoi);
            im_tentuyenxe = view.findViewById(R.id.im_tentuyenxe);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(tableListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public ContactsAdapter(Context context, List<Table> tableList, ContactsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.tableList = tableList;
        this.tableListFiltered = tableList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Table table = tableListFiltered.get(position);
        holder.tvtuyenxe.setText(table.getString1());
        holder.tvTramDauCuoi.setText(table.getString2());
        holder.im_tentuyenxe.setImageResource(table.getImage());
    }

    @Override
    public int getItemCount() {
        return tableListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    tableListFiltered = tableList;
                } else {
                    List<Table> filteredList = new ArrayList<>();
                    for (Table row : tableList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getString1().toLowerCase().contains(charString.toLowerCase()) || row.getString2().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    tableListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = tableListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                tableListFiltered = (ArrayList<Table>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(Table table);
    }
}
