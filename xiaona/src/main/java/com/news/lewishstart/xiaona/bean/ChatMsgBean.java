package com.news.lewishstart.xiaona.bean;

import java.util.Date;

/**
 * Created by Lewish on 2016/6/22.
 */
public class ChatMsgBean {
    private String name;//speaker
    private TextBean textBean;//消息类
    private Date date;//时间

    public ChatMsgBean(String name, TextBean textBean, Date date) {
        this.name = name;
        this.textBean = textBean;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TextBean getTextBean() {
        return textBean;
    }

    public void setTextBean(TextBean textBean) {
        this.textBean = textBean;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
