package id.klikdigital.csaiapp.login.response;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;

import java.util.List;

import id.klikdigital.csaiapp.login.model.UserItem;

public class ApiResponse {

    @SerializedName("status")
    private boolean status;

    @SerializedName("response")
    private String response;

    public List<UserItem> getData() {
        return data;
    }

    @SerializedName("data")
    private List<UserItem> data;

    public boolean isStatus() {
        return status;
    }

    public String getResponse() {
        return response;
    }


}
