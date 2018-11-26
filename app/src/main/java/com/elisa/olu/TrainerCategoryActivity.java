package com.elisa.olu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import Adapter.CategoryListAdapter;
import ApiEntity.CategoryEntity;
import ApiObject.CategoriesListObject;
import ApiObject.TodayBookingObject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import infrastructure.AppCommon;


public class TrainerCategoryActivity extends GenricActivity {

    @BindView(R.id.categoryRecyclerView)
    RecyclerView categoryRecyclerView;

    @BindView(R.id.categorySpinner)
    Spinner categorySpinner;

    @BindView(R.id.addCategoryPopup)
    RelativeLayout addCategoryPopup;

    @BindView(R.id.addCategory)
    ImageView addCategory;

    @BindView(R.id.singlePrice)
    EditText singlePrice;

    @BindView(R.id.singlePrice2)
    EditText singlePrice2;

    @BindView(R.id.singlePrice3)
    EditText singlePrice3;

    @BindView(R.id.singlePrice4)
    EditText singlePrice4;

    @BindView(R.id.companyPrice)
    EditText companyPrice;

    List<CategoriesListObject> categoriesListObjectList = new ArrayList<>();

    String[] categoryList = {"KickBoxing", "Entrenamiento Funcional", "Stretching",
            "Yoga", "Pilates", "Dance Fit", "Gimnasia Adulto", "fisioterapia", "Masajes Deportivos"};

    String[] categoryListId = {"1", "2", "3", "4", "5", "11", "10", "9", "8"};
    CategoryListAdapter categoryListAdapter;

    int selectedIndexForUdateCategoryData = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trainer_category);
        ButterKnife.bind(this);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(arrayAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        categoryRecyclerView.setLayoutManager(layoutManager);
        setAdapter();
        if (getIntent().getStringExtra("category") != null) {
            CategoryEntity categoryEntity = new Gson().fromJson(getIntent().getStringExtra("category"), CategoryEntity.class);
            categoriesListObjectList = categoryEntity.getCategoriesListObjectList();
            setAdapter();
        }

        singlePrice.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    if (!singlePrice.getText().toString().isEmpty() && !singlePrice.getText().toString().trim().contains("."))
                        singlePrice.setText(AppCommon.getInstance(TrainerCategoryActivity.this).getPriceInUSFormat(singlePrice.getText().toString()));

                }
                return false;
            }
        });

        singlePrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {

                } else {
                    if (!singlePrice.getText().toString().isEmpty() && !singlePrice.getText().toString().trim().contains("."))
                        singlePrice.setText(AppCommon.getInstance(TrainerCategoryActivity.this).getPriceInUSFormat(singlePrice.getText().toString()));
                }
            }
        });

        singlePrice2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    if (!singlePrice2.getText().toString().isEmpty())
                        singlePrice2.setText(AppCommon.getInstance(TrainerCategoryActivity.this).getPriceInUSFormat(singlePrice2.getText().toString()));

                }
                return false;
            }
        });

        singlePrice2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {

                } else {
                    if (!singlePrice2.getText().toString().isEmpty() && !singlePrice2.getText().toString().trim().contains("."))
                        singlePrice2.setText(AppCommon.getInstance(TrainerCategoryActivity.this).getPriceInUSFormat(singlePrice2.getText().toString()));
                }
            }
        });

        singlePrice3.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    if (!singlePrice3.getText().toString().isEmpty())
                        singlePrice3.setText(AppCommon.getInstance(TrainerCategoryActivity.this).getPriceInUSFormat(singlePrice3.getText().toString()));

                }
                return false;
            }
        });

        singlePrice3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {

                } else {
                    if (!singlePrice3.getText().toString().isEmpty() && !singlePrice3.getText().toString().trim().contains("."))
                        singlePrice3.setText(AppCommon.getInstance(TrainerCategoryActivity.this).getPriceInUSFormat(singlePrice3.getText().toString()));
                }
            }
        });
        singlePrice4.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    if (!singlePrice4.getText().toString().isEmpty())
                        singlePrice4.setText(AppCommon.getInstance(TrainerCategoryActivity.this).getPriceInUSFormat(singlePrice4.getText().toString()));

                }
                return false;
            }
        });

        singlePrice4.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {

                } else {
                    if (!singlePrice4.getText().toString().isEmpty() && !singlePrice4.getText().toString().trim().contains("."))
                        singlePrice4.setText(AppCommon.getInstance(TrainerCategoryActivity.this).getPriceInUSFormat(singlePrice4.getText().toString()));
                }
            }
        });

        companyPrice.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    if (!companyPrice.getText().toString().isEmpty())
                        companyPrice.setText(AppCommon.getInstance(TrainerCategoryActivity.this).getPriceInUSFormat(companyPrice.getText().toString()));

                }
                return false;
            }
        });

        companyPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {

                } else {
                    if (!companyPrice.getText().toString().isEmpty() && !companyPrice.getText().toString().trim().contains("."))
                        companyPrice.setText(AppCommon.getInstance(TrainerCategoryActivity.this).getPriceInUSFormat(companyPrice.getText().toString()));
                }
            }
        });
    }

    void setAdapter() {
        categoryListAdapter = new CategoryListAdapter(this, categoriesListObjectList);
        categoryRecyclerView.setAdapter(categoryListAdapter);
    }

    @OnClick(R.id.backButtonClick)
    public void backButtonClick(View v) {
        if (categoriesListObjectList.size() == 0) {
            setResult(RESULT_CANCELED);
            finish();
        } else {
            if (!this.isFinishing()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setMessage("¿Seguro que quieres salir sin guardar las categorías?");
                builder.setCancelable(false);
                builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                });
                builder.setPositiveButton(getResources().getString(R.string.save), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        doneButton();
                    }
                });
                builder.show();
            }
        }

    }

    @OnClick(R.id.addCategory)
    public void setAddCategory() {
        addCategoryPopup.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.addCategoryPopup)
    public void setAddCategoryPopup() {
        addCategoryPopup.setVisibility(View.GONE);
    }

    @OnClick(R.id.doneButton)
    public void doneButton() {
        if (categoriesListObjectList.size() == 0) {
            Toast.makeText(this, "Por favor agrega al menos una Categoría", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent();
            intent.putExtra("category", new Gson().toJson(new CategoryEntity(categoriesListObjectList)));
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @OnClick(R.id.addCategoryBtn)
    public void addCategoryBtn() {
        String single = singlePrice.getText().toString().trim();
        String single2 = singlePrice2.getText().toString().trim();
        String single3 = singlePrice3.getText().toString().trim();
        String single4 = singlePrice4.getText().toString().trim();
        String company = companyPrice.getText().toString().trim();

        if (!single.contains(".")) {
            single = AppCommon.getInstance(this).getPriceInUSFormat(single);
        }
        if (!single2.contains(".")) {
            single2 = AppCommon.getInstance(this).getPriceInUSFormat(single2);
        }
        if (!single3.contains(".")) {
            single3 = AppCommon.getInstance(this).getPriceInUSFormat(single3);
        }
        if (!single4.contains(".")) {
            single4 = AppCommon.getInstance(this).getPriceInUSFormat(single4);
        }
        if (!company.contains(".")) {
            company = AppCommon.getInstance(this).getPriceInUSFormat(company);
        }

        if (!isCategoryAlreadyAdded(categoryListId[categorySpinner.getSelectedItemPosition()])) {
            CategoriesListObject categoriesListObject = new CategoriesListObject(categorySpinner.getSelectedItem().toString(), categoryListId[categorySpinner.getSelectedItemPosition()], single, single2, single3, single4, company);
            categoriesListObjectList.add(categoriesListObject);
            setAdapter();
            addCategoryPopup.setVisibility(View.GONE);
            categorySpinner.setSelection(0);
            singlePrice.setText("");
            singlePrice2.setText("");
            singlePrice3.setText("");
            singlePrice4.setText("");
            companyPrice.setText("");
        } else {
            if (selectedIndexForUdateCategoryData != -1) {
                CategoriesListObject obj = categoriesListObjectList.get(selectedIndexForUdateCategoryData);
                obj.setCompanyPrice(company);
                obj.setSinglePrice(single);
                obj.setGroupPrice2(single2);
                obj.setGroupPrice3(single3);
                obj.setGroupPrice4(single4);
                addCategoryPopup.setVisibility(View.GONE);
                singlePrice.setText("");
                singlePrice2.setText("");
                singlePrice3.setText("");
                singlePrice4.setText("");
                companyPrice.setText("");

                selectedIndexForUdateCategoryData = -1;
            }
        }
    }

    public boolean isCategoryAlreadyAdded(String catID) {
        boolean isCatExit = false;
        for (int i = 0; i < categoriesListObjectList.size(); i++) {
            CategoriesListObject obj = categoriesListObjectList.get(i);
            String categryID = obj.getCategryID();
            if (categryID.equals(catID)) {
                isCatExit = true;
                break;
            }
        }
        return isCatExit;
    }

    public void editCategoryInfo(int position) {
        selectedIndexForUdateCategoryData = position;
        CategoriesListObject obj = categoriesListObjectList.get(position);

        int index = 0;
        for (int i = 0; i < categoryList.length; i++) {
            String categoryName = categoryList[i];
            if (categoryName.equals(obj.getCatergoryName())) {
                index = i;
                break;
            }
        }
        categorySpinner.setSelection(index);
        singlePrice.setText(obj.getSinglePrice());
        singlePrice2.setText(obj.getGroupPrice2());
        singlePrice3.setText(obj.getGroupPrice3());
        singlePrice4.setText(obj.getGroupPrice4());
        companyPrice.setText(obj.getCompanyPrice());
        addCategoryPopup.setVisibility(View.VISIBLE);
    }
}
