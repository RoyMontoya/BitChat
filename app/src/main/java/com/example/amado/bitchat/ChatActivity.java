package com.example.amado.bitchat;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;


public class ChatActivity extends ActionBarActivity implements View.OnClickListener, MessageDataSource.Listener {
    public static final String CONTACT_NUMBER = "CONTACT_NUMBER";
    public static final String TAG = "ChatActivity";

    private ArrayList<Message> mMessages;
    private MessagesAdapter mAdapter;
    private String mRecipient;
    private ListView mList;
    private Handler mHandler = new Handler();
    private Date mLastMessageDate= new Date();


    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "fetched new messages");
            MessageDataSource.fetchMessagesAfter(ContactDataSource.getCurrentUser().getPhoneNumber(),
                    mRecipient,
                    mLastMessageDate,
                    ChatActivity.this);
                  mHandler.postDelayed(this, 2000);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mRecipient = getIntent().getStringExtra(CONTACT_NUMBER);

        mMessages = new ArrayList<Message>();


        mList = (ListView)findViewById(R.id.messages_list);
        mAdapter = new MessagesAdapter(mMessages);
        mList.setAdapter(mAdapter);


        Button sendButton = (Button)findViewById(R.id.send_button);
        sendButton.setOnClickListener(this);

        MessageDataSource.fetchMessages(ContactDataSource.getCurrentUser().getPhoneNumber(),
                mRecipient, this);
    }

    public void onClick(View v) {
        EditText newMessageView = (EditText)findViewById(R.id.new_message);
        String newMessage = newMessageView.getText().toString();
        newMessageView.setText("");
        Message message = new Message(newMessage, ContactDataSource.getCurrentUser().getPhoneNumber());
        mAdapter.notifyDataSetChanged();
        MessageDataSource.sendMessage(message.getSender(), mRecipient, message.getText());
    }

    @Override
    public void onFetchedMessages(ArrayList<Message> messages) {
        mMessages.clear();
        addMessage(messages);

        mHandler.postDelayed(mRunnable, 1000);

    }

    private void addMessage(ArrayList<Message> messages){
        mMessages.addAll(messages);
        mAdapter.notifyDataSetChanged();

        if(mMessages.size()>0){
            mList.setSelection(mMessages.size() - 1);
            Message message = mMessages.get(mMessages.size()-1);
            mLastMessageDate = message.getDate();

        }
    }

    private class MessagesAdapter extends ArrayAdapter<Message> {

        public MessagesAdapter(ArrayList<Message> messages) {
            super(ChatActivity.this, R.layout.messages_list_item, R.id.message, messages);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = super.getView(position, convertView, parent);

            Message message = getItem(position);

            TextView messageView = (TextView)convertView.findViewById(R.id.message);
            messageView.setText(message.getText());
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)
                    messageView.getLayoutParams();


            int sdk = Build.VERSION.SDK_INT;
            if(message.getSender().equals(ContactDataSource.getCurrentUser().getPhoneNumber())){
                    if(sdk>Build.VERSION_CODES.JELLY_BEAN) {
                        messageView.setBackground(getResources().getDrawable(R.drawable.bubble_right_green));
                    }else{
                        messageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.bubble_right_green));
                    }

                layoutParams.gravity= Gravity.RIGHT;


            }else{
                if(sdk>Build.VERSION_CODES.JELLY_BEAN) {
                    messageView.setBackground(getResources().getDrawable(R.drawable.bubble_left_gray));
                }else{
                    messageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.bubble_left_gray));
                }
                layoutParams.gravity= Gravity.LEFT;
            }

            messageView.setLayoutParams(layoutParams);

            return convertView;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    public void onAddMessages(ArrayList<Message> messages) {
        addMessage(messages);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
