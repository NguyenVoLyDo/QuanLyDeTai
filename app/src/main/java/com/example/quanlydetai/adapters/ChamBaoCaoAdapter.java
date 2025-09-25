package com.example.quanlydetai.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlydetai.R;
import com.example.quanlydetai.models.BaoCao;
import com.google.firebase.firestore.FirebaseFirestore;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.List;


public class ChamBaoCaoAdapter extends RecyclerView.Adapter<ChamBaoCaoAdapter.BaoCaoViewHolder> {

    private Context context;
    private List<BaoCao> baoCaoList;
    private boolean isGiangVien;
    private FirebaseFirestore db;

    public ChamBaoCaoAdapter(Context context, List<BaoCao> baoCaoList, boolean isGiangVien) {
        this.context = context;
        this.baoCaoList = baoCaoList;
        this.isGiangVien = isGiangVien;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public BaoCaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_baocao, parent, false);
        return new BaoCaoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BaoCaoViewHolder holder, int position) {
        BaoCao bc = baoCaoList.get(position);

        holder.txtTenBaoCao.setText(bc.getTenBaoCao());
        holder.txtLinkFile.setText("File: " + bc.getLinkFile());
        holder.txtNgayNop.setText("Ngày nộp: " + bc.getNgayNop());

        // Click mở file từ URL Firestore
        holder.txtLinkFile.setOnClickListener(v -> {
            String url = bc.getLinkFile();
            if (url != null && !url.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "File không tồn tại", Toast.LENGTH_SHORT).show();
            }
        });

        if (isGiangVien) {
            holder.txtDiem.setVisibility(View.VISIBLE);
            holder.txtNhanXet.setVisibility(View.VISIBLE);
            holder.txtDiem.setText("Điểm: " + (bc.getDiem() == null ? "-" : bc.getDiem()));
            holder.txtNhanXet.setText("Nhận xét: " + (bc.getNhanXet() == null ? "-" : bc.getNhanXet()));

            // Ấn giữ để chấm điểm + nhận xét
            holder.itemView.setOnLongClickListener(v -> {
                showChamDiemDialog(bc, position);
                return true;
            });
        } else {
            holder.txtDiem.setVisibility(View.GONE);
            holder.txtNhanXet.setVisibility(View.GONE);
        }
    }

    private void showChamDiemDialog(BaoCao bc, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chấm báo cáo");

        EditText edtDiem = new EditText(context);
        edtDiem.setHint("Điểm");
        edtDiem.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        if (bc.getDiem() != null) edtDiem.setText(bc.getDiem());

        EditText edtNhanXet = new EditText(context);
        edtNhanXet.setHint("Nhận xét");
        if (bc.getNhanXet() != null) edtNhanXet.setText(bc.getNhanXet());

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(edtDiem);
        layout.addView(edtNhanXet);
        layout.setPadding(20, 20, 20, 20);

        builder.setView(layout);

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String diem = edtDiem.getText().toString().trim();
            String nhanXet = edtNhanXet.getText().toString().trim();
            String ngayCham = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());


            db.collection("baocao").document(bc.getBaoCaoId())
                    .update("diem", diem, "nhanXet", nhanXet, "trangThai", "Đã chấm", "ngayCham", ngayCham)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(context, "Đã chấm điểm", Toast.LENGTH_SHORT).show();
                        bc.setDiem(diem);
                        bc.setNhanXet(nhanXet);
                        notifyItemChanged(position);
                    })
                    .addOnFailureListener(e -> Toast.makeText(context, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });

        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    @Override
    public int getItemCount() {
        return baoCaoList.size();
    }

    static class BaoCaoViewHolder extends RecyclerView.ViewHolder {
        TextView txtTenBaoCao, txtLinkFile, txtNgayNop, txtDiem, txtNhanXet;

        public BaoCaoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenBaoCao = itemView.findViewById(R.id.txtTenBaoCao);
            txtLinkFile = itemView.findViewById(R.id.txtLinkFile);
            txtNgayNop = itemView.findViewById(R.id.txtNgayNop);
            txtDiem = itemView.findViewById(R.id.txtDiem);
            txtNhanXet = itemView.findViewById(R.id.txtNhanXet);
        }
    }
}
