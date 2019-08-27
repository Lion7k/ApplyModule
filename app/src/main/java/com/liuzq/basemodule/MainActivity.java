package com.liuzq.basemodule;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.liuzq.basemodule.ui.activity.FragmentActivity;
import com.liuzq.basemodule.ui.activity.MultipleLayoutActivity;
import com.liuzq.basemodule.ui.activity.NetActivity;
import com.liuzq.basemodule.ui.activity.RefreshActivity;

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
                MultipleLayoutActivity.startActivity(this);
                break;
            case R.id.refresh_layout:
                RefreshActivity.startActivity(this);
                break;
            case R.id.fragment_layout:
                FragmentActivity.startActivity(this);
                break;
        }
    }
}
