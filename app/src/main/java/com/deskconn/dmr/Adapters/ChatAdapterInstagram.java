package com.deskconn.dmr.Adapters;

import android.app.Activity;
import android.graphics.BlurMaskFilter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.deskconn.dmr.R;
import com.deskconn.dmr.database.InstagramData;

import java.util.List;


public class ChatAdapterInstagram extends ArrayAdapter<String> {

    private List<InstagramData> dataList;
    private Activity context;

    public ChatAdapterInstagram(Activity context, List<InstagramData> dataList) {
        super(context.getApplicationContext(), R.layout.listview_chat);
        this.context = context;
        this.dataList = dataList;
    }


    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = context.getLayoutInflater().inflate(R.layout.listview_chat_instagram, parent,
                    false);
            viewHolder = new ViewHolder();
            viewHolder.textViewName = convertView.findViewById(R.id.textview_name);
            viewHolder.textViewMessage = convertView.findViewById(R.id.textview_message);
            viewHolder.circleImageView = convertView.findViewById(R.id.profile_image);
            convertView.setTag(viewHolder);
            Log.i("TAG", " creating new");
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            Log.i("TAG", " using old one");
        }
        viewHolder.textViewName.setText(dataList.get(position).getName());
        String message = dataList.get(position).getMessages();
        String[] messageArray = message.split(",");
        viewHolder.textViewMessage.setText(messageArray[messageArray.length - 1]);
        viewHolder.circleImageView.setImageResource(R.drawable.instagram_logo);

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
        return dataList.get(position).getName();
    }

    class ViewHolder {
        TextView textViewName;
        TextView textViewMessage;
        ImageView circleImageView;
    }

}
