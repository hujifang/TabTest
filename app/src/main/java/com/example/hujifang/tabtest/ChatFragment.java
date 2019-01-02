package com.example.hujifang.tabtest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    private List<FrdMsg> frdMsgList = new ArrayList<>();
    private FrdMsgAdapter frdMsgAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrdMsg frdMsg = new FrdMsg(R.drawable.head_img,"Jake","你好，我是Jake");
        frdMsgList.add(frdMsg);
        for(int i = 1;i <= 5;i++) {
            FrdMsg frdMsg1 = new FrdMsg(R.drawable.head_img, "Tom" + i, "你好，我是Tom" + i + "!");
            frdMsgList.add(frdMsg1);
        }
        for(int i = 1;i <= 5;i++) {
            FrdMsg frdMsg2 = new FrdMsg(R.drawable.head_img, "Mary" + i, "你好，我是Mary" + i + "!");
            frdMsgList.add(frdMsg2);
        }
        for(int i = 1;i <= 5;i++) {
            FrdMsg frdMsg3 = new FrdMsg(R.drawable.head_img, "Lisa" + i, "你好，我是Lisa" + i + "!");
            frdMsgList.add(frdMsg3);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.chat_page,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final RecyclerView chatRecyclerView = getActivity().findViewById(R.id.chat_recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        chatRecyclerView.setLayoutManager(layoutManager);

        frdMsgAdapter = new FrdMsgAdapter(frdMsgList);
        chatRecyclerView.setAdapter(frdMsgAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        FileInputStream in;
        BufferedReader reader = null;
        try {
            for(int i = 0;i < frdMsgList.size();i++) {
                in = getActivity().openFileInput(frdMsgList.get(i).getName() + "data");
                reader = new BufferedReader((new InputStreamReader(in)));
                String line;
                String currenthint = "";
                while ((line = reader.readLine()) != null) {
                    currenthint = line.substring(0, line.length() - 2);
                }
                if (!currenthint.equals("")) {
                    frdMsgList.get(i).setHint(currenthint);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        frdMsgAdapter.notifyDataSetChanged();
    }
}
