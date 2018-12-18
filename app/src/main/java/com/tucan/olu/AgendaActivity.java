package com.tucan.olu;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import Adapter.SesionerAdapter;
import ApiObject.UserListObject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AgendaActivity extends GenricActivity {

    @BindView(R.id.comprarRecyclerView)
    RecyclerView comprarRecyclerView;

    @BindView(R.id.text)
    TextView text;

    @BindView(R.id.text2)
    TextView text2;

    @BindView(R.id.noDataFound)
    TextView noDataFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprar_plan);
        ButterKnife.bind(this);
        text.setText("SESIONER");
        text2.setText("AGENDADAS");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        comprarRecyclerView.setLayoutManager(layoutManager);
        if (getIntent().getStringExtra("userObject") != null) {
            UserListObject userListObject = new Gson().fromJson(getIntent().getStringExtra("userObject"), UserListObject.class);
            comprarRecyclerView.setAdapter(new SesionerAdapter(this, userListObject.getBookingDetailObjectList(), "3",false));
            if (userListObject.getBookingDetailObjectList().size() == 0) {
                noDataFound.setVisibility(View.VISIBLE);
            } else {
                noDataFound.setVisibility(View.GONE);
            }
        }


    }

    @OnClick(R.id.backButtonClick)
    public void backButtonClick(View v) {
        this.finish();
    }
}
