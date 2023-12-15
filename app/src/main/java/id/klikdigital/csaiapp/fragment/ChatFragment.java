package id.klikdigital.csaiapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Objects;

import id.klikdigital.csaiapp.R;
import id.klikdigital.csaiapp.chat.acivity.ChatRoom;
import id.klikdigital.csaiapp.chat.adapter.CustomListAdapter;
import id.klikdigital.csaiapp.chat.interfaces.ChatService;
import id.klikdigital.csaiapp.chat.model.ChatModel;
import id.klikdigital.csaiapp.chat.response.ChatResponse;
import id.klikdigital.csaiapp.chat.response.PusherResponse;
import id.klikdigital.csaiapp.config.Config;
import id.klikdigital.csaiapp.login.model.UserItem;
import id.klikdigital.csaiapp.session.SessionManage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class ChatFragment extends Fragment {
    private Context context;
    private Pusher pusher;
    private ArrayList<ChatModel> chatModels;
    private String perangkat,member,session,message;
    private CustomListAdapter adapter;
    private int limit;
    private View view;
    private AppCompatButton btn_chat_new;
    private String whatsapp;
    String[] count = {"All Contact","10","25","50","100"};
    private Spinner spinner;
    private SwipeRefreshLayout swipeRefreshLayout;
   private String app_id = "1578008";
    private String key = "560f792226b7069d0cd9";
   private String secret = "e8b3af6069e8cc5db7fb";
   private ListView listView;
   private String cluster = "ap1";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat, container, false);
        context = getActivity();
        listView = view.findViewById(R.id.listView);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        SessionManage sessionManage = SessionManage.getInstance(getActivity());
        UserItem userItem = sessionManage.getUserData();
        perangkat = userItem.getMemberKode();
        member = userItem.getMemberKode();
        session = userItem.getKdUser();
        limit =0;
        chatModels = new ArrayList<>();
        adapter = new CustomListAdapter(getActivity(),new ArrayList<>());
        btn_chat_new = view.findViewById(R.id.btn_new_chat);
        spinner = view.findViewById(R.id.spinner_chat_list);
        //start dropdown
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, count);
        stringArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(stringArrayAdapter);
     //end dropdown
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            ChatModel chatModel = chatModels.get(i);
            Intent intent = new Intent(getActivity(), ChatRoom.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("nomor_wa",chatModel.getNomorWhatsapp());
            intent.putExtra("nama_user",chatModel.getNama());
            startActivity(intent);
            getActivity().finish();
            pusher.disconnect();
        });
        getDataFromServer();
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(true);
            getDataFromServer();
            swipeRefreshLayout.setRefreshing(false);
        });
        btn_chat_new.setOnClickListener(view ->
                Toast.makeText(context,"ini adalah button chat new",Toast.LENGTH_SHORT).show());
        return view;
    }
    private void getDataFromServer() {
        ChatService chatService = Config.htppclient().create(ChatService.class);
        Call<ChatResponse> chatResponseCall = chatService.getChatData(perangkat, member, session, limit);
        chatResponseCall.enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(@NonNull Call<ChatResponse> call, @NonNull Response<ChatResponse> response) {
                if (response.isSuccessful()) {
                    ChatResponse chatResponse = response.body();
                    if (chatResponse != null && chatResponse.isStatus() && chatResponse.getData() != null) {
                        chatModels = chatResponse.getData();
                        if (!chatModels.isEmpty()) {
                            whatsapp = chatModels.get(0).getNomorWhatsapp();
                        }
                        for (int i = 0; i < chatModels.size(); i++) {
                            ChatModel chatModel = chatModels.get(i);
                            chatModel.setDisplayMessage(chatModel.getShortMessage());
                        }
                        adapter.clear();
                        adapter.addAll(chatModels);
                        adapter.notifyDataSetChanged();
                        listView.setAdapter(adapter);
                        getDataFromPusher();
                    } else {
                        getDataFromPusher();
                        Log.d("response debug", "data kosong");
                    }
                } else {
                    Log.d("Response debug", "server not responding");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ChatResponse> call, @NonNull Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getDataFromPusher() {
        PusherOptions options = new PusherOptions().setCluster(cluster);
         pusher = new Pusher(key, options);
        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                Log.d("Pusher", "State: " + change.getCurrentState());
            }
            @Override
            public void onError(String message, String code, Exception e) {
                Log.e("Pusher", "Error: " + message);
            }
        }, ConnectionState.ALL);
        pusher.connect();
        Channel channel = pusher.subscribe(member + "-messages");
        channel.bind(member, event -> {
            getDataFromServer();
//                        try {
//                    JSONObject jsonData = new JSONObject(event.getData());
//                    Log.d("RESPONSE JSON", jsonData.toString());
//                    // Gunakan Gson untuk mengonversi JSON ke objek PusherResponse
//                    Gson gson = new Gson();
//                    PusherResponse pusherResponse = gson.fromJson(jsonData.toString(), PusherResponse.class);
//        // Ambil nilai dari objek PusherResponse
//        String phoneNumberWithDomain = pusherResponse.getNomorWhatsapp();
//        String name = pusherResponse.getName();
//        String message = pusherResponse.getPesan();
//        String datetime = pusherResponse.getDatetime();
//        String[] parts = phoneNumberWithDomain.split(",");
//        String phoneNumber = parts[0].trim();
//        String[] waktu = datetime.split(",");
//        String jam = waktu[1].trim();
//        Log.d("NOMOR SUB","\n"+phoneNumber);
//                            if (Objects.equals(message, "image")){
//                                getDataFromServer();
//                            } else if (Objects.equals(message, "document")) {
//                                getDataFromServer();
//                            } else if (Objects.equals(message, "audio")) {
//                                getDataFromServer();
//                            } else if (Objects.equals(message, "video")) {
//                                getDataFromServer();
//                            }else {
//                                ChatModel newChatModel = new ChatModel();
//                                newChatModel.setNomorWhatsapp(phoneNumber);
//                                newChatModel.setNama(name);
//                                newChatModel.setPesan(message);
//                                newChatModel.setTime(jam);
//
//                                requireActivity().runOnUiThread(() -> {
//                                    if (!chatModels.isEmpty()) {
//                                        boolean isExisting = false;
//                                        for (int i = 0; i < chatModels.size(); i++) {
//                                            if (chatModels.get(i).getNomorWhatsapp().equals(phoneNumber)) {
//                                                chatModels.set(i, newChatModel);
//                                                isExisting = true;
//                                                break;
//                                            } else {
//                                                Toast.makeText(context, "TIDAK ADA", Toast.LENGTH_SHORT).show();
//                                            }
//                                        }
//                                        if (!isExisting) {
//                                            chatModels.add(0, newChatModel);
//                                        }
//                                        chatModels.sort((chatModel1, chatModel2) ->
//                                                chatModel2.getTime().compareTo(chatModel1.getTime()));
//                                        adapter.clear();
//                                        adapter.addAll(chatModels);
//                                        adapter.notifyDataSetChanged();
//                                        listView.setAdapter(adapter);
//                                    } else {
//                                        Toast.makeText(context, "KOOSSSONNG", Toast.LENGTH_LONG).show();
//                                    }
//                                });
//                            }
//        } catch (JSONException e) {
//        Log.d("RESPONSE ERROR JSON", "error" + e.getMessage());
//        e.printStackTrace();
//        }
        });
    }
}

