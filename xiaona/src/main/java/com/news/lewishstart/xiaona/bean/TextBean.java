package com.news.lewishstart.xiaona.bean;

/**
 * Created by Lewish on 2016/6/21.
 */
public class TextBean {
    /**
     * code : 100000
     * text : 你也好 嘻嘻
     */

    private int code;

    private String text;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public TextBean(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public TextBean() {
    }
}
