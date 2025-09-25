package com.example.quanlydetai.models;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class BaoCao implements Serializable {
    private String baoCaoid;
    private String tenBaoCao;
    private String linkFile;
    private String sinhVienId;
    private String deTaiId;
    private String ngayNop;
    private String trangThai;
    private String diem;        // điểm do GV chấm
    private String nhanXet;     // nhận xét do GV chấm
    private String ngayCham;    // ngày chấm

    public BaoCao() {}

    public BaoCao(String baoCaoid, String tenBaoCao, String linkFile, String sinhVienId,
                  String deTaiId, String ngayNop, String trangThai,
                  String diem, String nhanXet, String ngayCham) {
        this.baoCaoid = baoCaoid;
        this.tenBaoCao = tenBaoCao;
        this.linkFile = linkFile;
        this.sinhVienId = sinhVienId;
        this.deTaiId = deTaiId;
        this.ngayNop = ngayNop;
        this.trangThai = trangThai;
        this.diem = diem;
        this.nhanXet = nhanXet;
        this.ngayCham = ngayCham;
    }

    // Getter & Setter
    public String getBaoCaoId() { return baoCaoid; }
    public void setBaoCaoId(String id) { this.baoCaoid = id; }

    public String getTenBaoCao() { return tenBaoCao; }
    public void setTenBaoCao(String tenBaoCao) { this.tenBaoCao = tenBaoCao; }

    public String getLinkFile() { return linkFile; }
    public void setLinkFile(String linkFile) { this.linkFile = linkFile; }

    public String getSinhVienId() { return sinhVienId; }
    public void setSinhVienId(String sinhVienId) { this.sinhVienId = sinhVienId; }

    public String getDeTaiId() { return deTaiId; }
    public void setDeTaiId(String deTaiId) { this.deTaiId = deTaiId; }

    public String getNgayNop() { return ngayNop; }
    public void setNgayNop(String ngayNop) { this.ngayNop = ngayNop; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getDiem() { return diem; }
    public void setDiem(String diem) { this.diem = diem; }

    public String getNhanXet() { return nhanXet; }
    public void setNhanXet(String nhanXet) { this.nhanXet = nhanXet; }

    public String getNgayCham() { return ngayCham; }
    public void setNgayCham(String ngayCham) { this.ngayCham = ngayCham; }
}
