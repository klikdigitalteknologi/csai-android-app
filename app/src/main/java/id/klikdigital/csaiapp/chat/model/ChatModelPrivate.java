package id.klikdigital.csaiapp.chat.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatModelPrivate {
    @SerializedName("kd_chats")
    private String kdChat;
    @SerializedName("kd_perangkat")
    private String kdPerangkat;
    @SerializedName("nomor_whatsapp")
    private String NomorWhatsapp;
    @SerializedName("nama")
    private String nama;
    @SerializedName("grup")
    private String grup;
    @SerializedName("pic")
    private String pic;
    @SerializedName("text")
    private String text;
    @SerializedName("file")
    private String file;
    @SerializedName("type_file")
    private String typeFile;
    @SerializedName("type")
    private String type;
    @SerializedName("jenis")
    private String jenis;
    @SerializedName("time")
    private String time;
    @SerializedName("date")
    private String date;

     public String getKdChat() {
        return kdChat;
    }
    public void setKdChat(String kdChat) {
        this.kdChat = kdChat;
    }
    public String getKdPerangkat() {
        return kdPerangkat;
    }
    public void setKdPerangkat(String kdPerangkat) {
        this.kdPerangkat = kdPerangkat;
    }
    public String getNomorWhatsapp() {
        return NomorWhatsapp;
    }
    public void setNomorWhatsapp(String nomorWhatsapp) {
        NomorWhatsapp = nomorWhatsapp;
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
    public String getPic() {
        return pic;
    }
    public void setPic(String pic) {
        this.pic = pic;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getFile() {
        return file;
    }
    public void setFile(String file) {
        this.file = file;
    }
    public String getTypeFile() {
        return typeFile;
    }
    public void setTypeFile(String typeFile) {
        this.typeFile = typeFile;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getJenis() {
        return jenis;
    }
    public void setJenis(String jenis) {
        this.jenis = jenis;
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