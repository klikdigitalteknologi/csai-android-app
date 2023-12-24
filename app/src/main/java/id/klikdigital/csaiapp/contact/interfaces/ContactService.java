package id.klikdigital.csaiapp.contact.interfaces;

import id.klikdigital.csaiapp.contact.response.ContactResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ContactService {
    @GET("/kontak")
    Call<ContactResponse> getContact(
            @Query("member") String member,
            @Query("whatsapp") String whatsapp
    );
}
