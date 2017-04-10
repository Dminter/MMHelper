package com.zncm.dminter.mmhelper.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import com.zncm.dminter.mmhelper.R;

import java.util.List;
import java.util.Map;

/**
 * Created by jiaomx on 2017/4/10.
 */


//底部弹出菜单
public abstract class BottomSheetDlg {


    public BottomSheetDlg(Activity activity, List<Map<String, Object>> list, boolean isSys) {

        final Dialog dialog = new Dialog(activity, R.style.BottomSheetDialog);
        View view = activity.getLayoutInflater().inflate(R.layout.bottom_gridview, null);
        GridView gridView = (GridView) view.findViewById(R.id.gridView);
        RelativeLayout popupWindow = (RelativeLayout) view.findViewById(R.id.popupWindow);
        popupWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        String from[] = {"text"};
        int to[] = {R.id.text};
        //4排以上，固定高度
        if (Xutils.listNotNull(list) && list.size() >= 20) {
            gridView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Xutils.dip2px(300)));

        }
        gridView.setAdapter(new SimpleAdapter(activity, list, R.layout.bottom_item, from, to));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onGridItemClickListener(position);
                dialog.dismiss();
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                onGridItemLongClickListener(position);
                dialog.dismiss();
                return true;
            }
        });

        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                onOutClickListener();
                dialog.dismiss();
            }
        });
        dialog.show();


    }

    public abstract void onGridItemClickListener(int position);

    public abstract void onGridItemLongClickListener(int position);

    public abstract void onOutClickListener();


}
