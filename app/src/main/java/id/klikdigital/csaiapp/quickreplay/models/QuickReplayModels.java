package id.klikdigital.csaiapp.quickreplay.models;

import com.google.gson.annotations.SerializedName;

public class QuickReplayModels {
    @SerializedName("bc_id")
    private String bcId;
    @SerializedName("kd_replay_fast")
    private String kdReplyFast;
    @SerializedName("member_kode")
    private String memberKode;
    @SerializedName("kd_perangkat")
    private String kdPerangkat;
    @SerializedName("keyword")
    private String keyword;
    @SerializedName("type")
    private String type;
    @SerializedName("file")
    private String file;
    @SerializedName("pesan")
    private String pesan;
    @SerializedName("button")
    private String button;
    @SerializedName("url")
    private String url;
    @SerializedName("footer")
    private String footer;
    public String getBcId() {
        return bcId;
    }

    public void setBcId(String bcId) {
        this.bcId = bcId;
    }

    public String getKdReplyFast() {
        return kdReplyFast;
    }

    public void setKdReplyFast(String kdReplyFast) {
        this.kdReplyFast = kdReplyFast;
    }

    public String getMemberKode() {
        return memberKode;
    }

    public void setMemberKode(String memberKode) {
        this.memberKode = memberKode;
    }

    public String getKdPerangkat() {
        return kdPerangkat;
    }

    public void setKdPerangkat(String kdPerangkat) {
        this.kdPerangkat = kdPerangkat;
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


}
