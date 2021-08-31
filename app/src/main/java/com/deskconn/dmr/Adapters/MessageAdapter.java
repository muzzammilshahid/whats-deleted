package com.deskconn.dmr.Adapters;

import android.app.Activity;
import android.graphics.BlurMaskFilter;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.deskconn.dmr.R;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends ArrayAdapter<Pair<String, String>> {

    private Activity context;
    private List<Pair<String, String>> realData;
    private String[] timeArray;
    private ViewHolder viewHolder;


    public MessageAdapter(Activity context, List<String> dataList, String[] timeArray) {
        super(context.getApplicationContext(), R.layout.listview_message);
        this.context = context;

        this.timeArray = timeArray;

        List<String> changed = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).equals(context.getString(R.string.this_message_was_deleted))) {
                changed.add(i, null);
            } else {
                changed.add(dataList.get(i));
            }
        }

        List<Pair<String, String>> tempData = new ArrayList<>();
        for (int i = 0; i < changed.size(); i++) {
            Pair<String, String> pair;
            int index = i + 1;
            if (index < changed.size() && changed.get(i + 1) == null) {
                pair = new Pair<>("red", changed.get(i));
            } else {
                pair = new Pair<>("none", changed.get(i));
            }
            tempData.add(pair);
        }

        realData = new ArrayList<>();
        for (Pair<String, String> tempDatum : tempData) {
            if (tempDatum.second != null) {
                realData.add(tempDatum);
            }
        }
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Log.e("TAG", " ==================== ");
        Log.e("TAG", " position " + position);
        Log.e("TAG", " percent " + position % 7);
        Log.e("TAG", " type " + getItemViewType(position));

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

        viewHolder.textViewTime.setText(timeArray[position]);

        Pair<String, String> data = realData.get(position);
        if (data.first.equals("red")) {
            viewHolder.textViewMessage.setText(data.second.trim());
            viewHolder.textViewMessage.setBackgroundResource(R.color.red);
        } else {
            viewHolder.textViewMessage.setText(data.second.trim());
        }


        convertView.setFocusable(false);
        return convertView;
    }

    @Override
    public int getCount() {
        return realData.size();
    }

    @Nullable
    @Override
    public Pair<String, String> getItem(int position) {
        return realData.get(position);
    }

    class ViewHolder {
        TextView textViewMessage;
        TextView textViewTime;
    }

}
