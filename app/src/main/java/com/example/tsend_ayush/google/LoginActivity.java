package com.example.tsend_ayush.google;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.view.View;
import com.example.tsend_ayush.google.custom.BaseActivity;
import com.example.tsend_ayush.google.utils.Utils;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * Created by Tsend-Ayush on 11/15/2015.
 */
public class LoginActivity extends BaseActivity
{

    /** The username edittext. */
    private EditText user;

    /** The password edittext. */
    private EditText pwd;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        setTouchNClick(R.id.btnLogin);
        setTouchNClick(R.id.btnReg);

        user = (EditText) findViewById(R.id.user);
        pwd = (EditText) findViewById(R.id.pwd);
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v.getId() == R.id.btnReg)
        {
            startActivityForResult(new Intent(this, RegisterActivity.class), 10);
        }
        else
        {

            String username = user.getText().toString();
            String password = pwd.getText().toString();
            if (username.length() == 0 || password.length() == 0)
            {
                Utils.showDialog(this, R.string.err_fields_empty);
                return;
            }
            final ProgressDialog dia = ProgressDialog.show(this, null,
                    getString(R.string.alert_wait));
            ParseUser.logInInBackground(username, password, new LogInCallback() {

                @Override
                public void done(ParseUser parseuser, ParseException e) {
                    dia.dismiss();
                    if (parseuser != null) {
                        UserListActivity.user = parseuser;
                        startActivity(new Intent(LoginActivity.this, UserListActivity.class));
                        finish();
                    } else {
                        String errortxt = getString(R.string.err_login);

                        switch (e.getCode()) {
                            case ParseException.USERNAME_TAKEN:
                                errortxt = "Sorry, this username has already been taken.";
                                break;
                            case ParseException.USERNAME_MISSING:
                                errortxt = "Sorry, you must supply a username to register.";
                                break;
                            case ParseException.PASSWORD_MISSING:
                                errortxt = "Sorry, you must supply a password to register.";
                                break;
                            case ParseException.OBJECT_NOT_FOUND:
                                errortxt = "Sorry, those credentials were invalid.";
                                break;
                            default:
                                errortxt = e.getLocalizedMessage();
                                break;
                        }
                        Utils.showDialog(LoginActivity.this, errortxt + " " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK)
            finish();

    }
}
