package info.devexchanges.contentproviderwithsqlitedb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "db_friend";

    public static final String TABLE_FRIENDS = "friend";
    public static final String ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_JOB = "job";

    private static final String CREATE_TABLE_FRIENDS = "create table " + TABLE_FRIENDS
            + " (" + ID + " integer primary key autoincrement, " + COL_NAME
            + " text not null, " + COL_JOB + " text not null);";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_FRIENDS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIENDS);
        onCreate(db);
    }
}