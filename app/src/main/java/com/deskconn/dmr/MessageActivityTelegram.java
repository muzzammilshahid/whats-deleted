package com.deskconn.dmr;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.deskconn.dmr.Adapters.MessageTelegramAdapter;
import com.deskconn.dmr.database.RoomDB;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MessageActivityTelegram extends AppCompatActivity {

    ListView listViewMessagesTelegram;
    MessageTelegramAdapter messageTelegramAdapter;
    List<String> messageList;
    RoomDB roomDB;
    String name;
    String messages;
    String imagePath;
    String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_telegram);

        ActionBar actionBar = this.getSupportActionBar();

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.custom_action_bar);
        View view = getSupportActionBar().getCustomView();

        ImageView imageViewBack = view.findViewById(R.id.backImageView);
        TextView textViewName = view.findViewById(R.id.textview_name);
        listViewMessagesTelegram = findViewById(R.id.listview_messages_telegram);

        name = getIntent().getStringExtra("name");
        imagePath = getIntent().getStringExtra("images");

        textViewName.setText(name);

        imageViewBack.setOnClickListener(v -> onBackPressed());


        roomDB = RoomDB.getInstance(this);
        messages = roomDB.mainDao().getAllMessagesTelegram(name);
        String[] messageArray = messages.split(",");

        messageList = new LinkedList<>(Arrays.asList(messageArray));


        time = roomDB.mainDao().getAllTimeTelegram(name);
        String[] timeArray = time.split(",");


        messageTelegramAdapter = new MessageTelegramAdapter(this, messageList, timeArray);
        listViewMessagesTelegram.setAdapter(messageTelegramAdapter);

    }
}