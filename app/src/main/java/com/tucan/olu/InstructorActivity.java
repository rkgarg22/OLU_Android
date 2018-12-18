package com.tucan.olu;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;

import Adapter.InstructorAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Ankit Chhabra on 4/25/2018.
 */

public class InstructorActivity extends GenricActivity {

    @BindView(R.id.instructorRecyclerView)
    RecyclerView instructorRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_instructor);
        ButterKnife.bind(this);

        final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL);
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        //layoutManager.setMaxVisibleItems(4);
        layoutManager.offsetChildrenHorizontal(50);
        instructorRecyclerView.setLayoutManager(layoutManager);
        instructorRecyclerView.setHasFixedSize(true);
        //categoryRecyclerView.addOnScrollListener(new CenterScrollListener());

        instructorRecyclerView.setAdapter(new InstructorAdapter(this));
    }

    @OnClick(R.id.backButtonClick)
    public void backButtonClick(View v) {
        this.finish();
    }

}
