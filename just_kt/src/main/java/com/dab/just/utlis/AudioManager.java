package com.dab.just.utlis;

import android.media.MediaRecorder;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by dab on 2018/1/18 0018 18:06
 * 音频管理类
 */

public class AudioManager {
    static class AudioHelper{
        private MediaRecorder mMediaRecorder;
        private File mCurrentFile;

        private AudioHelper(MediaRecorder mediaRecorder, @NonNull File currentFile) {
            mMediaRecorder = mediaRecorder;
            mCurrentFile = currentFile;
        }

        /***
         * 取消本次录音操作
         */
        public void cancel() {
            stopAndRelease();
            mCurrentFile.delete();
        }
        /**
         * 停止录音
         */
        void stopAndRelease() {
            if (mMediaRecorder == null) return;
            mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }

        /**
         * 停止录音
         */
        void start() {
            try {
                mMediaRecorder.prepare();
                mMediaRecorder.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static AudioHelper startRecord(String savePath) {
        MediaRecorder mediaRecorder = new MediaRecorder();
        File file = new File(savePath, UUID.randomUUID().toString() + ".amr");
        String absolutePath = file.getAbsolutePath();
        mediaRecorder.setOutputFile(absolutePath);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        // 设置录音的保存格式
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        // 设置录音的编码
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        return new AudioHelper(mediaRecorder,file);
    }
}
