package com.example.tsend_ayush.google;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tsend_ayush.google.adapter.ChatListAdapter;
import com.example.tsend_ayush.google.custom.BaseActivity;
import com.example.tsend_ayush.google.model.Conversation;
import com.example.tsend_ayush.google.utils.Const;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Tsend-Ayush on 11/15/2015.
 */
public class ChatActivity extends BaseActivity
{

    /** The Conversation list. */
    private ArrayList<Conversation> convList;
    //private ArrayList<ParseObject> msgList;

    /** The chat_layout adapter. */
    private ChatListAdapter mChatAdapter;

    private TextView tvname;
    /** The Editext to compose the message. */
    private EditText etxtMessage;

    /** The user name of buddy. */
    private String buddy;

    /** The date of last message in conversation. */
    private Date lastMsgDate;

    /** Flag to hold if the activity is running or not. */
    private boolean isRunning;

    /** The handler. */
    private static Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);

        convList = new ArrayList<Conversation>();
        ListView list = (ListView) findViewById(R.id.list);

        mChatAdapter = new ChatListAdapter(ChatActivity.this,UserListActivity.user.getUsername(),convList);
        list.setAdapter(mChatAdapter);
        list.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        list.setStackFromBottom(true);

        etxtMessage = (EditText) findViewById(R.id.txt);
        etxtMessage.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        setTouchNClick(R.id.btnSend);

        buddy = getIntent().getStringExtra(Const.EXTRA_DATA);
        tvname = (TextView)findViewById(R.id.tvName);
        tvname.setText(buddy);
        handler = new Handler();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        isRunning = true;
        loadConversationList();
    }


    @Override
    protected void onPause()
    {
        super.onPause();
        isRunning = false;
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v.getId() == R.id.btnSend)
        {
            sendMessage();
        }

    }

    /**
     * Call this method to Send message to opponent. It does nothing if the text
     * is empty otherwise it creates a Parse object for ChatActivity message and send it
     * to Parse server.
     */
    private void sendMessage()
    {
        if (etxtMessage.length() == 0)
            return;

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etxtMessage.getWindowToken(), 0);

        String s = etxtMessage.getText().toString();

        final Conversation c = new Conversation(s, new Date(),
                UserListActivity.user.getUsername());
        c.setStatus(Conversation.STATUS_SENDING);
        convList.add(c);
        mChatAdapter.notifyDataSetChanged();
        etxtMessage.setText(null);

        ParseObject po = new ParseObject("ChatMessage");
        po.put("sender", UserListActivity.user.getUsername());
        po.put("receiver", buddy);
        // po.put("createdAt", "");
        po.put("message", s);
        po.saveEventually(new SaveCallback() {

            @Override
            public void done(ParseException e) {
                if (e == null)
                    c.setStatus(Conversation.STATUS_SENT);
                else
                    c.setStatus(Conversation.STATUS_FAILED);
                mChatAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Load the conversation list from Parse server and save the date of last
     * message that will be used to load only recent new messages
     */
    private void loadConversationList()
    {
        ParseQuery<ParseObject> senderquery = ParseQuery.getQuery("ChatMessage");
        ParseQuery<ParseObject> receivequery = ParseQuery.getQuery("ChatMessage");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ChatMessage");
		if (convList.size() == 0)
		{
			// load all messages...
			ArrayList<String> al = new ArrayList<String>();
			al.add(buddy);
			al.add(UserListActivity.user.getUsername());
			query.whereContainedIn("sender", al);
			query.whereContainedIn("receiver", al);
		}
		else
		{
			// load only newly received message..
			if (lastMsgDate != null)
				query.whereGreaterThan("createdAt", lastMsgDate);
			query.whereEqualTo("sender", buddy);
			query.whereEqualTo("receiver", UserListActivity.user.getUsername());
		}
        if (lastMsgDate != null)
            query.whereGreaterThan("createdAt", lastMsgDate);
        query.orderByDescending("createdAt");
        query.setLimit(50);

        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> li, ParseException e) {
                if (li != null && li.size() > 0) {
                    Log.d("Ayush", "li size" + li.size());
                    for (int i = li.size() - 1; i >= 0; i--) {
                        ParseObject po = li.get(i);
                        Conversation c = new Conversation(po.getString("message"), po.getCreatedAt(),
                                po.getString("sender"));
                        convList.add(c);

                        Log.d("Ayush", "li size " + po.getString("message") + " " + po.getString("sender") + "");

                        if (lastMsgDate == null || lastMsgDate.before(c.getDate()))
                            lastMsgDate = c.getDate();
                        mChatAdapter.notifyDataSetChanged();
                    }
                }
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (isRunning)
                            loadConversationList();
                    }
                }, 1000);
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chat_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logout();
                break;
		/*	case R.id.action_contacts:
				goToContacts();
				break;*/
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        //UserListActivity.user.g
        ParseUser.getCurrentUser().logOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
