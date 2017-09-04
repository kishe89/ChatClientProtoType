package com.example.kjw.chatclientprototype;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<Message> mMessages;

    public MessageAdapter(Context context, List<Message> messages) {
        mMessages = messages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = -1;
        switch (viewType) {
        case Message.TYPE_MY_MESSAGE:
            layout = R.layout.item_message;
            break;
        case Message.TYPE_LOG:
            layout = R.layout.item_log;
            break;
        case Message.TYPE_ATTRIBUTE_MESSAGE:
            layout = R.layout.item_message_attribute;
            break;
        case Message.TYPE_OTHERS_MESSAGE:
            layout = R.layout.item_message_another;
            break;
        case Message.TYPE_ALLOWMESSAGE:
            layout = R.layout.item_message;
            break;
        case Message.TYPE_FAIL:
            layout = R.layout.item_message_fail;
            break;
        }
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Message message = mMessages.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
        viewHolder.setMessage(message.getMessage());
        viewHolder.setUsername(message.getUsername());
        viewHolder.setmMessageTime(sdf.format(message.getDate()));
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mMessages.get(position).getType();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mUsernameView;
        private TextView mMessageView;
        private Button mRetrysendBtn;
        private ImageView thumnailView;
        private TextView mMessageTime;
        public ViewHolder(View itemView) {
            super(itemView);

            mUsernameView = (TextView) itemView.findViewById(R.id.username);
            mMessageView = (TextView) itemView.findViewById(R.id.message);
            mRetrysendBtn = (Button) itemView.findViewById(R.id.retry_sendBtn);
            thumnailView = (ImageView) itemView.findViewById(R.id.thumnailView);
            mMessageTime = (TextView) itemView.findViewById(R.id.message_time);

        }

        public void setmMessageTime(String mMessageTime) {
            if(null == mMessageTime) return;
            this.mMessageTime.setText(mMessageTime);
        }

        public void setThumnail(String url){
            if(null == thumnailView) return;
            //set imageview uri
        }
        public void setUsername(String username) {
            if (null == mUsernameView) return;
            mUsernameView.setText(username);
        }

        public void setMessage(String message) {
            if (null == mMessageView) return;
            mMessageView.setText(message);
        }
        public void setmRetrysendBtnOnClickListener(Button mRetrysendBtn){
            if(null == mRetrysendBtn) return;
            if(mRetrysendBtn != null)mRetrysendBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.retry_sendBtn : break;
            }
        }
    }

}
