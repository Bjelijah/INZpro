package com.howellsdk.api;

import java.io.FileNotFoundException;

public interface HWDownloadApi {
    HWDownloadApi open(String path) throws FileNotFoundException;
    HWDownloadApi start();
    HWDownloadApi stop();
    HWDownloadApi close();
}
