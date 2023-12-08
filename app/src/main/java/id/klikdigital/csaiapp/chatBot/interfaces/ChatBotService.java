package id.klikdigital.csaiapp.chatBot.interfaces;

import id.klikdigital.csaiapp.chatBot.response.ChatBotResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ChatBotService {
    @GET("/autoreplay")
    Call<ChatBotResponse>getChatbot(
            @Query("member") String member,
            @Query("perangkat") String perangkat,
            @Query("autoreplay") String autoreplay
    );
}
