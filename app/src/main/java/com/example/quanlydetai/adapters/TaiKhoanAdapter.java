package com.example.quanlydetai.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.quanlydetai.R;
import com.example.quanlydetai.activitys.adminactivity.AccountFormActivity;
import com.example.quanlydetai.models.TaiKhoan;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class TaiKhoanAdapter extends BaseAdapter {

    private final Context context;
    private final List<TaiKhoan> taiKhoanList;

    public TaiKhoanAdapter(Context context, List<TaiKhoan> taiKhoanList) {
        this.context = context;
        this.taiKhoanList = taiKhoanList;
    }

    @Override
    public int getCount() {
        return taiKhoanList.size();
    }

    @Override
    public Object getItem(int position) {
        return taiKhoanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_taikhoan, parent, false);
        }

        TextView txtHoTen = convertView.findViewById(R.id.txtHoTen);
        TextView txtTenDangNhap = convertView.findViewById(R.id.txtTenDangNhap);
        TextView txtRole = convertView.findViewById(R.id.txtRole);
        Button btnSua = convertView.findViewById(R.id.btnSua);
        Button btnXoa = convertView.findViewById(R.id.btnXoa);

        TaiKhoan taiKhoan = taiKhoanList.get(position);

        txtHoTen.setText(taiKhoan.getHoTen());
        txtTenDangNhap.setText(taiKhoan.getTenDangNhap());

        if (taiKhoan.getTenLoaiTK().equals("Chưa phân quyền")) {
            txtRole.setText("Chưa phê duyệt");
            txtRole.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));
        } else {
            txtRole.setText(taiKhoan.getTenLoaiTK());
            txtRole.setTextColor(ContextCompat.getColor(context, android.R.color.black));
        }

        // ✅ Nút Sửa
        btnSua.setOnClickListener(v -> {
            Intent intent = new Intent(context, AccountFormActivity.class);
            intent.putExtra("taiKhoan", taiKhoan);
            context.startActivity(intent);
        });

        // ✅ Nút Xóa
        btnXoa.setOnClickListener(v -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(taiKhoan.getId())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        taiKhoanList.remove(taiKhoan);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(context, "Lỗi khi xóa tài khoản", Toast.LENGTH_SHORT).show()
                    );
        });

        return convertView;
    }
}
