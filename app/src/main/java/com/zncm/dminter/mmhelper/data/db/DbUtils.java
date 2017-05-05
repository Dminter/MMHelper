package com.zncm.dminter.mmhelper.data.db;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zncm.dminter.mmhelper.Constant;
import com.zncm.dminter.mmhelper.MyApplication;
import com.zncm.dminter.mmhelper.data.CardInfo;
import com.zncm.dminter.mmhelper.data.EnumInfo;
import com.zncm.dminter.mmhelper.data.PkInfo;
import com.zncm.dminter.mmhelper.data.RefreshEvent;
import com.zncm.dminter.mmhelper.t9search.T9SearchSupport;
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
 * 数据库查询工具类
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


    /**
     * 导入txt
     */
    public static List<String> importTxt(File file) {
        List<String> dataList = new ArrayList<String>();

        BufferedReader br = null;
        try {
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return dataList;
    }

    /**
     * 活动排序
     */
    public static void updateCardPos(int cardId, int pos) {

        try {
            CardInfo cardInfo = getCardInfoById(cardId);
            if (cardInfo != null) {
                cardInfo.setExi4(pos);
                cardInfoDao.update(cardInfo);
            }
            return;
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    /**
     * 应用排序
     */
    public static void updatePkPos(String packageName, int pos) {

        try {
            PkInfo pkInfo = getPkOne(packageName);
            if (pkInfo != null) {
                pkInfo.setExb3(pos);
                pkDao.update(pkInfo);
            }
            return;
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    /**
     * 活动导出为txt
     */
    public static void importCardFromTxt(List<String> list, boolean isFirst) {
        init();
        try {
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

    /**
     * 根据包名，查询活动
     */
    public static ArrayList<CardInfo> getCardInfosByPackageName(String packageName) {
        init();
        ArrayList<CardInfo> cardInfos = new ArrayList();
        try {
            QueryBuilder builder = cardInfoDao.queryBuilder();
            if (Xutils.isNotEmptyOrNull(packageName)) {
                builder.where().eq("status", EnumInfo.cStatus.NORMAL.getValue()).and().eq("exi1", "1").and().eq("packageName", packageName);
                builder.orderBy("exi2", false);
            }
            builder.orderBy("time", false).limit(Constant.MAX_DB_QUERY);
            cardInfos = (ArrayList<CardInfo>) cardInfoDao.query(builder.prepare());
        } catch (Exception localException) {
            localException.printStackTrace();
        }
        return cardInfos;
    }

    /**
     * 应用转换为活动
     */
    public static ArrayList<CardInfo> getPkInfosToCard() {
        init();
        ArrayList<CardInfo> cardInfos = new ArrayList();
        try {
            QueryBuilder builder = pkDao.queryBuilder();
            builder.where().eq("exi1", Integer.valueOf(EnumInfo.pkStatus.NORMAL.getValue()));
            builder.groupBy("packageName");
            builder.orderBy("name", true).limit(Constant.MAX_DB_QUERY);
            List<PkInfo> pkInfos = pkDao.query(builder.prepare());
            if (Xutils.listNotNull(pkInfos)) {
                for (PkInfo tmp : pkInfos
                        ) {
                    CardInfo info = new CardInfo();
                    info.setTitle(tmp.getName());
                    info.setPackageName(tmp.getPackageName());
                    info.setImg(tmp.getIcon());
                    info.setEx3(tmp.getEx3());
                    info.setType(EnumInfo.cType.START_APP.getValue());
                    info.setDisabled(tmp.getStatus() == EnumInfo.appStatus.DISABLED.getValue());
                    cardInfos.add(info);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cardInfos;
    }

    /**
     * 根据类名获取活动
     */
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

    /**
     * 根据命令查询活动
     */
    public static CardInfo getCardInfoByCmd(String cmd) {
        if (Xutils.isEmptyOrNull(cmd)) {
            return null;
        }
        CardInfo ret = null;
        init();
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
        return getPkOne(packageName, true);
    }

    /**
     * 根据包名查询应用
     */
    public static PkInfo getPkOne(String packageName, boolean isNormal) {
        if (Xutils.isEmptyOrNull(packageName)) {
            return null;
        }
        PkInfo ret = null;
        init();
        try {
            List<PkInfo> list = getPkInfos(packageName, isNormal);
            if (Xutils.listNotNull(list)) {
                ret = list.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 插入活动
     */
    public static void insertCard(CardInfo cardInfo) {
        init();
        try {
            if (cardInfo != null) {
                if (!Xutils.isNotEmptyOrNull(cardInfo.getTitle())) {
                    ApplicationInfo applicationInfo = Xutils.getAppInfo(cardInfo.getPackageName());
                    PackageManager pm = MyApplication.getInstance().ctx.getPackageManager();
                    String appName = applicationInfo.loadLabel(pm).toString();
                    if (applicationInfo != null && Xutils.isNotEmptyOrNull(appName)) {
                        cardInfo.setEx3(T9SearchSupport.buildT9Key(appName));
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

    /**
     * 插入应用
     */
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
                    if (Xutils.isEmptyOrNull(tmp.getEx3())) {
                        pkInfo.setEx3(T9SearchSupport.buildT9Key(pkInfo.getName()));
                    }
                    pkDao.update(tmp);
                } else {
                    pkInfo.setEx3(T9SearchSupport.buildT9Key(pkInfo.getName()));
                    pkDao.create(pkInfo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 删除应用
     */
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

    /**
     * 更新活动
     */
    public static void updateCard(CardInfo cardInfo) {
        init();
        try {
            if (cardInfo != null) {
                if (Xutils.isNotEmptyOrNull(cardInfo.getTitle())) {
                    cardInfo.setEx3(T9SearchSupport.buildT9Key(cardInfo.getTitle()));
                }
                cardInfoDao.update(cardInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新应用
     */
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

    /**
     * 查询最大活动id用于置顶
     */
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


    /**
     * 根据包名获取所有活动
     */
    public static ArrayList<CardInfo> getCardInfos(String packageName) {
        return getCardInfos(packageName, Constant.MAX_DB_QUERY);
    }

    /**
     * 根据包名获取所有活动 分页
     */
    public static ArrayList<CardInfo> getCardInfos(String packageName, int limit) {
        init();
        ArrayList<CardInfo> datas = new ArrayList<CardInfo>();
        try {
            QueryBuilder<CardInfo, Integer> builder = cardInfoDao.queryBuilder();
            if (Xutils.isNotEmptyOrNull(packageName)) {
                if (packageName.equals(EnumInfo.homeTab.LIKE.getValue())) {
                    builder.where().eq("status", Integer.valueOf(EnumInfo.cStatus.NORMAL.getValue())).and().eq("exi1", 1);
                    builder.orderBy("exi2", false);
                    builder.orderBy("exi4", true);
                    builder.orderBy("time", false).limit(limit);
                } else if (packageName.equals(EnumInfo.homeTab.ALL.getValue())) {
                    builder.where().eq("status", EnumInfo.cStatus.NORMAL.getValue());
                } else {
                    builder.where().eq("status", EnumInfo.cStatus.NORMAL.getValue()).and().eq("exi3", packageName);
                    builder.orderBy("exi2", false);
                }

            } else {
                builder.where().eq("status", EnumInfo.cStatus.NORMAL.getValue());
            }
            builder.orderBy("time", false).limit(limit);
            List<CardInfo> list = cardInfoDao.query(builder.prepare());
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


    /**
     * 根据包名获取应用
     */
    public static ArrayList<PkInfo> getPkInfos(String pkgName) {
        return getPkInfos(pkgName, true);
    }

    public static ArrayList<PkInfo> getPkInfos(String pkgName, boolean isNormal) {
        init();
        ArrayList<PkInfo> datas = new ArrayList<PkInfo>();
        try {

            QueryBuilder<PkInfo, Integer> builder = pkDao.queryBuilder();
            if (isNormal) {
                if (Xutils.isNotEmptyOrNull(pkgName)) {
                    builder.where().eq("packageName", pkgName);
                }
                builder.where().eq("exi1", Integer.valueOf(EnumInfo.pkStatus.NORMAL.getValue()));
                builder.groupBy("packageName");
                builder.orderBy("exb3", true).orderBy("ex4", false).orderBy("name", true).limit(Constant.MAX_DB_QUERY);
            } else {
                if (Xutils.isNotEmptyOrNull(pkgName)) {
                    //.and().eq("exi1", Integer.valueOf(EnumInfo.pkStatus.NORMAL.getValue()))
                    builder.where().eq("packageName", pkgName);
                } else {
                    builder.where().eq("exi1", Integer.valueOf(EnumInfo.pkStatus.NORMAL.getValue()));
                }
            }
            List<PkInfo> list = pkDao.query(builder.prepare());
            if (Xutils.listNotNull(list)) {
                datas.addAll(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datas;
    }


    /**
     * 根据ID查询活动
     */
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


    /**
     * 开发者微信
     */
    public static void cardXm() {
        if (DbUtils.getCardInfoByCmd(Constant.author_wx) != null) {
            return;
        }
        //勾搭开发者
        DbUtils.insertCard(new CardInfo(EnumInfo.cType.WX.getValue(), Constant.author_wx, "开发者微信"));
    }

    /**
     * 更新应用
     */
    public static void cardUpdate() {

        if (DbUtils.getCardInfoByCmd(Constant.update_url) != null) {
            return;
        }
        //更新
        DbUtils.insertCard(new CardInfo(EnumInfo.cType.URL.getValue(), Constant.update_url, "更新地址"));
    }
}
