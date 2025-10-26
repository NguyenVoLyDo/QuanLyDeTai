package com.example.quanlydetai.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quanlydetai.R;
import com.example.quanlydetai.models.DeTaiGoiY;

import java.util.List;

public class DeTaiGoiYAdapter extends RecyclerView.Adapter<DeTaiGoiYAdapter.ViewHolder> {

    private List<DeTaiGoiY> list;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(View view, DeTaiGoiY deTai);
    }

    public DeTaiGoiYAdapter(List<DeTaiGoiY> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_detai_goiy, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int i) {
        DeTaiGoiY d = list.get(i);
        h.ten.setText(d.getTenDeTai());
        h.moTa.setText(d.getMoTa());

        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(v, d);
        });
    }

    @Override public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView ten, moTa;
        ViewHolder(View v) {
            super(v);
            ten = v.findViewById(R.id.txtTenDeTai);
            moTa = v.findViewById(R.id.txtMoTa);
        }
    }
}