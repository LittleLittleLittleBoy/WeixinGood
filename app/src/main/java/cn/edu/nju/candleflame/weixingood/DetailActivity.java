package cn.edu.nju.candleflame.weixingood;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {

    private String url="";
    private int num=0;


    private LinearLayout peopleLinearLayout;
    private ImageView picImage;
    private TextView descView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        num = intent.getIntExtra("num", 10);
        url = intent.getStringExtra("url");
        String date=intent.getStringExtra("date");
        String comment=intent.getStringExtra("comment");


        peopleLinearLayout=findViewById(R.id.detail_people_LinerLayout);
        picImage=findViewById(R.id.detail_picImage);
        descView=findViewById(R.id.detail_desc);
        TextView dateView=findViewById(R.id.detail_date);
        dateView.setText(date);
        TextView commentView=findViewById(R.id.detail_comment);
        commentView.setText(comment);

        getWebText();
        getPeopleList();
    }


    private void getPeopleList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int[] peoples = PeopleUtil.getNumbers(DetailActivity.this.num);
                printPeople(peoples);
            }
        }).start();
    }


    public void getWebText(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                Map<String, String> map = new HashMap<String, String>();

                Document document = null;
                try {
                    document = Jsoup.connect(url).timeout(5000).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Elements e = document.getElementsByTag("script");

                /*循环遍历script下面的JS变量*/
                for (Element element : e) {

                    /*取得JS变量数组*/
                    String[] data = element.data().toString().split("var");

                    /*取得单个JS变量*/
                    for(String variable : data){

                        /*过滤variable为空的数据*/
                        if(variable.contains("=")){

                            /*取到满足条件的JS变量*/
                            if(variable.contains("msg_title") || variable.contains("msg_cdn_url")){

                                String[]  kvp = variable.split("=");

                                /*取得JS变量存入map*/
                                if(!map.containsKey(kvp[0].trim()))
                                    map.put(kvp[0].trim(), kvp[1].trim().substring(0, kvp[1].trim().length()-1).toString());
                            }
                        }
                    }
                }
                Bitmap bitmap = null;
                String desc="";
                if (map.get("msg_cdn_url")!=null){
                    try {
                        URL url = new URL(map.get("msg_cdn_url").substring(1));
                        URLConnection conn = url.openConnection();
                        conn.connect();
                        InputStream in;
                        in = conn.getInputStream();
                        bitmap = BitmapFactory.decodeStream(in);
                        // TODO Auto-generated catch block
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }

                if (map.get("msg_title")!=null){
                    String msgTitle=map.get("msg_title");
                    desc=msgTitle.substring(1,msgTitle.length()-1);
                }

                printResult(desc,bitmap);
            }
        }).start();


    }

    private void printResult(final String desc, final Bitmap bitmap){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                picImage.setImageBitmap(bitmap);
                descView.setText(desc);
            }
        });
    }

    private void printPeople(final int[] peopleList){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                peopleLinearLayout.removeAllViews();
                LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                int width=dip2px(DetailActivity.this,31);
                int padding=dip2px(DetailActivity.this,3);
                LinearLayout.LayoutParams imageParams=new LinearLayout.LayoutParams(width,width);
                imageParams.setMargins(padding,padding,padding,padding);
                for (int i=0;i<peopleList.length+1/8;i++){

                    LinearLayout linearLayout=new LinearLayout(DetailActivity.this);
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    linearLayout.setLayoutParams(layoutParams);

                    for (int j=i*8;j<num&&j<i*8+8;j++){
                        ImageView imageView=new ImageView(DetailActivity.this);
                        imageView.setLayoutParams(imageParams);
                        imageView.setImageResource(peopleList[j]);
                        linearLayout.addView(imageView);
                    }
                    peopleLinearLayout.addView(linearLayout);
                }

                peopleLinearLayout.setLayoutParams(layoutParams);
            }
        });
    }


    public static int dip2px(Context context, float dpValue) {

        final float scale = context.getResources().getDisplayMetrics().density;

        return (int) (dpValue * scale + 0.5f);

    }



}
