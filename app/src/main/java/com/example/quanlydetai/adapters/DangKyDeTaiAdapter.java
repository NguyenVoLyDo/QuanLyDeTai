package com.example.quanlydetai.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlydetai.R;
import com.example.quanlydetai.models.DeTai;

import java.util.List;

public class DangKyDeTaiAdapter extends RecyclerView.Adapter<DangKyDeTaiAdapter.DeTaiViewHolder> {
    private List<DeTai> list;

    public DangKyDeTaiAdapter(List<DeTai> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public DeTaiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detai, parent, false);
        return new DeTaiViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DeTaiViewHolder holder, int position) {
        DeTai deTai = list.get(position);
        holder.txtTenDeTai.setText(deTai.getTenDeTai());
        holder.txtMoTa.setText(deTai.getMoTa());
        holder.txtTrangThai.setText("Trạng thái: " + deTai.getTrangThai());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class DeTaiViewHolder extends RecyclerView.ViewHolder {
        TextView txtTenDeTai, txtMoTa, txtTrangThai;

        public DeTaiViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenDeTai = itemView.findViewById(R.id.txtTenDeTai);
            txtMoTa = itemView.findViewById(R.id.txtMoTa);
            txtTrangThai = itemView.findViewById(R.id.txtTrangThai);
        }
    }
}
