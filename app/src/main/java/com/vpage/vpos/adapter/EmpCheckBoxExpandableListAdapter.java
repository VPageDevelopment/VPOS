package com.vpage.vpos.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.vpage.vpos.R;
import com.vpage.vpos.pojos.employee.EmployeeData;


public class EmpCheckBoxExpandableListAdapter extends BaseExpandableListAdapter implements ExpandableListView.OnGroupClickListener,ExpandableListView.OnChildClickListener {


    private static final String TAG = EmpCheckBoxExpandableListAdapter.class.getName();

    Activity activity;

    // Sample data set.  children[i] contains the children (String[]) for groups[i].
    private EmployeeData[] groups = { new EmployeeData("People Names",false), new EmployeeData("Dog Names",false), new EmployeeData("Cat Names",false), new EmployeeData("Fish Names",false) };
    private EmployeeData[][] children = {
            { new EmployeeData("Arnold",false), new EmployeeData("Barry",false), new EmployeeData("Chuck",false), new EmployeeData("David",false) },
            { new EmployeeData("Ace",false), new EmployeeData("Bandit",false), new EmployeeData("Cha-Cha",false), new EmployeeData("Deuce",false) },
            { new EmployeeData("Fluffy",false), new EmployeeData("Snuggles",false) },
            { new EmployeeData("Goldy",false), new EmployeeData("Bubbles",false) }
    };

    public EmpCheckBoxExpandableListAdapter(Activity activity) {
        this.activity = activity;
    }

    public Object getChild(int groupPosition, int childPosition) {
        return children[groupPosition][childPosition];
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public int getChildrenCount(int groupPosition) {
        Log.e(TAG,"getChildrenCount() ... groupPosition "+groupPosition+" children count: "+children[groupPosition].length);
        return children[groupPosition].length;
    }

    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout mParentLayout = (LinearLayout) mInflater.inflate(R.layout.group_child, null);
        TextView textView = (TextView)mParentLayout.findViewById(R.id.view);


        final CheckBox lCheckBox = (CheckBox) mParentLayout.findViewById(R.id.checkBox);

        lCheckBox.setTag(R.string.groupPos,groupPosition);
        lCheckBox.setTag(R.string.childPos,childPosition);

        EmployeeData lGroupData = groups[groupPosition];
        lCheckBox.setChecked(lGroupData.isChecked);


        lCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e(TAG,"onCheckedChanged isChecked : "+isChecked);
                int aGroupPos = (Integer) buttonView.getTag(R.string.groupPos);
                int aChildPos = (Integer) buttonView.getTag(R.string.childPos);

                EmployeeData lChildData = children[aGroupPos][aChildPos];
                lChildData.isChecked = isChecked;

                EmployeeData lChildrens[] = children[aGroupPos];
                int lCheckedCount = 0;
                int lUncheckedCount = 0;

                for(EmployeeData data :lChildrens){
                    if(data.isChecked){
                        lCheckedCount++;
                    }else{
                        lUncheckedCount++;
                    }
                }
                EmployeeData lGroup = groups[aGroupPos];
                if(lCheckedCount == lChildrens.length){
                    lGroup.isChecked = true;
                }
                if(lUncheckedCount == lChildrens.length){
                    lGroup.isChecked = false;
                }
            }
        });


        textView.setText(getChild(groupPosition, childPosition).toString());
        return mParentLayout;
    }

    public Object getGroup(int groupPosition) {
        return groups[groupPosition];
    }

    public int getGroupCount() {
        Log.e(TAG,"getGroupCount() ... group count: "+groups.length);

        return groups.length;
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                             ViewGroup parent) {
//            TextView textView = getGenericView();
        LayoutInflater mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout mParentLayout = (LinearLayout) mInflater.inflate(R.layout.group_parent, null);
        TextView textView = (TextView)mParentLayout.findViewById(R.id.view);
        textView.setText(getGroup(groupPosition).toString());

        final CheckBox lCheckBox = (CheckBox) mParentLayout.findViewById(R.id.checkBox);
        lCheckBox.setTag(groupPosition);

        EmployeeData lGroupData = groups[groupPosition];
        lCheckBox.setChecked(lGroupData.isChecked);


        lCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e(TAG,"onCheckedChanged isChecked : "+isChecked);
                int aPos = (Integer) buttonView.getTag();
                EmployeeData lGroup = groups[aPos];
                lGroup.isChecked = isChecked;
                EmployeeData[] lChildData = children[aPos];
                for(EmployeeData lData : lChildData){
                    lData.isChecked = isChecked;
                }
            }
        });
        return mParentLayout;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v,
                                int groupPosition, long id) {

        Log.e(TAG,"onGroupClick groupPosition : "+groupPosition);


        return false;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v,
                                int groupPosition, int childPosition, long id) {
        Log.e(TAG,"onChildClick groupPosition : "+groupPosition+" childPosition: "+childPosition);
        return false;
    }
}
