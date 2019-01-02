package com.example.hujifang.tabtest;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FrdMsgAdapter extends RecyclerView.Adapter<FrdMsgAdapter.ViewHolder> {

    private static final int FRDVIEW_TYPE = 0;
    private static final int SEARCHVIEW_TYPE = 1;
    private List<FrdMsg> mfrdMsgList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout frdMsgLayout;
        CircleImageView frdImage;
        TextView frdName;
        TextView frdHint;
        LinearLayout llsearch;

        ViewHolder(View view) {
            super(view);
            frdMsgLayout = view.findViewById(R.id.frd_msg);
            frdImage = view.findViewById(R.id.frd_msg_img);
            frdName = view.findViewById(R.id.frd_msg_name);
            frdHint = view.findViewById(R.id.frd_msg_hint);
            llsearch = view.findViewById(R.id.ll_search);
        }
    }

    FrdMsgAdapter(List<FrdMsg> frdMsgList){
        mfrdMsgList = frdMsgList;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        if (viewType == SEARCHVIEW_TYPE){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item,parent,false);
            ViewHolder holder = new ViewHolder(view);
            holder.llsearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), SearchActivity.class);
                    intent.putExtra("mfrdMsgList",(Serializable) mfrdMsgList);
                    v.getContext().startActivity(intent);
                }
            });
            return holder;
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.frdmsg_item, parent, false);
            final ViewHolder holder = new ViewHolder(view);
            holder.frdMsgLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    FrdMsg frdMsg = mfrdMsgList.get(position - 1);
                    Intent intent = new Intent(v.getContext(), RobotActivity.class);
                    intent.putExtra("name", frdMsg.getName());
                    v.getContext().startActivity(intent);
                }
            });
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(position > 0) {
            FrdMsg frdMsg = mfrdMsgList.get(position - 1);
            holder.frdImage.setImageResource(frdMsg.getFrdImage());
            holder.frdName.setText(frdMsg.getName());
            holder.frdHint.setText(frdMsg.getHint());
        }
    }

    @Override
    public int getItemCount() {
        return mfrdMsgList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType;
        if(position == 0){
            viewType = SEARCHVIEW_TYPE;
        }else{
            viewType = FRDVIEW_TYPE;
        }
        return viewType;
    }
}
