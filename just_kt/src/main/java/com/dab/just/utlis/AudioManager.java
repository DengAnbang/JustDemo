package com.dab.just.utlis;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.dab.just.JustConfig.getApplicationContext;

/**
 * Created by dab on 2018/1/18 0018 18:06
 * 音频管理类
 */

public class AudioManager {
    private static final String TAG = "AudioManager";

    private AudioManager() {

    }

    public static AudioRecorderHelper record(String savePath) {
        //麦克风
        int audioSource = MediaRecorder.AudioSource.MIC;
        int sampleRate = 44100;
        //单声道
        int channelConfig = AudioFormat.CHANNEL_IN_MONO;
        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
        int bufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
        AudioRecord audioRecord = new AudioRecord(audioSource, sampleRate, channelConfig, audioFormat, bufferSizeInBytes);
        return new AudioRecorderHelper(audioRecord, savePath, bufferSizeInBytes);
    }
    public static AudioPlayHelper play() {
        return new AudioPlayHelper();
    }

   public static class AudioRecorderHelper {
        private AudioRecord mAudioRecord;
        private String mSavePath;
        private File mFile;
        private int mBufferSizeInBytes;
        private int status;//0 未开始 1 开始 2 暂停

        private AudioRecorderHelper(AudioRecord audioRecord, @NonNull String savePath, @NonNull int bufferSizeInBytes) {
            mAudioRecord = audioRecord;
            mSavePath = savePath;
            mBufferSizeInBytes = bufferSizeInBytes;
        }

        /***
         * 取消本次录音操作
         */
        public void cancel() {
            if (status == 1) {
                mAudioRecord.stop();
            }
            status = 0;
            if (mFile != null) {
                mFile.delete();
            }
        }

        /**
         * 暂停录音
         */
        public void pause() {
            if (status == 1) {
                status = 2;
                mAudioRecord.stop();
            } else {
                Log.e(TAG, "非法状态,已忽略" + status);
            }

        }

        /**
         * 完成录音
         */
        public String finish() {
            if (status != 0) {
                status = 0;
                mAudioRecord.stop();
                String sourcePath = mFile.getAbsolutePath();
                String sinkPath = sourcePath.substring(0, mFile.getAbsolutePath().lastIndexOf(".")) + ".wav";
                boolean succeed = makePCMFileToWAVFile(sourcePath, sinkPath, true);
                mAudioRecord = null;
                if (succeed) {
                    mFile = null;
                    return sinkPath;
                } else return "";
            } else {
                Log.e(TAG, "非法状态,已忽略" + status);
            }
            return "";
        }

        /**
         * 开始录音
         */
        public  void start() {
            if (status == 0) {
                status = 1;
                mAudioRecord.startRecording();
                read();
            } else if (status == 2) {
                status = 1;
                mAudioRecord.startRecording();
            } else {
                Log.e(TAG, "非法状态,已忽略" + status);
            }
        }

        private void read() {
            if (mFile == null) {
                new File(mSavePath).mkdirs();
                mFile = new File(mSavePath, System.currentTimeMillis() + ".pcm");
                final byte[] buffer = new byte[mBufferSizeInBytes];
                if (!mFile.exists()) {
                    mFile.exists();
                }
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(mFile);// 建立一个可存取字节的文件
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                FileOutputStream finalFos = fos;
                new Thread(() -> {
                    while (status != 0) {
                        while (status == 1) {
                            int readsize = mAudioRecord.read(buffer, 0, mBufferSizeInBytes);
                            if (AudioRecord.ERROR_INVALID_OPERATION != readsize && finalFos != null) {
                                try {
                                    finalFos.write(buffer);
                                } catch (IOException e) {
                                    Log.e("AudioRecorder", e.getMessage());
                                }
                            }
                        }
                    }
                    if (finalFos != null) {
                        try {
                            finalFos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }).start();
            }

        }


    }

public static class AudioPlayHelper{
    private String url;
    private MediaPlayer mMediaPlayer;
    private boolean isPlaying;
    public AudioPlayHelper() {
        mMediaPlayer = init();
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void start(String url) {
        this.url = url;
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.start();
        isPlaying = true;
    }
    public void stop() {
        mMediaPlayer.stop();
        isPlaying = false;
    }

    public MediaPlayer init() {
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.reset();
        mediaPlayer.setOnErrorListener((mp, what, extra) -> {
            Toast.makeText(getApplicationContext(), "播放错误！"+what+" extra"+extra, Toast.LENGTH_SHORT).show();
            return true;
        });
        mediaPlayer.setOnCompletionListener(mp -> {
            isPlaying = false;
        });
        return mediaPlayer;
    }
}
    /**
     * 将一个pcm文件转化为wav文件
     *
     * @param pcmPath         pcm文件路径
     * @param destinationPath 目标文件路径(wav)
     * @param deletePcmFile   是否删除源文件
     * @return
     */
    private static boolean makePCMFileToWAVFile(String pcmPath, String destinationPath, boolean deletePcmFile) {
        byte buffer[] = null;
        int TOTAL_SIZE = 0;
        File file = new File(pcmPath);
        if (!file.exists()) {
            return false;
        }
        TOTAL_SIZE = (int) file.length();
        // 填入参数，比特率等等。这里用的是16位单声道 8000 hz
        WaveHeader header = new WaveHeader();
        // 长度字段 = 内容的大小（TOTAL_SIZE) +
        // 头部字段的大小(不包括前面4字节的标识符RIFF以及fileLength本身的4字节)
        header.fileLength = TOTAL_SIZE + (44 - 8);
        header.FmtHdrLeth = 16;
        header.BitsPerSample = 16;
        header.Channels = 5;
        header.FormatTag = 0x0001;
        header.SamplesPerSec = 8000;
        header.BlockAlign = (short) (header.Channels * header.BitsPerSample / 8);
        header.AvgBytesPerSec = header.BlockAlign * header.SamplesPerSec;
        header.DataHdrLeth = TOTAL_SIZE;
        byte[] h = null;
        try {
            h = header.getHeader();
        } catch (IOException e1) {
            Log.e("PcmToWav", e1.getMessage());
            return false;
        }

        if (h.length != 44) // WAV标准，头部应该是44字节,如果不是44个字节则不进行转换文件
            return false;

        //先删除目标文件
        File destfile = new File(destinationPath);
        if (destfile.exists())
            destfile.delete();

        //合成所有的pcm文件的数据，写到目标文件
        try {
            buffer = new byte[1024 * 4]; // Length of All Files, Total Size
            InputStream inStream = null;
            OutputStream ouStream = null;

            ouStream = new BufferedOutputStream(new FileOutputStream(destinationPath));
            ouStream.write(h, 0, h.length);
            inStream = new BufferedInputStream(new FileInputStream(file));
            int size = inStream.read(buffer);
            while (size != -1) {
                ouStream.write(buffer);
                size = inStream.read(buffer);
            }
            inStream.close();
            ouStream.close();
        } catch (FileNotFoundException e) {
            Log.e("PcmToWav", e.getMessage());
            return false;
        } catch (IOException ioe) {
            Log.e("PcmToWav", ioe.getMessage());
            return false;
        }
        if (deletePcmFile) {
            file.delete();
        }
        Log.i("PcmToWav", "makePCMFileToWAVFile  success!" + new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date()));
        return true;
    }

    /**
     * wav文件的头
     */
    private static class WaveHeader {
        public final char fileID[] = {'R', 'I', 'F', 'F'};
        public int fileLength;
        public char wavTag[] = {'W', 'A', 'V', 'E'};
        public char FmtHdrID[] = {'f', 'm', 't', ' '};
        public int FmtHdrLeth;
        public short FormatTag;
        public short Channels;
        public int SamplesPerSec;
        public int AvgBytesPerSec;
        public short BlockAlign;
        public short BitsPerSample;
        public char DataHdrID[] = {'d', 'a', 't', 'a'};
        public int DataHdrLeth;

        public byte[] getHeader() throws IOException {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            WriteChar(bos, fileID);
            WriteInt(bos, fileLength);
            WriteChar(bos, wavTag);
            WriteChar(bos, FmtHdrID);
            WriteInt(bos, FmtHdrLeth);
            WriteShort(bos, FormatTag);
            WriteShort(bos, Channels);
            WriteInt(bos, SamplesPerSec);
            WriteInt(bos, AvgBytesPerSec);
            WriteShort(bos, BlockAlign);
            WriteShort(bos, BitsPerSample);
            WriteChar(bos, DataHdrID);
            WriteInt(bos, DataHdrLeth);
            bos.flush();
            byte[] r = bos.toByteArray();
            bos.close();
            return r;
        }

        private void WriteShort(ByteArrayOutputStream bos, int s) throws IOException {
            byte[] mybyte = new byte[2];
            mybyte[1] = (byte) ((s << 16) >> 24);
            mybyte[0] = (byte) ((s << 24) >> 24);
            bos.write(mybyte);
        }

        private void WriteInt(ByteArrayOutputStream bos, int n) throws IOException {
            byte[] buf = new byte[4];
            buf[3] = (byte) (n >> 24);
            buf[2] = (byte) ((n << 8) >> 24);
            buf[1] = (byte) ((n << 16) >> 24);
            buf[0] = (byte) ((n << 24) >> 24);
            bos.write(buf);
        }

        private void WriteChar(ByteArrayOutputStream bos, char[] id) {
            for (int i = 0; i < id.length; i++) {
                char c = id[i];
                bos.write(c);
            }
        }
    }

}
