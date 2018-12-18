package com.tucan.olu.TutorialFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tucan.olu.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ankit Chhabra on 4/29/2018.
 */

public class TutorialFragment3 extends Fragment {

    @BindView(R.id.parentRow)
    LinearLayout parentRow;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tutorial_fragment, container, false);
        ButterKnife.bind(this, view);
        parentRow.setBackgroundResource(R.drawable.t3);
        return view;
    }
}

