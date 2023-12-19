package id.klikdigital.csaiapp.chat.response;

public class ResponseDto {
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

    private boolean status;
    private String response;
}
