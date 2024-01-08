package id.klikdigital.csaiapp.chat.response;

public class SendImageResponse {
    private boolean status;
    private ResponseData response;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ResponseData getResponse() {
        return response;
    }

    public void setResponse(ResponseData response) {
        this.response = response;
    }

    public static class ResponseData {
        private KeyData key;
        private MessageData message;
        private long messageTimestamp;
        private String status;

        public KeyData getKey() {
            return key;
        }

        public void setKey(KeyData key) {
            this.key = key;
        }

        public MessageData getMessage() {
            return message;
        }

        public void setMessage(MessageData message) {
            this.message = message;
        }

        public long getMessageTimestamp() {
            return messageTimestamp;
        }

        public void setMessageTimestamp(long messageTimestamp) {
            this.messageTimestamp = messageTimestamp;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public static class KeyData {
        private String remoteJid;
        private boolean fromMe;
        private String id;

        public String getRemoteJid() {
            return remoteJid;
        }

        public void setRemoteJid(String remoteJid) {
            this.remoteJid = remoteJid;
        }

        public boolean isFromMe() {
            return fromMe;
        }

        public void setFromMe(boolean fromMe) {
            this.fromMe = fromMe;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public static class MessageData {
        private ImageMessageData imageMessage;

        public ImageMessageData getImageMessage() {
            return imageMessage;
        }

        public void setImageMessage(ImageMessageData imageMessage) {
            this.imageMessage = imageMessage;
        }
    }

    public static class ImageMessageData {
        private String url;
        private String mimetype;
        private String fileSha256;
        private String fileLength;
        private int height;
        private int width;
        private String mediaKey;
        private String fileEncSha256;
        private String directPath;
        private String mediaKeyTimestamp;
        private String jpegThumbnail;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getMimetype() {
            return mimetype;
        }

        public void setMimetype(String mimetype) {
            this.mimetype = mimetype;
        }

        public String getFileSha256() {
            return fileSha256;
        }

        public void setFileSha256(String fileSha256) {
            this.fileSha256 = fileSha256;
        }

        public String getFileLength() {
            return fileLength;
        }

        public void setFileLength(String fileLength) {
            this.fileLength = fileLength;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public String getMediaKey() {
            return mediaKey;
        }

        public void setMediaKey(String mediaKey) {
            this.mediaKey = mediaKey;
        }

        public String getFileEncSha256() {
            return fileEncSha256;
        }

        public void setFileEncSha256(String fileEncSha256) {
            this.fileEncSha256 = fileEncSha256;
        }

        public String getDirectPath() {
            return directPath;
        }

        public void setDirectPath(String directPath) {
            this.directPath = directPath;
        }

        public String getMediaKeyTimestamp() {
            return mediaKeyTimestamp;
        }

        public void setMediaKeyTimestamp(String mediaKeyTimestamp) {
            this.mediaKeyTimestamp = mediaKeyTimestamp;
        }

        public String getJpegThumbnail() {
            return jpegThumbnail;
        }

        public void setJpegThumbnail(String jpegThumbnail) {
            this.jpegThumbnail = jpegThumbnail;
        }
    }
}
