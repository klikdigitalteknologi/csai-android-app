package id.klikdigital.csaiapp.chat.adapter;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;
import id.klikdigital.csaiapp.R;
import id.klikdigital.csaiapp.chat.model.ChatModelPrivate;
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_SEND = 1;
    private static final int VIEW_TYPE_RECEIVE = 2;
    private static final int VIEW_TYPE_RECEIVE_IMAGE = 3;
    private static final int VIEW_TYPE_SEND_IMAGE = 4;
    private static final int VIEW_TYPE_SEND_VIDEO = 5;
    private static final int VIEW_TYPE_RECEIVE_VIDEO = 6;
    private static final int VIEW_TYPE_SEND_AUDIO = 7;
    private static final int VIEW_TYPE_RECEIVE_AUDIO = 8;
    private static final int VIEW_TYPE_SEND_DOCUMENT = 9;
    private static final int VIEW_TYPE_RECEIVE_DOCUMENT = 10;
    private static final String URL_CDN = "https://baf1-36-85-221-17.ngrok-free.app/file/";
    private final List<ChatModelPrivate> chatList;

    // Konstruktor adapter dengan menerima daftar pesan
    public ChatAdapter(List<ChatModelPrivate> chatList) {
        this.chatList = chatList;
    }

    @Override
    public int getItemViewType(int position) {
        // Menentukan jenis tata letak berdasarkan data pada posisi tertentu
        ChatModelPrivate chat = chatList.get(position);

        if (chat.getJenis() != null && chat.getJenis().equals("keluar")) {
            String type = chat.getTypeFile();
            switch (type) {
                case "image":
                    return VIEW_TYPE_SEND_IMAGE;
                case "video":
                    return VIEW_TYPE_SEND_VIDEO;
                case "audio":
                    return VIEW_TYPE_SEND_AUDIO;
                case "document":
                    return VIEW_TYPE_SEND_DOCUMENT;
                default:
                    return VIEW_TYPE_SEND;
            }
//            if ("image".equals(chat.getTypeFile())){
//                if ("document".equals(chat.getTypeFile())){
//                    return VIEW_TYPE_SEND_IMAGE;
//                }else {
//                    return VIEW_TYPE_SEND_IMAGE;
//                }
//            } else {
//            return VIEW_TYPE_SEND;
//            }
        } else {
            String type = chat.getTypeFile();
            switch (type) {
                case "image":
                    return VIEW_TYPE_SEND_IMAGE;
                case "video":
                    return VIEW_TYPE_SEND_VIDEO;
                case "audio":
                    return VIEW_TYPE_SEND_AUDIO;
                case "document":
                    return VIEW_TYPE_SEND_DOCUMENT;
                default:
                    return VIEW_TYPE_RECEIVE;
            }
//            if ("image".equals(chat.getTypeFile())){
//                return VIEW_TYPE_RECEIVE_IMAGE;
//            }else {
//                return VIEW_TYPE_RECEIVE;
//            }

        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case VIEW_TYPE_SEND:
                View sendView = inflater.inflate(R.layout.item_container_send_message, parent, false);
                return new SendViewHolder(sendView);
            case VIEW_TYPE_SEND_IMAGE:
                View sendImageView = inflater.inflate(R.layout.item_container_send_message,parent,false);
                return new SendImageViewHolder(sendImageView);
            case VIEW_TYPE_RECEIVE:
                View receiveView = inflater.inflate(R.layout.background_receive_message, parent, false);
                return new ReceiveViewHolder(receiveView);
            case VIEW_TYPE_RECEIVE_IMAGE:
                View receiveImageView = inflater.inflate(R.layout.background_receive_message,parent,false);
                return new ReceiveImageViewHolder(receiveImageView);
            case VIEW_TYPE_SEND_VIDEO:
                View sendVideoView = inflater.inflate(R.layout.item_container_send_message, parent, false);
                return new SendVideoViewHolder(sendVideoView);
            case VIEW_TYPE_RECEIVE_VIDEO:
                View receiveVideoView = inflater.inflate(R.layout.background_receive_message, parent, false);
                return new ReceiveVideoViewHolder(receiveVideoView);
            case VIEW_TYPE_SEND_AUDIO:
                View sendAudioView = inflater.inflate(R.layout.item_container_send_message, parent, false);
                return new SendAudioViewHolder(sendAudioView);
            case VIEW_TYPE_RECEIVE_AUDIO:
                View receiveAudioView = inflater.inflate(R.layout.background_receive_message, parent, false);
                return new ReceiveAudioViewHolder(receiveAudioView);
            case VIEW_TYPE_SEND_DOCUMENT:
                View sendDocumentView = inflater.inflate(R.layout.item_container_send_message, parent, false);
                return new SendDocumentViewHolder(sendDocumentView);
            case VIEW_TYPE_RECEIVE_DOCUMENT:
                View receiveDocumentView = inflater.inflate(R.layout.background_receive_message, parent, false);
                return new ReceiveDocumentViewHolder(receiveDocumentView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // Memasukkan data ke ViewHolder yang sesuai
        ChatModelPrivate chat = chatList.get(position);
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_SEND:
                SendViewHolder sendViewHolder = (SendViewHolder) holder;
                sendViewHolder.bind(chat);
                break;
            case VIEW_TYPE_SEND_IMAGE:
                SendImageViewHolder sendImageViewHolder = (SendImageViewHolder) holder;
                sendImageViewHolder.bind(chat);
                break;
            case VIEW_TYPE_RECEIVE:
                ReceiveViewHolder receiveViewHolder = (ReceiveViewHolder) holder;
                receiveViewHolder.bind(chat);
                break;
            case VIEW_TYPE_RECEIVE_IMAGE:
                ReceiveImageViewHolder receiveImageViewHolder = (ReceiveImageViewHolder) holder;
                receiveImageViewHolder.bind(chat);
                break;
            case VIEW_TYPE_SEND_VIDEO:
                SendVideoViewHolder sendVideoViewHolder = (SendVideoViewHolder) holder;
                sendVideoViewHolder.bind(chat);
                break;
            case VIEW_TYPE_RECEIVE_VIDEO:
                ReceiveVideoViewHolder receiveVideoViewHolder = (ReceiveVideoViewHolder) holder;
                receiveVideoViewHolder.bind(chat);
                break;
            case VIEW_TYPE_SEND_AUDIO:
                SendAudioViewHolder sendAudioViewHolder = (SendAudioViewHolder) holder;
                sendAudioViewHolder.bind(chat);
                break;
            case VIEW_TYPE_RECEIVE_AUDIO:
                ReceiveAudioViewHolder receiveAudioViewHolder = (ReceiveAudioViewHolder) holder;
                receiveAudioViewHolder.bind(chat);
                break;
            case VIEW_TYPE_SEND_DOCUMENT:
                SendDocumentViewHolder sendDocumentViewHolder = (SendDocumentViewHolder) holder;
                sendDocumentViewHolder.bind(chat);
                break;
            case VIEW_TYPE_RECEIVE_DOCUMENT:
                ReceiveDocumentViewHolder receiveDocumentViewHolder = (ReceiveDocumentViewHolder) holder;
                receiveDocumentViewHolder.bind(chat);
            default:
                break;
        }
    }
    @Override
    public int getItemCount() {
        return chatList.size();
    }

    // ViewHolder untuk pesan yang dikirim
    private static class SendViewHolder extends RecyclerView.ViewHolder {
        TextView textSendMessage;
        TextView textDatetime;
        SendViewHolder(View itemView) {
            super(itemView);
            textSendMessage = itemView.findViewById(R.id.textSendMessage);
            textDatetime = itemView.findViewById(R.id.textTimeSendMessage);
        }
        void bind(ChatModelPrivate chat) {
            String time = chat.getTime();
            String waktu = time.substring(0,5);
            textSendMessage.setText(chat.getText());
            textDatetime.setText(waktu);
            Log.d("SendViewHolder", "Time: " + chat.getTime());

        }
    }
    private static class ReceiveViewHolder extends RecyclerView.ViewHolder {
        TextView textReceiveChat;
        TextView textTimeReceive;
        ReceiveViewHolder(View itemView) {
            super(itemView);
            textReceiveChat = itemView.findViewById(R.id.textreceivechat);
            textTimeReceive = itemView.findViewById(R.id.texttimereceive);
        }
        void bind(ChatModelPrivate chat) {
            String time = chat.getTime();
            String waktu = time.substring(0,5);
            // Mengisi data ke layout_receive_message.xml
            textReceiveChat.setText(chat.getText());
            textTimeReceive.setText(waktu);
        }
    }
    private static class SendImageViewHolder extends RecyclerView.ViewHolder{
        TextView sendMessage;
        TextView sendTimeMessage;
        ImageView sendImageView;
        LinearLayout layout;
    public SendImageViewHolder(@NonNull View itemView) {
        super(itemView);
        sendMessage = itemView.findViewById(R.id.textSendMessage);
        sendTimeMessage = itemView.findViewById(R.id.textTimeSendMessage);
        sendImageView = itemView.findViewById(R.id.imageSend);
        layout = itemView.findViewById(R.id.layoutSendMessage);
    }
    void bind(ChatModelPrivate chat){
        String time = chat.getTime();
        String waktu = time.substring(0,5);
        sendMessage.setText(chat.getText());
        sendTimeMessage.setText(waktu);
        if ("image".equals(chat.getTypeFile())){
            if(!chat.getText().isEmpty()) {
                sendImageView.setVisibility(View.VISIBLE);
                String file = chat.getFile();
                final String url = URL_CDN + file;
                Picasso.get().load(url).into(sendImageView);
            }else {
                sendImageView.setVisibility(View.VISIBLE);
                String file = chat.getFile();
                final String url = URL_CDN + file;
                Picasso.get().load(url).into(sendImageView);
                layout.setVisibility(View.GONE);
            }
        } else if ("document".equals(chat.getTypeFile())){
            sendImageView.setVisibility(View.VISIBLE);
            String file = chat.getFile();
            Drawable drawable = Drawable.createFromPath(String.valueOf(R.drawable.pdf));
            sendImageView.setImageDrawable(drawable);
        }
    }
}
    private static class ReceiveImageViewHolder extends RecyclerView.ViewHolder {
        Context mct;
        TextView textReceiveChat,textTimeReceive;
        ImageView imageReceive;
        LinearLayout layout;
        public ReceiveImageViewHolder(View receiveImageView) {
            super(receiveImageView);
            textReceiveChat = receiveImageView.findViewById(R.id.textreceivechat);
            textTimeReceive = receiveImageView.findViewById(R.id.texttimereceive);
            imageReceive = receiveImageView.findViewById(R.id.imageReceive);
            layout = receiveImageView.findViewById(R.id.layouttextview);
        }
         void bind(ChatModelPrivate chat) {
             String time = chat.getTime();
             String waktu = time.substring(0,5);
            textTimeReceive.setText(waktu);
             if ("image".equals(chat.getTypeFile())) {
                 if (!chat.getText().isEmpty()){
                     String file = chat.getFile();
                     final String url = URL_CDN + file;
                     imageReceive.setVisibility(View.VISIBLE);
                     imageReceive.setOnClickListener(view -> {
                         showImage(url);
                     });
                     textReceiveChat.setVisibility(View.VISIBLE);
                     textReceiveChat.setText(chat.getText());
                     Picasso.get().load(url).into(imageReceive);
                 } else {
                     textReceiveChat.setVisibility(View.INVISIBLE);
                     imageReceive.setVisibility(View.VISIBLE);
                     String file = chat.getFile();
                     final String url = URL_CDN + file;
                     Picasso.get().load(url).into(imageReceive);
                 }
             } else {
                 imageReceive.setVisibility(View.GONE);
             }
        }

        private void showImage(String url) {

        }
    }
    //buatkan class seperti ini private static class ReceiveImageViewHolder extends RecyclerView.ViewHolder
    private static class SendVideoViewHolder extends RecyclerView.ViewHolder {
        TextView sendMessage;
        TextView sendTimeMessage;
        ImageView sendImageView;
        LinearLayout layout;

        public SendVideoViewHolder(View receiveImageView) {
            super(receiveImageView);
            sendMessage = receiveImageView.findViewById(R.id.textSendMessage);
            sendTimeMessage = receiveImageView.findViewById(R.id.textTimeSendMessage);
            sendImageView = receiveImageView.findViewById(R.id.imageSend);
            layout = receiveImageView.findViewById(R.id.layoutSendMessage);
        }

        public void bind(ChatModelPrivate chat) {
            String time = chat.getTime();
            String waktu = time.substring(0,5);
            sendMessage.setText(chat.getText());
            sendTimeMessage.setText(waktu);
            if ("image".equals(chat.getTypeFile())){
                if(!chat.getText().isEmpty()) {
                    sendImageView.setVisibility(View.VISIBLE);
                    String file = chat.getFile();
                    final String url = URL_CDN + file;
                    Picasso.get().load(url).into(sendImageView);
                }else {
                    sendImageView.setVisibility(View.VISIBLE);
                    String file = chat.getFile();
                    final String url = URL_CDN + file;
                    Picasso.get().load(url).into(sendImageView);
                    layout.setVisibility(View.GONE);
                }
            } else if ("document".equals(chat.getTypeFile())){
                sendImageView.setVisibility(View.VISIBLE);
                String file = chat.getFile();
                Drawable drawable = Drawable.createFromPath(String.valueOf(R.drawable.pdf));
                sendImageView.setImageDrawable(drawable);
            }
        }

        // Add your methods here

    }
    private static class ReceiveVideoViewHolder extends RecyclerView.ViewHolder {
        // Add your class variables here

        public ReceiveVideoViewHolder(View receiveImageView) {
            super(receiveImageView);
            // Initialize your class variables here
        }

        public void bind(ChatModelPrivate chat) {
        }

        // Add your methods here

    }
    private static class SendAudioViewHolder extends RecyclerView.ViewHolder {
        // Add your class variables here

        public SendAudioViewHolder(View receiveImageView) {
            super(receiveImageView);
            // Initialize your class variables here
        }

        public void bind(ChatModelPrivate chat) {
        }

        // Add your methods here

    }
    private static class ReceiveAudioViewHolder extends RecyclerView.ViewHolder {
        // Add your class variables here

        public ReceiveAudioViewHolder(View receiveImageView) {
            super(receiveImageView);
            // Initialize your class variables here
        }

        public void bind(ChatModelPrivate chat) {
        }

        // Add your methods here

    }
    private static class SendDocumentViewHolder extends RecyclerView.ViewHolder {
        // Add your class variables here

        public SendDocumentViewHolder(View receiveImageView) {
            super(receiveImageView);
            // Initialize your class variables here
        }

        public void bind(ChatModelPrivate chat) {
        }

        // Add your methods here

    }
    private static class ReceiveDocumentViewHolder extends RecyclerView.ViewHolder {
        // Add your class variables here

        public ReceiveDocumentViewHolder(View receiveImageView) {
            super(receiveImageView);
            // Initialize your class variables here
        }

        public void bind(ChatModelPrivate chat) {
        }

        // Add your methods here

    }

}


