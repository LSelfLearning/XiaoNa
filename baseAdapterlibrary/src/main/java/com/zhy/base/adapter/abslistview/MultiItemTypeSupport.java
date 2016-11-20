package com.zhy.base.adapter.abslistview;

public interface MultiItemTypeSupport<T>
{
	/**
	 * 得到布局Id
	 */
	int getLayoutId(int position, T t);
	/**
	 * 一共有多少种View?
	 */
	int getViewTypeCount();
	/**
	 * 得到Item的View的类型
	 */
	int getItemViewType(int position, T t);
}