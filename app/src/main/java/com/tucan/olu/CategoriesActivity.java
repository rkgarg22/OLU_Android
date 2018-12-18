package com.tucan.olu;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;

import java.util.ArrayList;

import Adapter.CategoriesAdapter;
import CustomControl.AvenirNextCondensedRegularTextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import infrastructure.AppCommon;

public class CategoriesActivity extends GenricActivity {

    @BindView(R.id.categoryRecyclerView)
    RecyclerView categoryRecyclerView;

    @BindView(R.id.kickboxTextView)
    TextView kickboxTextView;

    @BindView(R.id.cardioTextView)
    TextView cardioTextView;

    @BindView(R.id.stretchingTextView)
    TextView stretchingTextView;

    @BindView(R.id.yogaTextView)
    TextView yogaTextView;

    @BindView(R.id.pilatesTextView)
    TextView pilatesTextView;

    @BindView(R.id.danzaFitTextView)
    TextView danzaFitTextView;

    @BindView(R.id.gimnaTextView)
    TextView gimnaTextView;

    @BindView(R.id.fisioterapiaTextView)
    TextView fisioterapiaTextView;

    @BindView(R.id.masajesTextView)
    TextView masajesTextView;

    @BindView(R.id.kickboxDescriptionTextview)
    AvenirNextCondensedRegularTextView kickboxDescriptionTextview;

    @BindView(R.id.cardioDescTextView)
    AvenirNextCondensedRegularTextView cardioDescTextView;

    @BindView(R.id.stretchingDescriptionTextview)
    AvenirNextCondensedRegularTextView stretchingDescriptionTextview;

    @BindView(R.id.yogaDescriptionTextview)
    AvenirNextCondensedRegularTextView yogaDescriptionTextview;

    @BindView(R.id.pilatesDescriptionTextview)
    AvenirNextCondensedRegularTextView pilatesDescriptionTextview;

    @BindView(R.id.danzaFitDescriptionTextview)
    AvenirNextCondensedRegularTextView danzaFitDescriptionTextview;

    @BindView(R.id.fisioterapiaDescriptionTextview)
    AvenirNextCondensedRegularTextView fisioterapiaDescriptionTextview;

    @BindView(R.id.masajesDescriptionTextview)
    AvenirNextCondensedRegularTextView masajesDescriptionTextview;

    @BindView(R.id.gimnaDescriptionTextview)
    AvenirNextCondensedRegularTextView gimnaDescriptionTextview;


    @BindView(R.id.plusicon)
    ImageView plusicon;

    @BindView(R.id.cardioPlusicon)
    ImageView cardioPlusicon;

    @BindView(R.id.stretchingPlusicon)
    ImageView stretchingPlusicon;

    @BindView(R.id.yogaPlusicon)
    ImageView yogaPlusicon;

    @BindView(R.id.pilatesPlusicon)
    ImageView pilatesPlusicon;

    @BindView(R.id.danzaFitPlusIcon)
    ImageView danzaFitPlusIcon;

    @BindView(R.id.fisioterapiaPlusicon)
    ImageView fisioterapiaPlusicon;

    @BindView(R.id.gimnaPlusIcon)
    ImageView gimnaPlusIcon;

    @BindView(R.id.masajesPlusicon)
    ImageView masajesPlusicon;

    @BindView(R.id.scrollView)
    ScrollView scrollView;

    CategoriesAdapter categoriesAdapter;

    int lastPosition;
    int selectedPosition = -1;

    String[] categoryList = new String[]{"0", "0", "0", "0", "0", "0", "0", "0", "0"};

    ArrayList<AvenirNextCondensedRegularTextView> descTextViewArrayList = new ArrayList<AvenirNextCondensedRegularTextView>();
    ArrayList<TextView> titleTextViewArrayList = new ArrayList<TextView>();
    ArrayList<ImageView> plusBtnArray = new ArrayList<ImageView>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        ButterKnife.bind(this);

        AppCommon.getInstance(this).updateAnalytics("ACTIVIDADES SCREEN");
        setUpDescArrayList();
        final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL);
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());

        layoutManager.setMaxVisibleItems(2);
        layoutManager.offsetChildrenHorizontal(50);
        categoryRecyclerView.setLayoutManager(layoutManager);
        categoryRecyclerView.setHasFixedSize(true);
        //categoryRecyclerView.addOnScrollListener(new CenterScrollListener());
        categoriesAdapter = new CategoriesAdapter(this, categoryList);
        categoryRecyclerView.setAdapter(categoriesAdapter);

        if (getIntent().getStringExtra("isMenu") != null) {
            String isMenu = getIntent().getStringExtra("isMenu");
            if (isMenu.equals("1")) {
                kickboxDescriptionTextview.setVisibility(View.VISIBLE);
                cardioDescTextView.setVisibility(View.VISIBLE);
                stretchingDescriptionTextview.setVisibility(View.VISIBLE);
                yogaDescriptionTextview.setVisibility(View.VISIBLE);
                pilatesDescriptionTextview.setVisibility(View.VISIBLE);
                danzaFitDescriptionTextview.setVisibility(View.VISIBLE);
                fisioterapiaDescriptionTextview.setVisibility(View.VISIBLE);
                gimnaDescriptionTextview.setVisibility(View.VISIBLE);
                masajesDescriptionTextview.setVisibility(View.VISIBLE);
            }
        }


        categoryRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (RecyclerView.SCROLL_STATE_IDLE == recyclerView.getScrollState()) {
                    final CarouselLayoutManager lm = (CarouselLayoutManager) recyclerView.getLayoutManager();
                    final int scrollNeeded = lm.getCenterItemPosition();
                    if (selectedPosition != scrollNeeded) {
                        setOnselected(scrollNeeded);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }


        });
    }

    public void setUpDescArrayList() {
        descTextViewArrayList.add(kickboxDescriptionTextview);
        descTextViewArrayList.add(cardioDescTextView);
        descTextViewArrayList.add(stretchingDescriptionTextview);
        descTextViewArrayList.add(yogaDescriptionTextview);
        descTextViewArrayList.add(pilatesDescriptionTextview);
        descTextViewArrayList.add(danzaFitDescriptionTextview);
        descTextViewArrayList.add(gimnaDescriptionTextview);
        descTextViewArrayList.add(fisioterapiaDescriptionTextview);
        descTextViewArrayList.add(masajesDescriptionTextview);

        titleTextViewArrayList.add(kickboxTextView);
        titleTextViewArrayList.add(cardioTextView);
        titleTextViewArrayList.add(stretchingTextView);
        titleTextViewArrayList.add(yogaTextView);
        titleTextViewArrayList.add(pilatesTextView);
        titleTextViewArrayList.add(danzaFitTextView);
        titleTextViewArrayList.add(gimnaTextView);
        titleTextViewArrayList.add(fisioterapiaTextView);
        titleTextViewArrayList.add(masajesTextView);

        plusBtnArray.add(plusicon);
        plusBtnArray.add(cardioPlusicon);
        plusBtnArray.add(stretchingPlusicon);
        plusBtnArray.add(yogaPlusicon);
        plusBtnArray.add(pilatesPlusicon);
        plusBtnArray.add(danzaFitPlusIcon);
        plusBtnArray.add(fisioterapiaPlusicon);
        plusBtnArray.add(gimnaPlusIcon);
        plusBtnArray.add(masajesPlusicon);


    }

    @OnClick(R.id.backButtonClick)
    public void backButtonClick(View v) {
        this.finish();
    }

    @OnClick(R.id.plusicon)
    public void lickBoxingPlusClick(View v) {
        setOnselected(0);
    }

    @OnClick(R.id.cardioPlusicon)
    public void cardioPlusClick(View v) {
        setOnselected(1);
    }

    @OnClick(R.id.stretchingPlusicon)
    public void stretchingPlusClick(View v) {
        setOnselected(2);
    }

    @OnClick(R.id.yogaPlusicon)
    public void yogaPlusClick(View v) {
        setOnselected(3);
    }

    @OnClick(R.id.pilatesPlusicon)
    public void pilatesPlusClick(View v) {
        setOnselected(4);
    }

    @OnClick(R.id.danzaFitPlusIcon)
    public void danzaFitPlusClick(View v) {
        setOnselected(5);
    }

    @OnClick(R.id.fisioterapiaPlusicon)
    public void fisioterapiaPlusClick(View v) {
        setOnselected(7);
    }

    @OnClick(R.id.gimnaPlusIcon)
    public void gimnaPlusIcon(View v) {
        setOnselected(6);
    }

    @OnClick(R.id.masajesPlusicon)
    public void masajesPlusicon(View v) {
        setOnselected(8);
    }

    @OnClick(R.id.kickBoxReservarBtn)
    public void kickBoxReservarbtn(View v) {
        Intent intent = new Intent(this, ReservarActivity.class);
        intent.putExtra("categoryId", "1");
        startActivity(intent);
    }

    @OnClick(R.id.functionalReservarBtn)
    public void functionalReservarBtn(View v) {
        Intent intent = new Intent(this, ReservarActivity.class);
        intent.putExtra("categoryId", "2");
        startActivity(intent);
    }

    @OnClick(R.id.stretchingReservarBtn)
    public void stretchingReservarBtn(View v) {
        Intent intent = new Intent(this, ReservarActivity.class);
        intent.putExtra("categoryId", "3");
        startActivity(intent);
    }

    @OnClick(R.id.yogaReservarBtn)
    public void yogaReservarBtn(View v) {
        Intent intent = new Intent(this, ReservarActivity.class);
        intent.putExtra("categoryId", "4");
        startActivity(intent);
    }

    @OnClick(R.id.gimnasiaReservarBtn)
    public void gimnasiaReservarBtn(View v) {
        Intent intent = new Intent(this, ReservarActivity.class);
        intent.putExtra("categoryId", "10");
        startActivity(intent);
    }

    @OnClick(R.id.masajesReservarBtn)
    public void masajesReservarBtn(View v) {
        Intent intent = new Intent(this, ReservarActivity.class);
        intent.putExtra("categoryId", "8");
        startActivity(intent);
    }

    @OnClick(R.id.fisioterpiaReservarBtn)
    public void fisioterpiaReservarBtn(View v) {
        Intent intent = new Intent(this, ReservarActivity.class);
        intent.putExtra("categoryId", "9");
        startActivity(intent);
    }

    @OnClick(R.id.danceReservarBtn)
    public void danceReservarBtn(View v) {
        Intent intent = new Intent(this, ReservarActivity.class);
        intent.putExtra("categoryId", "11");
        startActivity(intent);
    }

    @OnClick(R.id.pilatesReservarBtn)
    public void pilatesReservarBtn(View v) {
        Intent intent = new Intent(this, ReservarActivity.class);
        intent.putExtra("categoryId", "5");
        startActivity(intent);
    }


    public void setOnselected(int adapterPosition) {
        selectedPosition = adapterPosition;
        categoryRecyclerView.smoothScrollToPosition(adapterPosition);
        categoryList[lastPosition] = "0";
        categoryList[adapterPosition] = "1";
        lastPosition = adapterPosition;
        categoriesAdapter.notifyDataSetChanged();
        openDescription(adapterPosition);
    }

    private void openDescription(int adapterPosition) {
        TextView titleTextView = null;
        for (int i = 0; i < descTextViewArrayList.size(); i++) {
            AvenirNextCondensedRegularTextView textView = descTextViewArrayList.get(i);
            ImageView view = plusBtnArray.get(i);
            if (i == adapterPosition) {
                if (textView.getVisibility() == View.GONE) {
                    view.setImageResource(R.drawable.minus);
                    textView.setVisibility(View.VISIBLE);
                    titleTextView = titleTextViewArrayList.get(i);
                    //scrollView.smoothScrollTo(0, (int)tv.getY());
                } else {
                    view.setImageResource(R.drawable.plus_icon);
                    textView.setVisibility(View.GONE);
                }
            } else {
                view.setImageResource(R.drawable.plus_icon);
                textView.setVisibility(View.GONE);
            }



        }
        if (titleTextView != null) {
            focusOnView(titleTextView, adapterPosition);
            // scrollView.scrollTo(0,titleTextView.getTop());
            //scrollToView(scrollView, titleTextView);
        }
    }

    private final void focusOnView(final TextView titleTextView, final int pos) {
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, titleTextView.getTop());
                if (pos >= 5) {
                    scrollView.fullScroll(View.FOCUS_DOWN);
                }
            }
        });
    }

    private void scrollToView(final ScrollView scrollViewParent, final View view) {
        // Get deepChild Offset
        Point childOffset = new Point();
        getDeepChildOffset(scrollViewParent, view.getParent(), view, childOffset);
        // Scroll to child.
        scrollViewParent.smoothScrollTo(0, childOffset.y);
    }

    private void getDeepChildOffset(final ViewGroup mainParent, final ViewParent parent, final View child, final Point accumulatedOffset) {
        ViewGroup parentGroup = (ViewGroup) parent;
        accumulatedOffset.x += child.getLeft();
        accumulatedOffset.y += child.getTop();
        if (parentGroup.equals(mainParent)) {
            return;
        }
        getDeepChildOffset(mainParent, parentGroup.getParent(), parentGroup, accumulatedOffset);
    }

}