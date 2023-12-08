package id.klikdigital.csaiapp.chat.response;

import java.util.List;
import id.klikdigital.csaiapp.chat.model.ChatModelPrivate;

public class ChatRoomResponse {
    private boolean status;
    private String response;

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

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    private List<ChatModelPrivate> data;
    private Pagination pagination;

    // Buat getter/setter sesuai kebutuhan
}

