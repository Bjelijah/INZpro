package com.inz.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.inz.bean.PictureBean;
import com.inz.inzpro.R;

import java.util.List;

public class MyPictureAdapter extends  RecyclerView.Adapter<MyPictureAdapter.ViewHolder> {
    Context mContext;
    List<PictureBean> mList;
    OnItemClickListener mListener;

    public MyPictureAdapter(Context c,OnItemClickListener l){
        mContext = c;
        mListener = l;
    }

    public MyPictureAdapter(Context c,List<PictureBean> list,OnItemClickListener l){
        mContext = c;
        mList = list;
        mListener = l;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picture,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PictureBean b = mList.get(position);
        init(holder,b,position);
    }

    @Override
    public int getItemCount() {
        return mList==null?0:mList.size();
    }

    public interface OnItemClickListener{
        void onItemClick(int pos);
        void onItemLongClick(int pos);
    }

    private void init(ViewHolder h, PictureBean b, final int pos){
        //TODO get pic
        h.iv.setImageDrawable(mContext.getDrawable(R.mipmap.ic_launcher));
        h.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener==null)return;
                mListener.onItemClick(pos);
            }
        });
        h.ll.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mListener==null)return false;
                mListener.onItemLongClick(pos);
                return false;
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iv;
        LinearLayout ll;
        public ViewHolder(View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.item_picture_iv);
            ll = itemView.findViewById(R.id.item_picture_ll);
        }
    }

}
