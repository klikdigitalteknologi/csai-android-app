package id.klikdigital.csaiapp.chat.model;

import com.google.gson.annotations.SerializedName;

public class ChatModel {
    @SerializedName("pic")
    private String pic;
    @SerializedName("nomor_whatsapp")
    private String nomorWhatsapp;
    @SerializedName("nama")
    private String nama;
    @SerializedName("grup")
    private String grup;
    @SerializedName("pesan")
    private String pesan;
    @SerializedName("status")
    private String status;
    @SerializedName("read")
    private String read;
    @SerializedName("type")
    private String type;
    @SerializedName("time")
    private String time;
    @SerializedName("date")
    private String date;
    private String displayMessage;
    public static ChatModel create(String nomorWhatsapp, String message, String datetime) {
        ChatModel chatModel = new ChatModel();
        chatModel.setNomorWhatsapp(nomorWhatsapp);
        chatModel.setDisplayMessage(message); // Sesuaikan sesuai kebutuhan
        // Set nilai-nilai lainnya jika diperlukan
        return chatModel;
    }
    public String getPic() {
        return pic;
    }
    public void setPic(String pic) {
        this.pic = pic;
    }
    public String getNomorWhatsapp() {
        return nomorWhatsapp;
    }
    public void setNomorWhatsapp(String nomorWhatsapp) {
        this.nomorWhatsapp = nomorWhatsapp;
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
    public String getPesan() {
        return pesan;
    }
    public void setPesan(String pesan) {
        this.pesan = pesan;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getRead() {
        return read;
    }
    public void setRead(String read) {
        this.read = read;
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
    public String getDisplayMessage() {
        return displayMessage;
    }

    public void setDisplayMessage(String displayMessage) {
        this.displayMessage = displayMessage;
    }
    public String getShortMessage(){
        int maxLength = 15; // Ubah sesuai kebutuhan Anda
        if (pesan.length() > maxLength) {
            return pesan.substring(0, maxLength) + "...";
        } else {
            return pesan;
        }
    }
}
