package com.example.tsend_ayush.google.custom;

/**
 * Created by Tsend-Ayush on 11/15/2015.
 */
import android.app.ActionBar;
import android.app.Activity;
import android.os.Build;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.tsend_ayush.google.R;
import com.example.tsend_ayush.google.utils.TouchEffect;


/**
 * @author Faheem
 * This is a common activity that all other activities of the app can extend to
 * inherit the common behaviors like implementing a common interface that can be
 * used in all child activities.
 */
public class BaseActivity extends FragmentActivity implements OnClickListener
{

    /**
     * Apply this Constant as touch listener for views to provide alpha touch
     * effect. The view must have a Non-Transparent background.
     */
    public static final TouchEffect TOUCH = new TouchEffect();


    @Override
    public void setContentView(int layoutResID)
    {
        super.setContentView(layoutResID);
        //	setupActionBar();
    }


    protected void setupActionBar()
    {
        final ActionBar actionBar = getActionBar();
        if (actionBar == null)
            return;
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayUseLogoEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            actionBar.setLogo(R.mipmap.ic_launcher);
        }
        actionBar.setBackgroundDrawable(getResources().getDrawable(
                R.mipmap.actionbar_bg));
        actionBar.setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            actionBar.setHomeButtonEnabled(true);
        }
    }

    /**
     * Sets the touch and click listener for a view with given id.
     *
     * @param id
     *            the id
     * @return the view on which listeners applied
     */
    public View setTouchNClick(int id)
    {

        View v = setClick(id);
        if (v != null)
            v.setOnTouchListener(TOUCH);
        return v;
    }

    /**
     * Sets the click listener for a view with given id.
     *
     * @param id
     *            the id
     * @return the view on which listener is applied
     */
    public View setClick(int id)
    {

        View v = findViewById(id);
        if (v != null)
            v.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v)
    {

    }
}