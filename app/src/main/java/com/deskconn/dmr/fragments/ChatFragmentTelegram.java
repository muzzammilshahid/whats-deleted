package com.deskconn.dmr.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.deskconn.dmr.Adapters.ChatAdapterTelegram;
import com.deskconn.dmr.MessageActivityTelegram;
import com.deskconn.dmr.R;
import com.deskconn.dmr.database.RoomDB;
import com.deskconn.dmr.database.TelegramData;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class ChatFragmentTelegram extends Fragment {

    ListView listViewChatTelegram;
    ChatAdapterTelegram chatAdapterTelegram;
    RoomDB roomDB;
    private List<TelegramData> arrayListData;

    public ChatFragmentTelegram() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_telegram, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final SwipeRefreshLayout pullToRefresh = view.findViewById(R.id.pullToRefresh_telegram);
        listViewChatTelegram = view.findViewById(R.id.listview_chat_telegram);
        roomDB = RoomDB.getInstance(getContext());
        arrayListData = roomDB.mainDao().getAllTelegram();
        Collections.reverse(arrayListData);
        chatAdapterTelegram = new ChatAdapterTelegram(requireActivity(), arrayListData);
        listViewChatTelegram.setAdapter(chatAdapterTelegram);



        pullToRefresh.setOnRefreshListener(() -> {
            List<TelegramData> newList = roomDB.mainDao().getAllTelegram();
            Collections.reverse(newList);
            arrayListData.clear();
            arrayListData.addAll(newList);
            chatAdapterTelegram.notifyDataSetChanged();
            pullToRefresh.setRefreshing(false);
        });

        listViewChatTelegram.setOnItemClickListener((parent, view1, position, id) -> {
                List<TelegramData> insideOnClick = arrayListData;
                TelegramData data = insideOnClick.get(position);
                Intent intent = new Intent(getActivity(), MessageActivityTelegram.class);
                intent.putExtra("name", data.getName());
                intent.putExtra("messages", data.getMessages());
                intent.putExtra("images", data.getImage());
                startActivity(intent);
        });

        listViewChatTelegram.setOnItemLongClickListener((parent, view12, position, id) -> {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireActivity());
            builder.setMessage(R.string.are_you_sure_you_want_to_delete_this_chat);
            builder.setPositiveButton(R.string.yes, (dialog, which) -> {

                roomDB.mainDao().deleteChatTelegram(arrayListData.get(position).getName());
                List<TelegramData> newList = roomDB.mainDao().getAllTelegram();
                Collections.reverse(newList);
                arrayListData.clear();
                arrayListData.addAll(newList);
                chatAdapterTelegram.notifyDataSetChanged();
            });
            builder.setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss());
            builder.show();
            return true;
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        List<TelegramData> newList = roomDB.mainDao().getAllTelegram();
        Collections.reverse(newList);
        arrayListData.clear();
        arrayListData.addAll(newList);
        chatAdapterTelegram.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        List<TelegramData> newList = roomDB.mainDao().getAllTelegram();
        Collections.reverse(newList);
        arrayListData.clear();
        arrayListData.addAll(newList);
        chatAdapterTelegram.notifyDataSetChanged();
    }
}