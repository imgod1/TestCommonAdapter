package com.kk.imgod.testcommonadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by imgod on 2016/7/9.
 */
public abstract class CustomCommonAdapter<T> extends CommonAdapter<T> {
    public CustomCommonAdapter(Context context, int layoutId, List<T> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, T t, int position) {
        int tempposition = position == 0 ? 0 : position - 1;
        setCustomClick(holder, tempposition);
        convertView(holder, t, tempposition);
    }

    protected abstract void convertView(ViewHolder holder, T t, int position);

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener<T> {
        void onItemClick(View view, RecyclerView.ViewHolder holder, T o, int position);

        boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, T o, int position);
    }

    public void setCustomOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    /**
     * 设置点击事件
     *
     * @param viewHolder viewholder 对象
     * @param position   下标位置
     */
    private void setCustomClick(final ViewHolder viewHolder, final int position) {
        if (mOnItemClickListener != null) {
            viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(v, viewHolder, mDatas.get(position), position);
                }
            });

            viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mOnItemClickListener.onItemLongClick(v, viewHolder, mDatas.get(position), position);
                }
            });
        }
    }
}
