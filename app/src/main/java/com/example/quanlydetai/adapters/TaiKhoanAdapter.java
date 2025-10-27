package com.example.quanlydetai.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.widget.PopupMenu;
import com.example.quanlydetai.R;
import com.example.quanlydetai.interfaces.OnAccountActionListener;
import com.example.quanlydetai.models.TaiKhoan;
import java.util.List;

public class TaiKhoanAdapter extends ArrayAdapter<TaiKhoan> {

    private final Context context;
    private final List<TaiKhoan> taiKhoanList;
    private final OnAccountActionListener listener;

    public TaiKhoanAdapter(Context context, List<TaiKhoan> list, OnAccountActionListener listener) {
        super(context, R.layout.item_taikhoan, list);
        this.context = context;
        this.taiKhoanList = list;
        this.listener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_taikhoan, parent, false);
        }

        TaiKhoan tk = taiKhoanList.get(position);
        TextView tvHoTen = view.findViewById(R.id.tvHoTen);
        TextView tvTenDangNhap = view.findViewById(R.id.tvTenDangNhap);
        TextView tvLoaiTaiKhoan = view.findViewById(R.id.tvLoaiTaiKhoan);
        ImageButton btnMore = view.findViewById(R.id.btnMore);

        tvHoTen.setText(tk.getHoTen());
        tvTenDangNhap.setText("Tên đăng nhập: " + tk.getTenDangNhap());
        tvLoaiTaiKhoan.setText(tk.getTenLoaiTK());

        btnMore.setOnClickListener(v -> showPopupMenu(v, tk));

        return view;
    }

    private void showPopupMenu(View anchor, TaiKhoan tk) {
        PopupMenu popupMenu = new PopupMenu(context, anchor);
        popupMenu.getMenu().add("Sửa tài khoản");
        popupMenu.getMenu().add("Xóa tài khoản");

        popupMenu.setOnMenuItemClickListener(item -> {
            String title = item.getTitle().toString();
            if (title.equals("Sửa tài khoản")) {
                listener.onEdit(tk);
                return true;
            } else if (title.equals("Xóa tài khoản")) {
                listener.onDelete(tk);
                return true;
            }
            return false;
        });

        popupMenu.show();
    }
}
