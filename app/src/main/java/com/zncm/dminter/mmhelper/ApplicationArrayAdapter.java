package com.zncm.dminter.mmhelper;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zncm.dminter.mmhelper.utils.CustomizableArrayAdapter;
import com.zncm.dminter.mmhelper.utils.ResourceUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class ApplicationArrayAdapter
        extends CustomizableArrayAdapter<PackageInfo> {
    PackageManager packageManager;

    public ApplicationArrayAdapter(Context context, List<PackageInfo> list) {
        super(context, new ArrayList());
        this.packageManager = context.getPackageManager();
        resetData(prepareData(list));
        setViewResource(R.layout.app_row_dropdown);
        setDropDownViewResource(R.layout.app_row);
    }

    private List<PackageInfo> prepareData(List<PackageInfo> objects) {
        Collections.sort(objects, new Comparator<PackageInfo>() {
            @Override
            public int compare(PackageInfo lhs, PackageInfo rhs) {
                CharSequence ll = getLabel(lhs), rl = getLabel(rhs);
                if (ll != null && rl != null) {
                    return ll.toString().compareTo(rl.toString());
                } else {
                    return lhs.packageName.compareTo(rhs.packageName);
                }
            }
        });
        return objects;
    }

    public CharSequence getLabel(PackageInfo paramPackageInfo) {
        return this.packageManager.getApplicationLabel(paramPackageInfo.applicationInfo);
    }

    public void prepareItemView(int position, PackageInfo item, View view) {
        TextView labelView = (TextView) view.findViewById(R.id.app_label);
        ImageView iconView = (ImageView) view.findViewById(R.id.app_icon);
        TextView descriptionView = (TextView) view.findViewById(R.id.app_desc);

        CharSequence label = getLabel(item);
        CharSequence description = ResourceUtils.getPackageString(packageManager, item.packageName,
                item.applicationInfo.descriptionRes, item.applicationInfo);
        CharSequence packageName = item.packageName;
        Drawable icon = packageManager.getApplicationIcon(item.applicationInfo);

        String info = packageName + (description != null ? " " + description : "");

        labelView.setText(label);
        descriptionView.setText(info);
        iconView.setImageDrawable(icon);
    }
}
