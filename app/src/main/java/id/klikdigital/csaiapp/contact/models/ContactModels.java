package id.klikdigital.csaiapp.contact.models;

import com.google.gson.annotations.SerializedName;

public class ContactModels {
    @SerializedName("kontak_id")
    private String kontakId;
    @SerializedName("kd_save_whatsapp")
    private String kdSaveWa;
    @SerializedName("nomor_whatsapp")
    private String whatsapp;
    @SerializedName("nama_lengkap")
    private String namaLengkap;
    @SerializedName("pic")
    private String pic;
    @SerializedName("member_kode")
    private String memberKode;
    @SerializedName("type")
    private String type;
    @SerializedName("label")
    private String label;
    @SerializedName("namelabel")
    private String nameLabel;

    public String getKontakId() {
        return kontakId;
    }

    public void setKontakId(String kontakId) {
        this.kontakId = kontakId;
    }

    public String getKdSaveWa() {
        return kdSaveWa;
    }

    public void setKdSaveWa(String kdSaveWa) {
        this.kdSaveWa = kdSaveWa;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getMemberKode() {
        return memberKode;
    }

    public void setMemberKode(String memberKode) {
        this.memberKode = memberKode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getNameLabel() {
        return nameLabel;
    }

    public void setNameLabel(String nameLabel) {
        this.nameLabel = nameLabel;
    }
}
