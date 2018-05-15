package com.example.tempest.iyteclubsy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by Bora Gültekin on 13.05.2018.
 */

public class ClubActionAdapter extends ArrayAdapter<ClubAction>{

    private ArrayList<ClubAction> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView subject;
        TextView description;
        TextView date;
        TextView time;
    }

    public ClubActionAdapter(ArrayList<ClubAction> data, Context context) {
        super(context, R.layout.club_action_item, data);
        this.dataSet = data;
        this.mContext=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ClubAction dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.club_action_item, parent, false);
            viewHolder.subject = (TextView) convertView.findViewById(R.id.subject);
            viewHolder.description = (TextView) convertView.findViewById(R.id.description);
            viewHolder.date = (TextView) convertView.findViewById(R.id.date);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.subject.setText(dataModel.getSubject());
        viewHolder.description.setText(dataModel.getDescription());
        viewHolder.date.setText(dataModel.getEventDate());
        viewHolder.time.setText(dataModel.getEventTime());

        // Return the completed view to render on screen
        return convertView;
    }

}
