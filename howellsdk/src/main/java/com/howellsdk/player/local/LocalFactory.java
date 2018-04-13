package com.howellsdk.player.local;

import com.howell.jni.JniUtil;
import com.howellsdk.api.HWPlayApi;
import com.howellsdk.player.HwBasePlay;

public class LocalFactory {
    String mPath;
    int mCrypto;
    private LocalFactory(String p,int c){
        mPath = p;
        mCrypto = c;
    }

    public LocalProduct create(){
        return new LocalProduct();
    }

    public static final class Builder{
        String path;
        int crypto;

        public Builder setPath(String path) {
            this.path = path;
            return this;
        }

        public Builder setCrypto(int crypto) {
            this.crypto = crypto;
            return this;
        }

        public LocalFactory build(){
            return new LocalFactory(path,crypto);
        }
    }

    public final class LocalProduct extends HwBasePlay {
        @Override
        public boolean connect() {
            return true;
        }

        @Override
        public boolean disconnect() {
            return true;
        }

        @Override
        public void setUri(String uri) {
            mPath = uri;
        }

        @Override
        public void play(boolean isSub) {
            if (JniUtil.isNetReady()){
                JniUtil.netStopPlay();
                JniUtil.stopView();
            }
            JniUtil.localReadyPlay(mCrypto,mPath);
            super.play(isSub);
        }

        @Override
        public void stop() {
            super.stop();
            JniUtil.releasePlay();
        }

        @Override
        public int getStreamLen() {
            return JniUtil.netGetStreamLenSomeTime();
        }

        @Override
        public long getFirstTimestamp() {
            return JniUtil.getFirstTimeStamp();
        }

        @Override
        public long getTimestamp() {
            return JniUtil.getTimeStamp();
        }
    }
}
