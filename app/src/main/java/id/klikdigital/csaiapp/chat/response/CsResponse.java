package id.klikdigital.csaiapp.chat.response;

import java.util.List;

import id.klikdigital.csaiapp.chat.model.CsModel;

public class CsResponse {
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

    public List<CsModel> getData() {
        return data;
    }

    public void setData(List<CsModel> data) {
        this.data = data;
    }

    private boolean status;
    private String response;
    private List<CsModel>data;
}
