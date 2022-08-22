package com.masterweb.japripay.classes;

public class DateIndo {
    String bulan;
    public DateIndo(){
        bulan = null;
    }
    public String show(String timestamp ){
        String[] split = timestamp.split(" ");
        String[] date = split[0].split("-");
        String[] time = split[1].split(":");
        switch (date[1]){
            case "01":
                bulan="Januari";
                break;
            case "02":
                bulan="Februari";
                break;
            case "03":
                bulan="Maret";
                break;
            case "04":
                bulan="April";
                break;
            case "05":
                bulan="Mei";
                break;
            case "06":
                bulan="Juni";
                break;
            case "07":
                bulan="Juli";
                break;
            case "08":
                bulan="Agustus";
                break;
            case "09":
                bulan="September";
                break;
            case "10":
                bulan="Oktober";
                break;
            case "11":
                bulan="November";
                break;
            case "12":
                bulan="Desember";
                break;
        }
        return date[2]+" "+bulan+" "+date[0]+" "+time[0]+":"+time[1];
    }
}
