package id.klikdigital.csaiapp.quickreplay.response;

import java.util.List;

import id.klikdigital.csaiapp.quickreplay.models.QuickReplayModels;

public class QuickReplayResponse {
    private boolean status;
    private String response;
    private List<QuickReplayModels>data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public List<QuickReplayModels> getData() {
        return data;
    }

    public void setData(List<QuickReplayModels> data) {
        this.data = data;
    }
}

