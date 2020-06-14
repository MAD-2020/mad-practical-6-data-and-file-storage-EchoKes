package sg.edu.np.week_6_whackamole_3_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity {

    /*
        1. This is the main page for user to log in
        2. The user can enter - Username and Password
        3. The user login is checked against the database for existence of the user and prompts
           accordingly via Toastbox if user does not exist. This loads the level selection page.
        4. There is an option to create a new user account. This loads the create user page.
     */
    private static final String FILENAME = "MainActivity.java";
    private static final String TAG = "Whack-A-Mole3.0!";

    EditText username,password;
    Button login;
    TextView newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.ET_username);
        password = findViewById(R.id.ET_pass);
        login = findViewById(R.id.btn_login);
        newUser = findViewById(R.id.TV_newUser);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = username.getText().toString().trim();
                String pass = password.getText().toString().trim();

                if(isValidUser(name,pass)){
                    Log.v(TAG, FILENAME + ": Valid User! Logging in");
                    MyDBHandler myDBHandler = new MyDBHandler(MainActivity.this);
                    UserData data = myDBHandler.findUser(name);

                    Log.v(TAG, FILENAME + ": Logging in with: " + name + ": " + pass);
                    Intent intent = new Intent(MainActivity.this , Main3Activity.class);
                    intent.putExtra("data", data);
                    startActivity(intent);
                }
                else{
                    Log.v(TAG, FILENAME + ": Invalid user!");
                    Toast.makeText(MainActivity.this , "Invalid Username and Password" , Toast.LENGTH_SHORT).show();
                }
            }
        });

        newUser.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.v(TAG, FILENAME + ": Create new user!");
                Intent create = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(create);
            }
        });
    }

    protected void onStop(){
        super.onStop();
        finish();
    }

    public boolean isValidUser(String userName, String password){
        MyDBHandler myDBHandler = new MyDBHandler(MainActivity.this);
        UserData data = myDBHandler.findUser(userName);

        if (data == null) {
            return false;
        }
        else {
            Log.v(TAG, FILENAME + ": Running Checks..." + data.getMyUserName() + ": "
                    + data.getMyPassword() +" <--> "+ userName + " " + password);
            return true;
        }

    }

}
