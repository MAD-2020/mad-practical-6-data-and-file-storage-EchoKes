package sg.edu.np.week_6_whackamole_3_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;

public class Main2Activity extends AppCompatActivity {
    /* Hint:
        1. This is the create new user page for user to log in
        2. The user can enter - Username and Password
        3. The user create is checked against the database for existence of the user and prompts
           accordingly via Toastbox if user already exists.
        4. For the purpose the practical, successful creation of new account will send the user
           back to the login page and display the "User account created successfully".
           the page remains if the user already exists and "User already exist" toastbox message will appear.
        5. There is an option to cancel. This loads the login user page.
     */


    private static final String FILENAME = "Main2Activity.java";
    private static final String TAG = "Whack-A-Mole3.0!";

    EditText username,password;
    Button createBtn,cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        username = findViewById(R.id.ET_createUsername);
        password = findViewById(R.id.ET_createPass);
        createBtn = findViewById(R.id.btn_create);
        cancelBtn = findViewById(R.id.btn_cancel);

        createBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String name = username.getText().toString();
                String pass = password.getText().toString();

                if(!isValidUser(name, pass)){
                    MyDBHandler myDBHandler = new MyDBHandler(Main2Activity.this);

                    ArrayList<Integer> levelList = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9,10));
                    ArrayList<Integer> scoreList = new ArrayList<>(Arrays.asList(0,0,0,0,0,0,0,0,0,0));

                    myDBHandler.addUser(new UserData(name , pass , levelList ,scoreList));

                    Log.v(TAG, FILENAME + ": New user created successfully!");
                    Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(Main2Activity.this, "User Created Successfully", Toast.LENGTH_SHORT).show();

                }else{
                    Log.v(TAG, FILENAME + ": User already exist during new user creation!");
                    Toast.makeText(Main2Activity.this, "User Already Exist. Please Try Again", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                username.setText("");
                password.setText("");

                Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public boolean isValidUser(String userName, String password){
        MyDBHandler myDBHandler = new MyDBHandler(Main2Activity.this);
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

    protected void onStop() {
        super.onStop();
        finish();
    }
}
