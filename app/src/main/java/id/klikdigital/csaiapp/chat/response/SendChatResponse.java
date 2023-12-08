package id.klikdigital.csaiapp.chat.response;

import com.google.gson.annotations.SerializedName;

public class SendChatResponse {
    @SerializedName("status")
    private boolean status;

    @SerializedName("response")
    private ChatResponseBody response;

    public boolean isStatus() {
        return status;
    }

    public ChatResponseBody getResponse() {
        return response;
    }

    public static class ChatResponseBody {

        @SerializedName("key")
        private Key key;

        @SerializedName("message")
        private Message message;

        @SerializedName("messageTimestamp")
        private String messageTimestamp;

        @SerializedName("status")
        private String status;

        public Key getKey() {
            return key;
        }

        public Message getMessage() {
            return message;
        }

        public String getMessageTimestamp() {
            return messageTimestamp;
        }

        public String getStatus() {
            return status;
        }
    }

    public static class Key {

        @SerializedName("remoteJid")
        private String remoteJid;

        @SerializedName("fromMe")
        private boolean fromMe;

        @SerializedName("id")
        private String id;

        public String getRemoteJid() {
            return remoteJid;
        }

        public boolean isFromMe() {
            return fromMe;
        }

        public String getId() {
            return id;
        }
    }

    public static class Message {

        @SerializedName("extendedTextMessage")
        private ExtendedTextMessage extendedTextMessage;

        public ExtendedTextMessage getExtendedTextMessage() {
            return extendedTextMessage;
        }
    }

    public static class ExtendedTextMessage {

        @SerializedName("text")
        private String text;

        public String getText() {
            return text;
        }
    }
}
