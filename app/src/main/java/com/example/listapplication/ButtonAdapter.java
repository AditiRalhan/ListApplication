package com.example.listapplication;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ButtonAdapter extends RecyclerView.Adapter<ButtonAdapter.CustomViewHolder>
{
    ArrayList<ButtonsMsg> buttonsMsgArrayList;

    public ButtonAdapter(ArrayList<ButtonsMsg> buttonsMsgArrayList)
    {
       this.buttonsMsgArrayList = buttonsMsgArrayList;
    }

    @NonNull
    @Override
    public ButtonAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chatbuttons,viewGroup,false);
        return new ButtonAdapter.CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder customViewHolder, int i)
    {
        customViewHolder.tv.setText(buttonsMsgArrayList.get(i).getButtonValue());
    }

    @Override
    public int getItemCount() {
        return buttonsMsgArrayList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv;
        public CustomViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tv = (TextView)itemView.findViewById(R.id.bttnTv);
        }
    }
}
