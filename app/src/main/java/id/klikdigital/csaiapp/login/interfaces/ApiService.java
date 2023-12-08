package id.klikdigital.csaiapp.login.interfaces;

import id.klikdigital.csaiapp.login.response.ApiResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("/login")
    Call<ApiResponse> loginUser(
            @Query("username") String username,
            @Query("password") String password,
            @Query("member") String member
    );
}
