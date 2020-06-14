package sg.edu.np.week_6_whackamole_3_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;

public class Main4Activity extends AppCompatActivity {

    private static final String FILENAME = "Main4Activity.java";
    private static final String TAG = "Whack-A-Mole3.0!";

    TextView Score;
    int Count, level;
    Button B1, B2, B3, B4, B5, B6, B7, B8, B9, backBtn, mole1, mole2;
    Button[] advBList;
    long millisUntilFinished;
    long interval;
    CountDownTimer myCountDown;
    CountDownTimer MoleCountDown;

    UserData data;

    private void readyTimer(){
        millisUntilFinished = 5000;
        interval = 1000;
        myCountDown = new CountDownTimer(millisUntilFinished, interval) {

            @Override
            public void onTick(long millisUntilFinished) {
                Toast.makeText(getApplicationContext(),"Get Ready In " + (millisUntilFinished/interval), Toast.LENGTH_SHORT).show();
                Log.d("#d", "Ready CountDown!" + millisUntilFinished/interval);
                //Score.setText("" + (millisUntilFinished/interval));
            }

            @Override
            public void onFinish() {
                Toast.makeText(Main4Activity.this,"GO!", Toast.LENGTH_SHORT).show();
                Log.d("#d","Ready Countdown Complete!");
                placeMoleTimer();
            }
        };
        myCountDown.start();
    }

    private void placeMoleTimer(){
        MoleCountDown = new CountDownTimer(Long.MAX_VALUE, 11000-(level*1000)) {
            @Override
            public void onTick(long millisUntilFinished) {
                setNewMole();
                Log.d("#d", "New Mole Location!");
            }

            @Override
            public void onFinish() {
                MoleCountDown.start();
            }
        };
        MoleCountDown.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        backBtn = findViewById(R.id.btn_gameBack);
        Score = findViewById(R.id.TV_score);
        data = (UserData) getIntent().getSerializableExtra("data");
        level = getIntent().getIntExtra("level",1);
        Score.setText("0");

        B1 = findViewById(R.id.b1);
        B2 = findViewById(R.id.b2);
        B3 = findViewById(R.id.b3);
        B4 = findViewById(R.id.b4);
        B5 = findViewById(R.id.b5);
        B6 = findViewById(R.id.b6);
        B7 = findViewById(R.id.b7);
        B8 = findViewById(R.id.b8);
        B9 = findViewById(R.id.b9);

        advBList = new Button[]{B1,B2,B3,B4,B5,B6,B7,B8,B9};
        readyTimer();

        for (final Button b : advBList) {
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    doCheck(b);
                    setNewMole();
                }
            });
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserScore();

                Intent intent = new Intent(Main4Activity.this, Main3Activity.class);
                intent.putExtra("data", data);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onStart(){
        super.onStart();
    }
    private void doCheck(Button b)
    {
        if (b.getText() == "*"){
            Score.setText("" + (Count+=1));
            Log.v("#d", "Hit, score added!");
        }
        else {
            Score.setText("" + (Count-=1));
            Log.v("#d", "Missed, point deducted!");
        }
    }

    public void setNewMole() //used zachary's code
    {
        Random ran = new Random();
        int randomLocation = ran.nextInt(9);
        int newRandomLocation;

        mole1 = advBList[randomLocation];
        mole1.setText("*");

        if(level>=6){
            do {
                newRandomLocation = ran.nextInt(9);
            }
            while (randomLocation == newRandomLocation);
            mole2 = advBList[newRandomLocation];
            mole2.setText("*");
        }

        for(Button b:advBList){
            if(b!=mole1 && b!=mole2){
                b.setText("O");
            }
        }
//        for (int i = 0; i<advBList.length; i++){
//            if(randomLocation==i){
//                advBList[i].setText("*");
//            }
//            else{
//                advBList[i].setText("O");
//            }
//        }

    }

    private void updateUserScore()
    {
        Log.v(TAG, FILENAME + ": Update User Score...");
        MoleCountDown.cancel();
        myCountDown.cancel();

        int prevCount = data.getScores().get(0);
        if(Count>prevCount){
            MyDBHandler myDBHandler = new MyDBHandler(Main4Activity.this);
            myDBHandler.deleteAccount(data.getMyUserName());
            data.getScores().set(level-1,Count);
            myDBHandler.addUser(data);
        }
    }
}
