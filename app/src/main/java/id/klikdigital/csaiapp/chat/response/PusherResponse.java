package id.klikdigital.csaiapp.chat.response;

import com.google.gson.annotations.SerializedName;

public class PusherResponse {

    @SerializedName("number")
    private String nomorWhatsapp;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @SerializedName("name")
    private String name;

    public String getNomorWhatsapp() {
        return nomorWhatsapp;
    }

    public void setNomorWhatsapp(String nomorWhatsapp) {
        this.nomorWhatsapp = nomorWhatsapp;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    @SerializedName("message")
    private String pesan;

    @SerializedName("datetime")
    private String datetime;
}

