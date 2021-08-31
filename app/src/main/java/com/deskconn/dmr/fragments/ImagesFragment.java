package com.deskconn.dmr.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.deskconn.dmr.Adapters.DataAdapter;
import com.deskconn.dmr.ImageUrl;
import com.deskconn.dmr.R;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class ImagesFragment extends Fragment {

    private ImageView imageView;
    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    ArrayList<ImageUrl> imageUrlList;
    DataAdapter dataAdapter;


    public ImagesFragment() {
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
        return inflater.inflate(R.layout.fragment_images, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView = view.findViewById(R.id.imageView);
        recyclerView = view.findViewById(R.id.recyclerView);
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);


        imageUrlList = prepareData();
        Collections.reverse(imageUrlList);
        dataAdapter = new DataAdapter(getActivity(), imageUrlList);
        recyclerView.setAdapter(dataAdapter);
    }

    private ArrayList<ImageUrl> prepareData() {
        String path = Environment.getExternalStorageDirectory() + "/DMR/Whatsapp/Whatsapp Images";
        System.out.println("prepare " + path);
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File[] files = directory.listFiles();
        ArrayList<ImageUrl> imageUrlList = new ArrayList<>();
        try {
            for (File file : files) {
                ImageUrl imageUrl = new ImageUrl();
                imageUrl.setImageUrl(file + "");
                imageUrlList.add(imageUrl);
                Log.d("Files", "FileName:" + file.getName());
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return imageUrlList;
    }

    @Override
    public void onResume() {
        super.onResume();
        ArrayList<ImageUrl> imageUrlArrayList = prepareData();
        Collections.reverse(imageUrlArrayList);
        imageUrlList.clear();
        imageUrlList.addAll(imageUrlArrayList);
        dataAdapter.notifyDataSetChanged();
    }
}