package com.masterweb.japripay.classes.api;


import com.masterweb.japripay.classes.Model.AuthModel;
import com.masterweb.japripay.classes.Model.BeritaModel;
import com.masterweb.japripay.classes.Model.HargaModel;
import com.masterweb.japripay.classes.Model.MembersModel;
import com.masterweb.japripay.classes.Model.PascaModel;
import com.masterweb.japripay.classes.Model.ResultModel;
import com.masterweb.japripay.classes.Model.TransaksiModel;
import com.masterweb.japripay.classes.Model.UpgradeModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("auth")
    Call<AuthModel> auth(
            @Field("phone") String phone,
            @Field("pin") String pin
    );

    @FormUrlEncoded
    @POST("otp")
    Call<AuthModel> sendOtp(
            @Field("phone") String phones,
            @Field("code") String code
    );

    @FormUrlEncoded
    @POST("members/tokenfcm")
    Call<AuthModel> insertToken(
            @Field("phone") String phones,
            @Field("token") String code
    );

    @FormUrlEncoded
    @POST("auth/register")
    Call<AuthModel> register(
            @Field("name") String name,
            @Field("phone") String phone,
            @Field("pin") String pin,
            @Field("upline") String reff,
            @Field("device") String device
    );

    @FormUrlEncoded
    @POST("otp/validasi")
    Call<AuthModel> validasiOtp(
            @Field("phone") String phones,
            @Field("code") String code
    );

    @FormUrlEncoded
    @POST("members")
    Call<MembersModel> getMembers(
            @Field("phone") String phones
    );

    @GET("produk/pulsa")
    Call<List<HargaModel>> get_harga();

    @GET("produk/provider_pulsa")
    Call<List<HargaModel>> get_provider();

    @GET("produk/provider_tagihan")
    Call<List<HargaModel>> get_provider_pasca();

    @GET("produk/info")
    Call<List<BeritaModel>> get_info();
    @GET("produk/promo")
    Call<List<BeritaModel>> get_promo();

    @GET("produk/tagihan")
    Call<List<HargaModel>> get_pasca();

    @GET("produk/transfer")
    Call<List<HargaModel>> get_transfer();

    @GET("produk/emoney")
    Call<HargaModel> get_emoney();

    @FormUrlEncoded
    @POST("transfer/cek_rekening")
    Call<AuthModel> cekRekening(
            @Field("rek") String phones,
            @Field("bank") String code
    );

    @FormUrlEncoded
    @POST("deposit")
    Call<AuthModel> sendDeposit(
            @Field("phone") String phones,
            @Field("invoice") String code,
            @Field("amount") String amount
    );
    @FormUrlEncoded
    @POST("deposit/invoice")
    Call<TransaksiModel> trx(
            @Field("phone") String phones,
            @Field("invoice") String invoice
    );

    @FormUrlEncoded
    @POST("transaksi")
    Call<List<TransaksiModel>> get_laporan(
            @Field("phone") String phones
    );
    @FormUrlEncoded
    @POST("transaksi/deposit")
    Call<List<TransaksiModel>> get_deposit(
            @Field("phone") String phones
    );

    @FormUrlEncoded
    @POST("transaksi/bonus")
    Call<List<TransaksiModel>> get_bonus(
            @Field("phone") String phones
    );
    @FormUrlEncoded
    @POST("transaksi/list")
    Call<List<TransaksiModel>> get_transaksi(
            @Field("phone") String phones
    );

    @FormUrlEncoded
    @POST("transaksi/mutasi")
    Call<List<TransaksiModel>> get_mutasi(
            @Field("phone") String phones
    );

    @FormUrlEncoded
    @POST("members/upgrade")
    Call<AuthModel> upgrade(
            @Field("phone") String phones
    );

    @FormUrlEncoded
    @POST("members/level")
    Call<List<MembersModel>> getLevel(
            @Field("phone") String phones
    );

    @FormUrlEncoded
    @POST("transaksi/kirim_saldo")
    Call<AuthModel> transferSaldoMembers(
            @Field("pengirim") String phones,
            @Field("penerima") String level,
            @Field("amount") String amount
    );
    @FormUrlEncoded
    @POST("transaksi/kirim_saldo_qrcode")
    Call<AuthModel> transferQRMembers(
            @Field("pengirim") String phones,
            @Field("penerima") String level,
            @Field("amount") String amount
    );
    @FormUrlEncoded
    @POST("prabayar")
    Call<List<HargaModel>> getPrabayar(
            @Field("phone") String phone,
            @Field("prefix") String prefix,
            @Field("kategori") String kategori,
            @Field("pelanggan") String cst
    );
    @FormUrlEncoded
    @POST("prabayar/kategori")
    Call<List<HargaModel>> getkatPrabayar(
            @Field("phone") String phones,
            @Field("kategori") String kategori,
            @Field("pelanggan") String cst
    );
    @FormUrlEncoded
    @POST("prabayar/get_kategori")
    Call<List<HargaModel>> getkategoriPrabayar(
            @Field("kategori") String kategori,
            @Field("brand") String brand
    );
    @FormUrlEncoded
    @POST("prabayar/get_kategori_pasca")
    Call<List<HargaModel>> getkategoriPasca(
            @Field("brand") String kategori
    );
    @FormUrlEncoded
    @POST("prabayar/brand")
    Call<List<HargaModel>> getbrandPrabayar(
            @Field("phone") String phones,
            @Field("brand") String kategori,
            @Field("pelanggan") String cst
    );
    @FormUrlEncoded
    @POST("prabayar/getVoucher")
    Call<List<HargaModel>> getvoucherPrabayar(
            @Field("phone") String phones,
            @Field("brand") String kategori,
            @Field("pelanggan") String cst
    );
    @FormUrlEncoded
    @POST("prabayar/pln")
    Call<List<HargaModel>> getPln(
            @Field("phone") String phones,
            @Field("kategori") String kategori,
            @Field("pelanggan") String cst
    );
    @FormUrlEncoded
    @POST("prabayar/emoney")
    Call<List<HargaModel>> getEmoney(
            @Field("phone") String phones,
            @Field("brand") String kategori,
            @Field("pelanggan") String cst
    );
    @FormUrlEncoded
    @POST("transaksi/cek_pin")
    Call<AuthModel> getPinTrx(
            @Field("phone") String phones,
            @Field("pin") String pin
    );

    @FormUrlEncoded
    @POST("prabayar/beliPulsa")
    Call<AuthModel> beliPulsa(
            @Field("phone") String phones,
            @Field("produk") String produk,
            @Field("customers") String customers,
            @Field("invoice") String invoice,
            @Field("price") String price
    );

    @FormUrlEncoded
    @POST("prabayar/ceK_transaksi")
    Call<TransaksiModel> cekPulsa(
            @Field("phone") String phones,
            @Field("invoice") String invoice
    );

    @FormUrlEncoded
    @POST("pascabayar/cek")
    Call<PascaModel> cekTagihan(
            @Field("phone") String phones,
            @Field("invoice") String invoice,
            @Field("customers") String customers,
            @Field("product") String product
    );
    @FormUrlEncoded
    @POST("pascabayar/bayar")
    Call<AuthModel> bayarTagihan(
            @Field("phone") String phones,
            @Field("invoice") String invoice,
            @Field("customers") String customers,
            @Field("product") String product,
            @Field("price") String price
    );
    @FormUrlEncoded
    @POST("members/cekMembers")
    Call<MembersModel> cekMembers(
            @Field("phone") String phones
    );

    @FormUrlEncoded
    @POST("members/update")
    Call<AuthModel> UpdateMembers(
            @Field("phone") String phone,
            @Field("name") String name,
            @Field("address") String address,
            @Field("pin") String pin
    );
    @FormUrlEncoded
    @POST("deposit/alfamart")
    Call<AuthModel> depoAlfa(
            @Field("phone") String phone,
            @Field("amount") int amount
    );

    @FormUrlEncoded
    @POST("members/ceK_pulsa")
    Call<AuthModel> CekPulsa(
            @Field("phone") String phone
    );
    @FormUrlEncoded
    @POST("transaksi/topup")
    Call<AuthModel> topupDeposit(
            @Field("phone") String phones,
            @Field("bank") String code,
            @Field("amount") String amount,
            @Field("invoice") String invoice
    );

    @FormUrlEncoded
    @POST("bank/cron")
    Call<AuthModel> UpdateDeposit(
            @Field("phone") String phones);

    @FormUrlEncoded
    @POST("bank/cron")
    Call<AuthModel> UpdateDepositService(
            @Field("phone") String phones);

    @FormUrlEncoded
    @POST("members/update_members")
    Call<AuthModel> updateMembers(
            @Field("phone") String phones,
            @Field("name") String name,
            @Field("address") String address
    );

    @FormUrlEncoded
    @POST("members/update_pins")
    Call<AuthModel> UpdatePinTrx(
            @Field("phone") String phones,
            @Field("pin") String pin
    );
    @FormUrlEncoded
    @POST("members/contact")
    Call<AuthModel> getContact(
            @Field("contact") String phones
    );

    @GET("forbiden/view_members")
    Call<List<MembersModel>> getInsertMembers();

    @FormUrlEncoded
    @POST("bank/save")
    Call<ResultModel> updateBank(
            @Field("phone") String phones,
            @Field("bank") String bank,
            @Field("norek") String norek,
            @Field("name") String name
    );
    @FormUrlEncoded
    @POST("bank/get")
    Call<ResultModel> getBank(
            @Field("phone") String phones
    );

    @FormUrlEncoded
    @POST("transaksi/penarikan_saldo")
    Call<ResultModel> TarikSaldo(
            @Field("phone") String phones,
            @Field("amount") String amount
    );

    @FormUrlEncoded
    @POST("produk/paket")
    Call<UpgradeModel> DetailPaket(
            @Field("type") String type
    );
    @FormUrlEncoded
    @POST("produk/beli_paket")
    Call<AuthModel> beliPaket(
            @Field("paket") String paket,
            @Field("phone") String phone
    );
}
