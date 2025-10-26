package com.example.quanlydetai.models;

public class DeTaiGoiY {
    private String deTaiGoiYId;
    private String tenDeTai;
    private String moTa;
    private String giangVienId;

    public DeTaiGoiY() {}

    public DeTaiGoiY(String deTaiGoiYId, String tenDeTai, String moTa, String giangVienId) {
        this.deTaiGoiYId = deTaiGoiYId;
        this.tenDeTai = tenDeTai;
        this.moTa = moTa;
        this.giangVienId = giangVienId;
    }

    public String getDeTaiGoiYId() { return deTaiGoiYId; }
    public void setDeTaiGoiYId(String deTaiGoiYId) { this.deTaiGoiYId = deTaiGoiYId; }

    public String getTenDeTai() { return tenDeTai; }
    public void setTenDeTai(String tenDeTai) { this.tenDeTai = tenDeTai; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    public String getGiangVienId() { return giangVienId; }
    public void setGiangVienId(String giangVienId) { this.giangVienId = giangVienId; }
}
