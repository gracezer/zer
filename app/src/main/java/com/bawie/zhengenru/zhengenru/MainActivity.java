package com.bawie.zhengenru.zhengenru;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bawie.zhengenru.zhengenru.bean.Prodect;
import com.bwie.xlistviewlibrary.View.XListView;
import com.bwie.xlistviewlibrary.utils.NetWordUtils;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    int page = 0;
    private XListView xlv;
    private MyAdapter adapter;
    String url = "http://api.expoon.com/AppNews/getNewsList/type/1/p/1";
    ArrayList<Prodect.DataBean> list = new ArrayList();
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String json = (String) msg.obj;
            Gson gson = new Gson();
            Prodect prodect = gson.fromJson(json, Prodect.class);
            List<Prodect.DataBean> data = prodect.getData();
            list.addAll(data);
            adapter.notifyDataSetChanged();
            xlv.stopRefresh();
            xlv.stopLoadMore();
            xlv.setRefreshTime("刚刚"+System.currentTimeMillis());
        }
    };
    private ImageLoader instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        xlv = findViewById(R.id.xlv);
        instance = ImageLoader.getInstance();
        getNetData(0);
        adapter = new MyAdapter();
        xlv.setAdapter(adapter);
         xlv.setPullLoadEnable(true);
        xlv.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
              list.clear();
              getNetData(0);
            }

            @Override
            public void onLoadMore() {
              page++;
              getNetData(page);
            }
        });
    }

    private void getNetData(int i) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                String s = NetWordUtils.getNetjson(url + page);
                Message message = new Message();
                message.obj = s;
                handler.sendMessage(message);
            }
        }.start();
    }

    private class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            return position%2;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            int count = getViewTypeCount();
                switch(count){
                        case 0:
                           ViewHolder1 holder1;
                           if (view == null){
                               view = View.inflate(MainActivity.this,R.layout.item1,null);
                               holder1 = new ViewHolder1();
                               holder1.img01 = view.findViewById(R.id.image1);

                               holder1.tv01 = view.findViewById(R.id.text);
                              view.setTag(holder1);
                           }else{
                             holder1 = (ViewHolder1) view.getTag();
                           }
                           holder1.tv01.setText(list.get(i).getNews_title());
                           instance.displayImage(list.get(i).getPic_url(),holder1.img01);

                        break;

                        case 1:
                            ViewHolder2 holder2;
                            if (view == null){
                                view = View.inflate(MainActivity.this,R.layout.item2,null);
                                holder2 = new ViewHolder2();
                                holder2.textView1 = view.findViewById(R.id.tv1);
                                holder2.textView2 = view.findViewById(R.id.tv2);
                                holder2.imageView = view.findViewById(R.id.iv);
                                view.setTag(holder2);
                            }else{
                                holder2 = (ViewHolder2) view.getTag();
                            }
                            holder2.textView1.setText(list.get(i).getNews_title());
                            holder2.textView2.setText(list.get(i).getNews_summary());
                            instance.displayImage(list.get(i).getPic_url(),holder2.imageView);

                        break;

                    }
            return view;
        }
        class ViewHolder1{
            ImageView img01;

            TextView tv01;
        }
        class ViewHolder2{
            ImageView imageView;
            TextView textView1;
            TextView textView2;
        }
    }
}
