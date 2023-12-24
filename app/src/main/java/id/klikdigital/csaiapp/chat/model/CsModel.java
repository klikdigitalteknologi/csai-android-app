package id.klikdigital.csaiapp.chat.model;

import com.google.gson.annotations.SerializedName;

public class CsModel {
    @SerializedName("cs_id")
    private String csId;
    @SerializedName("kd_customer_service")
    private String kdCs;
    @SerializedName("member_kode")
    private String member;
    @SerializedName("nama_lengkap")
    private String nama;
    @SerializedName("email")
    private String email;
    @SerializedName("nomor_whatsapp")
    private String whatsapp;
    @SerializedName("username")
    private String username;
    @SerializedName("status")
    private String status;
    @SerializedName("id_level")
    private int level;

    public String getCsId() {
        return csId;
    }

    public void setCsId(String csId) {
        this.csId = csId;
    }

    public String getKdCs() {
        return kdCs;
    }

    public void setKdCs(String kdCs) {
        this.kdCs = kdCs;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
