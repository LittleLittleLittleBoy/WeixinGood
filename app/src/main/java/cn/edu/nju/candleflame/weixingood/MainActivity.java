package cn.edu.nju.candleflame.weixingood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Button button=findViewById(R.id.main_to_detail_button);
        final EditText urlEditText=findViewById(R.id.main_url);
        final EditText numberEditText=findViewById(R.id.main_number);
        final EditText dateText=findViewById(R.id.main_date);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number=numberEditText.getText().toString();
                String url=urlEditText.getText().toString();

                if (number==null||url==null||number.equals("")||url.equals("")){
                    Toast.makeText(MainActivity.this,"参数不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                Pattern pattern = Pattern.compile("(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]");
                Matcher matcher = pattern.matcher(url);
                if (!matcher.matches()){
                    Toast.makeText(MainActivity.this,"链接不正确",Toast.LENGTH_SHORT).show();
                    return;
                }
                int num=0;
                try {
                    num=Integer.valueOf(number);//把字符串强制转换为数字
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this,"请输入数字",Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent=new Intent(MainActivity.this,DetailActivity.class);
                intent.putExtra("url",url);
                intent.putExtra("num",num);
                intent.putExtra("date",dateText.getText().toString());
                startActivity(intent);
            }
        });
    }
}
