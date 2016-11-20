package com.news.lewishstart.xiaona.bean;

import java.util.List;

/**
 * Created by Lewish on 2016/6/21.
 */
public class NewsBean extends TextBean {

    /**
     * article : 工信部:今年将大幅提网速降手机流量费
     * source : 网易新闻
     * icon :
     * detailurl : http://news.163.com/15/0416/03/AN9SORGH0001124J.html
     */

    private List<NewsDetailBean> list;

    public List<NewsDetailBean> getList() {
        return list;
    }

    public void setList(List<NewsDetailBean> list) {
        this.list = list;
    }

    public static class NewsDetailBean {
        private String article;
        private String source;
        private String icon;
        private String detailurl;

        public String getArticle() {
            return article;
        }

        public void setArticle(String article) {
            this.article = article;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getDetailurl() {
            return detailurl;
        }

        public void setDetailurl(String detailurl) {
            this.detailurl = detailurl;
        }
    }
}
