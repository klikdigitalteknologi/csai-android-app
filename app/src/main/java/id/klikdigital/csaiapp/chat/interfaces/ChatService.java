package id.klikdigital.csaiapp.chat.interfaces;

import id.klikdigital.csaiapp.chat.response.ChatResponse;
import id.klikdigital.csaiapp.chat.response.ChatRoomResponse;
import id.klikdigital.csaiapp.chat.response.CsResponse;
import id.klikdigital.csaiapp.chat.response.NotifResponse;
import id.klikdigital.csaiapp.chat.response.ResponseDto;
import id.klikdigital.csaiapp.chat.response.SendChatResponse;
import id.klikdigital.csaiapp.chat.response.SendImageResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ChatService {
    @GET("/chatList")
    Call<ChatResponse> getChatData(
            @Query("perangkat") String kdPerangkat,
            @Query("member") String memberKode,
            @Query("session") String session,
            @Query("limit") int limit);
    @GET("/chats")
    Call<ChatRoomResponse> getChats(
            @Query("perangkat") String perangkat,
            @Query("whatsapp") String whatsapp,
            @Query("page") int page,
            @Query("limit") int limit);
    @FormUrlEncoded
    @POST("/send-message")
    Call<SendChatResponse>sendMessage(
            @Field("sender") int sender,
            @Field("whatsapp") String whatsapp,
            @Field("message") String message);

    @GET("/readChat")
    Call<ChatRoomResponse>readChat(
            @Query("perangkat") String perangkat,
            @Query("whatsapp") String whatsapp
    );

    @GET("/chatNew")
    Call<ChatRoomResponse>newChat(
            @Query("perangkat") String perangkat,
            @Query("member") String member,
            @Query("whatsapp") String whatsapp,
            @Query("session") String session
    );

    @Multipart
    @POST("/send-image")
    Call<SendImageResponse>sendImage(
            @Part("sender") RequestBody sender,
            @Part("whatsapp") RequestBody whatsapp,
            @Part MultipartBody.Part image,
            @Part("message") RequestBody message);

    @GET("/notif")
    Call<NotifResponse>getNotif(
            @Query("perangkat") String perangkat,
            @Query("member") String member,
            @Query("session") String session,
            @Query("limit") int limit);

    @GET("/closeChat")
    Call<ResponseDto>closeChat(
            @Query("member") String member,
            @Query("whatsapp") String whatsapp,
            @Query("session") String session
    );

    @GET("/customerservice")
    Call<CsResponse>getCs(
            @Query("member") String member,
            @Query("customerservice") String kdCs
    );
}



