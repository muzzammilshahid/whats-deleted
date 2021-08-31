package com.deskconn.dmr;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.deskconn.dmr.Adapters.MessageInstagramAdapter;
import com.deskconn.dmr.database.RoomDB;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MessageActivityInstagram extends AppCompatActivity {

    ListView listViewMessagesInstagram;
    MessageInstagramAdapter messageInstagramAdapter;
    List<String> messageList;
    RoomDB roomDB;
    String name;
    String messages;
    String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_instagram);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        View view = getSupportActionBar().getCustomView();

        ImageView imageViewBack = view.findViewById(R.id.backImageView);
        TextView textViewName = view.findViewById(R.id.textview_name);
        listViewMessagesInstagram = findViewById(R.id.listview_messages_instagram);

        name = getIntent().getStringExtra("name");
        textViewName.setText(name);

        imageViewBack.setOnClickListener(v -> onBackPressed());


        roomDB = RoomDB.getInstance(this);
        messages = roomDB.mainDao().getAllMessagesInstagram(name);
        String[] messageArray = messages.split(",");

        messageList = new LinkedList<>(Arrays.asList(messageArray));


        time = roomDB.mainDao().getAllTimeInstagram(name);
        String[] timeArray = time.split(",");


        messageInstagramAdapter = new MessageInstagramAdapter(this, messageList, timeArray);
        listViewMessagesInstagram.setAdapter(messageInstagramAdapter);

    }
}