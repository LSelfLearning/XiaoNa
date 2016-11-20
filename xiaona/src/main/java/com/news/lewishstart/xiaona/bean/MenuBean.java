package com.news.lewishstart.xiaona.bean;

import java.util.List;

/**
 * Created by Lewish on 2016/6/21.
 */
public class MenuBean extends TextBean {

    /**
     * name : 鱼香肉丝
     * icon : http://i4.xiachufang.com/image/280/cb1cb7c49ee011e38844b8ca3aeed2d7.jpg
     * info : 猪肉、鱼香肉丝调料 | 香菇、木耳、红萝卜、黄酒、玉米淀粉、盐
     * detailurl : http://m.xiachufang.com/recipe/264781/
     */

    private List<MenuDetailBean> list;

    public List<MenuDetailBean> getList() {
        return list;
    }

    public void setList(List<MenuDetailBean> list) {
        this.list = list;
    }

    public static class MenuDetailBean {
        private String name;
        private String icon;
        private String info;
        private String detailurl;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getDetailurl() {
            return detailurl;
        }

        public void setDetailurl(String detailurl) {
            this.detailurl = detailurl;
        }
    }
}
