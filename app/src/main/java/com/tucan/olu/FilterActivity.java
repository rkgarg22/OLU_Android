package com.tucan.olu;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FilterActivity extends GenricActivity {

    @BindView(R.id.boy)
    ImageView boy;

    @BindView(R.id.girl)
    ImageView girl;

    @BindView(R.id.clearFilter)
    ImageView clearFilter;

    @BindView(R.id.confirmBtn)
    Button confirmBtn;

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

    @BindView(R.id.fisioterapiaTextView)
    TextView fisioterapiaTextView;

    @BindView(R.id.gimnaTextView)
    TextView gimnaTextView;

    @BindView(R.id.masajesTextView)
    TextView masajesTextView;

    String gender = "";
    String categoryId = "";

    boolean isBoySlected = false;
    boolean isGirlSelected = false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        ButterKnife.bind(this);
        if (getIntent().getStringExtra("gender") != null) {
            gender = getIntent().getStringExtra("gender");
            categoryId = getIntent().getStringExtra("categoryId");
            if (gender.equals("Hombre")) {
                isBoySlected = true;
                boy.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorAccent));
                girl.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.greyColor));
            } else if (gender.equals("Mujer")) {
                isGirlSelected = true;
                boy.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.greyColor));
                girl.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorAccent));
            }
            setupCatergoriesId(categoryId);
        }
    }

    private void setupCatergoriesId(String categoryId) {
        switch (categoryId) {
            case "1":
                plusicon.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent));
                kickboxTextView.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                break;
            case "2":
                cardioPlusicon.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent));
                cardioTextView.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                break;
            case "3":
                stretchingPlusicon.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent));
                stretchingTextView.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                break;
            case "4":
                yogaPlusicon.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent));
                yogaTextView.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                break;
            case "5":
                pilatesPlusicon.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent));
                pilatesTextView.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                break;
            case "11":
                danzaFitPlusIcon.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent));
                danzaFitTextView.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                break;
            case "10":
                gimnaPlusIcon.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent));
                gimnaTextView.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                break;
            case "9":
                fisioterapiaPlusicon.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent));
                fisioterapiaTextView.setTextColor(ContextCompat.getColor(this, R.color.textColor));
                break;
            case "8":
                masajesPlusicon.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent));
                masajesTextView.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                break;
        }
    }

    @OnClick(R.id.backButtonClick)
    public void backButtonClick(View v) {
        this.finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick(R.id.boy)
    void setBoy() {
        if (isBoySlected) {
            isBoySlected = false;
            boy.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.greyColor));
        } else {
            isBoySlected = true;
            boy.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorAccent));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick(R.id.girl)
    void setGirl() {
        if (isGirlSelected) {
            isGirlSelected = false;
            girl.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.greyColor));
        } else {
            isGirlSelected = true;
            girl.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorAccent));
        }
    }

    @OnClick(R.id.confirmBtn)
    void setConfirmBtn() {
        if (isGirlSelected && isBoySlected) {
            gender = "";
        }else if(isGirlSelected){
            gender = "Mujer";
        }else if(isBoySlected){
            gender = "Hombre";
        }
        Intent intent = new Intent();
        intent.putExtra("gender", gender);
        intent.putExtra("categoryId", categoryId);
        setResult(RESULT_OK, intent);
        finish();
    }

    @OnClick(R.id.clearFilter)
    void setClearFilter() {
        Intent intent = new Intent();
        intent.putExtra("gender", "");
        intent.putExtra("categoryId", "");
        setResult(RESULT_OK, intent);
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick(R.id.plusicon)
    public void lickBoxingPlusClick(View v) {
        if (categoryId != null) {
            String lastCategoryId = categoryId;
            unselectCategories(lastCategoryId);
        }
        categoryId = "1";
        plusicon.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent));
        kickboxTextView.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick(R.id.cardioPlusicon)
    public void cardioPlusClick(View v) {
        if (categoryId != null) {
            String lastCategoryId = categoryId;
            unselectCategories(lastCategoryId);
        }
        categoryId = "2";
        cardioPlusicon.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent));
        cardioTextView.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick(R.id.stretchingPlusicon)
    public void stretchingPlusClick(View v) {
        if (categoryId != null) {
            String lastCategoryId = categoryId;
            unselectCategories(lastCategoryId);
        }
        categoryId = "3";
        stretchingPlusicon.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent));
        stretchingTextView.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick(R.id.yogaPlusicon)
    public void yogaPlusClick(View v) {
        if (categoryId != null) {
            String lastCategoryId = categoryId;
            unselectCategories(lastCategoryId);
        }
        categoryId = "4";
        yogaPlusicon.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent));
        yogaTextView.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick(R.id.pilatesPlusicon)
    public void pilatesPlusClick(View v) {
        if (categoryId != null) {
            String lastCategoryId = categoryId;
            unselectCategories(lastCategoryId);
        }
        categoryId = "5";
        pilatesPlusicon.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent));
        pilatesTextView.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick(R.id.danzaFitPlusIcon)
    public void danzaFitPlusClick(View v) {
        if (categoryId != null) {
            String lastCategoryId = categoryId;
            unselectCategories(lastCategoryId);
        }
        categoryId = "11";
        danzaFitPlusIcon.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent));
        danzaFitTextView.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick(R.id.fisioterapiaPlusicon)
    public void fisioterapiaPlusClick(View v) {
        if (categoryId != null) {
            String lastCategoryId = categoryId;
            unselectCategories(lastCategoryId);
        }
        categoryId = "9";
        fisioterapiaPlusicon.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent));
        fisioterapiaTextView.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick(R.id.gimnaPlusIcon)
    public void gimnaPlusIcon(View v) {
        if (categoryId != null) {
            String lastCategoryId = categoryId;
            unselectCategories(lastCategoryId);
        }
        categoryId = "10";
        gimnaPlusIcon.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent));
        gimnaTextView.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick(R.id.masajesPlusicon)
    public void masajesPlusicon(View v) {
        if (categoryId != null) {
            String lastCategoryId = categoryId;
            unselectCategories(lastCategoryId);
        }
        categoryId = "8";
        masajesPlusicon.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent));
        masajesTextView.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
    }

    private void unselectCategories(String lastCategoryId) {
        switch (lastCategoryId) {
            case "1":
                plusicon.setColorFilter(ContextCompat.getColor(this, R.color.greyColor));
                kickboxTextView.setTextColor(ContextCompat.getColor(this, R.color.textColor));
                break;
            case "2":
                cardioPlusicon.setColorFilter(ContextCompat.getColor(this, R.color.greyColor));
                cardioTextView.setTextColor(ContextCompat.getColor(this, R.color.textColor));
                break;
            case "3":
                stretchingPlusicon.setColorFilter(ContextCompat.getColor(this, R.color.greyColor));
                stretchingTextView.setTextColor(ContextCompat.getColor(this, R.color.textColor));
                break;
            case "4":
                yogaPlusicon.setColorFilter(ContextCompat.getColor(this, R.color.greyColor));
                yogaTextView.setTextColor(ContextCompat.getColor(this, R.color.textColor));
                break;
            case "5":
                pilatesPlusicon.setColorFilter(ContextCompat.getColor(this, R.color.greyColor));
                pilatesTextView.setTextColor(ContextCompat.getColor(this, R.color.textColor));
                break;
            case "11":
                danzaFitPlusIcon.setColorFilter(ContextCompat.getColor(this, R.color.greyColor));
                danzaFitTextView.setTextColor(ContextCompat.getColor(this, R.color.textColor));
                break;
            case "10":
                gimnaPlusIcon.setColorFilter(ContextCompat.getColor(this, R.color.greyColor));
                gimnaTextView.setTextColor(ContextCompat.getColor(this, R.color.textColor));
                break;
            case "9":
                fisioterapiaPlusicon.setColorFilter(ContextCompat.getColor(this, R.color.greyColor));
                fisioterapiaTextView.setTextColor(ContextCompat.getColor(this, R.color.textColor));
                break;
            case "8":
                masajesPlusicon.setColorFilter(ContextCompat.getColor(this, R.color.greyColor));
                masajesTextView.setTextColor(ContextCompat.getColor(this, R.color.textColor));
                break;
        }
    }
}
