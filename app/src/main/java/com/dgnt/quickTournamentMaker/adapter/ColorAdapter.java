package com.dgnt.quickTournamentMaker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.dgnt.quickTournamentMaker.R;


import java.util.Map;

/**
 * Created by Owner on 1/31/2016.
 */
public class ColorAdapter extends ArrayAdapter<String> {

    private Map<String, Integer> colorMap;

    public ColorAdapter(Context context, int resource, String[] items, final Map<String, Integer> colorMap) {
        super(context, resource,items);
        this.colorMap = colorMap;

    }
    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            final LayoutInflater vi = LayoutInflater.from(getContext());
            convertView = vi.inflate(R.layout.color_item, null);
        }

        final TextView colorName_tv = (TextView) convertView.findViewById(R.id.colorName_tv);
        final String colorName = getItem(position);
        colorName_tv.setText(colorName);
        final Integer color = colorMap.get(colorName);
        if (color != null) {
            colorName_tv.setTextColor(color);
        }
        return convertView;
    }
}
