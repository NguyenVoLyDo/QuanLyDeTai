package com.example.quanlydetai.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlydetai.R;
import com.example.quanlydetai.models.DeTai;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HienThiDeTaiAdapter extends RecyclerView.Adapter<HienThiDeTaiAdapter.DeTaiViewHolder>
        implements Filterable {

    private final List<DeTai> deTaiList;
    private List<DeTai> deTaiListFull;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(View view, DeTai deTai);
    }

    public HienThiDeTaiAdapter(List<DeTai> deTaiList, OnItemClickListener listener) {
        this.deTaiList = deTaiList;
        this.deTaiListFull = new ArrayList<>(deTaiList);
        this.listener = listener;
    }

    @NonNull
    @Override
    public DeTaiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_detai, parent, false);
        return new DeTaiViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DeTaiViewHolder holder, int position) {
        DeTai deTai = deTaiList.get(position);
        holder.txtTenDeTai.setText(deTai.getTenDeTai());
        holder.txtMoTa.setText(deTai.getMoTa());
        holder.txtTrangThai.setText("Trạng thái: " + deTai.getTrangThai());

        holder.itemView.setOnClickListener(v -> listener.onItemClick(v, deTai));
    }

    @Override
    public int getItemCount() {
        return deTaiList.size();
    }

    public static class DeTaiViewHolder extends RecyclerView.ViewHolder {
        TextView txtTenDeTai, txtMoTa, txtTrangThai;
        public DeTaiViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenDeTai = itemView.findViewById(R.id.txtTenDeTai);
            txtMoTa = itemView.findViewById(R.id.txtMoTa);
            txtTrangThai = itemView.findViewById(R.id.txtTrangThai);
        }
    }

    @Override
    public Filter getFilter() { return filter; }

    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<DeTai> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(deTaiListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (DeTai item : deTaiListFull) {
                    if (item.getTenDeTai().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            deTaiList.clear();
            deTaiList.addAll((List<DeTai>) results.values);
            notifyDataSetChanged();
        }
    };

    public void updateList(List<DeTai> newList) {
        deTaiList.clear();
        deTaiList.addAll(newList);
        deTaiListFull.clear();
        deTaiListFull.addAll(newList);
        notifyDataSetChanged();
    }
}
