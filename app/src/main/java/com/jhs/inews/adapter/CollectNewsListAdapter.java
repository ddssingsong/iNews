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
 * Created by dds on 2016/5/8.
 */
public class CollectNewsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private boolean mShowFooter = true;
    private List<News> datalist = new ArrayList<News>();


    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void addDataToAdapter(List<News> list, boolean isClear) {
        if (isClear) {
            this.datalist.clear();
        }
        this.datalist.addAll(list);
        notifyDataSetChanged();
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

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_news, parent, false);
            ItemViewHolder vh = new ItemViewHolder(v);
            return vh;
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.footer, null);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            return new FooterViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            News news = datalist.get(position);
            ((ItemViewHolder) holder).swipe_item_title.setText(news.getTitle());
            ((ItemViewHolder) holder).swipe_item_date.setText(news.getCtime());
            ImageLoader.getInstance().displayImage(news.getPicUrl(), ((ItemViewHolder) holder).swipe_item_icon, BaseActivity.options);
        }
    }

    @Override
    public int getItemCount() {
        int begin = mShowFooter ? 1 : 0;
        if (datalist == null) {
            return begin;
        }
        return datalist.size() + begin;
    }
    public void isShowFooter(boolean showFooter) {
        this.mShowFooter = showFooter;
    }

    public boolean isShowFooter() {
        return this.mShowFooter;
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View view) {
            super(view);
        }

    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView swipe_item_title;
        public TextView swipe_item_date;
        public ImageView swipe_item_icon;


        public ItemViewHolder(View view) {
            super(view);
            swipe_item_title = (TextView) view.findViewById(R.id.swipe_item_title);
            swipe_item_date = (TextView) view.findViewById(R.id.swipe_item_date);
            swipe_item_icon = (ImageView) view.findViewById(R.id.swipe_item_icon);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(view, this.getPosition());
            }
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }
}
