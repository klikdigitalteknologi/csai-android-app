    package id.klikdigital.csaiapp.chatBot.adapter;

    import android.annotation.SuppressLint;
    import android.content.Context;
    import android.os.Bundle;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.TextView;

    import androidx.fragment.app.FragmentActivity;
    import androidx.fragment.app.FragmentManager;
    import androidx.fragment.app.FragmentTransaction;

    import java.util.List;

    import de.codecrafters.tableview.TableDataAdapter;
    import id.klikdigital.csaiapp.R;
    import id.klikdigital.csaiapp.chatBot.fragment.DetailChatBotFragment;
    import id.klikdigital.csaiapp.chatBot.model.ChatModelBot;

    public class ChatBotTableDataAdapter extends TableDataAdapter<ChatModelBot> {
        private FragmentManager fragmentManager;
        private Context mct;
        private String kdAutoReplay,pesan,key,jenis;
        public ChatBotTableDataAdapter(Context context, List<ChatModelBot> data, FragmentManager fragmentManager) {
            super(context,data);
            this.fragmentManager = fragmentManager;
            this.mct = context;
        }
        @Override
        public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
            ChatModelBot rowData = getRowData(rowIndex);
            kdAutoReplay = rowData.getKd_autoreplay();
            key = rowData.getKeyword();
            pesan = rowData.getPesan();
            jenis = rowData.getType();
            View renderedView = null;
            switch (columnIndex) {
                case 0:
                    renderedView = renderString(rowData.getKeyword());
                    break;
                case 1:
                    renderedView = renderString(rowData.getPesan());
                    break;
                case 2:
                    renderedView = renderString(rowData.getType());
                    break;
                case 3:
                    // Renderkan tombol di sini
                    renderedView = renderButton("View",rowData.getKd_autoreplay());
                    break;
            }
            return renderedView;
        }
        private View renderString(String value) {
            TextView textView = new TextView(getContext());
            String truncatedValue = value.length() > 10 ? value.substring(0, 5) + "..." : value;
            textView.setText(truncatedValue);
            textView.setTextSize(16);
            textView.setPadding(20, 20, 20, 20);
            return textView;
        }
        @SuppressLint("SetTextI18n")
        private View renderButton(String value, String rowData) {
            TextView textView = new TextView(getContext());
            textView.setText(value);
            int color = getResources().getColor(R.color.colorLink);
            textView.setTextColor(color);
            textView.setOnClickListener(view -> {
                showDetailFragment(rowData);
            });
            return textView;
        }

        private void showDetailFragment(String rowData) {
            if (getContext() instanceof FragmentActivity) {
                FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                DetailChatBotFragment detailFragment = new DetailChatBotFragment();
                Bundle bundle = new Bundle();
                bundle.putString("kd_autoreplay", rowData);
                detailFragment.setArguments(bundle);
                transaction.replace(R.id.navHostFragment, detailFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        }
    }

