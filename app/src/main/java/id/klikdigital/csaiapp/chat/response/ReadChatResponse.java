package id.klikdigital.csaiapp.chat.response;

import java.util.List;

import id.klikdigital.csaiapp.chat.model.ChatModelPrivate;

public class ReadChatResponse {

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

    public List<ChatModelPrivate> getData() {
        return data;
    }

    public void setData(List<ChatModelPrivate> data) {
        this.data = data;
    }

    private boolean status;
    private String response;

    private List<ChatModelPrivate> data;
}
