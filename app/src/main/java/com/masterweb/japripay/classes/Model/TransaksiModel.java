package com.masterweb.japripay.classes.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransaksiModel {
    @SerializedName("invoice")
    @Expose
    private String invoice;
    @SerializedName("product")
    @Expose
    private String produk;
    @SerializedName("customers")
    @Expose
    private String customers;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("created_at")
    @Expose
    private String created_at;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("masuk")
    @Expose
    private String masuk;
    @SerializedName("keluar")
    @Expose
    private String keluar;
    @SerializedName("saldo")
    @Expose
    private String saldo;
    @SerializedName("sale")
    @Expose
    private String sale;
    @SerializedName("admin")
    @Expose
    private String admin;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("sn")
    @Expose
    private String sn;
    public String getSaldo() { return saldo; }
    public void setSaldo(String saldo) { this.saldo = saldo; }

    public String getMasuk() { return masuk; }
    public void setMasuk(String masuk) { this.masuk = masuk; }
    public String getKeluar() { return keluar; }
    public void setKeluar(String keluar) { this.keluar = keluar; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getCreated_at() { return created_at; }
    public void setCreated_at(String created_at) { this.created_at = created_at; }
    public String getInvoice() { return invoice; }
    public void setInvoice(String invoice) { this.invoice = invoice; }
    public String getProduk() { return produk; }
    public void setProduk(String produk) { this.produk = produk; }
    public String getCustomers() { return customers; }
    public void setCustomers(String customers) { this.customers = customers; }
    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getDesc() { return desc; }
    public void setDesc(String desc) { this.desc = desc; }
    public String getSale() { return sale; }
    public void setSale(String sale) { this.sale = sale; }
    public String getAdmin() { return admin; }
    public void setAdmin(String admin) { this.admin = admin; }
    public String getTotal() { return total; }
    public void setTotal(String total) { this.total = total; }
    public String getSn() { return sn; }
    public void setSn(String sn) { this.sn = sn; }

}
