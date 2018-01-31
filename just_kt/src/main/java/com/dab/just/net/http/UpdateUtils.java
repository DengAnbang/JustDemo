package com.dab.just.net.http;

import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

/**
 * Created by dab on 2017/9/28 0028 09:55
 */

public class UpdateUtils {
    public interface OnDownloadListener {
        boolean onDownload(long progress, long total, String saveDir, @Nullable Exception e);
    }
    /**
     * @param url 下载连接
     * @param saveDir 储存下载文件的SDCard目录
     * @param listener 下载监听
     */
    public static void download(final String url, final String saveDir, final OnDownloadListener listener) {
        Uri uri = Uri.parse(url);
        String filename = uri.getLastPathSegment();// get fileName
        File root;
        if (!TextUtils.isEmpty(saveDir)) {
            root = new File(saveDir);
        } else {
            root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        }
        if (!root.exists()) {
            boolean mkdirs = root.mkdirs();
        }
        File output = new File(root, filename);
        if (output.exists()) {
            boolean delete = output.delete();
        }
        String absolutePath = output.getAbsolutePath();
        try {
            Request request = new Request.Builder().url(uri.toString()).build();
            new OkHttpClient().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    listener.onDownload(-1,-1,absolutePath, e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    BufferedSink sink = Okio.buffer(Okio.sink(output));
                    Buffer buffer = sink.buffer();
                    long total = 0;
                    long len;
                    int bufferSize = 200 * 1024; //200kb
                    ResponseBody body = response.body();
                    if (body == null) {
                        throw new IOException();
                    }
                    long contentLength = body.contentLength();
                    BufferedSource source = body.source();
                    while ((len = source.read(buffer, bufferSize)) != -1) {
                        sink.emit();
                        total += len;
                        boolean cancel = listener.onDownload(total,contentLength,absolutePath, null);
                        if (cancel) {
                            call.cancel();
                            source.close();
                            sink.close();
                            return;
                        }
                    }
                    source.close();
                    sink.close();
                }
            });
        } catch (Exception e) {
            listener.onDownload(-1,-1,absolutePath, e);
        }



    }

}
