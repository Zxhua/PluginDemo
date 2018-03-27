package code.zxhua.hookamsdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import code.zxhua.hookamsdemo.hook.HookExtension;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), HookActivity.class);
            intent.putExtra(HookExtension.UNREGISTERED,true);
            startActivity(intent);
        });


    }
}
