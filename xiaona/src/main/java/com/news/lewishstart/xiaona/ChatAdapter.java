package com.news.lewishstart.xiaona;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import com.news.lewishstart.xiaona.bean.ChatMsgBean;
import com.news.lewishstart.xiaona.bean.LinkBean;
import com.news.lewishstart.xiaona.bean.MenuBean;
import com.news.lewishstart.xiaona.bean.NewsBean;
import com.news.lewishstart.xiaona.bean.TextBean;
import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.abslistview.MultiItemCommonAdapter;
import com.zhy.base.adapter.abslistview.MultiItemTypeSupport;

import java.text.SimpleDateFormat;
import java.util.List;

//100000 	文本类
//200000 	链接类
//302000 	新闻类
//308000 	菜谱类
public class ChatAdapter extends MultiItemCommonAdapter<ChatMsgBean>{

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ChatAdapter(Context context, List<ChatMsgBean> datas) {
        super(context, datas, new MultiItemTypeSupport<ChatMsgBean>() {
            @Override
            public int getLayoutId(int position, ChatMsgBean chatMsgBean) {
                if(XiaonaApp.Me.equals(chatMsgBean.getName())) {//是我的消息
                    return R.layout.chat_item_right;
                }else if(XiaonaApp.XiaoNa.equals(chatMsgBean.getName())) {//小娜的消息
                    int code = chatMsgBean.getTextBean().getCode();
                    if(code==100000) {
                        return R.layout.chat_item_left_100000;
                    }else if(code==200000) {
                        return R.layout.chat_item_left_200000;
                    }else if(code==302000) {
                        return R.layout.chat_item_left_302000;
                    }else if(code==308000) {
                        return R.layout.chat_item_left_308000;
                    }
                }
                return R.layout.chat_item_left_100000;
            }

            @Override
            public int getViewTypeCount() {
                return 5;
            }

            @Override
            public int getItemViewType(int position, ChatMsgBean chatMsgBean) {
                if(XiaonaApp.Me.equals(chatMsgBean.getName())) {//是我的消息
                    return 0;
                }else if(XiaonaApp.XiaoNa.equals(chatMsgBean.getName())) {//小娜的消息
                    int code = chatMsgBean.getTextBean().getCode();
                    if(code==100000) {
                        return 1;
                    }else if(code==200000) {
                        return 2;
                    }else if(code==302000) {
                        return 3;
                    }else if(code==308000) {
                        return 4;
                    }
                }
                return 1;
            }
        });
    }

    @Override
    public void convert(ViewHolder holder, ChatMsgBean chatMsgBean) {
        switch (holder.getLayoutId()) {
            case R.layout.chat_item_left_100000 :
                Log.e("Tag","文本类");
                //文本类
                holder.setText(R.id.chat_item_left100000_tv_time,simpleDateFormat.format(chatMsgBean.getDate()));
                holder.setText(R.id.chat_item_left100000_tv_msg,chatMsgBean.getTextBean().getText());
                break;
            case R.layout.chat_item_left_200000 :
                Log.e("Tag","链接类");
                //链接类
                final LinkBean linkBean = (LinkBean) chatMsgBean.getTextBean();
                holder.setText(R.id.chat_item_left200000_tv_time,simpleDateFormat.format(chatMsgBean.getDate()));
                holder.setText(R.id.chat_item_left200000_tv_msg,linkBean.getText());
                //holder.setText(R.id.chat_item_left200000_tv_url,linkBean.getUrl());
                holder.setOnClickListener(R.id.chat_item_left200000_tv_url, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(linkBean.getUrl()));
                        mContext.startActivity(intent);
                    }
                });
                break;
            case R.layout.chat_item_left_302000 :
                Log.e("Tag","新闻类");
                //新闻类
                NewsBean newsBean = (NewsBean) chatMsgBean.getTextBean();
                final List<NewsBean.NewsDetailBean> list = newsBean.getList();
                holder.setText(R.id.chat_item_left302000_tv_time,simpleDateFormat.format(chatMsgBean.getDate()));
                holder.setText(R.id.chat_item_left302000_tv_msg,newsBean.getText());

                holder.setText(R.id.chat_item_left302000_tv_article1,list.get(0).getArticle());
                holder.setText(R.id.chat_item_left302000_tv_article2,list.get(1).getArticle());
                holder.setText(R.id.chat_item_left302000_tv_article3,list.get(2).getArticle());

                holder.setOnClickListener(R.id.chat_item_left302000_tv_url1, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(list.get(0).getDetailurl()));
                        mContext.startActivity(intent);
                    }
                });
                holder.setOnClickListener(R.id.chat_item_left302000_tv_url2, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(list.get(1).getDetailurl()));
                        mContext.startActivity(intent);
                    }
                });
                holder.setOnClickListener(R.id.chat_item_left302000_tv_url3, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(list.get(2).getDetailurl()));
                        mContext.startActivity(intent);
                    }
                });
                break;
            case R.layout.chat_item_left_308000 :
                Log.e("Tag","菜谱类");
                //菜谱类
                final MenuBean menuBean = (MenuBean) chatMsgBean.getTextBean();
                holder.setText(R.id.chat_item_left308000_tv_time,simpleDateFormat.format(chatMsgBean.getDate()));
                holder.setText(R.id.chat_item_left308000_tv_menuname,menuBean.getList().get(0).getName());
                holder.setText(R.id.chat_item_left308000_tv_menu_info,menuBean.getList().get(0).getInfo());
                holder.setImageURI(R.id.chat_item_left308000_iv_menuicon,menuBean.getList().get(0).getIcon());
                holder.setOnClickListener(R.id.chat_item_left308000_tv_menudetailurl, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(menuBean.getList().get(0).getDetailurl()));
                        mContext.startActivity(intent);
                    }
                });
                break;
            case R.layout.chat_item_right :
                Log.e("Tag","用户输入");
                //用户输入
                TextBean textBean = chatMsgBean.getTextBean();
                holder.setText(R.id.chat_item_right_tv_msg,textBean.getText());
                holder.setText(R.id.chat_item_right_tv_time,simpleDateFormat.format(chatMsgBean.getDate()));
                break;
        }
    }
}