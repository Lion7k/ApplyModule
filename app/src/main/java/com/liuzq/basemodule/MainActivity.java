package com.liuzq.basemodule;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.liuzq.basemodule.activity.ErrorActivity;
import com.liuzq.basemodule.activity.NetActivity;

public class MainActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void my_click(View view){
        switch (view.getId()){
            case R.id.net_request:
                NetActivity.startActivity(this);
                break;
            case R.id.error_layout:
                ErrorActivity.startActivity(this);
                break;
        }
    }
}
