package com.example.hujifang.tabtest;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder>{

    private List<FrdMsg> resultList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout frdMsgLayout;
        CircleImageView frdImage;
        TextView frdName;
        TextView frdHint;

        ViewHolder(View view) {
            super(view);
            frdMsgLayout = view.findViewById(R.id.frd_msg);
            frdImage = view.findViewById(R.id.frd_msg_img);
            frdName = view.findViewById(R.id.frd_msg_name);
            frdHint = view.findViewById(R.id.frd_msg_hint);
        }
    }

    SearchResultAdapter(List<FrdMsg> resultList){
        this.resultList = resultList;
    }

    @Override
    public SearchResultAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.frdmsg_item, parent, false);
        final SearchResultAdapter.ViewHolder holder = new SearchResultAdapter.ViewHolder(view);
        holder.frdMsgLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                FrdMsg frdMsg = resultList.get(position);
                Intent intent = new Intent(v.getContext(), RobotActivity.class);
                intent.putExtra("name", frdMsg.getName());
                v.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(SearchResultAdapter.ViewHolder holder, int position) {
        FrdMsg frdMsg = resultList.get(position);
        holder.frdImage.setImageResource(frdMsg.getFrdImage());
        holder.frdName.setText(frdMsg.getName());
        holder.frdHint.setText(frdMsg.getHint());
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }
}
