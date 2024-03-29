package com.example.mymusic;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "database.db";

    public static final int DB_VERSION = 1;

    public static final String TABLE_STUDENT = "music";
    private String WORD_CREATE_TABLE_SQL = "create table " + TABLE_STUDENT + "("
            + "id varchar(50) primary key ,"
            + "SongName varchar(100) not null,"
            + "path varchar(100) not null,"
            + "singer varchar(100) not null"
            + ");";

    public SQLiteDbHelper(Context context) {
        // 传递数据库名与版本号给父类
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // 在这里通过 db.execSQL 函数执行 SQL 语句创建所需要的表
        // 创建 students 表
        db.execSQL(WORD_CREATE_TABLE_SQL);








    }
    //sqlite
    public static String Querryy(SQLiteDatabase db,String s){
        Cursor cursor = db.query(SQLiteDbHelper.TABLE_STUDENT, null,
                "name=s",
                null, null, null, null);
        String res="";
        while (cursor.moveToNext()) {
            // 直接通过索引获取字段值
            int stuId = cursor.getInt(0);

            res = cursor.getString(2);
        }
        // 关闭光标
        cursor.close();
        return res;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // 数据库版本号变更会调用 onUpgrade 函数，在这根据版本号进行升级数据库
        switch (oldVersion) {
            case 1:
                // do something
                break;

            default:
                break;
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // 启动外键
            db.execSQL("PRAGMA foreign_keys = 1;");

            //或者这样写
            String query = String.format("PRAGMA foreign_keys = %s", "ON");
            db.execSQL(query);
        }
    }
}