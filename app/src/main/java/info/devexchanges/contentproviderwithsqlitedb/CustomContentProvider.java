package info.devexchanges.contentproviderwithsqlitedb;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

public class CustomContentProvider extends ContentProvider {

    private DBHelper dbHelper;
    private SQLiteDatabase database;
    private static final String AUTHORITY = "info.devexchanges.contentprovider.CustomContentProvider";
    public static final int FRIENDS = 100;
    public static final int FRIEND_ID = 110;

    private static final String FRIENDS_BASE_PATH = "friend";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + FRIENDS_BASE_PATH);

    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/mt-tutorial";
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/mt-tutorial";
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, FRIENDS_BASE_PATH, FRIENDS);
        uriMatcher.addURI(AUTHORITY, FRIENDS_BASE_PATH + "/#", FRIEND_ID);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());

        // permissions to be writable
        database = dbHelper.getWritableDatabase();
        if (database == null)
            return false;
        else
            return true;
    }

    @SuppressWarnings("ConstantConditions")
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DBHelper.TABLE_FRIENDS);

        int uriType = uriMatcher.match(uri);
        switch (uriType) {
            case FRIEND_ID:
                queryBuilder.appendWhere(DBHelper.ID + "=" + uri.getLastPathSegment());
                break;
            case FRIENDS:
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }

        Cursor cursor = queryBuilder.query(dbHelper.getReadableDatabase(),
                projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @SuppressWarnings("ConstantConditions")
    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long row = database.insert(DBHelper.TABLE_FRIENDS, "", values);

        // If record is added successfully
        if (row > 0) {
            Uri newUri = ContentUris.withAppendedId(CONTENT_URI, row);
            getContext().getContentResolver().notifyChange(newUri, null);
            return newUri;
        }
        throw new SQLException("Fail to add a new record into " + uri);

    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = uriMatcher.match(uri);
        int rowsAffected = 0;
        switch (uriType) {
            case FRIENDS:
                rowsAffected = database.delete(DBHelper.TABLE_FRIENDS, selection, selectionArgs);
                break;
            case FRIEND_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsAffected = database.delete(DBHelper.TABLE_FRIENDS, DBHelper.ID + "=" + id, null);
                } else {
                    rowsAffected = database.delete(DBHelper.TABLE_FRIENDS, selection + " and " + DBHelper.ID + "=" + id, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown or Invalid URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return rowsAffected;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
