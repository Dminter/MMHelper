package com.zncm.dminter.mmhelper.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.zncm.dminter.mmhelper.data.CardInfo;
import com.zncm.dminter.mmhelper.data.PkInfo;

/**
 * Created by dminter on 2016/7/23.
 */

public class DbHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "mmhelper.db";
    private static final int DATABASE_VERSION = 7;
    private RuntimeExceptionDao<CardInfo, Integer> cardInfoDao = null;
    private RuntimeExceptionDao<PkInfo, Integer> pkDao = null;


    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, CardInfo.class);
            TableUtils.createTableIfNotExists(connectionSource, PkInfo.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
//            String updateDb = "alter table CardInfo add `ccc` INTEGER ; ";
//            db.execSQL(updateDb);
            if (oldVersion < 7) {
                cardInfoDao.executeRaw("ALTER TABLE `CardInfo` ADD COLUMN img BLOB ;");
            }
            onCreate(db, connectionSource);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public RuntimeExceptionDao<CardInfo, Integer> getCardInfoDao() {
        if (cardInfoDao == null) {
            cardInfoDao = getRuntimeExceptionDao(CardInfo.class);
        }
        return cardInfoDao;
    }

    public RuntimeExceptionDao<PkInfo, Integer> getPkInfoDao() {
        if (pkDao == null) {
            pkDao = getRuntimeExceptionDao(PkInfo.class);
        }
        return pkDao;
    }


    @Override
    public void close() {
        super.close();
        cardInfoDao = null;
        pkDao = null;
    }
}
