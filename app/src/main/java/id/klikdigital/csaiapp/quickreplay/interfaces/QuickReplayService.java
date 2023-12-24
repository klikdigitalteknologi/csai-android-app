package id.klikdigital.csaiapp.quickreplay.interfaces;

import id.klikdigital.csaiapp.quickreplay.response.QuickReplayResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface QuickReplayService {
    @GET("/balascepat")
    Call<QuickReplayResponse>getReplay(
            @Query("member") String member,
            @Query("perangkat") String perangkat,
            @Query("replyfast") String replayfast
    );
}
