package com.simple.note;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class about extends AppCompatActivity {

    private ListView alist;
    String[] abouts = new String[]{"Github","捐赠","博客"};
    private ArrayList<Map<String, String>> newlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        View fm1l = findViewById(R.id.am1);
        //沉浸式状态栏
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);//设置状态栏文字为浅色
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        int geth = this.getResources().getIdentifier("status_bar_height","dimen","android");//获取状态栏高度
        int result = this.getResources().getDimensionPixelSize(geth);//转换
        if(result>0) {
            fm1l.setPadding(0, result, 0, 0);//设置父布局内边距
        }

        alist = findViewById(R.id.alistview);
        newlist = new ArrayList<>();

        for (int i=0;i<abouts.length;i++) {
            Map<String,String> map = new HashMap<>();
            map.put("name", abouts[i]);
            newlist.add(map);
        }
        SimpleAdapter adapt = new SimpleAdapter(getApplicationContext(),newlist,R.layout.itemlayout,new String[]{"name"},new int[]{R.id.aitextView});
        alist.setAdapter(adapt);
        alist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String,String> map2 = (HashMap<String, String>) adapterView.getItemAtPosition(i);
                String name = map2.get("name");
                if (name=="Github"){
                    Uri uri = Uri.parse("https://github.com/ahzoa/simplenote");
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(intent);

                }else if(name=="捐赠"){
                    AlertDialog.Builder builder = new AlertDialog.Builder(about.this);
                    builder.setTitle("使用支付宝捐赠支持");
                    builder.setView(R.layout.picture);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.show();

                }else{
                    Uri uri = Uri.parse("https://ahzoo.cn");
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(intent);

                }
            }
        });


    }
    public void afinish(View view){
        finish();
    }

}