package com.kk.imgod.testcommonadapter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerview;
    private List<String> list;
    CustomCommonAdapter commonAdapter;
    LoadMoreWrapper loadMoreWrapper;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            loadMore();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager linear = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(linear);
        list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add("Hello World:" + i);
        }

        commonAdapter = new CustomCommonAdapter<String>(this, R.layout.item_text, list) {
            @Override
            protected void convertView(ViewHolder holder, String s, int position) {
                holder.setText(R.id.txt_title, "内容:" + s + "position:" + position);
            }
        };
        HeaderAndFooterWrapper headerAndFooterWrapper = new HeaderAndFooterWrapper(commonAdapter);
        View view = LayoutInflater.from(this).inflate(R.layout.item_text, recyclerview, false);
        headerAndFooterWrapper.addHeaderView(view);

        loadMoreWrapper = new LoadMoreWrapper(headerAndFooterWrapper);
        loadMoreWrapper.setLoadMoreView(R.layout.load_more);
        loadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                handler.sendEmptyMessageDelayed(0, 3000);
            }
        });

        commonAdapter.setCustomOnItemClickListener(new CustomCommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, Object o, int position) {
                Toast.makeText(MainActivity.this, "onItemClick position:" + position + "内容:" + o.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, Object o, int position) {
                Toast.makeText(MainActivity.this, "onItemLongClick position:" + position + "内容:" + o.toString(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        recyclerview.setAdapter(loadMoreWrapper);

    }

    private int position = 20;

    private void loadMore() {
        int end = position + 20;
        for (; position < end; position++) {
            list.add("Load More:" + position);
        }
//        commonAdapter.notifyDataSetChanged();//这种方式只能加载出来一个.不知道什么鬼
//        loadMoreWrapper.notifyDataSetChanged();//记得最后要用设置给RecyclerView的那个adapter进行刷新
        //推荐下方的方式
        recyclerview.getAdapter().notifyDataSetChanged();

    }
}
