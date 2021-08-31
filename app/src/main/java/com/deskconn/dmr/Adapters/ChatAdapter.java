package com.deskconn.dmr.Adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.deskconn.dmr.R;
import com.deskconn.dmr.database.WhatsappData;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends ArrayAdapter<String> {
    private List<WhatsappData> dataList;
    private Activity context;

    public ChatAdapter(Activity context, List<WhatsappData> dataList) {
        super(context.getApplicationContext(), R.layout.listview_chat);
        this.context = context;
        this.dataList = dataList;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = context.getLayoutInflater().inflate(R.layout.listview_chat, parent,
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
        File imgFile = new File(dataList.get(position).getImage());
        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        viewHolder.textViewName.setText(dataList.get(position).getName());
        viewHolder.circleImageView.setImageBitmap(myBitmap);

        String message = dataList.get(position).getMessages();
        String[] messageArray = message.split(",");
        viewHolder.textViewMessage.setText(messageArray[messageArray.length - 1]);

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
        CircleImageView circleImageView;
    }
}
