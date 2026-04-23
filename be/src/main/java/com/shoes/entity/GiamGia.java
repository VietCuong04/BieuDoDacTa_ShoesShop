package com.shoes.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "GiamGia")
public class GiamGia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaGiamGia")
    private Integer maGiamGia;

    @Column(name = "TenGiamGia")
    private String tenGiamGia;

    @Column(name = "MaCode", unique = true)
    private String maCode;

    @Column(name = "MoTa")
    private String moTa;

    @Column(name = "MucGiamGia")
    private String mucGiamGia;

    @Column(name = "LoaiGiamGia")
    private Integer loaiGiamGia;

    @Column(name = "GiamToiDa")
    private Integer giamToiDa;

    @Column(name = "SoLuong")
    private Integer soLuong;

    @Column(name = "DieuKienApDung")
    private Integer dieuKienApDung;

    @Column(name = "NgayBatDau")
    private LocalDateTime ngayBatDau;

    @Column(name = "NgayKetThuc")
    private LocalDateTime ngayKetThuc;

    @Column(name = "TrangThai")
    private Integer trangThai;

    @Column(name = "NgayKhoiTao")
    private LocalDateTime ngayKhoiTao;

    @Column(name = "NgayCapNhat")
    private LocalDateTime ngayCapNhat;

    // Getters and Setters
    public Integer getMaGiamGia() {
        return maGiamGia;
    }

    public void setMaGiamGia(Integer maGiamGia) {
        this.maGiamGia = maGiamGia;
    }

    public String getTenGiamGia() {
        return tenGiamGia;
    }

    public void setTenGiamGia(String tenGiamGia) {
        this.tenGiamGia = tenGiamGia;
    }

    public String getMaCode() {
        return maCode;
    }

    public void setMaCode(String maCode) {
        this.maCode = maCode;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getMucGiamGia() {
        return mucGiamGia;
    }

    public void setMucGiamGia(String mucGiamGia) {
        this.mucGiamGia = mucGiamGia;
    }

    public Integer getLoaiGiamGia() {
        return loaiGiamGia;
    }

    public void setLoaiGiamGia(Integer loaiGiamGia) {
        this.loaiGiamGia = loaiGiamGia;
    }

    public Integer getGiamToiDa() {
        return giamToiDa;
    }

    public void setGiamToiDa(Integer giamToiDa) {
        this.giamToiDa = giamToiDa;
    }

    public Integer getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(Integer soLuong) {
        this.soLuong = soLuong;
    }

    public Integer getDieuKienApDung() {
        return dieuKienApDung;
    }

    public void setDieuKienApDung(Integer dieuKienApDung) {
        this.dieuKienApDung = dieuKienApDung;
    }

    public LocalDateTime getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(LocalDateTime ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public LocalDateTime getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(LocalDateTime ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public Integer getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(Integer trangThai) {
        this.trangThai = trangThai;
    }

    public LocalDateTime getNgayKhoiTao() {
        return ngayKhoiTao;
    }

    public void setNgayKhoiTao(LocalDateTime ngayKhoiTao) {
        this.ngayKhoiTao = ngayKhoiTao;
    }

    public LocalDateTime getNgayCapNhat() {
        return ngayCapNhat;
    }

    public void setNgayCapNhat(LocalDateTime ngayCapNhat) {
        this.ngayCapNhat = ngayCapNhat;
    }
}
