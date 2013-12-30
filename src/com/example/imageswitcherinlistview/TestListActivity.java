package com.example.imageswitcherinlistview;

import android.os.Bundle;
import android.app.ListActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
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

    private final ViewFactory mFactory = new ViewFactory() {
        @Override
        public View makeView() {
            return new ImageView(TestListActivity.this);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new ArrayAdapter<Integer>(this, R.layout.list_item, android.R.id.text1, mFakeItems) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View newView = super.getView(position, convertView, parent);
                ImageSwitcher is = (ImageSwitcher)newView.findViewById(R.id.image1); //Cast
                if (null==convertView) {
                    is.setFactory(mFactory);
                    is.setImageResource(mItemIsOn[position] ? R.drawable.item_is_set : R.drawable.item_is_not_set);
                    is.getInAnimation().setAnimationListener(new AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            Log.d(TAG, "onInAnimationStart called");
                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) {}
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            Log.d(TAG, "onInAnimationEnd called");
                        }
                    });
                    is.getOutAnimation().setAnimationListener(new AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            Log.d(TAG, "onOutAnimationStart called");
                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) {}
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            Log.d(TAG, "onOutAnimationEnd called");
                        }
                    });
                    newView.setHasTransientState(true);	//preventing recycle of views doesn't help
                }
                return newView;
            }
            
        };
        setListAdapter(mAdapter);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.test_list, menu);
//        return true;
//    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        mItemIsOn[position] = !mItemIsOn[position];
        ImageSwitcher is = (ImageSwitcher)v.findViewById(R.id.image1);
        //start animation
        is.setImageResource(mItemIsOn[position] ? R.drawable.item_is_set : R.drawable.item_is_not_set);
    }

}
