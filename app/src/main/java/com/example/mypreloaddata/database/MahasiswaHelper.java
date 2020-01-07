package com.example.mypreloaddata.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.example.mypreloaddata.model.MahasiswaModel;

import java.util.ArrayList;

public class MahasiswaHelper {
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    private static MahasiswaHelper INSTANCE;

    public MahasiswaHelper(Context context){
        databaseHelper = new DatabaseHelper(context);
    }

    public static MahasiswaHelper getInstance(Context context){
        if (INSTANCE == null){
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null){
                    INSTANCE = new MahasiswaHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException{
        database = databaseHelper.getWritableDatabase();
    }

    public void close(){
        databaseHelper.close();

        if (database.isOpen())
            database.close();
    }

    public ArrayList<MahasiswaModel> getAllData() {
        Cursor cursor = database.query(DatabaseContract.TABLE_NAME, null, null, null, null, null, DatabaseContract.MahasiswaColumns._ID + " ASC", null);
        cursor.moveToNext();
        ArrayList<MahasiswaModel> arrayList = new ArrayList<>();
        MahasiswaModel mahasiswaModel;
        if (cursor.getCount() > 0) {
            do {
                mahasiswaModel = new MahasiswaModel();
                mahasiswaModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.MahasiswaColumns._ID)));
                mahasiswaModel.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MahasiswaColumns.NAMA)));
                mahasiswaModel.setNim(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MahasiswaColumns.NIM)));

                arrayList.add(mahasiswaModel);
                cursor.moveToNext();
            }
            while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insert(MahasiswaModel mahasiswaModel){
        ContentValues initialValues = new ContentValues();
        initialValues.put(DatabaseContract.MahasiswaColumns.NAMA, mahasiswaModel.getName());
        initialValues.put(DatabaseContract.MahasiswaColumns.NIM, mahasiswaModel.getNim());
        return database.insert(DatabaseContract.TABLE_NAME, null, initialValues);
    }

    public ArrayList<MahasiswaModel> getDataByName(String nama){
        Cursor cursor = database.query(DatabaseContract.TABLE_NAME, null, DatabaseContract.MahasiswaColumns.NAMA + " LIKE ?", new String[]{nama}, null, null, DatabaseContract.MahasiswaColumns._ID + " ASC", null);
        cursor.moveToFirst();
        ArrayList<MahasiswaModel> arrayList = new ArrayList<>();
        MahasiswaModel mahasiswaModel;
        if (cursor.getCount() > 0){
            do {
                mahasiswaModel = new MahasiswaModel();
                mahasiswaModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.MahasiswaColumns._ID)));
                mahasiswaModel.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MahasiswaColumns.NAMA)));
                mahasiswaModel.setNim(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MahasiswaColumns.NIM)));

                arrayList.add(mahasiswaModel);
                cursor.moveToNext();
            }
            while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }


    public void beginTransaction(){
        database.beginTransaction();
    }

    public void setTransactionSuccess(){
        database.setTransactionSuccessful();
    }

    public void endTransaction(){
        database.endTransaction();
    }

    public void insertTransaction(MahasiswaModel mahasiswaModel){
        String sql = "INSERT INTO " + DatabaseContract.TABLE_NAME + " (" + DatabaseContract.MahasiswaColumns.NAMA + ", " + DatabaseContract.MahasiswaColumns.NIM + ") VALUES (?, ?)";
        SQLiteStatement stmt = database.compileStatement(sql);
        stmt.bindString(1, mahasiswaModel.getName());
        stmt.bindString(2, mahasiswaModel.getNim());
        stmt.execute();
        stmt.clearBindings();
    }

}
