package com.elisa.olu;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.webkit.JavascriptInterface;

import com.google.gson.Gson;

import ApiEntity.CategoryEntity;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

class WebAppInterface {
    Context context;

    public WebAppInterface(Context context) {
        this.context = context;
    }

    @JavascriptInterface
    public void closeView() {
        ((WebViewActivity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                WebViewActivity activity = (WebViewActivity) context;
                if(activity.webView.getUrl().contains("https://test.placetopay.com/redirection/cancel")){
                    Intent intent = new Intent();
                    activity.setResult(RESULT_CANCELED, intent);
                    activity.finish();
                }else {
                    Intent intent = new Intent();
                    activity.setResult(RESULT_OK, intent);
                    activity.finish();
                }
            }
        });
    }
}
