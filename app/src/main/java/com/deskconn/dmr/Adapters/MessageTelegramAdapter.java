package com.deskconn.dmr.Adapters;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.deskconn.dmr.R;

import java.util.List;

public class MessageTelegramAdapter extends ArrayAdapter<String> {
    private List<String> dataList;
    private Activity context;
    private String[] timeArray;
    private ViewHolder viewHolder;


    public MessageTelegramAdapter(Activity context, List<String> dataList, String[] timeArray) {
        super(context.getApplicationContext(), R.layout.listview_message);
        this.context = context;
        this.dataList = dataList;
        this.timeArray = timeArray;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = context.getLayoutInflater().inflate(R.layout.listview_message, parent,
                    false);
            viewHolder = new ViewHolder();
            viewHolder.textViewMessage = convertView.findViewById(R.id.textview_message);
            viewHolder.textViewTime = convertView.findViewById(R.id.time);
            convertView.setTag(viewHolder);
            Log.i("TAG", " creating new");
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            Log.i("TAG", " using old one");
        }
        viewHolder.textViewMessage.setText(dataList.get(position));
        viewHolder.textViewTime.setText(timeArray[position]);

        convertView.setFocusable(false);
        return convertView;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return dataList.get(position);
    }

    class ViewHolder {
        TextView textViewMessage;
        TextView textViewTime;
    }

}
