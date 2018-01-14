package net.sharksystem.sharknet.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.sharksystem.sharknet.data.dao.LocationProfileSchema;

/**
 * Created by Max on 13.01.18.
 *
 * @author Max Oehme (546545)
 */

public class SharkNetSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "sharknet_database";

    public SharkNetSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(LocationProfileSchema.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + LocationProfileSchema.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
