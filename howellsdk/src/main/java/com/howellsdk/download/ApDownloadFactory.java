package com.howellsdk.download;

import com.howell.jni.JniUtil;
import com.howellsdk.api.HWDownloadApi;
import com.howellsdk.utils.FileUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ApDownloadFactory {

    private ApDownloadFactory(){

    }

    public HWDownloadApi create(){
        return new DownloadPudect();
    }

    public static final class Builder{


        public ApDownloadFactory build(){
            return new ApDownloadFactory();
        }
    }

    public final class DownloadPudect extends HwBaseDownload{
        RandomAccessFile mFile;



        @Override
        public HWDownloadApi open(String path) {
            try {
                mFile = FileUtil.createVideoFile(path);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //jni init
            JniUtil.downloadInit();
            JniUtil.downloadSetCallbackObj(this,0);
            JniUtil.downloadSetCallbackMethod("saveData",0);
            return this;
        }

        @Override
        public HWDownloadApi start() {
            JniUtil.downloadEnable(true);
            return this;
        }

        @Override
        public HWDownloadApi stop() {
            JniUtil.downloadEnable(false);
            return this;
        }


        private void saveData(byte[] data) {
            try {
                FileUtil.write2VideoFile(mFile,data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public HWDownloadApi close() {
            try {
                FileUtil.closeVideoFile(mFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //jni deinit
            JniUtil.downloadDeinit();
            return this;
        }
    }
}
