package com.deskconn.dmr.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.deskconn.dmr.Adapters.ChatAdapter;
import com.deskconn.dmr.MessagesActivity;
import com.deskconn.dmr.R;
import com.deskconn.dmr.database.RoomDB;
import com.deskconn.dmr.database.WhatsappData;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class ChatFragment extends Fragment {

    ListView listViewChat;
    ChatAdapter chatAdapter;
    WhatsappData whatsappData;
    RoomDB roomDB;
    private List<WhatsappData> arrayListData;

    public ChatFragment() {
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
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final SwipeRefreshLayout pullToRefresh = view.findViewById(R.id.pullToRefresh);

        listViewChat = view.findViewById(R.id.listview_chat);
        roomDB = RoomDB.getInstance(getContext());
        whatsappData = new WhatsappData();
        arrayListData = roomDB.mainDao().getAll();
        Collections.reverse(arrayListData);
        chatAdapter = new ChatAdapter(requireActivity(), arrayListData);
        listViewChat.setAdapter(chatAdapter);


        pullToRefresh.setOnRefreshListener(() -> {
            List<WhatsappData> newList = roomDB.mainDao().getAll();
            Collections.reverse(newList);
            arrayListData.clear();
            arrayListData.addAll(newList);
            chatAdapter.notifyDataSetChanged();
            pullToRefresh.setRefreshing(false);
        });

        listViewChat.setOnItemClickListener((parent, view1, position, id) -> {
            List<WhatsappData> insideOnClick = arrayListData;
            WhatsappData data = insideOnClick.get(position);
            Intent intent = new Intent(getActivity(), MessagesActivity.class);
            intent.putExtra("name", data.getName());
            intent.putExtra("messages", data.getMessages());
            intent.putExtra("images", data.getImage());
            startActivity(intent);

        });

        listViewChat.setOnItemLongClickListener((parent, view12, position, id) -> {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireActivity());
            builder.setMessage(R.string.are_you_sure_you_want_to_delete_this_chat);
            builder.setPositiveButton(R.string.yes, (dialog, which) -> {

                roomDB.mainDao().deleteChat(arrayListData.get(position).getName());
                List<WhatsappData> newList = roomDB.mainDao().getAll();
                Collections.reverse(newList);
                arrayListData.clear();
                arrayListData.addAll(newList);
                chatAdapter.notifyDataSetChanged();
            });
            builder.setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss());
            builder.show();
            return true;
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        List<WhatsappData> newList = roomDB.mainDao().getAll();
        Collections.reverse(newList);
        arrayListData.clear();
        arrayListData.addAll(newList);
        chatAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        List<WhatsappData> newList = roomDB.mainDao().getAll();
        Collections.reverse(newList);
        arrayListData.clear();
        arrayListData.addAll(newList);
        chatAdapter.notifyDataSetChanged();
    }
}