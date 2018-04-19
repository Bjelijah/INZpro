package com.inz.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.inz.bean.PictureBean;
import com.inz.inzpro.R;
import com.inz.utils.ScaleImageUtils;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MyPictureAdapter extends  RecyclerView.Adapter<MyPictureAdapter.ViewHolder> {
    Context mContext;
    List<PictureBean> mList;

    OnItemClickListener mListener;
    boolean mShareMode = false;
    int mViewWidth;
    public MyPictureAdapter(Context c,OnItemClickListener l){
        mContext = c;
        mListener = l;
        mShareMode = false;
    }

    public MyPictureAdapter(Context c,List<PictureBean> list,OnItemClickListener l){
        mContext = c;
        mList = list;
        mListener = l;
        mShareMode = false;
    }

    private Pair<Integer,Integer> getWidthHeight(){
        int width = mViewWidth/2  ;
        int height = width *9/16;
        return new Pair<>(width, height);
    }

    public void setWidth(int width){
        mViewWidth = width;
    }


    public void setData(List<PictureBean> l){
        mList = l;
        for (PictureBean b:mList){
            Log.i("123","path="+b.getPath());
            Pair<Integer,Integer>p = getWidthHeight();
            b.setWidth(p.first);
            b.setHeight(p.second);
        }
        Log.i("123"," we set data ");
        notifyDataSetChanged();
    }

    public void setShareMode(boolean isShareMode){
        if (mShareMode==isShareMode)return;
        mShareMode = isShareMode;
        notifyDataSetChanged();
    }


    public void addData(PictureBean b){
        Pair<Integer,Integer>p = getWidthHeight();
        b.setWidth(p.first);
        b.setHeight(p.second);
        mList.add(b);
        notifyItemChanged(mList.size()-1);
    }

    public void removeData(PictureBean b){
        try{
            int i = mList.indexOf(b);
            mList.remove(b);
            notifyItemChanged(i);
        }catch (Exception e){e.printStackTrace();}
    }

    public void removePos(int pos){
        if (pos<0 || pos > mList.size()-1)return;
        mList.remove(pos);
        notifyItemChanged(pos);
    }



    public void clearData(){
        mList.clear();
        notifyDataSetChanged();
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
        void onItemShareCheck(View v,int pos,PictureBean b,boolean isChecked);
        void onItemClick(View v,List<PictureBean> list,int pos);
        void onItemLongClick(View v,int pos,PictureBean b);
    }



    private void init(final ViewHolder h, final PictureBean b, final int pos){
        //TODO get pic
        Log.i("123","init   pos="+pos);
        ViewGroup.LayoutParams params = h.itemView.getLayoutParams();
        params.width = b.getWidth();
        params.height = b.getHeight();
        h.itemView.setLayoutParams(params);
        h.ck.setVisibility(mShareMode?View.VISIBLE:View.GONE);
        h.ck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i("123","isChecked  isChecked="+isChecked+"  pos="+pos);
                if (mListener==null)return;
                mListener.onItemShareCheck(buttonView,pos,b,isChecked);
            }
        });
        Observable.create(new ObservableOnSubscribe<String>(){

            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext(b.getPath());
            }
        })
                .map(new Function<String, Bitmap>() {
                    @Override
                    public Bitmap apply(String s) throws Exception {
                        return ScaleImageUtils.INSTANCE.decodeFile(b.getWidth(),b.getHeight(),new File(s));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Bitmap>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i("123"," on subscribe");
                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        Log.i("123","~~~~~~~~~~~on next  bitmap");
                        h.iv.setImageBitmap(bitmap);
//                        h.iv.setImageDrawable(mContext.getDrawable(R.mipmap.ic_launcher));
                        h.ll.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mListener==null)return;
                                if(mShareMode)return;
                                mListener.onItemClick(v,mList,pos);
                            }
                        });
                        h.ll.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                if (mListener==null)return false;
                                if (mShareMode)return false;
                                mListener.onItemLongClick(v,pos,b);
                                return true;
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iv;
        LinearLayout ll;
        CheckBox ck;
        public ViewHolder(View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.item_picture_iv);
            ll = itemView.findViewById(R.id.item_picture_ll);
            ck = itemView.findViewById(R.id.item_picture_ck);
        }
    }

}
