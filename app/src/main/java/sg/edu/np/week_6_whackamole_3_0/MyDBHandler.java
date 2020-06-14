package sg.edu.np.week_6_whackamole_3_0;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class MyDBHandler extends SQLiteOpenHelper {
    /*
        The Database has the following properties:
        1. Database name is WhackAMole.db
        2. The Columns consist of
            a. Username
            b. Password
            c. Level
            d. Score
        3. Add user method for adding user into the Database.
        4. Find user method that finds the current position of the user and his corresponding
           data information - username, password, level highest score for each level
        5. Delete user method that deletes based on the username
        6. To replace the data in the database, we would make use of find user, delete user and add user

        The database shall look like the following:

        Username | Password | Level | Score
        --------------------------------------
        User A   | XXX      | 1     |    0
        User A   | XXX      | 2     |    0
        User A   | XXX      | 3     |    0
        User A   | XXX      | 4     |    0
        User A   | XXX      | 5     |    0
        User A   | XXX      | 6     |    0
        User A   | XXX      | 7     |    0
        User A   | XXX      | 8     |    0
        User A   | XXX      | 9     |    0
        User A   | XXX      | 10    |    0
        User B   | YYY      | 1     |    0
        User B   | YYY      | 2     |    0

     */

    private static final String FILENAME = "MyDBHandler.java";
    private static final String TAG = "Whack-A-Mole3.0!";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Whack-A-Mole.db";
    private static final String TABLE_NAME = "Users";
    private static final String USERNAME = "Username";
    private static final String PASSWORD = "Password";
    private static final String LEVEL = "Level" ;
    private static final String SCORE = "Score";

    public MyDBHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_NAME +
                "(Username TEXT," +
                " Password TEXT," +
                " Level INTEGER," +
                " Score INTEGER)";

        db.execSQL(CREATE_PRODUCTS_TABLE);
        Log.v(TAG, "DB Created: " + TABLE_NAME);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addUser(UserData userData)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        for(int i = 0; i < userData.getLevels().size(); i++) {
            ContentValues values = new ContentValues();
            values.put(USERNAME, userData.getMyUserName());
            values.put(PASSWORD, userData.getMyPassword());
            values.put(LEVEL, userData.getLevels().get(i));
            values.put(SCORE, userData.getScores().get(i));

            Log.v(TAG, FILENAME + ": Adding data for Database: " + values.toString());

            db.insert(TABLE_NAME,null, values);
        }

        db.close();
    }

    public UserData findUser(String username)
    {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE "
                + USERNAME + " = \"" + username + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        if (cursor.moveToFirst()) {
            UserData data = new UserData();
            data.setMyUserName(cursor.getString(0));
            data.setMyPassword(cursor.getString(1));

            do{
                data.getLevels().add(cursor.getInt(2));
                data.getScores().add(cursor.getInt(3));
            }
            while(cursor.moveToNext());
            Log.v(TAG, FILENAME + ": QueryData: " + data.getLevels().toString() + data.getScores().toString());

            return data;
        }
        else {
            Log.v(TAG, FILENAME + ": No data found!");
            return null;
        }
    }

    public boolean deleteAccount(String username) {
        boolean result = false;

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE "
                + USERNAME + " = '" + username + "' ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        UserData data = new UserData();
        if (cursor.moveToFirst()) {
            data.setMyUserName(cursor.getString(0));
            db.delete(TABLE_NAME, USERNAME + " = ?",
                    new String[] { String.valueOf(data.getMyUserName())});
            cursor.close();
            Log.v(TAG, FILENAME + ": Database delete user: " + query);
            result = true;
        }

        db.close();

        return result;
    }
}
