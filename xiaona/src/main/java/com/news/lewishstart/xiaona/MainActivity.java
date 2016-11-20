package com.news.lewishstart.xiaona;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.news.lewishstart.xiaona.bean.ChatMsgBean;
import com.news.lewishstart.xiaona.bean.LinkBean;
import com.news.lewishstart.xiaona.bean.MenuBean;
import com.news.lewishstart.xiaona.bean.NewsBean;
import com.news.lewishstart.xiaona.bean.TextBean;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@ContentView(R.layout.main_layout)
public class MainActivity extends Activity implements View.OnClickListener{

    @ViewInject(R.id.main_lv_chat)
    private ListView main_lv_chat;//显示聊天内容的ListView

    @ViewInject(R.id.main_tv_sound_insert)
    private TextView main_tv_sound_insert;//语音输入按键

    @ViewInject(R.id.main_et_input_msg)
    private EditText main_et_input_msg;//手写输入内容显示

    @ViewInject(R.id.main_tv_send)
    private TextView main_tv_send;//发送按键

    private static final String TAG = "MainActivity";

    private String dictationnAlyticalData;//听写得到的数据

    private List<ChatMsgBean> chatDataList;//聊天数据集合

    private ChatAdapter myAdapter;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        chatDataList = new ArrayList<>();
        chatDataList.add(new ChatMsgBean(XiaonaApp.XiaoNa,new TextBean(100000,"你好,我是小娜~"),new Date()));
        myAdapter = new ChatAdapter(this,chatDataList);
        main_lv_chat.setAdapter(myAdapter);

        main_tv_sound_insert.setOnClickListener(this);
        main_tv_send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_tv_sound_insert :
                startVoice();
                break;
            case R.id.main_tv_send :
                String inputMeg = main_et_input_msg.getText().toString().trim();
                sendMes(new ChatMsgBean(XiaonaApp.Me,new TextBean(100000,inputMeg),new Date()));
                getDataFromNet(inputMeg);
                break;
        }
    }

    public void sendMes(ChatMsgBean chatMsgBean){
        chatDataList.add(chatMsgBean);
        myAdapter.notifyDataSetChanged();
        main_lv_chat.setSelection(myAdapter.getCount()-1);
    }
    /**
     * 向图灵官网请求数据
     * @param info
     */
    private void getDataFromNet(String info) {
        RequestParams params = new RequestParams("http://www.tuling123.com/openapi/api");
        params.addBodyParameter("key", XiaonaApp.TULINGAPIKEY);
        params.addBodyParameter("info",info);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d(TAG, "onSuccess: ");
                TextBean textBean = parseJson(result);

                speek(textBean.getText());
                sendMes(new ChatMsgBean(XiaonaApp.XiaoNa,textBean,new Date()));
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d(TAG, "onError: ");
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.d(TAG, "onCancelled: ");
            }

            @Override
            public void onFinished() {
                Log.d(TAG, "onFinished: ");
            }
        });
    }

    /**
     * 将请求得到的数据封装到TextBean中(多态)
     //100000 	文本类
     //200000 	链接类
     //302000 	新闻类
     //308000 	菜谱类
     * @param result
     * @return TextBean
     */
    private TextBean parseJson(String result) {
        TextBean textBean = new TextBean();
        try {
            JSONObject jsonObject = new JSONObject(result);
            int code = jsonObject.optInt("code");
            //判断请求码，封装数据
            switch (code) {
                //文本类
                case  100000:
                    textBean = new Gson().fromJson(result, TextBean.class);
                    break;
                //链接类
                case  200000:
                    textBean = new Gson().fromJson(result, LinkBean.class);
                    break;
                //新闻类
                case  302000:
                    textBean = new Gson().fromJson(result, NewsBean.class);
                    break;
                //菜谱类
                case  308000:
                    textBean = new Gson().fromJson(result, MenuBean.class);
                    break;
                default:
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return textBean;
        }
    }

    private void startVoice() {
        //1.创建RecognizerDialog对象
        RecognizerDialog mDialog = new RecognizerDialog(this, new MyInitListener());
        //2.设置accent、 language等参数
        mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");//设置中文
        mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");//设置普通话
        mDialog.setParameter(SpeechConstant.DOMAIN, "iat");//设置日常用语
        //若要将UI控件用于语义理解，必须添加以下参数设置，设置之后onResult回调返回将是语义理解
        //结果
        // mDialog.setParameter("asr_sch", "1");
        // mDialog.setParameter("nlp_version", "2.0");
        //3.设置回调接口
        mDialog.setListener(new MyRecognizerDialogListener());
        //4.显示dialog，接收语音输入
        mDialog.show();
    }

    /**
     * 将传入的字符串合成语音
     * @param str
     */
    private void speek(String str) {
        //1.创建 SpeechSynthesizer 对象, 第二个参数： 本地合成时传 InitListener
        SpeechSynthesizer mTts = SpeechSynthesizer.createSynthesizer(this, null);
        //2.合成参数设置，详见《 MSC Reference Manual》 SpeechSynthesizer 类
        //设置发音人（更多在线发音人，用户可参见 附录13.2
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan"); //设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围 0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
        //设置合成音频保存位置（可自定义保存位置），保存在“./sdcard/iflytek.pcm”
        //保存在 SD 卡需要在 AndroidManifest.xml 添加写 SD 卡权限
        //仅支持保存为 pcm 和 wav 格式， 如果不需要保存合成音频，注释该行代码
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");
        //3.开始合成 设置语音合成的监听
        mTts.startSpeaking(str.trim(), mSynListener);
    }

    /**
     * 合成监听器类
     */
    private SynthesizerListener mSynListener = new SynthesizerListener() {
        //会话结束回调接口，没有错误时， error为null
        public void onCompleted(SpeechError error) {
            //Toast.makeText(MainActivity.this,"结束了",Toast.LENGTH_SHORT).show();
        }

        //缓冲进度回调
        //percent为缓冲进度0~100， beginPos为缓冲音频在文本中开始位置， endPos表示缓冲音频在
        //文本中结束位置， info为附加信息。
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
        }

        //开始播放
        public void onSpeakBegin() {
            //Toast.makeText(MainActivity.this,"开始播放",Toast.LENGTH_SHORT).show();
        }

        //暂停播放
        public void onSpeakPaused() {
            //Toast.makeText(MainActivity.this, "暂停播放", Toast.LENGTH_SHORT).show();
        }

        //播放进度回调
        //percent为播放进度0~100,beginPos为播放音频在文本中开始位置， endPos表示播放音频在文
        //本中结束位置.
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
        }

        //恢复播放回调接口
        public void onSpeakResumed() {
        }

        //会话事件回调接口
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
        }
    };

    /**
     * 初始化监听器
     */
    class MyInitListener implements InitListener {

        @Override
        public void onInit(int i) {
            if (i != ErrorCode.SUCCESS) {
                Toast.makeText(MainActivity.this, "初始化失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 听写监听器类
     */
    class MyRecognizerDialogListener implements RecognizerDialogListener {

        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            Log.e("MainActivity", recognizerResult.getResultString());
            if(!b) {
                //得到解析的数据
                dictationnAlyticalData = getDictationResult(recognizerResult);
                //显示解析出的数据
                showDictationResult();
            }
        }

        @Override
        public void onError(SpeechError speechError) {
            Log.e("MainActivity", "出错了");
        }
    }

    /**
     * 解析语音输入，得到听写内容
     * @param results
     */
    private String getDictationResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        Log.e("MainActivity", "解析好的内容==" + text);

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }
        Log.e("MainActivity", "最终结果==" + resultBuffer.toString());
        return resultBuffer.toString();
    }

    /**
     * 显示听写得到的内容
     */
    private void showDictationResult(){
        main_et_input_msg.setText(dictationnAlyticalData);
        sendMes(new ChatMsgBean(XiaonaApp.Me,new TextBean(100000,dictationnAlyticalData),new Date()));
        getDataFromNet(dictationnAlyticalData);
    }
}
