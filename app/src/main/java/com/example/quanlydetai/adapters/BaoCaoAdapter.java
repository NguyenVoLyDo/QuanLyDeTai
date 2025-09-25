package com.example.quanlydetai.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlydetai.R;
import com.example.quanlydetai.models.BaoCao;

import java.util.List;

public class BaoCaoAdapter extends RecyclerView.Adapter<BaoCaoAdapter.BaoCaoViewHolder> {
    private List<BaoCao> list;

    public BaoCaoAdapter(List<BaoCao> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public BaoCaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_baocao, parent, false);
        return new BaoCaoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BaoCaoViewHolder holder, int position) {
        BaoCao bc = list.get(position);
        holder.txtTenBaoCao.setText(bc.getTenBaoCao());
        holder.txtLinkFile.setText(bc.getLinkFile());
        holder.txtNgayNop.setText("Ngày nộp: " + bc.getNgayNop());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class BaoCaoViewHolder extends RecyclerView.ViewHolder {
        TextView txtTenBaoCao, txtLinkFile, txtNgayNop;

        public BaoCaoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenBaoCao = itemView.findViewById(R.id.txtTenBaoCao);
            txtLinkFile = itemView.findViewById(R.id.txtLinkFile);
            txtNgayNop = itemView.findViewById(R.id.txtNgayNop);
        }
    }
}
