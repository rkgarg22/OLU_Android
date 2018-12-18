package com.tucan.olu;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import Adapter.FAQAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Ankit Chhabra on 4/29/2018.
 */

public class FAQActivity extends GenricActivity {

    @BindView(R.id.comprarRecyclerView)
    RecyclerView comprarRecyclerView;

    @BindView(R.id.text)
    TextView text;

    @BindView(R.id.text2)
    TextView text2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprar_plan);
        ButterKnife.bind(this);
        text.setText("FAQ");
        text2.setText("");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        comprarRecyclerView.setLayoutManager(layoutManager);
        comprarRecyclerView.setAdapter(new FAQAdapter(this));
    }

    @OnClick(R.id.backButtonClick)
    public void backButtonClick(View v) {
        this.finish();
    }
}
