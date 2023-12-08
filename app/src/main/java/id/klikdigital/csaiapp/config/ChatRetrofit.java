package id.klikdigital.csaiapp.config;

import id.klikdigital.csaiapp.chat.interfaces.ChatService;
import retrofit2.Retrofit;

public class ChatRetrofit {
    private static final String URL_PUBLIC ="https://engine.csai.id";
    private static final String URL_PRIVATE = "https://188.166.186.222";

    private static final ChatService servicePublic = RetrofitHelper.createRetrofit(URL_PUBLIC).create(ChatService.class);
    private static final ChatService servicePrivate = RetrofitHelper.createRetrofit(URL_PRIVATE)
            .create(ChatService.class);
    public static ChatService getServicePublic() {
        return servicePublic;
    }

    public static ChatService getServicePrivate() {
        return servicePrivate;
    }

}
