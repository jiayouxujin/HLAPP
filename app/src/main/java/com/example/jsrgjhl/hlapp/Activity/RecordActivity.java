package com.example.jsrgjhl.hlapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jsrgjhl.hlapp.Adapter.Record;
import com.example.jsrgjhl.hlapp.Adapter.RecordsAdapter;
import com.example.jsrgjhl.hlapp.PersonalSetting.OperateRecord;
import com.example.jsrgjhl.hlapp.R;
import com.example.jsrgjhl.hlapp.Utils.jsonstr2map;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RecordActivity extends AppCompatActivity implements Serializable{

    private TextView mstatictv;
    private List<Record> mrecordsList=new ArrayList<>();
    private String getrecordpath="http://47.100.107.158:8080/api/record/getrecordlist";
    private int flag;
    private RecordsAdapter adapter;
    private final static String Tag= OperateRecord.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        //toolbar部分
        Toolbar toolbar = (Toolbar) findViewById(R.id.recordtoolbar);
        mstatictv=(TextView)findViewById(R.id.static_tv);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        }
        //统计按钮事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mstatictv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RecordActivity.this,"这是要跳转到统计页面后面在做", Toast.LENGTH_SHORT).show();
            }
        });

        //初始化Records列表
        initRecords();
        adapter=new RecordsAdapter(RecordActivity.this, R.layout.record_list,mrecordsList);
        ListView recordlistView=(ListView)findViewById(R.id.record_listview);
        recordlistView.setAdapter(adapter);

        //item点击事件
        recordlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.setClass(RecordActivity.this, SolveActivity.class);
                Record record0=mrecordsList.get(i);
                intent.putExtra("record",(Serializable)record0);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mrecordsList.clear();
        initRecords();
        adapter.notifyDataSetChanged();
    }

    /**
     * 向服务器请求记录列表
     */
    private void initRecords() {
        flag=0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(getrecordpath).build();
                try {
                    Response response = client.newCall(request).execute();//发送请求
                    String result = response.body().string();
                    Map<String, Object> map= jsonstr2map.jsonstr2map(result);
                    /**
                     * 将 string 转为json格式
                     */

                        String temp = map.get("data").toString();
                        Log.i(Tag, temp);
                        temp = temp.substring(1, temp.length() - 1).replace(" ", "").replace("{", "").replace("}", "").replace("\"","").replace("\"","");
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String[] strs = temp.split(",");
                        Map<String, String> map2 = new HashMap<String, String>();
                        for (String s : strs) {
                            String sss=s.replace(" ","");
                            String[] ms = sss.split("=");

                            if (ms[1].equals("null")) {
                                ms[1] = "";
                            }
                            map2.put(ms[0], ms[1]);

                            if (ms[0].equals("deltime")) {
                                Date recordtime=format.parse(map2.get("recordtime"));
                                Date deltime=format.parse(map2.get("deltime"));
                                Record record1 = new Record((String) map2.get("recordnum"),recordtime, map2.get("recordstatus"), Integer.parseInt(map2.get("solutionID")) , Integer.parseInt(map2.get("userID")) , (String) map2.get("username"), (String) map2.get("title"), (String) map2.get("context"),Integer.parseInt(map2.get("deviceID")),(String)map2.get("devicenum"),(String)map2.get("deviceaddress"),map2.get("regionID"),map2.get("defposID"),Double.parseDouble(map2.get("devicelat")),Double.parseDouble(map2.get("devicelng")),map2.get("devicetype"),map2.get("devicestatus"),deltime);
                                mrecordsList.add(record1);
                                map2.clear();
                            }
                        }

                    if(mrecordsList.size()!=0){
                        flag=1;
                    }else flag=2;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        while (flag==0){
            try{
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        if(flag==1){
            Log.i(Tag,"设备记录请求成功");
        }

    }

}
