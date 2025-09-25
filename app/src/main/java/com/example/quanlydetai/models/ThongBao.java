package com.example.quanlydetai.models;

import java.io.Serializable;

public class ThongBao implements Serializable {
    private String id;
    private String tieuDe;
    private String noiDung;
    private String ngayGui;
    private String nguoiGui;
    private String nguoiNhan;

    public ThongBao() {}

    public ThongBao(String id, String tieuDe, String noiDung, String ngayGui, String nguoiGui, String nguoiNhan) {
        this.id = id;
        this.tieuDe = tieuDe;
        this.noiDung = noiDung;
        this.ngayGui = ngayGui;
        this.nguoiGui = nguoiGui;
        this.nguoiNhan = nguoiNhan;
    }

    // Getter & Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTieuDe() { return tieuDe; }
    public void setTieuDe(String tieuDe) { this.tieuDe = tieuDe; }

    public String getNoiDung() { return noiDung; }
    public void setNoiDung(String noiDung) { this.noiDung = noiDung; }

    public String getNgayGui() { return ngayGui; }
    public void setNgayGui(String ngayGui) { this.ngayGui = ngayGui; }

    public String getNguoiGui() { return nguoiGui; }
    public void setNguoiGui(String nguoiGui) { this.nguoiGui = nguoiGui; }

    public String getNguoiNhan() { return nguoiNhan; }
    public void setNguoiNhan(String nguoiNhan) { this.nguoiNhan = nguoiNhan; }
}

