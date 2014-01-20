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
import android.view.ViewGroup;
import android.view.animation.Animation;
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
                    Animation inAnim = is.getInAnimation();
                    if (null != inAnim) {
                        inAnim.setAnimationListener(new AnimationListener() {
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
                    }
                    final Animation outAnim = is.getOutAnimation();
                    if (null != outAnim) {
                        outAnim.setAnimationListener(new AnimationListener() {
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
                    }
                    newView.setHasTransientState(true);	//preventing recycle of views doesn't help
                    LayoutTransition lt = is.getLayoutTransition();
                    if (null != lt) {
                        //lt.setDuration(5*1000);

//                        Animator outAnimator = AnimatorInflater.loadAnimator(TestListActivity.this, R.animator.switch_image_prop_out);
//                        lt.setAnimator(LayoutTransition.DISAPPEARING, outAnimator);
//                        lt.setStartDelay(LayoutTransition.DISAPPEARING, 0);
//                        lt.setStagger(LayoutTransition.DISAPPEARING, 6*1000);
//                        outAnimator.addListener(new AnimLogger("DISAPPEARING "+position));

//                        Animator inAnimator = AnimatorInflater.loadAnimator(TestListActivity.this, R.animator.switch_image_prop_in);
//                        lt.setAnimator(LayoutTransition.APPEARING, inAnimator);
//                        lt.setStartDelay(LayoutTransition.APPEARING, 0*1000);
//                        lt.setStagger(LayoutTransition.APPEARING, 6*1000);
//                        inAnimator.addListener(new AnimLogger("APPEARING "+position));
                        lt.getAnimator(LayoutTransition.DISAPPEARING).addListener(new AnimLogger("DISAPPEARING "+position));
                        lt.getAnimator(LayoutTransition.APPEARING).addListener(new AnimLogger("APPEARING "+position));

                        lt.getAnimator(LayoutTransition.CHANGE_APPEARING).addListener(new AnimLogger("CHANGE_APPEARING "+position));
                        lt.getAnimator(LayoutTransition.CHANGE_DISAPPEARING).addListener(new AnimLogger("CHANGE_DISAPPEARING "+position));
                        lt.getAnimator(LayoutTransition.CHANGING).addListener(new AnimLogger("CHANGING "+position));
                    }
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
        mItemIsOn[position] = !mItemIsOn[position];
        ImageSwitcher is = (ImageSwitcher)v.findViewById(R.id.image1);
        //start animation
        is.setImageResource(mItemIsOn[position] ? R.drawable.item_is_set : R.drawable.item_is_not_set);
    }

    static class AnimLogger implements AnimatorListener {
        private final String mAnimatorName;
        AnimLogger (String name) {
            mAnimatorName = name;
        }

        @Override
        public void onAnimationStart(Animator animation) {
            Log.d(TAG, mAnimatorName + " start");
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            Log.d(TAG, mAnimatorName + " end");
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            Log.d(TAG, mAnimatorName + " cancel");
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
            Log.d(TAG, mAnimatorName + " repeat");
        }

    }
}
