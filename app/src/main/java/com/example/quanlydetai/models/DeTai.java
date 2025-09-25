package com.example.quanlydetai.models;
import java.io.Serializable;

public class DeTai implements Serializable {
    private String deTaiId;         // id document Firestore
    private String tenDeTai;
    private String moTa;
    private String sinhVienId;
    private String trangThai;
    private String ngayDangKy;

    public DeTai() {
        // Bắt buộc cần constructor rỗng cho Firestore
    }

    public DeTai(String deTaiId, String tenDeTai, String moTa, String sinhVienId,
                 String trangThai, String ngayDangKy) {
        this.deTaiId = deTaiId;
        this.tenDeTai = tenDeTai;
        this.moTa = moTa;
        this.sinhVienId = sinhVienId;
        this.trangThai = trangThai;
        this.ngayDangKy = ngayDangKy;
    }

    // Getter & Setter
    public String getDeTaiId() { return deTaiId; }
    public void setDeTaiId(String id) { this.deTaiId = id; }

    public String getTenDeTai() { return tenDeTai; }
    public void setTenDeTai(String tenDeTai) { this.tenDeTai = tenDeTai; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    public String getSinhVienId() { return sinhVienId; }
    public void setSinhVienId(String sinhVienId) { this.sinhVienId = sinhVienId; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getNgayDangKy() { return ngayDangKy; }
    public void setNgayDangKy(String ngayDangKy) { this.ngayDangKy = ngayDangKy; }
}
