package com.dgnt.quickTournamentMaker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.dgnt.quickTournamentMaker.R;
import com.dgnt.quickTournamentMaker.model.tournament.Ranking;

import java.util.List;
import java.util.Map;

/**
 * Created by Owner on 5/28/2016.
 */
public class RankingAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> rankGroupList;
    private Map<String, List<Ranking.RankHolder>> rankMap;


    public RankingAdapter(Context context, List<String> rankGroupList,
                          Map<String, List<Ranking.RankHolder>> rankMap
    ) {
        this.context = context;
        this.rankGroupList = rankGroupList;
        this.rankMap = rankMap;
    }

    @Override
    public Ranking.RankHolder getChild(int groupPosition, int childPosition) {
        return rankMap.get(rankGroupList.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final Ranking.RankHolder rankHolder = getChild(groupPosition, childPosition);


        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.rank_list_item, null);
        }

        final TextView participantListItem_tv = (TextView) convertView.findViewById(R.id.listItem_tv);

        participantListItem_tv.setText((rankHolder.getRank() == Ranking.UNKNOWN_RANK ? "" : (rankHolder.getRank() + ". ")) + rankHolder.getParticipant().getDisplayName());

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.rankMap.get(this.rankGroupList.get(groupPosition)).size();
    }

    @Override
    public String getGroup(int groupPosition) {
        return rankGroupList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.rankGroupList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        final String rankGroup = getGroup(groupPosition);


        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.rank_group_list_item, null);
        }

        final TextView groupListItem_tv = (TextView) convertView.findViewById(R.id.expGroupListItem_tv);
        groupListItem_tv.setText(rankGroup);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
