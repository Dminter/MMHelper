package com.zncm.dminter.mmhelper.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zncm.dminter.mmhelper.R;
import com.zncm.dminter.mmhelper.data.CardInfo;

import java.util.List;

public abstract class CardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CardInfo> items;
    private Activity ctx;
    public CardAdapter(Activity ctx) {
        this.ctx = ctx;
    }

    public void setItems(List<CardInfo> items) {
        this.items = items;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder2, int position) {
        CardViewHolder holder = (CardViewHolder) holder2;
        setData(position, holder);
    }

    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        }
        return 0;
    }

    public class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private MxItemClickListener clickListener;
        public ImageView image;
        public TextView title;
        public LinearLayout llBg;
        private View view;

        public CardViewHolder(View convertView) {
            super(convertView);
            view = convertView;
            llBg = (LinearLayout) convertView
                    .findViewById(R.id.llBg);
            image = (ImageView) convertView
                    .findViewById(R.id.image);
            title = (TextView) convertView
                    .findViewById(R.id.title);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        public void setClickListener(MxItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(view, getPosition(), false);
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onClick(view, getPosition(), true);
            return true;
        }
    }

    public abstract void setData(int position, CardViewHolder holder);

}