package com.elisa.olu;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import API.PretoAppService;
import API.ServiceGenerator;
import APIResponse.CommonIntResponse;
import Adapter.ChatAdapter;
import ApiEntity.SendMessageEntity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import ApiObject.ChatObject;
import ApiObject.ChatResponse;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import infrastructure.AppCommon;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class ChatScreenActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 0;

    private static final int SELECT_FILE = 1;

    private static final int PERMISSION_REQUEST_CODE = 100;

    @BindView(R.id.backButtonClick)
    ImageView imageViewLeft;

    @BindView(R.id.titleText)
    TextView titleText;

    @BindView(R.id.chatRecyclerView)
    RecyclerView chatRecyclerView;

    @BindView(R.id.messageBox)
    EditText editTextMessage;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.topProgressBar)
    ProgressBar topProgressBar;

    @BindView(R.id.noDataFound)
    TextView noDataFound;

    private List<ChatObject> chatObjectList = new ArrayList<>();

    ChatAdapter chatAdapter;

    Call call;

    Uri uri;

    Bitmap bm;

    String anthorUserId;

    String name;

    int offset = 0;

    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_screen_layout);
        ButterKnife.bind(this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        //layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setStackFromEnd(true);
        chatRecyclerView.setLayoutManager(layoutManager);
        anthorUserId = getIntent().getExtras().getString("AnotherUserID");
        name = getIntent().getExtras().getString("name");
        titleText.setText(name);
        progressBar.setVisibility(View.VISIBLE);
        getChatThread(anthorUserId);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(
                "com.received.message");
        mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                getChatThread(anthorUserId);
            }
        };
        //registering our receiver
        this.registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        //unregister our receiver
        this.unregisterReceiver(this.mReceiver);
    }

    private void getChatThread(String anthorUserId) {
        AppCommon.getInstance(this).setNonTouchableFlags(this);
        if (AppCommon.getInstance(this).isConnectingToInternet(this)) {
            int userId = AppCommon.getInstance(this).getUserID();
            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            call = pretoAppService.messagesList(AppCommon.getInstance(this).getUserID(), anthorUserId);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(ChatScreenActivity.this).clearNonTouchableFlags(ChatScreenActivity.this);
                    progressBar.setVisibility(View.GONE);
                    topProgressBar.setVisibility(View.GONE);
                    ChatResponse chatResponse = (ChatResponse) response.body();
                    if (chatResponse.getSuccess() == 1) {
                        chatObjectList.clear();
                        chatObjectList.addAll(chatResponse.getChatObjectList());
                        setupHeader();
                        chatAdapter = new ChatAdapter(chatObjectList, ChatScreenActivity.this);
                        chatRecyclerView.setAdapter(chatAdapter);
                        chatRecyclerView.scrollToPosition(0);
                        noDataFound.setVisibility(View.GONE);
                    } else {
                        noDataFound.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(ChatScreenActivity.this).clearNonTouchableFlags(ChatScreenActivity.this);
                    progressBar.setVisibility(View.GONE);
                    topProgressBar.setVisibility(View.GONE);
                    AppCommon.getInstance(ChatScreenActivity.this).showDialog(ChatScreenActivity.this, getResources().getString(R.string.network_error));
                }
            });


        } else {
            AppCommon.getInstance(ChatScreenActivity.this).clearNonTouchableFlags(ChatScreenActivity.this);
            topProgressBar.setVisibility(View.GONE);
            AppCommon.getInstance(ChatScreenActivity.this).showDialog(ChatScreenActivity.this, getResources().getString(R.string.network_error));
        }

    }

//    private void setupHeader() {
//        for (int i = 0; i < chatObjectList.size(); i++) {
//            chatObjectList.get(i).setIsHeader("0");
//            DateFormat formatterTime = new SimpleDateFormat("HH:mm", Locale.US);
//            formatterTime.setTimeZone(TimeZone.getTimeZone("UTC"));
//            String time = formatterTime.format(new Date(chatObjectList.get(i).getMessageTime()));
//            chatObjectList.get(i).setTime(time);
//
//            SimpleDateFormat formatterDate = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTimeInMillis(Long.parseLong(chatObjectList.get(i).getMessageTime()));
//            String date = formatterDate.format(calendar.getTime());
//            chatObjectList.get(i).setDate(date);
//        }
//    }

    private void setupHeader() {
        for (int i = 0; i < chatObjectList.size() - 1; i++) {
            SimpleDateFormat formatterDate = new SimpleDateFormat("dd-MM-yyyy");
            //formatterDate.setTimeZone(TimeZone.getTimeZone("UTC"));
            Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
            calendar.setTimeInMillis(Long.parseLong(chatObjectList.get(i).getMessageTime()) * 1000L);


            chatObjectList.get(i).setDate(formatterDate.format(calendar.getTime()));
            chatObjectList.get(i).setIsHeader("0");
        }
        for (int i = 0; i < chatObjectList.size() - 1; i++) {
            String date = chatObjectList.get(i).getDate();
            String date2 = chatObjectList.get(i + 1).getDate();
            if (!date.equals(date2)) {
                chatObjectList.get(i).setIsHeader("1");
            } else if (i == chatObjectList.size() - 1) {
                String date1 = chatObjectList.get(i - 1).getDate();
                if (!date.equals(date1)) {

                }
            }
        }
    }


    @OnClick(R.id.backButtonClick)
    void setImageViewLeft() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @OnClick(R.id.send)
    void setMessageSend() {
        if (editTextMessage.getText().toString().isEmpty()) {
            AppCommon.getInstance(this).showDialog(this, "Por favor ingrese el mensaje");
        } else {
            sendMessage(editTextMessage.getText().toString().trim(), "", "", "1");
        }
    }

    private void sendMessage(String message, String imgbase, final String url, final String s) {
        AppCommon.getInstance(this).hideSoftKeyboard(this);
        AppCommon.getInstance(this).setNonTouchableFlags(this);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String currentDateandTime = sdf.format(new Date());
        if (AppCommon.getInstance(this).isConnectingToInternet(this)) {
            progressBar.setVisibility(View.VISIBLE);
            final String userId = String.valueOf(AppCommon.getInstance(this).getUserID());
            SendMessageEntity sendMessageEntity = new SendMessageEntity(userId, anthorUserId, message);
            PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
            call = pretoAppService.sendMessage(sendMessageEntity);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(ChatScreenActivity.this).clearNonTouchableFlags(ChatScreenActivity.this);
                    progressBar.setVisibility(View.GONE);
                    CommonIntResponse commonIntResponse = (CommonIntResponse) response.body();
                    if (commonIntResponse.getSuccess() == 1) {
                        editTextMessage.setText("");
                        getChatThread(anthorUserId);
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    AppCommon.getInstance(ChatScreenActivity.this).clearNonTouchableFlags(ChatScreenActivity.this);
                    progressBar.setVisibility(View.GONE);
                    AppCommon.getInstance(ChatScreenActivity.this).showDialog(ChatScreenActivity.this, getResources().getString(R.string.network_error));
                }
            });

        } else {
            AppCommon.getInstance(ChatScreenActivity.this).clearNonTouchableFlags(ChatScreenActivity.this);
            AppCommon.getInstance(ChatScreenActivity.this).showDialog(ChatScreenActivity.this, getResources().getString(R.string.network_error));
        }


    }

    @OnClick(R.id.attachment)
    void setAttachment() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.choose_option_dialog);
        TextView camera = (TextView) dialog.findViewById(R.id.camera);
        TextView gallery = (TextView) dialog.findViewById(R.id.gallery);
        TextView textViewCancel = (TextView) dialog.findViewById(R.id.cancel);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkPermission()) {
                    ActivityCompat.requestPermissions(ChatScreenActivity.this, new String[]{READ_EXTERNAL_STORAGE, CAMERA}, PERMISSION_REQUEST_CODE);
                } else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                    dialog.dismiss();
                }
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkPermission()) {
                    ActivityCompat.requestPermissions(ChatScreenActivity.this, new String[]{READ_EXTERNAL_STORAGE, CAMERA}, PERMISSION_REQUEST_CODE);
                } else {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);//
                    startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
                    dialog.dismiss();
                }
            }
        });
        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

//    public void setClickAction(int position) {
//        if (chatObjectList.get(position).getType().equals("2")) {
//            Intent intent = new Intent(this, PreviewImageActivity.class);
//            intent.putExtra("name", name);
//            intent.putExtra("imgUrl", chatObjectList.get(position).getAttachmentUrl());
//            startActivity(intent);
//        }
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            }
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void onSelectFromGalleryResult(Intent data) {
        bm = null;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        uri = data.getData();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
//        ChatObject chatObject = new ChatObject(String.valueOf(AppCommon.getInstance(ChatScreenActivity.this).getUserID()), "", "2", uri.toString(),currentDateandTime);
//        chatObjectList.add(chatObject);
//        chatAdapter.notifyDataSetChanged();
//        chatRecyclerView.scrollToPosition(chatObjectList.size() - 1);
        encodeImage(bm, uri.toString());
    }

    private void onCaptureImageResult(Intent data) {
        bm = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        if (bm != null) {
            bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        }

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        uri = Uri.fromFile(destination);
        encodeImage(bm, uri.toString());
    }

    private String encodeImage(Bitmap bm, String s) {
        if (bm != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();
            String encImage = Base64.encodeToString(b, Base64.DEFAULT);
            //  sendMessage("", encImage, s, "2");
            return encImage;
        } else {
            return "";
        }
    }

    public void fetchMoreData() {
        topProgressBar.setVisibility(View.VISIBLE);
        offset = offset + 1;
        getChatThread(anthorUserId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (call != null) {
            call.cancel();
        }
    }

    public void setData() {
        Log.v("Inside Class", "Inside Class");

    }

}
