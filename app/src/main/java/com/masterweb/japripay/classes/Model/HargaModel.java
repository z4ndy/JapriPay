package com.masterweb.japripay.classes.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HargaModel {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("kategori")
    @Expose
    private String kategori;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("sale")
    @Expose
    private String sale;
    @SerializedName("members")
    @Expose
    private String members;
    @SerializedName("admin")
    @Expose
    private String admin;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("pelanggan")
    @Expose
    private String pelanggan;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("brand")
    @Expose
    private String brand;
    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getPelanggan() { return pelanggan; }
    public void setPelanggan(String pelanggan) { this.pelanggan = pelanggan; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDesc() { return desc; }
    public void setDesc(String desc) { this.desc = desc; }
    public String getSale() { return sale; }
    public void setSale(String sale) { this.sale = sale; }
    public String getMembers() { return members; }
    public void setMembers(String members) { this.members = members; }
    public String getAdmin() { return admin; }
    public void setAdmin(String admin) { this.admin = admin; }
}
