package id.klikdigital.csaiapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;
import java.util.ArrayList;
import java.util.List;

import id.klikdigital.csaiapp.MainActivity;
import id.klikdigital.csaiapp.R;
import id.klikdigital.csaiapp.chat.acivity.ChatRoom;
import id.klikdigital.csaiapp.chat.adapter.CustomListAdapter;
import id.klikdigital.csaiapp.chat.interfaces.ChatService;
import id.klikdigital.csaiapp.chat.model.ChatModel;
import id.klikdigital.csaiapp.chat.model.NotifModel;
import id.klikdigital.csaiapp.chat.response.ChatResponse;
import id.klikdigital.csaiapp.chat.response.NotifResponse;
import id.klikdigital.csaiapp.chatBot.fragment.DetailChatBotFragment;
import id.klikdigital.csaiapp.config.Config;
import id.klikdigital.csaiapp.home.Home;
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat, container, false);
        if (getActivity() instanceof Home) {
            Home mainActivity = (Home) getActivity();
            if (mainActivity != null) {
                mainActivity.showToolbar();
            }
        }
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
            String nomor = chatModel.getNomorWhatsapp();
            String nama = chatModel.getNama();
            if (getContext() instanceof FragmentActivity) {
                pusher.disconnect();

                    FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    ChatRoomFragment chatRoomFragment = new ChatRoomFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("nomor",nomor);
                    bundle.putString("nama", nama);
                    chatRoomFragment.setArguments(bundle);
                    transaction.replace(R.id.navHostFragment, chatRoomFragment);

                    transaction.addToBackStack(null);
                    transaction.commit();
                }else {
                    Toast.makeText(getContext(),"GAGAL MENUTUP TOOLBAR",Toast.LENGTH_SHORT).show();
                }
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
//                        getCountNotif();
                    } else {
                        getDataFromPusher();
                        Log.d("response debug", "data kosong");
                    }
                } else {
                    Toast.makeText(getContext(), "SERVER ERROR", Toast.LENGTH_SHORT).show();
                    Log.d("Response debug", "server not responding");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ChatResponse> call, @NonNull Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCountNotif() {
        ChatService chatService = Config.htppclient().create(ChatService.class);
        Call<NotifResponse> call = chatService.getNotif(perangkat,member,session,limit);
        call.enqueue(new Callback<NotifResponse>() {
            @Override
            public void onResponse(Call<NotifResponse> call, Response<NotifResponse> response) {
                if (response.isSuccessful()) {
                    List<NotifModel> chatResponse = response.body().getData();
                    if (response.body().isStatus()){
                        for (int i = 0; i < chatResponse.size(); i++) {
                            NotifModel notifModel = chatResponse.get(i);
                            int count = notifModel.getNotif();
                            Log.d("NOTIF", "WD " + count);
                            // Temukan TextView di layout_item_chatlist.xml
                            TextView textCountMessage = view.findViewById(R.id.textCountMessage);

                            // Pastikan TextView tidak null sebelum memanggil setText
                            if (textCountMessage != null) {
                                // Set nilai notif ke dalam textCountMessage
                                textCountMessage.setText(String.valueOf(count));
                                // Tampilkan textCountMessage jika notif > 0
                                if (count > 0) {
                                    textCountMessage.setVisibility(View.VISIBLE);
                                } else {
                                    textCountMessage.setVisibility(View.GONE);
                                }
                            } else {
                                Log.e("ERROR", "textCountMessage is null");
                            }
                        }
                    }else {
                        Log.d("RESPONSE","BELUM ADA PESAN MASUK");
                    }

                } else {
                    Toast.makeText(getContext(), "SERVER ERROR", Toast.LENGTH_SHORT).show();
                    Log.d("RESPONSE CODE", "CODE \n" + response.code());
                }
            }
            @Override
            public void onFailure(Call<NotifResponse> call, Throwable t) {
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getDataFromPusher() {
        PusherOptions options = new PusherOptions().setCluster(cluster);
         pusher = new Pusher(key, options);
        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                Log.d("Pusher CHATLIST", "State: " + change.getCurrentState());
            }
            @Override
            public void onError(String message, String code, Exception e) {
                Log.e("Pusher CHATLIST", "Error: " + message);
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
//                            if ("image".equals(pusherResponse.getPesan()) && "document".equals(pusherResponse.getPesan()) && "audio".equals(pusherResponse.getPesan()) && "video".equals(pusherResponse.getPesan())){
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

    @Override
    public void onDestroy() {
        if (pusher !=null && pusher.getConnection() != null){
            pusher.disconnect();
        }
        super.onDestroy();
    }
}

