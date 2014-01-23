package com.example.imageswitcherinlistview;

import org.w3c.dom.ls.LSException;

import android.os.Bundle;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorInflater;
import android.animation.LayoutTransition;
import android.app.ListActivity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.Animation.AnimationListener;
import android.widget.ArrayAdapter;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ViewSwitcher.ViewFactory;

public class TestListActivity extends ListActivity {
    private static final String TAG = "ImageSwitcherInListView";
    private static final int ITEM_COUNT = 30;
    private ArrayAdapter<Integer> mAdapter=null;
    private final Integer mFakeItems[];
    private final boolean mItemIsOn[];

    public TestListActivity() {
        mFakeItems = new Integer[ITEM_COUNT];
        mItemIsOn = new boolean[ITEM_COUNT];
        for (int x=0; x<30; ++x) {
            mFakeItems[x] = x;
            mItemIsOn[x] = (x%2==0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new ArrayAdapter<Integer>(this, R.layout.list_item, android.R.id.text1, mFakeItems) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View newView = super.getView(position, convertView, parent);
                if (null==convertView) {
                    newView.setHasTransientState(true);	//preventing recycle of views doesn't help

                    //setup image rotation click handler
                    final ImageView riv = (ImageView) newView.findViewById(R.id.rotatingImage); //Cast
                    final int finalPosition = position;
                    riv.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Animation rotAnimation = AnimationUtils.loadAnimation(TestListActivity.this, R.anim.switch_image_in);
                            rotAnimation.setAnimationListener(new AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {
                                    Log.d(TAG, "rotAnimation Start "+finalPosition);
                                }
                                @Override
                                public void onAnimationRepeat(Animation animation) {
                                    Log.d(TAG, "rotAnimation Repeat "+finalPosition);
                                }
                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    Log.d(TAG, "rotAnimation End "+finalPosition);
                                }
                            });
                            riv.startAnimation(rotAnimation);
                        }
                    });
                }
                return newView;
            }
            
        };
        setListAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.test_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent tmpIntent = new Intent(this, TransitionTestActivity.class);
            startActivity(tmpIntent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }
}
