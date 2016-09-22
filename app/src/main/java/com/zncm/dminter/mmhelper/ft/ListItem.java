package com.zncm.dminter.mmhelper.ft;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zncm.dminter.mmhelper.R;
import com.zncm.dminter.mmhelper.data.CardInfo;
import com.zncm.dminter.mmhelper.utils.Xutils;

import cn.nekocode.itempool.Item;
import cn.nekocode.itempool.ItemEventHandler;

public class ListItem extends Item<CardInfo> {
    TextView text1;
    TextView text2;
    TextView text3;
    TextView text4;

    @NonNull
    @Override
    public View onCreateItemView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.list_item, parent, false);
        text1 = (TextView) itemView.findViewById(R.id.text1);
        text2 = (TextView) itemView.findViewById(R.id.text2);
        text3 = (TextView) itemView.findViewById(R.id.text3);
        text4 = (TextView) itemView.findViewById(R.id.text4);
        return itemView;
    }

    @Override
    public void onBindItem(@NonNull final RecyclerView.ViewHolder holder, @NonNull CardInfo info, ItemEventHandler eventHandler) {
        if (Xutils.isNotEmptyOrNull(info.getTitle())) {
            text1.setVisibility(View.VISIBLE);
            text1.setText(info.getTitle());
        } else {
            text1.setVisibility(View.GONE);
        }


    }
}