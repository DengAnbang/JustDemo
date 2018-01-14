package com.home.dab.justdemo;


import com.dab.just.JustApplication;
import com.dab.ushare_java.UshareConfig;

/**
 * Created by dab on 2018/1/8 0008 14:28
 */

public class App extends JustApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        UshareConfig.init(this,"5a5ade39b27b0a737c000697","dsda",true);
        UshareConfig.setQQZone("1106678594","KMo05Lw9DpwkKIdz");
    }
}
