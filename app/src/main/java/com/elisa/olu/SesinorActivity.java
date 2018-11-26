package com.elisa.olu;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;

import java.util.ArrayList;
import java.util.List;

import Adapter.ComprarAdapter;
import Adapter.SesionerAdapter;
import ApiObject.TodayBookingObject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Ankit Chhabra on 4/29/2018.
 */

public class SesinorActivity extends GenricActivity {

    @BindView(R.id.comprarRecyclerView)
    RecyclerView comprarRecyclerView;

    @BindView(R.id.text)
    TextView text;

    @BindView(R.id.text2)
    TextView text2;

    List<TodayBookingObject> todayBookingObjectList =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprar_plan);
        ButterKnife.bind(this);
        text.setText("SESIONER");
        text2.setText("RELIZADAS");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        comprarRecyclerView.setLayoutManager(layoutManager);
       // comprarRecyclerView.setAdapter(new SesionerAdapter(this,todayBookingObjectList));
    }

    @OnClick(R.id.backButtonClick)
    public void backButtonClick(View v) {
        this.finish();
    }
}
