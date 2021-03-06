package com.example.tsend_ayush.google;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tsend_ayush.google.custom.BaseActivity;
import com.example.tsend_ayush.google.utils.Const;
import com.example.tsend_ayush.google.utils.Utils;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tsend-Ayush on 11/15/2015.
 */
public class UserListActivity extends BaseActivity
{

    /** The ChatActivity list. */
    private ArrayList<ParseUser> uList;
    private TextView textName;
    private Button logout;
    /** The user. */
    public static ParseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list);
        textName = (TextView)findViewById(R.id.textName);
        logout = (Button)findViewById(R.id.btnlogout);
        //getActionBar().setDisplayHomeAsUpEnabled(false);
        updateUserStatus(true);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseUser.logOut();
                finish();
            }
        });
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        updateUserStatus(false);
    }


    @Override
    protected void onResume()
    {
        super.onResume();
        loadUserList();

    }


    private void updateUserStatus(boolean online)
    {
        user.put("online", online);
        user.saveEventually();
    }

    /**
     * Load list of users.
     */
    private void loadUserList()
    {
        final ProgressDialog dia = ProgressDialog.show(this, null,
                getString(R.string.alert_loading));
        ParseUser.getQuery().whereNotEqualTo("username", user.getUsername())
                .findInBackground(new FindCallback<ParseUser>() {

                    @Override
                    public void done(List<ParseUser> li, ParseException e)
                    {
                        dia.dismiss();
                        if (li != null)
                        {
                            if (li.size() == 0)
                                Toast.makeText(UserListActivity.this,
                                        R.string.msg_no_user_found,
                                        Toast.LENGTH_SHORT).show();

                            uList = new ArrayList<ParseUser>(li);
                            ListView list = (ListView) findViewById(R.id.list);
                            list.setAdapter(new UserAdapter());
                            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> arg0,
                                                        View arg1, int pos, long arg3)
                                {

                                    startActivity(new Intent(UserListActivity.this,
                                            ChatActivity.class).putExtra(
                                            Const.EXTRA_DATA, uList.get(pos)
                                                    .getUsername()));
                                }
                            });
                        }
                        else
                        {
                            Utils.showDialog(
                                    UserListActivity.this,
                                    getString(R.string.err_users) + " "
                                            + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * The Class UserAdapter is the adapter class for User ListView. This
     * adapter shows the user name and it's only online status for each item.
     */
    private class UserAdapter extends BaseAdapter
    {


        @Override
        public int getCount()
        {
            return uList.size();
        }


        @Override
        public ParseUser getItem(int arg0)
        {
            return uList.get(arg0);
        }


        @Override
        public long getItemId(int arg0)
        {
            return arg0;
        }


        @Override
        public View getView(int pos, View v, ViewGroup arg2)
        {
            if (v == null)
                v = getLayoutInflater().inflate(R.layout.chat_item, null);

            ParseUser c = getItem(pos);
            TextView lbl = (TextView) v;
            lbl.setText(c.getUsername());
            lbl.setCompoundDrawablesWithIntrinsicBounds(
                    c.getBoolean("online") ? R.drawable.online
                            : R.drawable.offline, 0, R.drawable.spin, 0);

            return v;
        }

    }
}