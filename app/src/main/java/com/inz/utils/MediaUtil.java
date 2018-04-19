package com.inz.utils;

import android.media.MediaFormat;
import android.media.MediaMuxer;

import java.io.IOException;

public class MediaUtil {


    public void init(String mp4FilePath) throws IOException {
        MediaMuxer muxer = new MediaMuxer(mp4FilePath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);




    }
}
