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

import com.deskconn.dmr.Adapters.ChatAdapterInstagram;
import com.deskconn.dmr.MessageActivityInstagram;
import com.deskconn.dmr.R;
import com.deskconn.dmr.database.InstagramData;
import com.deskconn.dmr.database.RoomDB;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class ChatFragmentInstagram extends Fragment {

    ListView listViewChatInstagram;
    ChatAdapterInstagram chatAdapterInstagram;
    InstagramData instagramData;
    RoomDB roomDB;
    private List<InstagramData> arrayListData;

    public ChatFragmentInstagram() {
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
        return inflater.inflate(R.layout.fragment_chat_instagram, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final SwipeRefreshLayout pullToRefresh = view.findViewById(R.id.pullToRefresh_instagram);
        listViewChatInstagram = view.findViewById(R.id.listview_chat_instagram);
        roomDB = RoomDB.getInstance(getContext());
        instagramData = new InstagramData();
        arrayListData = roomDB.mainDao().getAllInstagram();
        Collections.reverse(arrayListData);
        chatAdapterInstagram = new ChatAdapterInstagram(requireActivity(), arrayListData);
        listViewChatInstagram.setAdapter(chatAdapterInstagram);


        pullToRefresh.setOnRefreshListener(() -> {
            List<InstagramData> newList = roomDB.mainDao().getAllInstagram();
            Collections.reverse(newList);
            arrayListData.clear();
            arrayListData.addAll(newList);
            chatAdapterInstagram.notifyDataSetChanged();
            pullToRefresh.setRefreshing(false);
        });

        listViewChatInstagram.setOnItemClickListener((parent, view1, position, id) -> {
            List<InstagramData> insideOnClick = arrayListData;
            InstagramData data = insideOnClick.get(position);
            Intent intent = new Intent(getActivity(), MessageActivityInstagram.class);
            intent.putExtra("name", data.getName());
            intent.putExtra("messages", data.getMessages());
            intent.putExtra("images", data.getImage());
            startActivity(intent);

        });

        listViewChatInstagram.setOnItemLongClickListener((parent, view12, position, id) -> {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireActivity());
            builder.setMessage(R.string.are_you_sure_you_want_to_delete_this_chat);
            builder.setPositiveButton(R.string.yes, (dialog, which) -> {

                roomDB.mainDao().deleteChatInstagram(arrayListData.get(position).getName());
                List<InstagramData> newList = roomDB.mainDao().getAllInstagram();
                Collections.reverse(newList);
                arrayListData.clear();
                arrayListData.addAll(newList);
                chatAdapterInstagram.notifyDataSetChanged();
            });
            builder.setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss());
            builder.show();
            return true;
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        List<InstagramData> newList = roomDB.mainDao().getAllInstagram();
        Collections.reverse(newList);
        arrayListData.clear();
        arrayListData.addAll(newList);
        chatAdapterInstagram.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        List<InstagramData> newList = roomDB.mainDao().getAllInstagram();
        Collections.reverse(newList);
        arrayListData.clear();
        arrayListData.addAll(newList);
        chatAdapterInstagram.notifyDataSetChanged();
    }
}