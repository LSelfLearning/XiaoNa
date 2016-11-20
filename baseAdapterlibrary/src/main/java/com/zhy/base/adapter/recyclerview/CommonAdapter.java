package com.zhy.base.adapter.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhy.base.adapter.ViewHolder;

import java.util.List;

/**
 * Created by zhy on 16/4/9.
 */
public abstract class CommonAdapter<T> extends RecyclerView.Adapter<ViewHolder>
{
    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener)
    {
        this.mOnItemClickListener = onItemClickListener;
    }

    public CommonAdapter(Context context, int layoutId, List<T> datas)
    {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mDatas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType)
    {
        ViewHolder viewHolder = ViewHolder.get(mContext, null, parent, mLayoutId, -1);
        setListener(parent, viewHolder, viewType);
        return viewHolder;
    }

    protected int getPosition(RecyclerView.ViewHolder viewHolder)
    {
        return viewHolder.getAdapterPosition();
    }

    protected boolean isEnabled(int viewType)
    {
        return true;
    }


    protected void setListener(final ViewGroup parent, final ViewHolder viewHolder, int viewType)
    {
        if (!isEnabled(viewType)) return;
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mOnItemClickListener != null)
                {
                    int position = getPosition(viewHolder);
                    mOnItemClickListener.onItemClick(parent, v, mDatas.get(position), position);
                }
            }
        });


        viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                if (mOnItemClickListener != null)
                {
                    int position = getPosition(viewHolder);
                    return mOnItemClickListener.onItemLongClick(parent, v, mDatas.get(position), position);
                }
                return false;
            }
        });
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.updatePosition(position);
        convert(holder, mDatas.get(position));
    }

    public abstract void convert(ViewHolder holder, T t);

    @Override
    public int getItemCount()
    {
        return mDatas.size();
    }

    /**
     * 向指定位置添加元素
     * @param position
     * @param data
     */
    public void addDataByPosition(int position,T data){
        if(data!=null) {
            mDatas.add(position,data);
            notifyItemInserted(position);
        }
    }

    /**
     * 删除指定位置元素
     * @param position
     */
    public void delDataByPosition(int position){
        if(position>=0) {
            mDatas.remove(position);
            notifyItemRemoved(position);
        }
    }

    /**
     * 向指定位置添加一些元素（未测试）
     * @param position
     * @param datas
     */
    public void addDatasByPosition(int position,List<T> datas){
        if(datas!=null&&datas.size()>0) {
            mDatas.addAll(position,datas);
            notifyItemRangeInserted(position,datas.size());
        }
    }

    /**
     * 向底部添加一些元素（未测试）
     * @param datas
     */
    public void addDatasToBottom(List<T> datas){
        if(datas!=null&&datas.size()>0) {
            int position = getItemCount();
            mDatas.addAll(position,datas);
            notifyItemRangeInserted(position,datas.size());
        }
    }
}
