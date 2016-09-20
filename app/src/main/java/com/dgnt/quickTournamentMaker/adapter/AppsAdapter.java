package com.dgnt.quickTournamentMaker.adapter;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.dgnt.quickTournamentMaker.R;

import java.util.List;

/**
 * Created by Andrew on 8/4/2015.
 */
public class AppsAdapter extends BaseAdapter {
    private Context context;
    private List<ResolveInfo> apps_arr;

    public AppsAdapter(Context context, List<ResolveInfo> apps_arr) {
        this.context = context;
        this.apps_arr = apps_arr;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        ResolveInfo info = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.app_list_item, parent, false);
        }
        // Lookup view for data population
        ImageView app_iv = (ImageView) convertView.findViewById(R.id.app_iv);
        TextView app_tv = (TextView) convertView.findViewById(R.id.app_tv);
        // Populate the data into the template view using the data object
        app_iv.setImageDrawable(info.loadIcon(context.getPackageManager()));
        app_tv.setText(info.loadLabel(context.getPackageManager()));
        // Return the completed view to render on screen
        return convertView;

    }


    public final int getCount() {
        return apps_arr.size();
    }

    public final ResolveInfo getItem(int position) {
        return apps_arr.get(position);
    }

    public final long getItemId(int position) {
        return position;
    }
}