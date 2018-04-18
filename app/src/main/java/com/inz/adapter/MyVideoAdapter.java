package com.inz.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inz.bean.VideoBean;
import com.inz.inzpro.R;

import java.util.List;

public class MyVideoAdapter extends RecyclerView.Adapter<MyVideoAdapter.ViewHolder> {
    Context mContext;
    List<VideoBean> mList;
    OnItemClickListener mListener;
    int mSelectPos=-1;
    public MyVideoAdapter(Context c,OnItemClickListener l){
        mContext = c;
        mListener = l;
    }

    public MyVideoAdapter(Context c, List<VideoBean> l,OnItemClickListener listener){
        mContext = c;
        mList = l;
        mListener = listener;
    }

    public void setData(List<VideoBean> l){
        mList = l;
        notifyDataSetChanged();
    }

    public void addData(VideoBean b){
        mList.add(b);
        notifyItemChanged(mList.size()-1);
    }

    public void removeData(VideoBean b){
        try{
            int i = mList.indexOf(b);
            mList.remove(b);
            notifyItemRemoved(i);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void clearData(){
        mList.clear();
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_ex,parent,false);
        return new ViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VideoBean b = mList.get(position);
        init(holder,b,position);
    }

    @Override
    public int getItemCount() {
        return mList==null?0:mList.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void init(ViewHolder holder, final VideoBean bean, final int pos){
        //get bitmap
        holder.iv.setImageDrawable(mContext.getDrawable(R.mipmap.ic_launcher));
        holder.tv.setText(bean.getName());
        if (mSelectPos==pos){
            //TODO 加边框
            holder.ll.setBackgroundResource(R.drawable.item_video_border);
        }else{
            //todo 没边框
            holder.ll.setBackgroundColor(mContext.getColor(R.color.main_bk_light));
        }
        holder.ll.setLongClickable(true);
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener==null)return;
                mListener.onItemClickListener(bean,pos);
                mSelectPos = pos;
                notifyDataSetChanged();
            }
        });
        holder.ll.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mListener==null)return false;
                mListener.onItemLongClickListener(v,bean,pos);
                return true;
            }
        });

    }

    public interface OnItemClickListener{
        void onItemClickListener(VideoBean b,int pos);
        void onItemLongClickListener(View v,VideoBean b,int pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv;
        ImageView iv;
        RelativeLayout ll;
        boolean isSelect = false;
        public ViewHolder(View itemView) {
            super(itemView);
            ll = itemView.findViewById(R.id.item_video_ll);
            iv = itemView.findViewById(R.id.item_video_iv);
            tv = itemView.findViewById(R.id.item_video_tv);
        }

    }

}
