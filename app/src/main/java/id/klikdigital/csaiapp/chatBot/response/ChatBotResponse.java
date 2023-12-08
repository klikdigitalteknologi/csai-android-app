package id.klikdigital.csaiapp.chatBot.response;

import java.util.ArrayList;

import id.klikdigital.csaiapp.chatBot.model.ChatModelBot;

public class ChatBotResponse {
    private boolean status;
    private String message;
    private ArrayList<ChatModelBot> data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<ChatModelBot> getData() {
        return data;
    }

    public void setData(ArrayList<ChatModelBot> data) {
        this.data = data;
    }
}
