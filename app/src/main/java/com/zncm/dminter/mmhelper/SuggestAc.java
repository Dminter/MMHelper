package com.zncm.dminter.mmhelper;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.kenumir.materialsettings.MaterialSettings;
import com.kenumir.materialsettings.items.DividerItem;
import com.kenumir.materialsettings.items.TextItem;
import com.kenumir.materialsettings.storage.StorageInterface;
import com.malinskiy.materialicons.Iconify;
import com.zncm.dminter.mmhelper.utils.Xutils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 建议活动 com.taobao.shoppingstreets,喵街com.haodou.recipe,好豆com.taobao.taobao,手机淘宝com.baidu.yuedu,百度阅读com.tencent.mm,微信com.dv.adm.pay,ADM
 * Procom.lerist.ghosts,Go Hostscom.mxtech.videoplayer.pro,MX 播放器专业版
 * com.zncm.mxgtd,MxGTDcn.wq.myandroidtools,MyAndroidToolscom.teslacoilsw.launcher,Nova
 * Launchercom.tencent.mobileqq,QQcom.sharpregion.tapet,Tapetcom.UCMobile,UC浏览器
 * com.ttxapps.wifiadb,WiFi ADBcom.duokan.phone.remotecontroller,万能遥控com.chinamworld.main,中国建设银行com.bankcomm.maidanba,买单吧org.fungo.fungolive,云图直播com.didi.echo,
 * 优步-Ubercom.ifeng.discovery,凤凰FM探索版tv.danmaku.bili,哔哩哔哩com.wm.dmall,多点com.dianping.v1,大众点评com.tencent.qqgame.xq,天天象棋com.babytree.apps.pregnancy,宝宝树孕育
 * com.hundsun.InternetSaleTicket,巴巴快巴com.sina.weibo,微博com.weqia.wq1,微洽com.smile.gifmaker,快手cn.com.open.mooc,慕课网com.zncm.library,我的库
 * com.jingdong.app.mall,手机京东com.xiaomi.mitv.phone.tvassistant,投屏神器com.wacai365,挖财记账理财com.daimajia.gold,掘金com.eg.android.AlipayGphone,支付宝com.youdao.note,有道云笔记com.dtdream.publictransit,
 * 杭州公交com.smk,杭州市民卡cn.pinming.zz,桩桩com.example.businesshall,浙江移动手机营业厅com.taobao.movie.android,淘票票com.baidu.netdisk,百度网盘com.zhihu.android,知乎com.magicmagnet,神奇磁力
 * com.netease.cloudmusic,网易云音乐com.kaola,网易考拉海购com.duowan.kiwi,虎牙直播fm.qingting.qtradio,蜻蜓FMcom.douban.frodo,豆瓣com.coolapk.market,酷安com.keramidas.TitaniumBackup,钛备份com.MobileTicket,
 * 铁路12306com.flyersoft.moonreaderp,静读天下专业版com.autonavi.minimap,高德地图
 *
 *
 * LIKE:com.eg.android.AlipayGphone,com.alipay.mobile.fund.ui.FundMainNewActivity_,余额宝
 * com.eg.android.AlipayGphone,com.alipay.mobile.onsitepay9.payer.OspTabHostActivity,付款码
 * com.eg.android.AlipayGphone,com.alipay.mobile.scan.as.main.MainCaptureActivity,扫一扫
 * com.tencent.mm,com.tencent.mm.plugin.offline.ui.WalletOfflineCoinPurseUI,付款码
 * com.tencent.mm,com.tencent.mm.plugin.scanner.ui.BaseScanUI,扫一扫 com.tencent.mm,com.tencent.mm.plugin.favorite.ui.FavoriteIndexUI,收藏
 * com.tencent.mm,com.tencent.mm.plugin.sns.ui.SnsTimeLineUI,朋友圈
 */
public class SuggestAc extends MaterialSettings {
    public static final String wx = "com.tencent.mm";
    public static final String zfb = "com.eg.android.AlipayGphone";
    public static final String wc = "com.wacai365";
    public static final String tb = "com.taobao.taobao";
    public static final String bili = "tv.danmaku.bili";
    public static final String dianping = "com.dianping.v1";
    public static final String youdaonote = "com.youdao.note";
    public static final String jingdong = "com.jingdong.app.mall";
    public static final String cloudmusic = "com.netease.cloudmusic";
    public static final String coolapk = "com.coolapk.market";
    public static final String douban = "com.douban.frodo";
    public static final String cmd = "CMD";
    private Activity ctx;
    private LinkedHashMap<String, String> items = new LinkedHashMap<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        /**
         *本地有才建议
         * 微信，支付宝，QQ,，网易云音乐，微博，哔哩哔哩，京东，高德地图，知乎，有道云笔记，
         */
        sugItems(items);
        /**
         *新本没有什么可用的去掉了
         */
//        if (Xutils.hasInstalledApp(ctx, bili)) {
//            items.put(bili, "哔哩哔哩");
//        }
        if (items != null && items.size() > 0) {
            for (Map.Entry<String, String> entry : items.entrySet()
                    ) {
                addMyItem(entry.getKey(), entry.getValue());
            }
        }

    }

    public static void sugItems(LinkedHashMap<String, String> items) {
        Context ctx = MyApplication.getInstance().ctx;



        if (Xutils.hasInstalledApp(ctx, wx)) {
            items.put(wx, "微信");
        }
        if (Xutils.hasInstalledApp(ctx, zfb)) {
            items.put(zfb, "支付宝");
        }
        if (Xutils.hasInstalledApp(ctx, cloudmusic)) {
            items.put(cloudmusic, "网易云音乐");
        }

        if (Xutils.hasInstalledApp(ctx, coolapk)) {
            items.put(coolapk, "酷安");
        }
        if (Xutils.hasInstalledApp(ctx, tb)) {
            items.put(tb, "淘宝");
        }
        if (Xutils.hasInstalledApp(ctx, jingdong)) {
            items.put(jingdong, "京东");
        }
//        if (Xutils.hasInstalledApp(ctx, dianping)) {
//            items.put(dianping, "大众点评");
//        }
//        if (Xutils.hasInstalledApp(ctx, wc)) {
//            items.put(wc, "挖财");
//        }
        if (Xutils.hasInstalledApp(ctx, youdaonote)) {
            items.put(youdaonote, "有道云笔记");
        }
//        if (Xutils.hasInstalledApp(ctx, douban)) {
//            items.put(douban, "豆瓣");
//        }

        items.put(cmd, "Shell");
    }

    private void addMyItem(final String key, final String value) {
        addItem(new TextItem(ctx, "").setTitle(value).setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem textItem) {
                Intent intent = new Intent(SuggestAc.this, SuggestInfoActivity.class);
                intent.putExtra("name", value);
                intent.putExtra("pkName", key);
                startActivity(intent);
            }
        }));
        addItem(new DividerItem(ctx));
    }

    @Override
    public StorageInterface initStorageInterface() {
        return null;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item == null || item.getTitle() == null) {
            return false;
        }
        if (item.getTitle().equals("back")) {
            backDo();
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        backDo();
    }


    private void backDo() {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("back").setIcon(Xutils.initIconWhite(Iconify.IconValue.md_arrow_back)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

}
