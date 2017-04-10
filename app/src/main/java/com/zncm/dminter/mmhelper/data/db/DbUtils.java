package com.zncm.dminter.mmhelper.data.db;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zncm.dminter.mmhelper.Constant;
import com.zncm.dminter.mmhelper.MyApplication;
import com.zncm.dminter.mmhelper.data.CardInfo;
import com.zncm.dminter.mmhelper.data.EnumInfo;
import com.zncm.dminter.mmhelper.data.PkInfo;
import com.zncm.dminter.mmhelper.data.RefreshEvent;
import com.zncm.dminter.mmhelper.utils.PinyinConv;
import com.zncm.dminter.mmhelper.utils.Xutils;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by dminter on 2016/7/23.
 */

public class DbUtils {

    static RuntimeExceptionDao<CardInfo, Integer> cardInfoDao;
    static RuntimeExceptionDao<PkInfo, Integer> pkDao;
    static DbHelper databaseHelper = null;

    static DbHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(MyApplication.getInstance().ctx, DbHelper.class);
        }
        return databaseHelper;
    }

    static void init() {
        if (cardInfoDao == null) {
            cardInfoDao = getHelper().getCardInfoDao();
        }
        if (pkDao == null) {
            pkDao = getHelper().getPkInfoDao();
        }

    }


    public static List<String> importTxt(File file) {
        List<String> dataList = new ArrayList<String>();

        BufferedReader br = null;
        try {
            //new FileReader(file)
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String line = "";
            while ((line = br.readLine()) != null) {
                dataList.add(line);
            }
        } catch (Exception e) {
        } finally {
            if (br != null) {
                try {
                    br.close();
                    br = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return dataList;
    }

    public static void importCardFromTxt(List<String> list, boolean isFirst) {
        init();
        try {
//            File file = new File(path);
            List<String> datas = list;
            if (Xutils.listNotNull(datas)) {
                for (String info :
                        datas) {
                    if (Xutils.isNotEmptyOrNull(info) && info.contains("\\|")) ;
                    {
                        try {
                            String acName = info.split("\\|")[0];
                            String pName = info.split("\\|")[1];
                            String cName = info.split("\\|")[2];
                            CardInfo card = new CardInfo(pName, cName, acName);
                            if (DbUtils.getCardInfoByClassName(cName) == null) {
                                if (isFirst) {
                                    card.setExi1(new Random().nextInt(2));
                                }
                                DbUtils.insertCard(card);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static ArrayList<CardInfo> getCardInfosByPackageName(String packageName) {
        init();
        ArrayList<CardInfo> cardInfos = new ArrayList();
        try {
            QueryBuilder builder = cardInfoDao.queryBuilder();
            if (Xutils.isNotEmptyOrNull(packageName)) {
                builder.where().eq("status", Integer.valueOf(EnumInfo.cStatus.NORMAL.getValue())).and().eq("exi1", "1").and().eq("packageName", packageName);
                builder.orderBy("exi2", false);
            }
            builder.orderBy("time", false).limit(Constant.MAX_DB_QUERY);
            List all = cardInfoDao.query(builder.prepare());
            if (!Xutils.listNotNull(all)) {
                cardInfos.addAll(all);
            }
        } catch (Exception localException) {
            localException.printStackTrace();
        }
        return cardInfos;
    }

    public static CardInfo getCardInfoByClassName(String cName) {
        if (Xutils.isEmptyOrNull(cName)) {
            return null;
        }
        CardInfo ret = null;
        init();
        ArrayList<CardInfo> datas = new ArrayList<CardInfo>();
        try {
            QueryBuilder<CardInfo, Integer> builder = cardInfoDao.queryBuilder();
            builder.where().eq("status", EnumInfo.cStatus.NORMAL.getValue()).and().eq("className", cName);
            builder.orderBy("time", false).limit(1);
            List<CardInfo> list = cardInfoDao.query(builder.prepare());
            if (Xutils.listNotNull(list)) {
                ret = list.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static CardInfo getCardInfoByCmd(String cmd) {
        if (Xutils.isEmptyOrNull(cmd)) {
            return null;
        }
        CardInfo ret = null;
        init();
        ArrayList<CardInfo> datas = new ArrayList<CardInfo>();
        try {
            QueryBuilder<CardInfo, Integer> builder = cardInfoDao.queryBuilder();
            builder.where().eq("status", EnumInfo.cStatus.NORMAL.getValue()).and().eq("cmd", cmd);
            builder.orderBy("time", false).limit(1);
            List<CardInfo> list = cardInfoDao.query(builder.prepare());
            if (Xutils.listNotNull(list)) {
                ret = list.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static PkInfo getPkOne(String packageName) {
        if (Xutils.isEmptyOrNull(packageName)) {
            return null;
        }
        PkInfo ret = null;
        init();
        try {
            List<PkInfo> list = getPkInfos(packageName);
            if (Xutils.listNotNull(list)) {
                ret = list.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static void insertCard(CardInfo cardInfo) {
        init();
        try {


            if (cardInfo != null) {
                if (!Xutils.isNotEmptyOrNull(cardInfo.getTitle())) {
                    ApplicationInfo applicationInfo = Xutils.getAppInfo(cardInfo.getPackageName());
                    PackageManager pm = MyApplication.getInstance().ctx.getPackageManager();
                    String appName = applicationInfo.loadLabel(pm).toString();
                    if (applicationInfo != null && Xutils.isNotEmptyOrNull(appName)) {
                        String pinyin = PinyinConv.cn2py(appName);
                        cardInfo.setEx2(pinyin);
                        cardInfo.setTitle(appName);
                    }
                }
                cardInfoDao.create(cardInfo);
                EventBus.getDefault().post(new RefreshEvent(EnumInfo.RefreshEnum.ALL.getValue()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertPkInfo(PkInfo pkInfo) {
        init();
        try {
            if (pkInfo != null) {
                if (Xutils.isEmptyOrNull(pkInfo.getPackageName())) {
                    return;
                }
                PkInfo tmp = getPkOne(pkInfo.getPackageName());
                if (tmp != null) {
                    tmp.setStatus(pkInfo.getStatus());
                    if (Xutils.isEmptyOrNull(tmp.getEx2())) {
                        String pinyin = PinyinConv.cn2py(pkInfo.getName());
                        tmp.setEx2(pinyin);
                    }
                    pkDao.update(tmp);
                } else {
                    String pinyin = PinyinConv.cn2py(pkInfo.getName());
                    pkInfo.setEx2(pinyin);
                    pkDao.create(pkInfo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clearPkInfo() {
        init();
        try {
            pkDao.deleteBuilder().where().ge("id", "1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteCard(CardInfo cardInfo) {
        init();
        try {
            if (cardInfo != null) {
                cardInfoDao.delete(cardInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deletePk(PkInfo pkInfo) {
        init();
        try {
            if (pkInfo != null) {
                pkDao.delete(pkInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateCard(CardInfo cardInfo) {
        init();
        try {
            if (cardInfo != null) {
                if (Xutils.isNotEmptyOrNull(cardInfo.getTitle())) {
                    String pinyin = PinyinConv.cn2py(cardInfo.getTitle());
                    cardInfo.setEx2(pinyin);
                }
                cardInfoDao.update(cardInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updatePkInfo(PkInfo pkInfo) {
        init();
        try {
            if (pkInfo != null) {
                pkDao.update(pkInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //select packageName FROM cardinfo WHERE className NOTNULL AND type = 2 group by packageName ORDER BY count(packageName) DESC


//    public static ArrayList<PkInfo> getPkInfos() {
//        init();
//        ArrayList<PkInfo> infos = new ArrayList<PkInfo>();
//        try {
//            GenericRawResults<String[]> rawResults =
//                    cardInfoDao.queryRaw("select packageName FROM cardinfo WHERE className NOTNULL AND type = 2 AND status =1 group by packageName ORDER BY count(packageName) DESC LIMIT 10");
//
//
//            ArrayList<String> pNames = new ArrayList<>();
//            for (String[] resultColumns : rawResults) {
//                pNames.add(resultColumns[0]);
//            }
//            if (Xutils.listNotNull(pNames))
//                for (String pkgName : pNames
//                        ) {
//                    if (Xutils.isNotEmptyOrNull(pkgName)) {
//                        ApplicationInfo app = Xutils.getAppInfo(pkgName);
//                        if (app != null) {
//                            PackageManager pm = MyApplication.getInstance().ctx.getPackageManager();
//                            Drawable icon = app.loadIcon(pm);
//                            String name = app.loadLabel(pm).toString();
//                            String packageName = pkgName;
//
//                            PkInfo item = new PkInfo(packageName, name, icon);
//                            infos.add(item);
//                        }
//
//                    }
//                }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return infos;
//    }

//    select max("index"+0) as max_sort  from cardinfo


    public static int getMaxIndex() {
        init();
        int max_sort = 0;
        try {
            GenericRawResults<String[]> rawResults =
                    cardInfoDao.queryRaw("select max(exi2+0) as max_sort  from cardinfo");
            for (String[] resultColumns : rawResults) {
                max_sort = Integer.parseInt(resultColumns[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return max_sort;
    }

    public static ArrayList<CardInfo> getCardInfos(String packageName) {
        init();
        ArrayList<CardInfo> datas = new ArrayList<CardInfo>();
        try {
            QueryBuilder<CardInfo, Integer> builder = cardInfoDao.queryBuilder();
//            builder.where().eq("status", EnumInfo.cStatus.NORMAL.getValue());
            if (Xutils.isNotEmptyOrNull(packageName)) {
                if (packageName.equals(EnumInfo.homeTab.LIKE.getValue())) {
                    builder.where().eq("status", EnumInfo.cStatus.NORMAL.getValue()).and().eq("exi1", 1);
                    builder.orderBy("exi2", false);
                } else if (packageName.equals(EnumInfo.homeTab.ALL.getValue())) {
                    builder.where().eq("status", EnumInfo.cStatus.NORMAL.getValue());
                } else {
                    builder.where().eq("status", EnumInfo.cStatus.NORMAL.getValue()).and().eq("exi3", packageName);
                    builder.orderBy("exi2", false);
//                    builder.where().eq("status", EnumInfo.cStatus.NORMAL.getValue()).and().eq("packageName", packageName);
                }

            } else {
                builder.where().eq("status", EnumInfo.cStatus.NORMAL.getValue());
            }
            builder.orderBy("time", false).limit(Constant.MAX_DB_QUERY);
//            builder.orderBy("exi2", false).orderBy("packageName", false).orderBy("time", false).limit(Constant.MAX_DB_QUERY);
            List<CardInfo> list = cardInfoDao.query(builder.prepare());
            if (Xutils.listNotNull(list)) {
                datas.addAll(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datas;
    }

    public static ArrayList<CardInfo> getCardInfosByTitle(String title) {
        if (Xutils.isEmptyOrNull(title)) {
            return null;
        }
        init();
        ArrayList<CardInfo> datas = new ArrayList<CardInfo>();
        try {
            QueryBuilder<CardInfo, Integer> builder = cardInfoDao.queryBuilder();
            builder.where().like("title", "%" + title + "%").or().like("ex1", "%" + title + "%").or().like("ex2", "%" + title + "%").and().eq("status", EnumInfo.cStatus.NORMAL.getValue());
            builder.orderBy("time", false).limit(Constant.MAX_DB_QUERY);
            List<CardInfo> list = cardInfoDao.query(builder.prepare());
            if (Xutils.listNotNull(list)) {
                datas.addAll(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datas;
    }


    public static ArrayList<PkInfo> getPkInfos(String packageName) {
        init();
        ArrayList<PkInfo> datas = new ArrayList<PkInfo>();
        try {
            QueryBuilder<PkInfo, Integer> builder = pkDao.queryBuilder();
            if (Xutils.isNotEmptyOrNull(packageName)) {
                builder.where().eq("packageName", packageName);
            }
            builder.orderBy("name", true).limit(Constant.MAX_DB_QUERY);
            List<PkInfo> list = pkDao.query(builder.prepare());
            if (Xutils.listNotNull(list)) {
                datas.addAll(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datas;
    }


    //冷冻室里面的APP
    public static ArrayList<PkInfo> getPkInfosBatStop(int type) {
        init();
        ArrayList<PkInfo> datas = new ArrayList<PkInfo>();
        try {
            QueryBuilder<PkInfo, Integer> builder = pkDao.queryBuilder();
            if (type != -1) {
                builder.where().eq("exb2", type).and().eq("exi1", Integer.valueOf(EnumInfo.pkStatus.NORMAL.getValue()));
            } else {
                builder.where().eq("exi1", Integer.valueOf(EnumInfo.pkStatus.NORMAL.getValue()));
            }
            builder.groupBy("packageName");
            builder.orderBy("exb3", true).orderBy("ex4", false).orderBy("ex2", true).limit(Constant.MAX_DB_QUERY);
            List<PkInfo> list = pkDao.query(builder.prepare());
            if (Xutils.listNotNull(list)) {
                datas.addAll(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datas;
    }

    public static ArrayList<CardInfo> getPkInfosByTitle(String title) {
        init();
        ArrayList<CardInfo> datas = new ArrayList<CardInfo>();
        try {
            QueryBuilder<PkInfo, Integer> builder = pkDao.queryBuilder();
            builder.where().like("name", "%" + title + "%").or().like("ex1", "%" + title + "%").or().like("ex2", "%" + title + "%");
            builder.orderBy("name", true).limit(Constant.MAX_DB_QUERY);
            List<PkInfo> list = pkDao.query(builder.prepare());
            if (Xutils.listNotNull(list)) {
//                datas.addAll(list);
                for (PkInfo tmp : list
                        ) {
                    CardInfo info = new CardInfo();
                    info.setTitle(tmp.getName());
                    info.setPackageName(tmp.getPackageName());
                    info.setImg(tmp.getIcon());
                    info.setType(EnumInfo.cType.START_APP.getValue());
                    info.setDisabled(tmp.getStatus() == EnumInfo.appStatus.DISABLED.getValue());
                    datas.add(info);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datas;
    }

    public static ArrayList<PkInfo> getPkInfos() {
        init();
        ArrayList<PkInfo> datas = new ArrayList<PkInfo>();
        try {
            QueryBuilder<PkInfo, Integer> builder = pkDao.queryBuilder();
            builder.orderBy("status", true).orderBy("ex2", true).limit(Constant.MAX_DB_QUERY);
            List<PkInfo> list = pkDao.query(builder.prepare());
            if (Xutils.listNotNull(list)) {
                datas.addAll(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datas;
    }

    public static void deletePk() {
        init();
        try {
            DeleteBuilder<PkInfo, Integer> builder = pkDao.deleteBuilder();
            builder.where().isNotNull("status");
            pkDao.delete(builder.prepare());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static CardInfo getCardInfoById(int cardId) {
        init();
        CardInfo cardInfo = null;
        try {
            cardInfo = cardInfoDao.queryForId(cardId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cardInfo;
    }


    public static void cardXm() {
        if (DbUtils.getCardInfoByCmd(Constant.author_wx) != null) {
            return;
        }
        //勾搭开发者
        DbUtils.insertCard(new CardInfo(EnumInfo.cType.WX.getValue(), Constant.author_wx, "开发者微信"));
    }

    public static void cardUpdate() {

        if (DbUtils.getCardInfoByCmd(Constant.update_url) != null) {
            return;
        }
        //更新
        DbUtils.insertCard(new CardInfo(EnumInfo.cType.URL.getValue(), Constant.update_url, "更新地址"));


    }
}
