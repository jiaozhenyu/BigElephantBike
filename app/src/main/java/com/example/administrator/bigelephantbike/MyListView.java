package com.example.administrator.bigelephantbike;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/4/26.
 */
public class MyListView extends ListView {
    private String[] mPlanetTitles ={"我的钱包","身份认证","推荐有奖","帮助"}; // listView的每一个item的名字
    private ListView mDrawerList;
    private View[] itemViews;
    private Context context;
    public MyListView(Context context) {
        super(context);
        this.context =context;
        init();
    }
    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context =context;
        init();
    }

    public void init(){
        super.onFinishInflate();
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        // 为ListView添加适配器
        Log.d("test", mPlanetTitles.length + "这是我的长度");
        if (mDrawerList == null)
            Log.d("test", mDrawerList.toString() + "这是List");
        mDrawerList.setAdapter(new ListAdapter(mPlanetTitles));
        // 监听ListView的点击事件
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Toast.makeText(context, "hhhhh", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public class ListAdapter extends BaseAdapter {

        public ListAdapter(String []itemsText) {
            // TODO Auto-generated constructor stub
            itemViews = new View[itemsText.length];
            for(int i=0;i<itemsText.length;i++){
                String question=itemsText[i];    //获取第i个对象
                //调用makeItemView，实例化一个Item
                itemViews[i]=makeItemView(question);
            }
        }
        @Override
        public int getCount() {
            return itemViews.length;
        }

        @Override
        public Object getItem(int position) {
            return itemViews[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                return itemViews[position];
            return convertView;
        }
    }
    //绘制Item的函数
    private View makeItemView(String question) {
        LayoutInflater inflater = (LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // 使用View的对象itemView与R.layout.item关联
        View itemView = inflater.inflate(R.layout.simple_list_item_1, null);

        // 通过findViewById()方法实例R.layout.item内各组件
        TextView content = (TextView) itemView.findViewById(R.id.option);
        content.setText(question);    //填入相应的值
        return itemView;
    }
}
