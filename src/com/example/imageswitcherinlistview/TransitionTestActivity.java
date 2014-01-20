package com.example.imageswitcherinlistview;

import android.os.Bundle;
import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

public class TransitionTestActivity extends Activity {

    private View mImg1=null;
    private View mImg2=null;
    private boolean mMode=false;
    private LinearLayout mLayout=null;
    static final int mGoneVisibility = View.GONE;   //or could be invisible

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition_test);

        mImg1 = findViewById(R.id.imageView1);
        mImg2 = findViewById(R.id.imageView2);
        mLayout = (LinearLayout) findViewById(R.id.LinearLayout1);  //Cast
//        LayoutTransition lt = mLayout.getLayoutTransition();
//        lt.enableTransitionType(LayoutTransition.CHANGING);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.transition_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_switch) {
            mMode = !mMode;
            mImg1.setVisibility(mMode?mGoneVisibility:View.VISIBLE);
            mImg2.setVisibility(mMode?View.VISIBLE:mGoneVisibility);

            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

}
