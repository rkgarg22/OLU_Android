package Adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.elisa.olu.ChatScreenActivity;
import com.elisa.olu.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import ApiObject.ChatObject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import infrastructure.AppCommon;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ListViewHolder> {
    private List<ChatObject> chatObjectList;
    private Activity activity;
    public DrawerLayout drawerLayout;
    int offset = 19;

    public ChatAdapter(List<ChatObject> list, Activity activity) {
        this.chatObjectList = list;
        this.activity = activity;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_screen_list_layout, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        int userId = AppCommon.getInstance(activity).getUserID();
        String anotherUser = chatObjectList.get(position).getMessageFromID();
        if (chatObjectList.get(position).getIsHeader() != null) {
            if (chatObjectList.get(position).getIsHeader().equals("1")) {
                //SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                //Calendar calendar = Calendar.getInstance();
                //calendar.setTimeInMillis(Long.parseLong(chatObjectList.get(position).getMessageTime()));
                holder.dateTime.setText(chatObjectList.get(position).getDate());
                holder.linearLayoutDate.setVisibility(View.VISIBLE);
            } else {
                holder.linearLayoutDate.setVisibility(View.GONE);
            }
        } else {
            holder.linearLayoutDate.setVisibility(View.GONE);
        }
        if (anotherUser.equals(String.valueOf(userId))) {
            holder.linearLayoutLeft.setVisibility(View.GONE);
            holder.textViewLeft.setVisibility(View.GONE);
            holder.textViewRight.setText(chatObjectList.get(position).getMessage());
            holder.textViewRight.setVisibility(View.VISIBLE);
            holder.draweeViewLeft.setVisibility(View.GONE);
            holder.progressBarLeft.setVisibility(View.GONE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.linearLayoutRight.setVisibility(View.VISIBLE);
            holder.messageLeftTime.setVisibility(View.GONE);
            holder.messageRightTime.setVisibility(View.VISIBLE);
            holder.messageRightTime.setText(convertMillisecondsToTime(chatObjectList.get(position).getMessageTime()));
        } else {
            holder.linearLayoutLeft.setVisibility(View.VISIBLE);
            holder.textViewLeft.setVisibility(View.VISIBLE);
            holder.linearLayoutRight.setVisibility(View.GONE);
            holder.textViewLeft.setText(chatObjectList.get(position).getMessage());
            holder.textViewRight.setVisibility(View.GONE);
            holder.draweeViewRight.setVisibility(View.GONE);
            holder.draweeViewLeft.setVisibility(View.GONE);
            holder.progressBarRight.setVisibility(View.GONE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.messageRightTime.setVisibility(View.GONE);
            holder.messageLeftTime.setVisibility(View.VISIBLE);
            holder.messageLeftTime.setText(convertMillisecondsToTime(chatObjectList.get(position).getMessageTime()));
        }
//        if (position == offset) {
//            ((ChatScreenActivity) activity).fetchMoreData();
//        }
    }

    @Override
    public int getItemCount() {
        return chatObjectList.size();
    }

    public void updateList(List<ChatObject> list, int offset) {
        this.offset = 19 * (offset + 1);
        this.chatObjectList = list;
        notifyDataSetChanged();
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {
        private final Context context;
        @BindView(R.id.messageLayoutLeft)
        LinearLayout linearLayoutLeft;
        @BindView(R.id.messageLayoutRight)
        LinearLayout linearLayoutRight;
        @BindView(R.id.messageLeft)
        TextView textViewLeft;
        @BindView(R.id.messageRight)
        TextView textViewRight;
        @BindView(R.id.attachmentLeft)
        SimpleDraweeView draweeViewLeft;
        @BindView(R.id.attachmentRight)
        SimpleDraweeView draweeViewRight;
        @BindView(R.id.dateTime)
        TextView dateTime;
        @BindView(R.id.dateLayout)
        LinearLayout linearLayoutDate;
        @BindView(R.id.progressBarLeft)
        ProgressBar progressBarLeft;
        @BindView(R.id.progressBarRight)
        ProgressBar progressBarRight;
        @BindView(R.id.leftLayout)
        RelativeLayout leftLayout;
        @BindView(R.id.rightLayout)
        RelativeLayout rightLayout;
        @BindView(R.id.messageLeftTime)
        TextView messageLeftTime;
        @BindView(R.id.messageRightTime)
        TextView messageRightTime;

        public ListViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.messageLayout)
        void messageLayout() {
            int position = getAdapterPosition();
            // ((ChatScreenActivity) context).setClickAction(position);
        }
    }

    String convertMillisecondsToTime(String millis)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm");

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(millis));
        return formatter.format(calendar.getTime());
    }
}
