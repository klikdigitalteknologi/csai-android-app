package id.klikdigital.csaiapp.chat.model;

import com.google.gson.annotations.SerializedName;

public class NotifModel {

    @SerializedName("kd_perangkat")
    private String kdPerangkat;
    @SerializedName("nomor_whatsapp")
    private String nomorWhatsApp;
    @SerializedName("nama")
    private String nama;
    @SerializedName("grup")
    private String grup;
    @SerializedName("status")
    private String status; // Hanya ada satu atribut "status"
    @SerializedName("notif")
    private int notif;
    @SerializedName("type")
    private String type;
    @SerializedName("time")
    private String time;
    @SerializedName("date")
    private String date;

    // Getter dan Setter sesuai kebutuhan



    public String getKdPerangkat() {
        return kdPerangkat;
    }

    public void setKdPerangkat(String kdPerangkat) {
        this.kdPerangkat = kdPerangkat;
    }

    public String getNomorWhatsApp() {
        return nomorWhatsApp;
    }

    public void setNomorWhatsApp(String nomorWhatsApp) {
        this.nomorWhatsApp = nomorWhatsApp;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getGrup() {
        return grup;
    }

    public void setGrup(String grup) {
        this.grup = grup;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getNotif() {
        return notif;
    }

    public void setNotif(int notif) {
        this.notif = notif;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

