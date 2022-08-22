package com.masterweb.japripay.classes.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BeritaModel {
    @SerializedName("judul")
    @Expose
    private String judul;
    @SerializedName("subjudul")
    @Expose
    private String subjudul;
    @SerializedName("konten")
    @Expose
    private String konten;
    @SerializedName("tanggal")
    @Expose
    private String tanggal;
    @SerializedName("hari")
    @Expose
    private String hari;
    @SerializedName("jam")
    @Expose
    private String jam;
    @SerializedName("images")
    @Expose
    private String images;
    @SerializedName("type")
    @Expose
    private String type;
    public String getJudul() { return judul; }
    public void setJudul(String judul) { this.judul = judul; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getSubjudul() { return subjudul; }
    public void setSubjudul(String subjudul) { this.subjudul = subjudul; }
    public String getKonten() { return konten; }
    public void setKonten(String konten) { this.konten = konten; }
    public String getTanggal() { return tanggal; }
    public void setTanggal(String tanggal) { this.tanggal = tanggal; }
    public String getHari() { return hari; }
    public void setHari(String hari) { this.hari = hari; }
    public String getImages() { return images; }
    public void setImages(String images) { this.images = images; }
    public String getJam() { return jam; }
    public void setJam(String jam) { this.jam = jam; }
}
