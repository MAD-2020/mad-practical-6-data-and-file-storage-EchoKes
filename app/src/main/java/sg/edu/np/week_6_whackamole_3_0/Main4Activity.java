package sg.edu.np.week_6_whackamole_3_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class Main4Activity extends AppCompatActivity {

    /* Hint:
        1. This creates the Whack-A-Mole layout and starts a countdown to ready the user
        2. The game difficulty is based on the selected level
        3. The levels are with the following difficulties.
            a. Level 1 will have a new mole at each 10000ms.
                - i.e. level 1 - 10000ms
                       level 2 - 9000ms
                       level 3 - 8000ms
                       ...
                       level 10 - 1000ms
            b. Each level up will shorten the time to next mole by 100ms with level 10 as 1000 second per mole.
            c. For level 1 ~ 5, there is only 1 mole.
            d. For level 6 ~ 10, there are 2 moles.
            e. Each location of the mole is randomised.
        4. There is an option return to the login page.
     */
    private static final String FILENAME = "Main4Activity.java";
    private static final String TAG = "Whack-A-Mole3.0!";

    TextView Score;
    int Count, level;
    Button B1, B2, B3, B4, B5, B6, B7, B8, B9, backBtn;
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
        MoleCountDown = new CountDownTimer(1000, 1000) {
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

    public void setNewMole()
    {
        Random ran = new Random();
        int randomLocation = ran.nextInt(9);
        for (int i = 0; i<advBList.length; i++){
            if(randomLocation==i){
                advBList[i].setText("*");
            }
            else{
                advBList[i].setText("O");
            }
        }
    }

    private void updateUserScore()
    {
        Log.v(TAG, FILENAME + ": Update User Score...");
        MoleCountDown.cancel();
        myCountDown.cancel();

        MyDBHandler myDBHandler = new MyDBHandler(Main4Activity.this);
        myDBHandler.deleteAccount(data.getMyUserName());
        data.getScores().set(level-1,Count);
        myDBHandler.addUser(data);
    }
}
