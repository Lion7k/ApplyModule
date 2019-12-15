package com.liuzq.basemodule.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.liuzq.addsubtractview.AmountView;
import com.liuzq.basemodule.R;
import com.liuzq.basemodule.ui.activity.base.CommActivity;

import butterknife.BindView;

public class AddSubtractActivity extends CommActivity {

    @BindView(R.id.amount_view)
    AmountView mAmountView;

    @Override
    public void initData(Bundle bundle) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_add_subtract;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void doBusiness() {
        mAmountView.setGoods_storage(5);
        mAmountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                Toast.makeText(getApplicationContext(), "Amount=>  " + amount, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
