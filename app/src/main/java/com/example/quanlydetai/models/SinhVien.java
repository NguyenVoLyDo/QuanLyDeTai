package com.example.quanlydetai.models;

import java.io.Serializable;

public class SinhVien implements Serializable {
    private String maSV;
    private String hoTen;
    private String email;
    private String lop;
    private String nganh;

    public SinhVien() {
        // Bắt buộc cần constructor rỗng cho Firestore
    }

    public SinhVien(String maSV, String hoTen, String email, String lop, String nganh) {
        this.maSV = maSV;
        this.hoTen = hoTen;
        this.email = email;
        this.lop = lop;
        this.nganh = nganh;
    }

    public String getMaSV() { return maSV; }
    public void setMaSV(String maSV) { this.maSV = maSV; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getLop() { return lop; }
    public void setLop(String lop) { this.lop = lop; }

    public String getNganh() { return nganh; }
    public void setNganh(String nganh) { this.nganh = nganh; }
}
