package id.klikdigital.csaiapp.login.model;

import com.google.gson.annotations.SerializedName;

public class UserItem {

    @SerializedName("kd_user")
    private String kdUser;

    @SerializedName("member_kode")
    private String memberKode;

    @SerializedName("nama_lengkap")
    private String namaLengkap;

    @SerializedName("username")
    private String username;

    @SerializedName("id_level")
    private int idLevel;
    @SerializedName("status")
    private String status;

    @SerializedName("amember_kode")
    private int amemberKode;

    @SerializedName("expired")
    private String expired;

    public String getMemberKode() {
        return memberKode;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public String getUsername() {
        return username;
    }

    public int getIdLevel() {
        return idLevel;
    }

    public String getStatus() {
        return status;
    }

    public int getAmemberKode() {
        return amemberKode;
    }

    public String getExpired() {
        return expired;
    }



    // Tambahkan atribut lainnya sesuai kebutuhan

    public String getKdUser() {
        return kdUser;
    }

}
