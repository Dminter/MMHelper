package com.zncm.dminter.mmhelper.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CustomizableArrayAdapter<T> extends BaseAdapter implements Filterable {
    private List<T> data;
    private List<T> filteredData;

    private int resource = 0;
    private int dropDownResource = 0;
    private LayoutInflater inflater;

    private SimpleFilter filter;

    public CustomizableArrayAdapter(Context context, List<T> data) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = this.filteredData = data;
        this.resource = dropDownResource = resource;
    }

    public void resetData(List<T> data) {
        this.data = this.filteredData = data;
        notifyDataSetChanged();
    }

    public void setViewResource(int resource) {
        this.resource = resource;
    }

    public void setDropDownViewResource(int resource) {
        this.dropDownResource = resource;
    }

    @Override
    public int getCount() {
        return filteredData.size();
    }

    @Override
    public T getItem(int position) {
        return filteredData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createView(position, convertView, parent, false);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createView(position, convertView, parent, true);
    }

    protected int getResourceId(boolean dropDown) {
        return dropDown ? dropDownResource : resource;
    }

    protected View createView(int position, View convertView,
                              ViewGroup parent, boolean dropDown) {
        int viewResId = getResourceId(dropDown);

        View v;
        if (convertView == null) {
            v = inflater.inflate(viewResId, parent, false);
        } else {
            v = convertView;
        }

        prepareItemView(position, getItem(position), v);

        return v;
    }

    protected void prepareItemView(int position, T item, View v) {
        if (v instanceof TextView) {
            TextView textView = (TextView) v;
            textView.setText(getItemString(position, item));
        } else {
            throw new IllegalStateException("CustomizableArrayAdapter should be overriden.");
        }
    }

    protected String getItemString(int position, T item) {
        return item.toString();
    }

    protected boolean filterItem(int position, T item, String prefix) {
        if (item == null) {
            return false;
        }

        String itemString = getItemString(position, item);
        String[] words = itemString.split(" ");
        int wordCount = words.length;

        for (int k = 0; k < wordCount; k++) {
            String word = words[k];

            if (word.toLowerCase().startsWith(prefix)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new SimpleFilter();
        }
        return filter;
    }

    protected class SimpleFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            List<T> orig = data;
            List<T> items;

            if (prefix == null || prefix.length() == 0) {
                items = orig;
            } else {
                items = new ArrayList<T>();

                String prefixString = prefix.toString().toLowerCase();
                int count = orig.size();
                for (int i = 0; i < count; i++) {
                    T item = orig.get(i);
                    if (filterItem(i, item, prefixString)) {
                        items.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = items;
            results.count = items.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            data = (List<T>) results.values;
            notifyDataSetChanged();
        }
    }
}
