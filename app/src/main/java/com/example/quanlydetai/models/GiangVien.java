package com.example.quanlydetai.models;

import java.io.Serializable;

public class GiangVien implements Serializable {
    private String maGV;
    private String hoTen;
    private String email;
    private String khoa;
    private String boMon;

    public GiangVien() {
        // Bắt buộc cho Firestore
    }

    public GiangVien(String maGV, String hoTen, String email, String khoa, String boMon) {
        this.maGV = maGV;
        this.hoTen = hoTen;
        this.email = email;
        this.khoa = khoa;
        this.boMon = boMon;
    }

    public String getMaGV() { return maGV; }
    public void setMaGV(String maGV) { this.maGV = maGV; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getKhoa() { return khoa; }
    public void setKhoa(String khoa) { this.khoa = khoa; }

    public String getBoMon() { return boMon; }
    public void setBoMon(String boMon) { this.boMon = boMon; }
}
