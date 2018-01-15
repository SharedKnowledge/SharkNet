package net.sharksystem.sharknet.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.geom.SharkPoint;
import net.sharkfw.knowledgeBase.geom.inmemory.InMemoSharkGeometry;
import net.sharksystem.sharknet.data.dao.LocationProfileSchema;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Max on 13.01.18.
 *
 * @author Max Oehme (546545)
 */

public class SharkNetDbHelper {
    private static final String TAG = "Database";
    private static final SharkNetDbHelper ourInstance = new SharkNetDbHelper();

    public static SharkNetDbHelper getInstance() {
        return ourInstance;
    }

    private SharkNetDbHelper() {
    }

    public long saveSharkPointToDB(Context context, SharkPoint sharkPoint) {
        SQLiteDatabase database = new SharkNetSQLiteOpenHelper(context).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LocationProfileSchema.COLUMN_WKT, sharkPoint.getWKT());

        return database.insert(LocationProfileSchema.TABLE_NAME, null, values);
    }

    public long saveAllSharkPointToDB(Context context, List<SharkPoint> sharkPointList) {
        SQLiteDatabase database = new SharkNetSQLiteOpenHelper(context).getWritableDatabase();

        ContentValues values = new ContentValues();
        for (SharkPoint pointGeometry : sharkPointList) {
            values.put(LocationProfileSchema.COLUMN_WKT, pointGeometry.getWKT());
        }

        Log.i(TAG, "Writing PointGeometries to DB");
        return database.insert(LocationProfileSchema.TABLE_NAME, null, values);
    }

    public List<SharkPoint> readSharkPointFromDB(Context context) {
        List<SharkPoint> pointGeometryList = new ArrayList<>();
        SQLiteDatabase database = new SharkNetSQLiteOpenHelper(context).getReadableDatabase();

        String[] projection = {
                LocationProfileSchema.COLUMN_WKT
        };
        Log.i(TAG, "Reading PointGeometries from DB");

        Cursor cursor = database.query(
                LocationProfileSchema.TABLE_NAME,     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                LocationProfileSchema.COLUMN_TIMESTAMP + " desc"                                      // don't sort
        );

        while(cursor.moveToNext()) {
            try {
                pointGeometryList.add(new SharkPoint(InMemoSharkGeometry.createGeomByWKT(cursor.getString(0))));
            } catch (SharkKBException e) {
                e.printStackTrace();
            }
        }

        return pointGeometryList;
    }
}
