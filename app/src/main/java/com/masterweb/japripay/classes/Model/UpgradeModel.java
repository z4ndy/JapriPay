package com.masterweb.japripay.classes.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpgradeModel {
    @SerializedName("keterangan")
    @Expose
    private String keterangan;
    @SerializedName("harga")
    @Expose
    private String harga;
    @SerializedName("bonus")
    @Expose
    private String bonus;

    public String getKeterangan() { return keterangan; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }
    public String getHarga() { return harga; }
    public void setHarga(String harga) { this.harga = harga; }
    public String getBonus() { return bonus; }
    public void setBonus(String bonus) { this.bonus = bonus; }
}
