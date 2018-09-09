package com.zncm.dminter.mmhelper;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zncm.dminter.mmhelper.data.MyAppInfo;
import com.zncm.dminter.mmhelper.utils.CustomizableArrayAdapter;
import com.zncm.dminter.mmhelper.utils.ResourceUtils;
import com.zncm.dminter.mmhelper.utils.Xutils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiaomx on 2017/4/12.
 */
public class ActivityArrayAdapter extends CustomizableArrayAdapter<MyAppInfo> {
    private PackageManager packageManager;

    public ActivityArrayAdapter(Context context, List<MyAppInfo> list) {
        super(context, new ArrayList<MyAppInfo>());

        packageManager = context.getPackageManager();
        resetData(list);
        setViewResource(R.layout.app_row_dropdown);
        setDropDownViewResource(R.layout.app_row);

    }


    public CharSequence getDescription(ActivityInfo activityInfo) {
        return ResourceUtils.getPackageString(this.packageManager, activityInfo.packageName, activityInfo.descriptionRes, activityInfo.applicationInfo);
    }

    public Drawable getIcon(ActivityInfo activityInfo) {
        return ResourceUtils.getPackageImage(this.packageManager, activityInfo.packageName, activityInfo.icon, activityInfo.applicationInfo);
    }

    public CharSequence getLabel(ActivityInfo activityInfo) {
        return ResourceUtils.getPackageString(this.packageManager, activityInfo.packageName, activityInfo.labelRes, activityInfo.applicationInfo);
    }

    public void prepareItemView(int position, MyAppInfo item, View view) {
        TextView name = (TextView) view.findViewById(R.id.app_label);
        ImageView icon = (ImageView) view.findViewById(R.id.app_icon);
        TextView desc = (TextView) view.findViewById(R.id.app_desc);
        String packageName = item.getPackageName();
        String className = item.getClassName();
        String appName = item.getAppName();
        String show = className;
        if ((Xutils.isNotEmptyOrNull(className)) && (Xutils.isNotEmptyOrNull(packageName)) && (className.contains(packageName))) {
            show = className.replace(packageName, "");
        }
        if (Xutils.isNotEmptyOrNull(appName)) {
            show = show + "|" + appName;
        }
        name.setText(show);
        desc.setVisibility(View.GONE);
        icon.setVisibility(View.GONE);
    }


}
