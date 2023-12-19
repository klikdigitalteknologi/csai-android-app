package id.klikdigital.csaiapp.chatBot.response;

import java.util.ArrayList;
import java.util.List;

import id.klikdigital.csaiapp.chatBot.model.ChatModelBot;

public class ChatBotResponse {
    private boolean status;
    private String response;
    private List<ChatModelBot> data;

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

    public List<ChatModelBot> getData() {
        return data;
    }

    public void setData(ArrayList<ChatModelBot> data) {
        this.data = data;
    }
}
