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
import com.vpage.vpos.pojos.employee.EmployeePermissionData;
import java.util.ArrayList;
import java.util.List;

public class EmpCheckBoxExpandableListAdapter extends BaseExpandableListAdapter implements ExpandableListView.OnGroupClickListener,ExpandableListView.OnChildClickListener {


    private static final String TAG = EmpCheckBoxExpandableListAdapter.class.getName();

    Activity activity;

    List<String> permissionParentList = new ArrayList<>();
    List<String> permissionChildList = new ArrayList<>();

    // Sample data set.  children[i] contains the children (String[]) for groups[i].
  /*  private EmployeePermissionData[] groups = { new EmployeePermissionData("key",false), new EmployeePermissionData("Dog Names",false), new EmployeePermissionData("Cat Names",false), new EmployeePermissionData("Fish Names",false) };
    private EmployeePermissionData[][] children = {
            { new EmployeePermissionData("Arnold",false), new EmployeePermissionData("Barry",false), new EmployeePermissionData("Chuck",false), new EmployeePermissionData("David",false) },
            { new EmployeePermissionData("Ace",false), new EmployeePermissionData("Bandit",false), new EmployeePermissionData("Cha-Cha",false), new EmployeePermissionData("Deuce",false) },
            { new EmployeePermissionData("Fluffy",false), new EmployeePermissionData("Snuggles",false) },
            { new EmployeePermissionData("Goldy",false), new EmployeePermissionData("Bubbles",false) }
    };*/

    private EmployeePermissionData[] groups;

    private EmployeePermissionData[][] children;

    String[] employeePermissionParentName,employeePermissionChildName;

    public EmpCheckBoxExpandableListAdapter(Activity activity, List<String> permissionParentList,List<String> permissionChildList) {
        this.activity = activity;
        this.permissionParentList = permissionParentList;
        this.permissionChildList = permissionChildList;
        try {

            employeePermissionParentName = activity.getResources().getStringArray(R.array.employeePermissionParentName);
            groups = new EmployeePermissionData[permissionParentList.size()];
            employeePermissionChildName = activity.getResources().getStringArray(R.array.employeePermissionChildName);
            children = new EmployeePermissionData[permissionParentList.size()][permissionChildList.size()];

            for(int i=0;i<permissionParentList.size();i++){

                if(permissionParentList.get(i).equals("Y")){
                    groups[i] = new EmployeePermissionData(employeePermissionParentName[i],true);
                }else {
                    groups[i] = new EmployeePermissionData(employeePermissionParentName[i],false);
                }
                if(i == 4){

                    for(int j=0;j<permissionChildList.size();j++) {

                        if (permissionChildList.get(j).equals("Y")) {
                            children[i][j] = new EmployeePermissionData(employeePermissionChildName[j], true);
                        } else {
                            children[i][j] = new EmployeePermissionData(employeePermissionChildName[j], false);
                        }
                    }

                }
            }

        }catch (ArrayIndexOutOfBoundsException e){

            Log.e(TAG,e.getMessage());
        }


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

        if(groupPosition == 5){

            lCheckBox.setTag(R.string.groupPos,groupPosition);
            lCheckBox.setTag(R.string.childPos,childPosition);

            EmployeePermissionData lGroupData = groups[groupPosition];
            lCheckBox.setChecked(lGroupData.isChecked);


            lCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.e(TAG,"onCheckedChanged isChecked : "+isChecked);
                    int aGroupPos = (Integer) buttonView.getTag(R.string.groupPos);
                    int aChildPos = (Integer) buttonView.getTag(R.string.childPos);

                    EmployeePermissionData lChildData = children[aGroupPos][aChildPos];
                    lChildData.isChecked = isChecked;

                    EmployeePermissionData lChildrens[] = children[aGroupPos];
                    int lCheckedCount = 0;
                    int lUncheckedCount = 0;

                    for(EmployeePermissionData data :lChildrens){
                        if(data.isChecked){
                            lCheckedCount++;
                        }else{
                            lUncheckedCount++;
                        }
                    }
                    EmployeePermissionData lGroup = groups[aGroupPos];
                    if(lCheckedCount == lChildrens.length){
                        lGroup.isChecked = true;
                    }
                    if(lUncheckedCount == lChildrens.length){
                        lGroup.isChecked = false;
                    }
                }
            });


            textView.setText(children[groupPosition][childPosition].toString());
        }


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
        textView.setText(groups[groupPosition].toString());

        final CheckBox lCheckBox = (CheckBox) mParentLayout.findViewById(R.id.checkBox);
        lCheckBox.setTag(groupPosition);

        EmployeePermissionData lGroupData = groups[groupPosition];
        lCheckBox.setChecked(lGroupData.isChecked);


        lCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e(TAG,"onCheckedChanged isChecked : "+isChecked);
                int aPos = (Integer) buttonView.getTag();
                EmployeePermissionData lGroup = groups[aPos];
                lGroup.isChecked = isChecked;
                if(null !=children[aPos]){

                    EmployeePermissionData[] lChildData = children[aPos];
                    for(EmployeePermissionData lData : lChildData){
                        lData.isChecked = isChecked;
                    }
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
