package id.klikdigital.csaiapp.contact.response;

import java.util.List;

import id.klikdigital.csaiapp.contact.models.ContactModels;

public class ContactResponse {
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

    public List<ContactModels> getData() {
        return data;
    }

    public void setData(List<ContactModels> data) {
        this.data = data;
    }

    private boolean status;
    private String response;

    private List<ContactModels>data;
}
