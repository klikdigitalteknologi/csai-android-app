package id.klikdigital.csaiapp.chatBot.model;

public class ChatModelBot {
    private String bc_id;
    private String kd_autoreplay;
    private String member_kode;
    private String kd_perangkat;
    private String keyword;
    private String type;
    private String file;
    private String pesan;
    private String button;
    private String url;
    private String footer;
    private String displayMessage;


    private String cutMessage;
    public String getBc_id() {
        return bc_id;
    }
    public void setBc_id(String bc_id) {
        this.bc_id = bc_id;
    }

    public String getKd_autoreplay() {
        return kd_autoreplay;
    }

    public void setKd_autoreplay(String kd_autoreplay) {
        this.kd_autoreplay = kd_autoreplay;
    }

    public String getMember_kode() {
        return member_kode;
    }

    public void setMember_kode(String member_kode) {
        this.member_kode = member_kode;
    }

    public String getKd_perangkat() {
        return kd_perangkat;
    }

    public void setKd_perangkat(String kd_perangkat) {
        this.kd_perangkat = kd_perangkat;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    public String getButton() {
        return button;
    }

    public void setButton(String button) {
        this.button = button;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public ChatModelBot(String bc_id, String kd_autoreplay, String member_kode, String kd_perangkat, String keyword, String type, String file, String pesan, String button, String url, String footer) {
        this.bc_id = bc_id;
        this.kd_autoreplay = kd_autoreplay;
        this.member_kode = member_kode;
        this.kd_perangkat = kd_perangkat;
        this.keyword = keyword;
        this.type = type;
        this.file = file;
        this.pesan = pesan;
        this.button = button;
        this.url = url;
        this.footer = footer;
    }
    public String getDisplayMessage() {
        return displayMessage;
    }

    public void setDisplayMessage(String displayMessage) {
        this.displayMessage = displayMessage;
    }
    public String getShortMessage(){
        int maxLength = 5; // Ubah sesuai kebutuhan Anda
        if (pesan.length() > maxLength) {
            return pesan.substring(0, maxLength) + "...";
        } else {
            return pesan;
        }
    }
    public String getCutMessage() {
        return cutMessage;
    }
    public void setCutMessage(String cutMessage) {
        this.cutMessage = cutMessage;
    }
    public String settingKetyword(){
        int maxLength = 4; // Ubah sesuai kebutuhan Anda
        if (keyword.length() > maxLength) {
            return keyword.substring(0, maxLength) + "...";
        } else {
            return keyword;
        }
    }
}
