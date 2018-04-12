package com.howellsdk.api;

public interface HWDownloadApi {
    void open(String path);
    void start();
    void stop();
    void close();
}
