package com.simple.note;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
;
    private ImageView image2;
    private ImageView image1;
    private Cursor cursor;
    private ListView listview;
    private ArrayList<Map<String, String>> list1;
    private SimpleAdapter adapter;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Created By Zohar
        // Home page ：https://ahzoo.cn
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View fm1l = findViewById(R.id.fm1);
        //沉浸式状态栏
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//设置状态栏文字为浅色
        int geth = this.getResources().getIdentifier("status_bar_height","dimen","android");//获取状态栏高度
        int result = this.getResources().getDimensionPixelSize(geth);//转换
        if(result>0) {
            fm1l.setPadding(0, result, 0, 0);//设置父布局内边距
        }

        image1 = findViewById(R.id.imageView);
        image2 = findViewById(R.id.imageView2);
        listview = findViewById(R.id.listview);
        Sqdata notedata = new Sqdata(MainActivity.this);
        database = notedata.getWritableDatabase();
        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //开启新建便签界面，并传递数据
                Intent intent =new Intent(getApplicationContext(),LayoutActivity.class);
                intent.putExtra("new","new");
                // startActivityForResult(intent,result1);
                startActivityForResult(intent,0x01);
            }
        });
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(getApplicationContext(),about.class);
                startActivity(intent2);
            }
        });

        start(listview);//首页列表数据设置

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //获取列表数据
                HashMap<String,String> map2=(HashMap<String,String>)adapterView.getItemAtPosition(i);
                String item= map2.get("001");
                //Object item = adapterView.getAdapter().getItem(i);
                //进入便签详情页，并传递获取到的列表数据
                Intent intent =new Intent(getApplicationContext(),LayoutActivity.class);
                intent.putExtra("update",item);
                startActivityForResult(intent,0x02);//设置标识码为002，用于和上面的新建便签区分
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0x01){
            String extra = data.getStringExtra("text2");
            //edit12.setText(extra);
            start(listview);
        }
    }

    public void start(View view){
        list1 = new ArrayList<Map<String, String>>();//新建一个list1
        String sql = "select * from user where 日期!=?";
        cursor = database.rawQuery(sql, new String[]{"*"});
        int i=1;//初始化i
        while (cursor.moveToNext()){
            String title = cursor.getString(cursor.getColumnIndex("标题"));
            String  detail= cursor.getString(cursor.getColumnIndex("内容"));
            if(detail!=null) {
                //int data= cursor.getInt(cursor.getColumnIndex("日期"));
                Map<String, String> map = new HashMap<>();//新建一个map
                map.put("001", title);//添加数据
                map.put("002", detail);
                map.put("003", String.valueOf(i));
                i++;
                list1.add(map);//将map数据添加到list1
            }
        }
        //adapter布局
        adapter = new SimpleAdapter(getApplicationContext(), list1, R.layout.listlayout, new String[]{"001","002","003"}, new int[]{R.id.ltextView,R.id.ltextView2,R.id.ltextView3});
        listview.setAdapter(adapter);

    }

    //双击退出
    private long exittime=0;
    @Override
    public boolean onKeyDown(int keyCode ,KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK&&event.getAction()==KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exittime)>2000){
                Toast.makeText(getApplicationContext(),"再按一次退出",Toast.LENGTH_SHORT).show();
                exittime=System.currentTimeMillis();
            }else{
                finish();//结束Activity
                System.exit(0);//结束APP
            }
            return true;
        }

        return super.onKeyDown(keyCode,event);
    }


}
