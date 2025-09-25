package com.example.quanlydetai.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlydetai.R;
import com.example.quanlydetai.models.BaoCao;

import java.util.List;

public class KetQuaAdapter extends RecyclerView.Adapter<KetQuaAdapter.KetQuaViewHolder> {
    private List<BaoCao> list;
    private Context context;

    public KetQuaAdapter(List<BaoCao> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public KetQuaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ketqua, parent, false);
        return new KetQuaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull KetQuaViewHolder holder, int position) {
        BaoCao bc = list.get(position);

        holder.txtTenBaoCao.setText(bc.getTenBaoCao());
        holder.txtLinkFile.setText(bc.getLinkFile() != null ? bc.getLinkFile() : "Chưa có file");
        holder.txtDiem.setText("Điểm: " + (bc.getDiem() != null ? bc.getDiem() : "-"));
        holder.txtNhanXet.setText("Nhận xét: " + (bc.getNhanXet() != null ? bc.getNhanXet() : "-"));
        holder.txtNgayCham.setText("Ngày chấm: " + (bc.getNgayCham() != null ? bc.getNgayCham() : "-"));

        // click mở link file
        holder.txtLinkFile.setOnClickListener(v -> {
            if (bc.getLinkFile() != null && !bc.getLinkFile().isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(bc.getLinkFile()));
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "Chưa có file để mở", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class KetQuaViewHolder extends RecyclerView.ViewHolder {
        TextView txtTenBaoCao, txtLinkFile, txtDiem, txtNhanXet, txtNgayCham;

        public KetQuaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenBaoCao = itemView.findViewById(R.id.txtTenBaoCao);
            txtLinkFile = itemView.findViewById(R.id.txtLinkFile);
            txtDiem = itemView.findViewById(R.id.txtDiem);
            txtNhanXet = itemView.findViewById(R.id.txtNhanXet);
            txtNgayCham = itemView.findViewById(R.id.txtNgayCham);
        }
    }
}
