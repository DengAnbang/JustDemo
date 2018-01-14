package com.dab.ushare_java;

import android.app.Activity;
import android.content.Context;

import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

/**
 * Created by dab on 2018/1/14 0014 11:41
 */

public class UshareConfig {
    public static void init(Context ctx,String appkey,String channel,boolean debug) {
        Context applicationContext = ctx.getApplicationContext();
//      s2  Push推送业务的secret
        UMConfigure.init(applicationContext == null ? ctx : applicationContext, appkey, channel, UMConfigure.DEVICE_TYPE_PHONE, "1fe6a20054bcef865eeb0991ee84525b");
        UMConfigure.setLogEnabled(debug);

    }

    public static void setQQZone(String id, String key) {
        PlatformConfig.setQQZone(id,key);
    }
    public static void setWeixin(String id, String key) {
        PlatformConfig.setWeixin(id,key);
    }
    public static void setSinaWeibo(String key, String secret, String redirectUrl) {
        PlatformConfig.setSinaWeibo(key,secret,redirectUrl);
    }

    public static void sadas(Activity activity,int icon) {
        UMWeb web = new UMWeb("http://www.baidu.com");
        web.setTitle("微服出行");//标题
        web.setThumb(new UMImage(activity,icon));  //缩略图 todo 不要圆角
        web.setDescription("微服出行，让我们的出行更方便");//描述
        new ShareAction(activity)
                .setPlatform(SHARE_MEDIA.QQ)
                .withMedia(web)
                .setCallback(listener)
                .share();
    }
    private static UMShareListener listener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {

        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            throwable.printStackTrace();
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {

        }
    };
}
