package com.news.lewishstart.xiaona;

import android.app.Application;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import org.xutils.x;

public class XiaonaApp extends Application{
    public static final String TULINGAPIKEY = "6acde68f9280c5c76fd27266f308b25c";
    public static final String XUNFEIAPPID = "5767f63f";
    public static final String Me = "me";
    public static final String XiaoNa = "小娜";
    @Override
    public void onCreate() {
        super.onCreate();
        //创建语音配置对象
        SpeechUtility.createUtility(this, SpeechConstant.APPID+"="+XUNFEIAPPID);

        //xutils框架的初始化
        x.Ext.init(this);
        x.Ext.setDebug(org.xutils.BuildConfig.DEBUG);
    }
}
