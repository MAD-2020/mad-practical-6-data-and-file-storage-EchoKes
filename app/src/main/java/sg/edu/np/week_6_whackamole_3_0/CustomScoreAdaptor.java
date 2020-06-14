package sg.edu.np.week_6_whackamole_3_0;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class CustomScoreAdaptor extends RecyclerView.Adapter<CustomScoreViewHolder> {

    private static final String FILENAME = "CustomScoreAdaptor.java";
    private static final String TAG = "Whack-A-Mole3.0!";

    Context context;
    UserData data;
    ArrayList<Integer> levelList = new ArrayList<>();
    ArrayList<Integer> scoreList = new ArrayList<>();

    public CustomScoreAdaptor(Context context, UserData userdata){
        this.context = context;
        this.data = userdata;
    }

    public CustomScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.level_select,parent,false);
        return new CustomScoreViewHolder(view);
    }

    public void onBindViewHolder(CustomScoreViewHolder holder, final int position){
        levelList = data.getLevels();
        scoreList = data.getScores();

        Log.v(TAG, FILENAME + " Showing level " + levelList.get(position) + " with highest score: " + scoreList.get(position));
        holder.level.setText("Level " + levelList.get(position));
        Log.v(TAG, FILENAME+ ": Load level " + position +" for: " + data.getMyUserName());
        holder.score.setText("Highest Score: " + scoreList.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Main4Activity.class);
                intent.putExtra("data", data);
                intent.putExtra("level", data.getLevels().get(position));
                context.startActivity(intent);
            }
        });
    }

    public int getItemCount(){
        return data.getLevels().size();
    }
}