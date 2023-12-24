package id.klikdigital.csaiapp.quickreplay.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.klikdigital.csaiapp.R;
import id.klikdigital.csaiapp.quickreplay.models.QuickReplayModels;

public class QuickReplayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<QuickReplayModels>replayModels;
    public QuickReplayAdapter(List<QuickReplayModels>replayModels){
        this.replayModels = replayModels;
    }
    @NonNull
    @Override
    public QuickReplayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_quick_replay,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        QuickReplayModels quickReplayModels = replayModels.get(position);
       ViewHolder viewHolder = (ViewHolder) holder;
       viewHolder.bind(quickReplayModels);
    }

    @Override
    public int getItemCount() {
        return replayModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textQuick;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textQuick = itemView.findViewById(R.id.textquickreplay);
            Log.d("QUICK","DATA" + textQuick);
        }

        public void bind(QuickReplayModels quickReplayModels) {
            textQuick.setText(quickReplayModels.getPesan());
            Log.d("RESPONSE QUICK",quickReplayModels.getPesan());
        }
    }
}
