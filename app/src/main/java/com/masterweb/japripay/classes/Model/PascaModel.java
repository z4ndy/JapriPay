package com.masterweb.japripay.classes.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PascaModel {
    @SerializedName("nama")
    @Expose
    private String nama;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("sn")
    @Expose
    private String sn;
    @SerializedName("admin")
    @Expose
    private String admin;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("saldo")
    @Expose
    private String saldo;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("invoice")
    @Expose
    private String invoice;
    @SerializedName("messages")
    @Expose
    private String messages;

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getSn() { return sn; }
    public void setSn(String sn) { this.sn = sn; }
    public String getAdmin() { return admin; }
    public void setAdmin(String admin) { this.admin = admin; }
    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }
    public String getDesc() { return desc; }
    public void setDesc(String desc) { this.desc = desc; }
    public String getSaldo() { return saldo; }
    public void setSaldo(String saldo) { this.saldo = saldo; }
    public String getTotal() { return total; }
    public void setTotal(String total) { this.total = total; }
    public String getInvoice() { return invoice; }
    public void setInvoice(String invoice) { this.invoice = invoice; }
    public String getMessages() { return messages; }
    public void setMessages(String messages) { this.messages = messages; }
}
