package com.linyelai.ffmepg;

import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.*;

public class ScreenRecorder {

    public static void recordScreen(String outputFile, int duration) throws Exception {
        // 创建帧抓取器
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber("desktop");
        grabber.setFormat("gdigrab");
        grabber.setFrameRate(30);
        grabber.setImageWidth(1920);
        grabber.setImageHeight(1080);

        // 创建帧录制器
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(
                outputFile,
                grabber.getImageWidth(),
                grabber.getImageHeight());
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        recorder.setFormat("mp4");
        recorder.setFrameRate(grabber.getFrameRate());
        recorder.setVideoBitrate(4000000);

        grabber.start();
        recorder.start();

        long startTime = System.currentTimeMillis();
        Frame frame;

        while ((System.currentTimeMillis() - startTime) < duration * 1000) {
            frame = grabber.grab();
            if (frame != null) {
                recorder.record(frame);
            }
        }

        recorder.close();
        grabber.close();
    }

    public static void main(String[] args) {
        try {
            recordScreen("screen_record.mp4", 10); // 录制10秒
            System.out.println("屏幕录制完成！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}