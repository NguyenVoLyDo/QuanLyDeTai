package com.example.quanlydetai.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlydetai.R;
import com.example.quanlydetai.models.ThongBao;

import java.util.List;

public class ThongBaoAdapter extends RecyclerView.Adapter<ThongBaoAdapter.ThongBaoViewHolder> {
    private final List<ThongBao> list;

    public ThongBaoAdapter(List<ThongBao> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ThongBaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thongbao, parent, false);
        return new ThongBaoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ThongBaoViewHolder holder, int position) {
        ThongBao tb = list.get(position);
        holder.txtTieuDe.setText(tb.getTieuDe());
        holder.txtNoiDung.setText(tb.getNoiDung());
        holder.txtNguoiGui.setText("Người gửi: " + tb.getNguoiGui());
        holder.txtNgayGui.setText("Ngày gửi: " + tb.getNgayGui());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ThongBaoViewHolder extends RecyclerView.ViewHolder {
        TextView txtTieuDe, txtNoiDung, txtNgayGui,txtNguoiGui;

        public ThongBaoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTieuDe = itemView.findViewById(R.id.txtTieuDe);
            txtNoiDung = itemView.findViewById(R.id.txtNoiDung);
            txtNguoiGui = itemView.findViewById(R.id.txtNguoiGui);
            txtNgayGui = itemView.findViewById(R.id.txtNgayGui);
        }
    }
}