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

        long re = database.insert(LocationProfileSchema.TABLE_NAME, null, values);
        database.close();
        return re;
    }

    public long saveAllSharkPointToDB(Context context, List<SharkPoint> sharkPointList) {
        SQLiteDatabase database = new SharkNetSQLiteOpenHelper(context).getWritableDatabase();
        long re = 0;

        database.beginTransaction();
        for (SharkPoint pointGeometry : sharkPointList) {
            ContentValues values = new ContentValues();

            values.put(LocationProfileSchema.COLUMN_WKT, pointGeometry.getWKT());
            re += database.insert(LocationProfileSchema.TABLE_NAME, null, values);
        }
        database.setTransactionSuccessful();
        database.endTransaction();

        Log.i(TAG, "Writing PointGeometries to DB");
        database.close();
        return re;
    }

    public List<SharkPoint> readSharkPointFromDB(Context context) {
        List<SharkPoint> pointGeometryList = new ArrayList<>();
        SQLiteDatabase database = new SharkNetSQLiteOpenHelper(context).getReadableDatabase();

        String[] projection = {
                LocationProfileSchema.COLUMN_WKT
        };
        Log.i(TAG, "Reading PointGeometries from DB");

        Cursor cursor = database.query(
                LocationProfileSchema.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                LocationProfileSchema.COLUMN_TIMESTAMP + " desc"
        );

        while(cursor.moveToNext()) {
            try {
                pointGeometryList.add(new SharkPoint(InMemoSharkGeometry.createGeomByWKT(cursor.getString(0))));
            } catch (SharkKBException e) {
                e.printStackTrace();
            }
        }

        database.close();
        return pointGeometryList;
    }
}
