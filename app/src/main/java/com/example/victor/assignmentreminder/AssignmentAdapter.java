package com.example.victor.assignmentreminder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.DialogFragment;

/**
 * Created by victor on 9/8/15.
 */
public class AssignmentAdapter extends BaseAdapter{


    private ArrayList<AssignmentRecord> alist = new ArrayList<AssignmentRecord>();

    private final Context aContext;
    private final Activity aActivity;

    public AssignmentAdapter (Context context, Activity activity){
        aContext = context;
        aActivity = activity;
    }


    public void addItem (AssignmentRecord item) {
        alist.add(item);
        notifyDataSetChanged();
    }

    public void deleteItem (AssignmentRecord item) {
        alist.remove(item);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return alist.size();
    }

    @Override
    public Object getItem(int position) {
        return alist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //FragmentManager fragManager = activity

        //get current item
        final AssignmentRecord assignmentRecord = alist.get(position);

        RelativeLayout itemlayoutview = null;

        if (convertView == null){
            convertView = ((LayoutInflater)aContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.assignment_item, null);
        }
        itemlayoutview = (RelativeLayout)convertView;

        final TextView assignmentTitle = (TextView)convertView.findViewById(R.id.adapter_item_title);
        assignmentTitle.setText(assignmentRecord.getItemName());

        final TextView dueDateText = (TextView)convertView.findViewById(R.id.adapter_dueDate);
        dueDateText.setText(AssignmentRecord.standardDateformat.format(assignmentRecord.getDueDate()));

        final CheckBox statusBox = (CheckBox)convertView.findViewById(R.id.adapter_finish_checkbox);
        if (assignmentRecord.getCurrentStatus()== AssignmentRecord.Status.NOTFINISHED){
            statusBox.setChecked(false);
        } else {
            statusBox.setChecked(true);
        }

        statusBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            class removeCheckedFragment extends DialogFragment{
                @Override
                public Dialog onCreateDialog(Bundle savedInstanceState) {
                    // Use the Builder class for convenient dialog construction
                    String alertMessage = "Do you want to remove this item?";
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(alertMessage)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    deleteItem(assignmentRecord);
                                    assignmentRecord.cancelNotification();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog
                                }
                            });
                    // Create the AlertDialog object and return it
                    return builder.create();
                }
            }

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){

                    //prompt to delete record;
                    assignmentRecord.setStatus(AssignmentRecord.Status.FINISHED);

                    //create dialog
                    FragmentManager fragManager = aActivity.getFragmentManager();
                    removeCheckedFragment newFragment = new removeCheckedFragment();
                    newFragment.show(fragManager, "TEST");


                    // cancel intent for alarm manager


                } else {
                    // do nothing
                }

            }
        });

        return itemlayoutview;
    }

}
