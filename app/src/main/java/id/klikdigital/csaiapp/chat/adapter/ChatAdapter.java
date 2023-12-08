package id.klikdigital.csaiapp.chat.adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
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
    private static final String URL_CDN = "https://engine.csai.id/file/";
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
            if ("image".equals(chat.getTypeFile())){
                return VIEW_TYPE_SEND_IMAGE;
            } else {
            return VIEW_TYPE_SEND;
            }
        } else {
            if ("image".equals(chat.getTypeFile())){
                return VIEW_TYPE_RECEIVE_IMAGE;
            }else {
                return VIEW_TYPE_RECEIVE;
            }
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
            textSendMessage.setText(chat.getText());
            textDatetime.setText(chat.getTime());
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
            // Mengisi data ke layout_receive_message.xml
            textReceiveChat.setText(chat.getText());
            textTimeReceive.setText(chat.getTime());
        }
    }
private static class SendImageViewHolder extends RecyclerView.ViewHolder{
        TextView sendMessage;
        TextView sendTimeMessage;
        ImageView sendImageView;
    public SendImageViewHolder(@NonNull View itemView) {
        super(itemView);
        sendMessage = itemView.findViewById(R.id.textSendMessage);
        sendTimeMessage = itemView.findViewById(R.id.textTimeSendMessage);
        sendImageView = itemView.findViewById(R.id.imageSend);
    }
    void bind(ChatModelPrivate chat){
        sendMessage.setText(chat.getText());
        sendTimeMessage.setText(chat.getTime());
        if ("image".equals(chat.getTypeFile())){
            sendImageView.setVisibility(View.VISIBLE);
            String file = chat.getFile();
            final String url = URL_CDN + file;
            Picasso.get().load(url).into(sendImageView);
        } else {
            sendImageView.setVisibility(View.INVISIBLE);
        }
    }
}
    private static class ReceiveImageViewHolder extends RecyclerView.ViewHolder {
        TextView textReceiveChat,textTimeReceive;
        ImageView imageReceive;
        public ReceiveImageViewHolder(View receiveImageView) {
            super(receiveImageView);
            textReceiveChat = receiveImageView.findViewById(R.id.textreceivechat);
            textTimeReceive = receiveImageView.findViewById(R.id.texttimereceive);
            imageReceive = receiveImageView.findViewById(R.id.imageReceive);
        }
         void bind(ChatModelPrivate chat) {
            textTimeReceive.setText(chat.getTime());
             if ("image".equals(chat.getTypeFile())) {
                 if (!chat.getText().isEmpty()){
                     imageReceive.setVisibility(View.VISIBLE);
                     textReceiveChat.setVisibility(View.INVISIBLE);
                     String file = chat.getFile();
                     final String url = URL_CDN + file;
                     Picasso.get().load(url).into(imageReceive);
                 } else {
                     textReceiveChat.setVisibility(View.VISIBLE);
                     imageReceive.setVisibility(View.VISIBLE);
                     String file = chat.getFile();
                     final String url = URL_CDN + file;
                     Picasso.get().load(url).into(imageReceive);
                 }
             } else {
                 imageReceive.setVisibility(View.GONE);
             }
        }
    }
}


