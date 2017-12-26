package cn.smiles.andclock.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.smiles.andclock.R;
import cn.smiles.andclock.SmilesApplication;

public class ViewPagerActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.vp_images)
    ViewPager vpImages;
    @BindView(R.id.tv_indicator)
    TextView tvIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        ButterKnife.bind(this);

        int[] imgIDs = {R.drawable.j6, R.drawable.j1, R.drawable.j2, R.drawable.j3, R.drawable.j4, R.drawable.j5, R.drawable.j6, R.drawable.j1};
        LayoutInflater inflater = LayoutInflater.from(this);
        ImageView[] images = new ImageView[imgIDs.length];
        for (int i = 0; i < images.length; i++) {
            ImageView iv = (ImageView) inflater.inflate(R.layout.vp_item_layout, vpImages, false);
            iv.setTag(String.valueOf(imgIDs[i]));
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ViewPagerActivity.this, v.getTag().toString(), Toast.LENGTH_SHORT).show();
                }
            });
            iv.setImageResource(imgIDs[i]);
            images[i] = iv;
        }

        MyAdapter myAdapter = new MyAdapter(this, images);
        vpImages.setAdapter(myAdapter);
        vpImages.setOffscreenPageLimit(6);
        vpImages.addOnPageChangeListener(this);
        vpImages.setCurrentItem(1, false);
        tvIndicator.setText(String.valueOf(1));

//        vpImages.setOnTouchListener(new View.OnTouchListener() {
//            boolean moved;
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
//                    if (!moved) {
//                        SmilesApplication.handler.removeCallbacks(myRunnable);
//                        moved = true;
//                        System.out.println("move");
//                    }
//                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
//                    moved = false;
//                    SmilesApplication.handler.postDelayed(myRunnable, 3000);
//                }
//                return false;
//            }
//        });
        vpImages.setPageTransformer(true, new ZoomOutPageTransformer());

    }

    ViewPager.PageTransformer pageTransformer = new ViewPager.PageTransformer() {
        @Override
        public void transformPage(View page, float position) {
            System.out.println(page.getTag().toString() + ";" + position);
            int width = page.getWidth();
            int height = page.getHeight();
            //我们给不同状态的页面设置不同的效果
            //通过position的值来分辨页面所处于的状态
            if (position < -1) {//滑出的页面
                page.setScrollX((int) (width * 0.88 * -1));
            } else if (position <= 1) {//[-1,1]
                page.setScrollX((int) (width * 0.88 * position));
                    /*float abs = 1 - Math.abs(width * position / width);
                    if (abs < 0.6f)
                        abs = 0.6f;
                    page.setPivotX(width / 2.0f);
                    page.setPivotY(height / 2.0f);
                    page.setScaleX(abs);
                    page.setScaleY(abs);*/
                    /*if (position < 0) {//[-1,0]
                        page.setScrollX((int) (width * 0.88 * position));
                    } else {//[0,1]
                        page.setScrollX((int) (width * 0.88 * position));
                    }*/
            } else {//即将滑入的页面
                page.setScrollX((int) (width * 0.88));
            }
        }
    };

    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        @SuppressLint("NewApi")
        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();
            Log.e("TAG", view + " , " + position + "");
            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);
            } else if (position <= 1) //a页滑动至b页 ； a页从 0.0 -1 ；b页从1 ~ 0.0
            { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }
                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE)
                        / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }

    public class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);
            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);
            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);
                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);
                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }

    private Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            int currentItem = vpImages.getCurrentItem();
            int count = vpImages.getAdapter().getCount();
            if (currentItem == (count - 1)) {
                currentItem = 0;
            } else {
                currentItem++;
            }
            vpImages.setCurrentItem(currentItem, true);
            SmilesApplication.handler.postDelayed(this, 3000);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
//        SmilesApplication.handler.postDelayed(myRunnable, 3000);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        SmilesApplication.handler.removeCallbacks(myRunnable);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //        若viewpager滑动未停止，直接返回
        if (state != ViewPager.SCROLL_STATE_IDLE) return;
//        若当前为第一张，设置页面为倒数第二张
        int position = vpImages.getCurrentItem();
        if (position == 0) {
            int xc = vpImages.getAdapter().getCount() - 2;
            vpImages.setCurrentItem(xc, false);
            tvIndicator.setText(String.valueOf(xc));
        } else if (position == (vpImages.getAdapter().getCount() - 1)) {
            vpImages.setCurrentItem(1, false);
            tvIndicator.setText(String.valueOf(1));
        } else {
            tvIndicator.setText(String.valueOf(position));
        }
    }

    private class MyAdapter extends PagerAdapter {

        private final Activity activity;
        private final View[] views;

        public MyAdapter(Activity activity, View[] views) {
            this.activity = activity;
            this.views = views;
        }

        @Override
        public int getCount() {
            return views.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View img = views[position];
            container.addView(img);
            return img;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return super.getPageTitle(position);
        }
    }
}
