package com.masterweb.japripay.classes.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultModel {
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("message")
    @Expose
    private String message;
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    @SerializedName("name_app")
    @Expose
    private String name_app;
    public String getName_app() { return name_app; }
    public void setName_app(String name_app) { this.name_app = name_app; }
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("reff")
    @Expose
    private String reff;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("saldo")
    @Expose
    private String saldo;
    @SerializedName("bonus")
    @Expose
    private String bonus;
    @SerializedName("pin")
    @Expose
    private String pin;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("saham")
    @Expose
    private String saham;
    @SerializedName("downline")
    @Expose
    private String downline;
    @SerializedName("nama")
    @Expose
    private String nama;
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPin() { return pin; }
    public void setPin(String pin) { this.pin = pin; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getReff() { return reff; }
    public void setReff(String reff) { this.reff = reff; }
    public String getSaldo() { return saldo; }
    public void setSaldo(String saldo) { this.saldo = saldo; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getBonus() { return bonus; }
    public void setBonus(String bonus) { this.bonus = bonus; }
    public String getSaham() { return saham; }
    public void setSaham(String saham) { this.saham = saham; }
    public String getDownline() { return downline; }
    public void setDownline(String downline) { this.downline = downline; }

}
