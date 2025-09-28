package com.example.quanlydetai.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlydetai.R;
import com.example.quanlydetai.models.DeTai;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class DuyetDeTaiAdapter extends RecyclerView.Adapter<DuyetDeTaiAdapter.DeTaiViewHolder> {

    private final Context context;
    private final List<DeTai> deTaiList;
    private final FirebaseFirestore db;

    public DuyetDeTaiAdapter(Context context, List<DeTai> deTaiList) {
        this.context = context;
        this.deTaiList = deTaiList;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public DeTaiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detai, parent, false);
        return new DeTaiViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DeTaiViewHolder holder, int position) {
        DeTai dt = deTaiList.get(position);

        holder.txtTenDeTai.setText(dt.getTenDeTai());
        holder.txtMoTa.setText(dt.getMoTa());
        holder.txtTrangThai.setText("Trạng thái: " + dt.getTrangThai());

        // Ấn giữ để duyệt hoặc từ chối
        holder.itemView.setOnLongClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Duyệt đề tài")
                    .setMessage("Bạn muốn duyệt hay từ chối đề tài này?")
                    .setPositiveButton("Duyệt", (dialog, which) -> updateDeTai(dt, "approved", position))
                    .setNegativeButton("Từ chối", (dialog, which) -> updateDeTai(dt, "rejected", position))
                    .setNeutralButton("Hủy", null)
                    .show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return deTaiList.size();
    }

    private void updateDeTai(DeTai deTai, String status, int position) {
        // Cập nhật trạng thái đề tài
        db.collection("detai").document(deTai.getDeTaiId())
                .update("trangThai", status)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context,
                            "Đã " + (status.equals("approved") ? "duyệt" : "từ chối"),
                            Toast.LENGTH_SHORT).show();

                    // Xóa item khỏi list để cập nhật UI
                    deTaiList.remove(position);
                    notifyItemRemoved(position);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(context, "Lỗi xử lý: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
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
}
