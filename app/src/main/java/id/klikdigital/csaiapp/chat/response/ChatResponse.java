package id.klikdigital.csaiapp.chat.response;

import java.util.ArrayList;

import id.klikdigital.csaiapp.chat.model.ChatModel;

public class ChatResponse {

    private boolean status;
    private String response;
    private ArrayList<ChatModel>data;

    public ChatResponse(boolean status, String response, ArrayList<ChatModel> data) {
        this.status = status;
        this.response = response;
        this.data = data;
    }

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

    public ArrayList<ChatModel> getData() {
        return data;
    }

    public void setData(ArrayList<ChatModel> data) {
        this.data = data;
    }
}
