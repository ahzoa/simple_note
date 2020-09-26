package com.simple.note;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

public class LayoutActivity extends AppCompatActivity {


    private EditText name;
    private EditText detail;
    private SQLiteDatabase database;
    private String times;
    private TextView text2;
    private String titlet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);
        View fm1l = findViewById(R.id.lm1);
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

        name = findViewById(R.id.editTextTextPersonName);
        detail = findViewById(R.id.editTextTextPersonName2);
        text2 = findViewById(R.id.dtextView2);

        Sqdata notedata = new Sqdata(LayoutActivity.this);
        database = notedata.getWritableDatabase();//设置数据库变量为读写
        titlet = getIntent().getStringExtra("update");//获取mainActivity传递的updata数据
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//获取系统时间
        times = simpleDateFormat.format(System.currentTimeMillis());
        text2.setText(times);

         if(titlet !=null) {
             name.setText(titlet);//接受并显标题
             String sql = "select * from user where 标题=?";
             Cursor cursor = database.rawQuery(sql, new String[]{getIntent().getStringExtra("update")});
             if (cursor.moveToNext()) {
                 String nr = cursor.getString(cursor.getColumnIndex("内容"));
                 String data = cursor.getString(cursor.getColumnIndex("日期"));
                 detail.setText(nr);//显示标题
                 text2.setText(data);//显示内容
             }
         }
    }
    public void csave(View view){
        if(titlet==null) {
          String sql = "select * from user where 标题=?";
          Cursor cursor = database.rawQuery(sql, new String[]{name.getText().toString()});
          if(cursor.getCount()==0) {

              ContentValues contentValues = new ContentValues();//设置需要保存的数据
              contentValues.put("标题", name.getText().toString());
              contentValues.put("内容", detail.getText().toString());
              contentValues.put("日期", text2.getText().toString());
              database.insert("user", null, contentValues);//增加数据
              Toast.makeText(getApplicationContext(), "已保存", Toast.LENGTH_SHORT).show();

              cfinish(text2);//返回主页
          }else{
            Toast.makeText(getApplicationContext(),"标题已存在",Toast.LENGTH_SHORT).show();
          }
        }else{
            String sql2 = "update user set 标题=?,内容=? where 日期=?";
            database.execSQL(sql2, new Object[]{name.getText().toString(), detail.getText().toString(), text2.getText().toString()});
            Toast.makeText(getApplicationContext(), "已保存", Toast.LENGTH_SHORT).show();

            cfinish(text2);//返回主页
        }
    }
    public void cdelete(View view){
        String sql="delete from user where 标题=?";
        database.execSQL(sql,new Object[]{name.getText().toString()});
        Toast.makeText(getApplicationContext(),"已删除",Toast.LENGTH_SHORT).show();
        cfinish(text2);//返回主页
    }

    public void cfinish(View view){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.putExtra("black","ok");//设置返回数据
        setResult(RESULT_OK,intent);//传递返回数据给mainActivity
        finish();
    }
}