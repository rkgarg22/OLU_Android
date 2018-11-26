package com.elisa.olu;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;

import Adapter.AgendaAdapter;
import Adapter.ConfimarAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Ankit Chhabra on 4/29/2018.
 */

public class ConfirmarActivity extends GenricActivity {

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
        text.setText("SESIONER");
        text2.setText("POR CONFIRMAR");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        comprarRecyclerView.setLayoutManager(layoutManager);
        comprarRecyclerView.setAdapter(new ConfimarAdapter(this));
    }

    @OnClick(R.id.backButtonClick)
    public void backButtonClick(View v) {
        this.finish();
    }
}