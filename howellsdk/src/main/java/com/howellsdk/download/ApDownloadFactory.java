package com.howellsdk.download;

import com.howellsdk.api.HWDownloadApi;
import com.howellsdk.utils.FileUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ApDownloadFactory {

    String mPathDirName;
    private ApDownloadFactory(String path){
        mPathDirName = path;
    }

    public HWDownloadApi create(){
        return new DownloadPudect();
    }

    public static final class Builder{
        String pathDirName;

        public Builder setPathDirName(String pathDirName) {
            this.pathDirName = pathDirName;
            return this;
        }

        public ApDownloadFactory build(){
            return new ApDownloadFactory(pathDirName);
        }
    }

    public final class DownloadPudect extends HwBaseDownload{
        RandomAccessFile mFile;

        @Override
        public void init() {
            try {
                mFile = FileUtil.createVideoFile(mPathDirName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //jni init
        }

        @Override
        public void start() {

        }

        @Override
        public void stop() {

        }


        private void saveData(byte[] data) {
            try {
                FileUtil.write2VideoFile(mFile,data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void close() {
            try {
                FileUtil.closeVideoFile(mFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //jni deinit

        }
    }
}
