package com.jhs.inews.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhs.inews.R;
import com.jhs.inews.base.BaseActivity;
import com.jhs.inews.entity.News;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/2/25.
 */
public class NewsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private boolean mShowFooter = true;
    private OnItemClickListener mOnItemClickListener;
    private List<News> list = new ArrayList<News>();

    /**
     * 设置点击监听
     */
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    /**
     * 增加数据到Adapter
     */
    public void addDataToAdapter(List<News> list, boolean isClear) {
        if (isClear) {
            this.list.clear();
        }
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public boolean isShowFooter() {
        return this.mShowFooter;
    }

    public void isShowFooter(boolean showFooter) {
        this.mShowFooter = showFooter;
    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if (!mShowFooter) {
            return TYPE_ITEM;
        }
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    /**
     * 创建新View，被LayoutManager所调用
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_news, viewGroup, false);
            ViewHolder vh = new ViewHolder(view);
            return vh;
        } else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.footer, null);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            return new FooterViewHolder(view);
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            News news = list.get(position);
            ((ViewHolder) holder).swipe_item_title.setText(news.getTitle());
            ((ViewHolder) holder).swipe_item_date.setText(news.getCtime());
            ((ViewHolder) holder).swipe_item_dec.setText(news.getDescription());
            ImageLoader.getInstance().displayImage(news.getPicUrl(), ((ViewHolder) holder).swipe_item_icon, BaseActivity.options);
        }
    }

    /**
     * 将数据与界面进行绑定的操作
     */


    //获取数据的数量
    @Override
    public int getItemCount() {
        int begin = mShowFooter ? 1 : 0;
        if (list == null) {
            return begin;
        }
        return list.size() + begin;
    }


    public class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View view) {
            super(view);
        }

    }

    /**
     * 自定义的ViewHolder，持有每个Item的的所有界面元素
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView swipe_item_title;
        public TextView swipe_item_dec;
        public TextView swipe_item_date;
        public ImageView swipe_item_icon;


        public ViewHolder(View view) {
            super(view);
            swipe_item_title = (TextView) view.findViewById(R.id.swipe_item_title);
            swipe_item_date = (TextView) view.findViewById(R.id.swipe_item_date);
            swipe_item_icon = (ImageView) view.findViewById(R.id.swipe_item_icon);
            swipe_item_dec= (TextView) view.findViewById(R.id.swipe_item_dec);
            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(view, this.getAdapterPosition());
            }
        }


    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

}