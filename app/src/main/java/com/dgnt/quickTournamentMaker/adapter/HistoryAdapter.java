package com.dgnt.quickTournamentMaker.adapter;

import android.content.Context;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dgnt.quickTournamentMaker.R;
import com.dgnt.quickTournamentMaker.model.history.HistoricalTournament;
import com.dgnt.quickTournamentMaker.util.PreferenceUtil;
import com.dgnt.quickTournamentMaker.util.TournamentUtil;

import java.util.List;

/**
 * Created by Owner on 1/31/2016.
 */
public class HistoryAdapter extends ArrayAdapter<HistoricalTournament> {


    private String searchTerm = null;

    public HistoryAdapter(Context context, int resource, List<HistoricalTournament> items) {
        super(context, resource, items);

    }

    public void setSearchTerm(final String searchTerm) {
        this.searchTerm = searchTerm;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            final LayoutInflater vi = LayoutInflater.from(getContext());
            convertView = vi.inflate(R.layout.history_item, null);
        }

        final HistoricalTournament.View viewMode = PreferenceUtil.getHistoricalTournamentViewMode(PreferenceManager.getDefaultSharedPreferences(getContext()));

        final TextView historicalTournamentName_tv = (TextView) convertView.findViewById(R.id.historicalTournamentName_tv);
        final String title = getItem(position).getName();
        historicalTournamentName_tv.setText(title);
        searchTermHighlighting(historicalTournamentName_tv, title, searchTerm);

        final SparseBooleanArray sparseBooleanArray = ((ListView) parent).getCheckedItemPositions();
        final boolean isChecked = sparseBooleanArray.get(position);

        final ImageView checked_iv = (ImageView) convertView.findViewById(R.id.checked_iv);
        checked_iv.setVisibility(isChecked ? View.VISIBLE : View.GONE);

        convertView.setBackgroundColor(ContextCompat.getColor(getContext(), (isChecked ? R.color.checkedItemBg : R.color.uncheckedItemBg)));

        final TextView historicalTournamentDate_tv = (TextView) convertView.findViewById(R.id.historicalTournamentCreationDate_tv);
        historicalTournamentDate_tv.setText(getItem(position).getCreationTimeAsDate());

        final TextView historicalTournamentLastModifiedDate_tv = (TextView) convertView.findViewById(R.id.historicalTournamentLastModifiedDate_tv);
        historicalTournamentLastModifiedDate_tv.setText(getItem(position).getLastModifiedTimeAsDate());

        final ViewGroup historicalTournamentDate_group = (ViewGroup) convertView.findViewById(R.id.historicalTournamentDate_group);
        historicalTournamentDate_group.setVisibility(viewMode == HistoricalTournament.View.DETAILED ? View.VISIBLE : View.GONE);

        final TextView historicalTournamentDescription_tv = (TextView) convertView.findViewById(R.id.historicalTournamentDescription_tv);
        final String description = getItem(position).getNote();
        historicalTournamentDescription_tv.setText(description);
        searchTermHighlighting(historicalTournamentDescription_tv, description, searchTerm);
        historicalTournamentDescription_tv.setVisibility(description.length() == 0 || viewMode == HistoricalTournament.View.MINIMAL ? View.GONE : View.VISIBLE);


        final ImageView historicalTournamentType_iv = (ImageView) convertView.findViewById(R.id.historicalTournamentType_iv);
        historicalTournamentType_iv.setImageResource(TournamentUtil.getProperTournamentDrawable(getItem(position).getType(),false));

        return convertView;
    }

    private void searchTermHighlighting(final TextView tv, final String origText, final String searchTerm) {
        final Spannable title_spannable = new SpannableString(origText);
        if (searchTerm != null) {
            int ofe = origText.indexOf(searchTerm, 0);
            for (int ofs = 0; ofs < origText.length() && ofe != -1; ofs = ofe + 1) {
                ofe = origText.indexOf(searchTerm, ofs);
                if (ofe == -1) {
                    break;
                }
                else {

                    title_spannable.setSpan(new BackgroundColorSpan(Color.YELLOW), ofe, ofe + searchTerm.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv.setText(title_spannable, TextView.BufferType.SPANNABLE);
                }
            }
        }
    }
}
