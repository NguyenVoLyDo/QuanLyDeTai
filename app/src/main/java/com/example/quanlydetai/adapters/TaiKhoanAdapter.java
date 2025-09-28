package com.example.quanlydetai.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.quanlydetai.R;
import com.example.quanlydetai.models.TaiKhoan;

import java.util.List;

public class TaiKhoanAdapter extends BaseAdapter {

    private final Context context;
    private final List<TaiKhoan> taiKhoanList;

    public TaiKhoanAdapter(Context context, List<TaiKhoan> taiKhoanList){
        this.context = context;
        this.taiKhoanList = taiKhoanList;
    }

    @Override
    public int getCount() { return taiKhoanList.size(); }

    @Override
    public Object getItem(int position) { return taiKhoanList.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_taikhoan, parent, false);
        }

        TextView txtHoTen = convertView.findViewById(R.id.txtHoTen);
        TextView txtTenDangNhap = convertView.findViewById(R.id.txtTenDangNhap);
        TextView txtRole = convertView.findViewById(R.id.txtRole);

        TaiKhoan tk = taiKhoanList.get(position);

        txtHoTen.setText(tk.getHoTen());
        txtTenDangNhap.setText(tk.getTenDangNhap());

        // Hiển thị "Chưa phê duyệt" nếu role chưa được cấp
        if(tk.getTenLoaiTK().equals("Chưa phân quyền")){
            txtRole.setText("Chưa phê duyệt");
            txtRole.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));
        } else {
            txtRole.setText(tk.getTenLoaiTK());
            txtRole.setTextColor(ContextCompat.getColor(context, android.R.color.black));
        }

        return convertView;
    }
}
