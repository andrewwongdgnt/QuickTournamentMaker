package com.dgnt.quickTournamentMaker.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dgnt.quickTournamentMaker.activity.R;
import com.dgnt.quickTournamentMaker.model.management.Group;
import com.dgnt.quickTournamentMaker.model.management.Person;

import java.util.List;
import java.util.Map;

/**
 * Created by Owner on 5/28/2016.
 */
public class PersonAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<Group> groupList;
    private Map<String, List<Person>> groupMap;


    public PersonAdapter(Context context, List<Group> groupList,
                         Map<String, List<Person>> groupMap
    ) {
        this.context = context;
        this.groupList = groupList;
        this.groupMap = groupMap;
    }

    @Override
    public Person getChild(int groupPosition, int childPosition) {
        return groupMap.get(groupList.get(groupPosition).getKey()).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final Person person = getChild(groupPosition, childPosition);

        final ExpandableListView elv = (ExpandableListView) parent;

        SparseBooleanArray checkedItemPositions = elv.getCheckedItemPositions();
        boolean checked = false;
        if (checkedItemPositions != null) {
            int checkedItemCount = checkedItemPositions.size();
            for (int i = 0; i < checkedItemCount; i++) {
                if (checkedItemPositions.valueAt(i)) {
                    int position = checkedItemPositions.keyAt(i);
                    long pos = elv.getExpandableListPosition(position);

                    int childPos = ExpandableListView.getPackedPositionChild(pos);
                    int groupPos = ExpandableListView.getPackedPositionGroup(pos);

                    if (childPos == childPosition && groupPos == groupPosition && ExpandableListView.getPackedPositionType(pos) != ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                        checked = true;
                        break;
                    }
                }
            }
        }
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.checkable_list_item, null);
        }

        final TextView personListItem_tv = (TextView) convertView.findViewById(R.id.listItem_tv);
        personListItem_tv.setText(person.getNote() == null || person.getNote().trim().length() == 0 ? person.getName() : "*" + person.getName());

        final ImageView checked_iv = (ImageView) convertView.findViewById(R.id.checked_iv);
        checked_iv.setVisibility(checked ? View.VISIBLE : View.INVISIBLE);

        convertView.setBackgroundColor(ContextCompat.getColor(context, (checked ? R.color.checkedItemBg : R.color.uncheckedItemBg)));

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.groupMap.get(this.groupList.get(groupPosition).getKey()).size();
    }

    @Override
    public Group getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.groupList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        final Group group = getGroup(groupPosition);

        final ExpandableListView elv = (ExpandableListView) parent;

        SparseBooleanArray checkedItemPositions = elv.getCheckedItemPositions();

        boolean checked = false;
        if (checkedItemPositions != null) {
            int checkedItemCount = checkedItemPositions.size();
            for (int i = 0; i < checkedItemCount; i++) {
                if (checkedItemPositions.valueAt(i)) {
                    int position = checkedItemPositions.keyAt(i);
                    long pos = elv.getExpandableListPosition(position);

                    int groupPos = ExpandableListView.getPackedPositionGroup(pos);

                    if (groupPos == groupPosition && ExpandableListView.getPackedPositionType(pos) == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                        checked = true;
                        break;
                    }
                }
            }
        }

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.checkable_group_list_item, null);
        }

        final TextView groupListItem_tv = (TextView) convertView.findViewById(R.id.expGroupListItem_tv);
        groupListItem_tv.setText(group.getNote() == null || group.getNote().trim().length() == 0 ? group.getName() : "*" + group.getName());

        convertView.setBackgroundColor(ContextCompat.getColor(context, (checked ? R.color.checkedItemBg : R.color.uncheckedItemBg)));

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
