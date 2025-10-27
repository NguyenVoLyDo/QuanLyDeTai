package com.example.quanlydetai.interfaces;

import com.example.quanlydetai.models.TaiKhoan;

public interface OnAccountActionListener {
    void onEdit(TaiKhoan taiKhoan);
    void onDelete(TaiKhoan taiKhoan);
}
