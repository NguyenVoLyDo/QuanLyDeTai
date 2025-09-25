package com.example.quanlydetai.models;
import java.io.Serializable;

public class TaiKhoan implements Serializable {
    private String id;  // uid từ FirebaseAuth
    private String tenDangNhap; // email hoặc username
    private String email;
    private String password;
    private String hoTen;
    private String maSV;
    private String maGV;
    private String tenLoaiTK;   // Sinh viên, Giảng viên, Admin, Quản lý hội đồng

    public TaiKhoan() {}

    public TaiKhoan(String id, String tenDangNhap, String email, String password,
                    String hoTen,String maSV, String maGV, String tenLoaiTK) {
        this.id = id;
        this.tenDangNhap = tenDangNhap;
        this.email = email;
        this.password = password;
        this.hoTen = hoTen;
        this.maSV = maSV;
        this.maGV = maGV;
        this.tenLoaiTK = tenLoaiTK;
    }

    // Getter & Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTenDangNhap() { return tenDangNhap; }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getTenLoaiTK() { return tenLoaiTK; }
    public void setTenLoaiTK(String tenLoaiTK) { this.tenLoaiTK = tenLoaiTK; }

    public String getMaSV() { return  maSV;}
    public void setMaSV(String maSV) { this.maSV = maSV; }

    public String getMaGV() { return  maGV;}
    public void setMaGV(String maGV) { this.maGV = maGV; }
}
