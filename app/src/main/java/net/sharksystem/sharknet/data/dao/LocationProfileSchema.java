package net.sharksystem.sharknet.data.dao;

import android.content.ContentValues;
import android.provider.BaseColumns;

/**
 * Created by Max on 13.01.18.
 *
 * @author Max Oehme (546545)
 */

public interface LocationProfileSchema extends BaseColumns{
    public static final String TABLE_NAME = "locationprofile";
    public static final String COLUMN_WKT = "wkt";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME + " (" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_WKT + " TEXT, " +
            COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP " + ")";
}
