package com.zncm.dminter.mmhelper;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;

import com.malinskiy.materialicons.Iconify;
import com.zncm.dminter.mmhelper.data.CardInfo;
import com.zncm.dminter.mmhelper.data.EnumInfo;
import com.zncm.dminter.mmhelper.ft.MyFt;
import com.zncm.dminter.mmhelper.utils.Xutils;

import java.util.ArrayList;
import java.util.List;

/**
 * 建议活动的grid
 */
public class SuggestInfoActivity extends BaseActivity {
    Activity ctx;
    private List<CardInfo> cardInfos = new ArrayList();
    MyFt fragment;
    String pkName;

    private void allInit() {

        /**
         *wx
         *
         * com.tencent.mm,com.tencent.mm.plugin.card.ui.CardHomePageUI,卡包
         com.tencent.mm,com.tencent.mm.plugin.luckymoney.f2f.ui.LuckyMoneyF2FQRCodeUI,面对面红包
         com.tencent.mm,com.tencent.mm.plugin.wallet.balance.ui.WalletBalanceManagerUI,零钱
         com.tencent.mm,com.tencent.mm.plugin.mall.ui.MallIndexUI,我的钱包
         com.tencent.mm,com.tencent.mm.plugin.favorite.ui.FavoriteIndexUI,我的收藏
         com.tencent.mm,com.tencent.mm.plugin.setting.ui.setting.SelfQRCodeUI,二维码名片
         com.tencent.mm,com.tencent.mm.plugin.appbrand.ui.AppBrandLauncherUI,小程序
         com.tencent.mm,com.tencent.mm.plugin.shake.ui.ShakeReportUI,摇一摇
         com.tencent.mm,com.tencent.mm.plugin.sns.ui.SnsMsgUI,朋友圈消息
         com.tencent.mm,com.tencent.mm.plugin.sns.ui.En_424b8e16,朋友圈
         com.tencent.mm,com.tencent.mm.plugin.brandservice.ui.BrandServiceIndexUI,公众号
         com.tencent.mm,com.tencent.mm.ui.contact.ChatroomContactUI,群聊
         com.tencent.mm,com.tencent.mm.ui.conversation.BizConversationUI,订阅号
         com.tencent.mm,com.tencent.mm.plugin.collect.ui.CollectMainUI,我要收款
         com.tencent.mm,com.tencent.mm.plugin.subapp.ui.pluginapp.AddMoreFriendsUI,添加朋友
         com.tencent.mm,com.tencent.mm.plugin.pwdgroup.ui.FacingCreateChatRoomAllInOneUI,面对面建群
         com.tencent.mm,com.tencent.mm.plugin.search.ui.FTSMainUI,微信搜索
         com.tencent.mm,com.tencent.mm.plugin.offline.ui.WalletOfflineCoinPurseUI,付款码
         com.tencent.mm,com.tencent.mm.plugin.scanner.ui.BaseScanUI,扫一扫
         */

        /**
         *String packageName, String className, String appName
         */


        cardInfos = new ArrayList<>();

        if (SuggestAc.wx.equals(pkName)) {
            ArrayList<CardInfo> wxCard = new ArrayList<>();
            wxCard.add(new CardInfo(SuggestAc.wx, "com.tencent.mm.plugin.card.ui.CardHomePageUI", "卡包"));
            wxCard.add(new CardInfo(SuggestAc.wx, "com.tencent.mm.plugin.luckymoney.f2f.ui.LuckyMoneyF2FQRCodeUI", "面对面红包"));
            wxCard.add(new CardInfo(SuggestAc.wx, "com.tencent.mm.plugin.wallet.balance.ui.WalletBalanceManagerUI", "零钱"));
            wxCard.add(new CardInfo(SuggestAc.wx, "com.tencent.mm.plugin.mall.ui.MallIndexUI", "我的钱包"));
            wxCard.add(new CardInfo(SuggestAc.wx, "com.tencent.mm.plugin.favorite.ui.FavoriteIndexUI", "我的收藏"));
            wxCard.add(new CardInfo(SuggestAc.wx, "com.tencent.mm.plugin.setting.ui.setting.SelfQRCodeUI", "二维码名片"));
            wxCard.add(new CardInfo(SuggestAc.wx, "com.tencent.mm.plugin.appbrand.ui.AppBrandLauncherUI", "小程序"));
            wxCard.add(new CardInfo(SuggestAc.wx, "com.tencent.mm.plugin.shake.ui.ShakeReportUI", "摇一摇"));
            wxCard.add(new CardInfo(SuggestAc.wx, "com.tencent.mm.plugin.sns.ui.SnsMsgUI", "朋友圈消息"));
            wxCard.add(new CardInfo(SuggestAc.wx, "com.tencent.mm.plugin.sns.ui.En_424b8e16", "朋友圈"));
            wxCard.add(new CardInfo(SuggestAc.wx, "com.tencent.mm.plugin.brandservice.ui.BrandServiceIndexUI", "公众号"));
            wxCard.add(new CardInfo(SuggestAc.wx, "com.tencent.mm.ui.contact.ChatroomContactUI", "群聊"));
            wxCard.add(new CardInfo(SuggestAc.wx, "com.tencent.mm.ui.conversation.BizConversationUI", "订阅号"));
            wxCard.add(new CardInfo(SuggestAc.wx, "com.tencent.mm.plugin.collect.ui.CollectMainUI", "我要收款"));
            wxCard.add(new CardInfo(SuggestAc.wx, "com.tencent.mm.plugin.subapp.ui.pluginapp.AddMoreFriendsUI", "添加朋友"));
            wxCard.add(new CardInfo(SuggestAc.wx, "com.tencent.mm.plugin.pwdgroup.ui.FacingCreateChatRoomAllInOneUI", "面对面建群"));
            wxCard.add(new CardInfo(SuggestAc.wx, "com.tencent.mm.plugin.search.ui.FTSMainUI", "微信搜索"));
            wxCard.add(new CardInfo(SuggestAc.wx, "com.tencent.mm.plugin.offline.ui.WalletOfflineCoinPurseUI", "付款码"));
            wxCard.add(new CardInfo(SuggestAc.wx, "com.tencent.mm.plugin.scanner.ui.BaseScanUI", "扫一扫"));
            cardInfos.addAll(wxCard);
        } else if (SuggestAc.zfb.equals(pkName)) {
            ArrayList<CardInfo> zfbCard = new ArrayList<>();
//            com.eg.android.AlipayGphone,com.alipay.android.phone.wealth.tally.activitys.TallyMainActivity_,记账本
//            com.eg.android.AlipayGphone,com.alipay.mobile.stock.ui.MainActivity,股票
//            com.eg.android.AlipayGphone,com.alipay.mobile.pubsvc.ui.PPServiceActivity,生活号
//            com.eg.android.AlipayGphone,com.alipay.mobile.transferapp.ui.TransferHomeActivity_,转账
//            com.eg.android.AlipayGphone,com.alipay.mobile.alipassapp.ui.list.activity.v2.OffersEntryActivity,卡包
//            com.eg.android.AlipayGphone,com.alipay.mobile.bill.list.ui.BillListActivity_,账单
//            com.eg.android.AlipayGphone,com.alipay.mobile.payee.ui.PayeeQRActivity_,收钱
//            com.eg.android.AlipayGphone,com.alipay.mobile.security.accountmanager.AccountInfo.ui.SecurityAccountQrCodeActivity_,我的二维码
//            com.eg.android.AlipayGphone,com.alipay.mobile.contactsapp.ui.AddFriendActivity_,添加朋友
//            com.eg.android.AlipayGphone,com.alipay.mobile.socialcontactsdk.contact.ui.ContactMainPageActivity,通讯录
//            com.eg.android.AlipayGphone,com.alipay.android.phone.businesscommon.globalsearch.ui.MainSearchActivity,搜索
//            com.eg.android.AlipayGphone,com.alipay.android.phone.voiceassistant.ui.VoiceAssistantActivity,语音搜索
//            com.eg.android.AlipayGphone,com.alipay.android.phone.personalapp.favorite.activity.FavoriteNewHomeActivity,我的收藏
//            com.eg.android.AlipayGphone,com.alipay.mobile.bill.list.ui.BillSearchActivity_,账单搜索
//            com.eg.android.AlipayGphone,com.alipay.mobile.fund.ui.FundMainNewActivity_,余额宝
//            com.eg.android.AlipayGphone,com.alipay.mobile.onsitepay9.payer.OspTabHostActivity,付款码
//            com.eg.android.AlipayGphone,com.alipay.mobile.scan.as.main.MainCaptureActivity,扫一扫
            zfbCard.add(new CardInfo(SuggestAc.zfb, "com.alipay.android.phone.wealth.tally.activitys.TallyMainActivity_", "记账本"));
            zfbCard.add(new CardInfo(SuggestAc.zfb, "com.alipay.mobile.stock.ui.MainActivity", "股票"));
            zfbCard.add(new CardInfo(SuggestAc.zfb, "com.alipay.mobile.pubsvc.ui.PPServiceActivity", "生活号"));
            zfbCard.add(new CardInfo(SuggestAc.zfb, "com.alipay.mobile.transferapp.ui.TransferHomeActivity_", "转账"));
            zfbCard.add(new CardInfo(SuggestAc.zfb, "com.alipay.mobile.alipassapp.ui.list.activity.v2.OffersEntryActivity", "卡包"));
            zfbCard.add(new CardInfo(SuggestAc.zfb, "com.alipay.mobile.bill.list.ui.BillListActivity_", "账单"));
            zfbCard.add(new CardInfo(SuggestAc.zfb, "com.alipay.mobile.payee.ui.PayeeQRActivity_", "收钱"));
            zfbCard.add(new CardInfo(SuggestAc.zfb, "com.alipay.mobile.security.accountmanager.AccountInfo.ui.SecurityAccountQrCodeActivity_", "我的二维码"));
            zfbCard.add(new CardInfo(SuggestAc.zfb, "com.alipay.mobile.contactsapp.ui.AddFriendActivity_", "添加朋友"));
            zfbCard.add(new CardInfo(SuggestAc.zfb, "com.alipay.mobile.socialcontactsdk.contact.ui.ContactMainPageActivity", "通讯录"));
            zfbCard.add(new CardInfo(SuggestAc.zfb, "com.alipay.android.phone.businesscommon.globalsearch.ui.MainSearchActivity", "搜索"));
            zfbCard.add(new CardInfo(SuggestAc.zfb, "com.alipay.android.phone.voiceassistant.ui.VoiceAssistantActivity", "语音搜索"));
            zfbCard.add(new CardInfo(SuggestAc.zfb, "com.alipay.android.phone.personalapp.favorite.activity.FavoriteNewHomeActivity", "我的收藏"));
            zfbCard.add(new CardInfo(SuggestAc.zfb, "com.alipay.mobile.bill.list.ui.BillSearchActivity_", "账单搜索"));
            zfbCard.add(new CardInfo(SuggestAc.zfb, "com.alipay.mobile.fund.ui.FundMainNewActivity_", "余额宝"));
            zfbCard.add(new CardInfo(SuggestAc.zfb, "com.alipay.mobile.onsitepay9.payer.OspTabHostActivity", "付款码"));
            zfbCard.add(new CardInfo(SuggestAc.zfb, "com.alipay.mobile.scan.as.main.MainCaptureActivity", "扫一扫"));
            cardInfos.addAll(zfbCard);
        }

        if (Xutils.listNotNull(cardInfos) && fragment != null && fragment.cardAdapter != null) {
            fragment.cardInfos = new ArrayList();
            fragment.cardInfos.addAll(cardInfos);
            fragment.cardAdapter.setItems(fragment.cardInfos);
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        fragment = new MyFt();
        Bundle bundle = new Bundle();
        bundle.putString("packageName", EnumInfo.homeTab.SUGGEST_ACTIVITY.getValue());
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
        if (getIntent().getExtras() != null) {
            pkName = getIntent().getExtras().getString("pkName");
            String name = getIntent().getExtras().getString("name");
            if (Xutils.isNotEmptyOrNull(name)) {
                toolbar.setTitle("建议活动 " + name);
            }
        }
        if (Xutils.isEmptyOrNull(pkName)) {
            return;
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        allInit();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.ac_suggestinfo;
    }

    public boolean onCreateOptionsMenu(Menu paramMenu) {
        paramMenu.add("back").setIcon(Xutils.initIconWhite(Iconify.IconValue.md_arrow_back)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(paramMenu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item == null || item.getTitle() == null) {
            return false;
        }

        if (item.getTitle().equals("back")) {
            finish();

        }

        return true;
    }


}
