package com.dgnt.quickTournamentMaker.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dgnt.quickTournamentMaker.R;
import com.dgnt.quickTournamentMaker.adapter.PersonAdapter;
import com.dgnt.quickTournamentMaker.model.management.Group;
import com.dgnt.quickTournamentMaker.model.management.Person;
import com.dgnt.quickTournamentMaker.util.DatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ManagementActivity extends AppCompatActivity implements ExpandableListView.OnGroupClickListener, ExpandableListView.OnChildClickListener {

    final static int REQUEST_CODE = 12345;

    private DatabaseHelper dh = new DatabaseHelper(this);
    private List<Group> groupList = new ArrayList<>();
    private Map<String, List<Person>> groupMap = new HashMap<>();
    private PersonAdapter personAdapter;
    private ArrayAdapter<String> groupAdapter;

    private ActionMode mActionMode;
    int expandableListSelectionType = ExpandableListView.PACKED_POSITION_TYPE_NULL;
    private TextView resultInformation_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ExpandableListView person_elv = (ExpandableListView) findViewById(R.id.person_elv);

        groupList = dh.getAllGroupsWithPersons();
        for (final Group group : groupList) {
            final List<Person> personList = new ArrayList<>();
            for (int i = 0; i < group.getTotalPersons(); i++) {
                personList.add(group.getPersonAt(i));
            }
            groupMap.put(group.getKey(), personList);
        }

        personAdapter = new PersonAdapter(this, groupList, groupMap);
        setUpGroupAdapter(groupList);

        person_elv.setAdapter(personAdapter);
        for (int i = 0; i < groupList.size(); i++)
            person_elv.expandGroup(i);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPersonFields(null, null);
            }
        });

        person_elv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        person_elv.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                  long id, boolean checked) {
                int count = person_elv.getCheckedItemCount();
                if (count == 1) {
                    expandableListSelectionType = ExpandableListView.getPackedPositionType(person_elv.getExpandableListPosition(position));
                }
                mode.setTitle(String.valueOf(count));
                configureMenu(mode.getMenu(), count);
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {

                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.contextual_actions_management, menu);
                mode.setTitle(String.valueOf(person_elv.getCheckedItemCount()));
                mActionMode = mode;
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                configureMenu(menu, person_elv.getCheckedItemCount());
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                int itemId = item.getItemId();
                SparseBooleanArray checkedItemPositions = person_elv.getCheckedItemPositions();
                final Set<Group> groupsSelected = new HashSet<>();
                final Set<Person> personsSelected = new HashSet<>();
                if (checkedItemPositions != null) {
                    int checkedItemCount = checkedItemPositions.size();
                    for (int i = 0; i < checkedItemCount; i++) {
                        if (checkedItemPositions.valueAt(i)) {
                            int position = checkedItemPositions.keyAt(i);
                            ContextMenu.ContextMenuInfo info;
                            long pos = person_elv.getExpandableListPosition(position);
                            int groupPos = ExpandableListView.getPackedPositionGroup(pos);
                            if (ExpandableListView.getPackedPositionType(pos) == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                                groupsSelected.add(groupList.get(groupPos));
                            } else {
                                int childPos = ExpandableListView.getPackedPositionChild(pos);
                                groupsSelected.add(groupList.get(groupPos));
                                personsSelected.add(groupList.get(groupPos).getPersonAt(childPos));
                            }
                        }
                    }
                }
                switch (itemId) {
                    case R.id.action_persons_delete:
                        deletePersons(personsSelected, groupsSelected);
                        break;
                    case R.id.action_groups_delete:
                        deleteGroups(groupsSelected);
                        break;
                    case R.id.action_person_edit:
                        if (personsSelected.size() > 0 && groupsSelected.size() > 0)
                            openPersonFields(personsSelected.iterator().next(), groupsSelected.iterator().next());
                        break;
                    case R.id.action_group_edit:
                        if (groupsSelected.size() > 0)
                            openGroupFields(groupsSelected.iterator().next());
                        break;
                    case R.id.action_persons_move:
                        movePersons(personsSelected, groupsSelected);
                        break;
                }
                mode.finish();
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                mActionMode = null;
            }
        });
        person_elv.setOnGroupClickListener(this);
        person_elv.setOnChildClickListener(this);

        resultInformation_tv = (TextView) findViewById(R.id.resultInformation_tv);
        resultInformation_tv.setText(groupList.size() == 0 ? getString(R.string.nobodyMsg) : getString(R.string.historicalItemHintMsg));

    }

    protected void configureMenu(Menu menu, int count) {
        boolean inGroup = expandableListSelectionType == ExpandableListView.PACKED_POSITION_TYPE_GROUP;
        menu.findItem(R.id.action_groups_delete).setVisible(inGroup);
        menu.findItem(R.id.action_group_edit).setVisible(inGroup && count == 1);
        menu.findItem(R.id.action_persons_delete).setVisible(!inGroup);
        menu.findItem(R.id.action_person_edit).setVisible(!inGroup && count == 1);
        menu.findItem(R.id.action_persons_move).setVisible(!inGroup);
    }

    private void setUpGroupAdapter(final List<Group> groupList) {
        final List<String> groupStringList = new ArrayList<>();
        for (final Group group : groupList) {
            groupStringList.add(group.getName());
        }

        groupAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, groupStringList);
    }

    private void openGroupFields(final Group group) {
        final ViewGroup layout_group_editor = (ViewGroup) getLayoutInflater().inflate(R.layout.layout_group_editor, null);

        final EditText groupName_et = (EditText) layout_group_editor.findViewById(R.id.groupName_et);
        groupName_et.setText(group.getName());

        final EditText groupNote_et = (EditText) layout_group_editor.findViewById(R.id.groupNote_et);
        groupNote_et.setText(group.getNote());

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(layout_group_editor);
        builder.setTitle(getString(R.string.editing, group.getName()));
        builder.setPositiveButton(getString(R.string.edit), null);
        builder.setNegativeButton(getString(android.R.string.cancel), null);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface dialog) {


                final View.OnClickListener clickListener = new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        final String groupNameToSave = groupName_et.getText().toString().trim().length() > 0 ? groupName_et.getText().toString() : getString(R.string.defaultGroupName);
                        final String noteToSave = groupNote_et.getText().toString();

                        boolean successful = true;
                        try {
                            dh.updateGroup(group.getName(), groupNameToSave, noteToSave, false);
                        } catch (SQLiteConstraintException e) {
                            Toast.makeText(getApplicationContext(), getString(R.string.duplicateMsg, groupNameToSave), Toast.LENGTH_LONG).show();

                            successful = false;
                        }

                        if (successful) {
                            updateUI();
                            dialog.cancel();

                            Toast.makeText(getApplicationContext(), getString(R.string.editSuccessfulMsg, groupNameToSave), Toast.LENGTH_LONG).show();
                        }
                    }
                };

                final Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setTag(true);
                positiveButton.setOnClickListener(clickListener);

            }
        });
        alertDialog.show();
    }

    private void openPersonFields(final Person person, final Group group) {
        final String name = person != null ? person.getName() : "";
        final String note = person != null ? person.getNote() : "";
        final boolean editing = person != null;

        final String groupName = group == null ? "" : group.getName();

        final ViewGroup layout_person_editor = (ViewGroup) getLayoutInflater().inflate(R.layout.layout_person_editor, null);

        final EditText personName_et = (EditText) layout_person_editor.findViewById(R.id.personName_et);
        personName_et.setText(name);

        final EditText personNote_et = (EditText) layout_person_editor.findViewById(R.id.personNote_et);
        personNote_et.setText(note);

        final AutoCompleteTextView groupName_actv = (AutoCompleteTextView) layout_person_editor.findViewById(R.id.groupName_actv);
        groupName_actv.setText(groupName);
        groupName_actv.setThreshold(2);
        groupName_actv.setAdapter(groupAdapter);


        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(layout_person_editor);
        builder.setTitle(editing ? getString(R.string.editing, name) : getString(R.string.adding));
        builder.setPositiveButton(editing ? getString(R.string.edit) : getString(R.string.add), null);
        if (!editing)
            builder.setNeutralButton(getString(R.string.addAndContinue), null);
        builder.setNegativeButton(getString(android.R.string.cancel), null);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface dialog) {


                final View.OnClickListener clickListener = new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        final String nameToSave = personName_et.getText().toString();
                        final String noteToSave = personNote_et.getText().toString();
                        final String groupNameToSave = groupName_actv.getText().toString().trim().length() > 0 ? groupName_actv.getText().toString() : getString(R.string.defaultGroupName);


                        boolean successful = true;

                        try {
                            if (editing)
                                dh.updatePersonAndAddGroup(name, nameToSave, noteToSave, groupName, groupNameToSave);
                            else
                                dh.addPersonAndGroup(nameToSave, noteToSave, groupNameToSave);
                        } catch (SQLiteConstraintException e) {
                            Toast.makeText(getApplicationContext(), getString(R.string.duplicateMsg, nameToSave), Toast.LENGTH_LONG).show();

                            successful = false;
                        }


                        if (successful) {
                            updateUI();
                            if ((Boolean) view.getTag()) {
                                dialog.cancel();
                            } else {
                                personName_et.setText("");
                            }
                            Toast.makeText(getApplicationContext(), editing ? getString(R.string.editSuccessfulMsg, nameToSave) : getString(R.string.addSuccessfulMsg, nameToSave), Toast.LENGTH_LONG).show();

                        }
                    }
                };

                final Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setTag(true);
                positiveButton.setOnClickListener(clickListener);

                if (!editing) {
                    final Button neutralButton = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
                    neutralButton.setTag(false);
                    neutralButton.setOnClickListener(clickListener);
                }
            }
        });
        alertDialog.show();

    }

    private void deleteGroups(final Set<Group> groupList) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.deleteGroupMsg, groupList.size()));
        builder.setTitle(getString(R.string.deletingGroups, groupList.size()));
        builder.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {

                dh.deleteGroups(groupList);

                Toast.makeText(getApplicationContext(), getString(R.string.deleteGroupSuccessfulMsg, groupList.size()), Toast.LENGTH_LONG).show();
                updateUI();

            }
        });
        builder.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                dialogInterface.cancel();
            }
        });
        builder.create().show();
    }

    private void deletePersons(final Set<Person> personList, final Set<Group> groupList) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.deletePlayerMsg, personList.size()));
        builder.setTitle(getString(R.string.deletingPlayers, personList.size()));
        builder.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {

                dh.deletePersonsAndGroups(personList, groupList);

                Toast.makeText(getApplicationContext(), getString(R.string.deletePlayerSuccessfulMsg, personList.size()), Toast.LENGTH_LONG).show();
                updateUI();

            }
        });
        builder.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                dialogInterface.cancel();
            }
        });
        builder.create().show();
    }

    private void movePersons(final Set<Person> personList, final Set<Group> groupList) {
        final ViewGroup layout_person_move_editor = (ViewGroup) getLayoutInflater().inflate(R.layout.layout_person_move_editor, null);

        final AutoCompleteTextView groupName_actv = (AutoCompleteTextView) layout_person_move_editor.findViewById(R.id.groupName_actv);
        groupName_actv.setThreshold(2);
        groupName_actv.setAdapter(groupAdapter);


        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.movingPlayers, personList.size()));
        builder.setView(layout_person_move_editor);
        builder.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {

                final String newGroup = groupName_actv.getText().toString().trim().length() > 0 ? groupName_actv.getText().toString() : getString(R.string.defaultGroupName);

                for (final Person person : personList) {
                    String oldGroup = "";
                    for (final Group group : groupList) {
                        for (int p = 0; p < group.getTotalPersons(); p++) {
                            if (person.equals(group.getPersonAt(p))) {
                                oldGroup = group.getKey();
                            }
                        }
                    }
                    dh.updatePersonAndAddGroup(person.getName(), person.getName(), person.getNote(), oldGroup, newGroup);
                }
                Toast.makeText(getApplicationContext(), getString(R.string.movePlayerSuccessfulMsg, personList.size()), Toast.LENGTH_LONG).show();
                updateUI();

            }
        });
        builder.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                dialogInterface.cancel();
            }
        });
        builder.create().show();
    }


    private void updateUI() {
        groupList.clear();
        groupList.addAll(dh.getAllGroupsWithPersons());

        groupMap.clear();
        for (final Group group : groupList) {
            final List<Person> personList = new ArrayList<>();
            for (int i = 0; i < group.getTotalPersons(); i++) {
                personList.add(group.getPersonAt(i));
            }
            groupMap.put(group.getKey(), personList);
        }

        personAdapter.notifyDataSetChanged();
        setUpGroupAdapter(groupList);

        resultInformation_tv.setText(groupList.size() == 0 ? getString(R.string.nobodyMsg) : getString(R.string.historicalItemHintMsg));
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        if (mActionMode != null) {
            if (expandableListSelectionType == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                int flatPosition = parent.getFlatListPosition(ExpandableListView.getPackedPositionForGroup(groupPosition));
                parent.setItemChecked(flatPosition, !parent.isItemChecked(flatPosition));

            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v,
                                int groupPosition, int childPosition, long id) {
        if (mActionMode != null) {
            if (expandableListSelectionType == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                int flatPosition = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition, childPosition));
                parent.setItemChecked(flatPosition, !parent.isItemChecked(flatPosition));
            }
        } else {
            final Group group = groupList.get(groupPosition);
            final Person person = groupMap.get(group.getKey()).get(childPosition);
            openPersonFields(person, group);

        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
