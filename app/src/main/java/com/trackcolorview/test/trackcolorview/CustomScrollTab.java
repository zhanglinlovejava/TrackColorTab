package com.trackcolorview.test.trackcolorview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.trackcolorview.test.R;
import com.trackcolorview.test.util.ScreenUtil;

/**
 * Created by Colin.Zhang on 2017/3/16.
 */

public class CustomScrollTab extends HorizontalScrollView {
    private final PageListener pageListener = new PageListener();
    public ViewPager.OnPageChangeListener delegatePageListener;

    private int textOriginColor = 0x000000;//字体改变前的颜色
    private int textChangeColor = 0xff0000;//字体改变中的颜色

    private int tabCount = 3;
    private int tabPadding = 0;
    private int lastScrollX = 0;
    private int scrollOffset = ScreenUtil.px2dip(ScreenUtil.screenWidth) - 100;
    private int currentPosition = 0;
    private int textSize;
    private LinearLayout tabsContainer;
    private ViewPager pager;

    public CustomScrollTab(Context context) {
        this(context, null);
    }

    public CustomScrollTab(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomScrollTab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFillViewport(true);
        setWillNotDraw(false);
        tabsContainer = new LinearLayout(context);
        tabsContainer.setGravity(Gravity.CENTER);
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(tabsContainer);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        tabPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, tabPadding, dm);
        textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize, dm);
        scrollOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, scrollOffset, dm);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomScrollTab);
        textOriginColor = a.getColor(R.styleable.CustomScrollTab_textOriginColor, textOriginColor);
        textChangeColor = a.getColor(R.styleable.CustomScrollTab_textChangeColor, textChangeColor);
        tabPadding = a.getDimensionPixelSize(R.styleable.CustomScrollTab_tabPaddingLeftRight, tabPadding);
        scrollOffset = a.getDimensionPixelSize(R.styleable.CustomScrollTab_scrollOffset, scrollOffset);
        textSize = a.getDimensionPixelSize(R.styleable.CustomScrollTab_textSize, scrollOffset);
        a.recycle();
    }

    public void setViewPager(ViewPager pager) {
        this.pager = pager;
        if (pager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }

        pager.addOnPageChangeListener(pageListener);

        notifyDataSetChanged();
    }

    private void updateTabStyles() {
        View v = tabsContainer.getChildAt(0);
        if (v instanceof ColorTrackView) {
            ColorTrackView tab = (ColorTrackView) v;
            tab.setProgress(1);
        }

    }

    public void notifyDataSetChanged() {
        tabsContainer.removeAllViews();
        tabCount = pager.getAdapter().getCount();
        for (int i = 0; i < tabCount; i++) {
            addTextTab(i, pager.getAdapter().getPageTitle(i).toString());
        }
        updateTabStyles();
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                currentPosition = pager.getCurrentItem();
                scrollToChild(currentPosition, 0);
            }
        });

    }

    private void addTextTab(final int position, String title) {
        ColorTrackView tab = new ColorTrackView(getContext(), textSize, textChangeColor, textOriginColor);
        tab.setText(title);
        addTab(position, tab);
    }

    private void addTab(final int position, View tab) {
        tab.setFocusable(true);
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(position);
            }
        });

        tab.setPadding(tabPadding, 0, tabPadding, 0);
        tabsContainer.addView(tab, position);
    }

    private class PageListener implements ViewPager.OnPageChangeListener {
        private boolean checkIfScroll = false;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            currentPosition = position;
            if (checkIfScroll) {
                if (positionOffset > 0) {
                    ColorTrackView left = (ColorTrackView) tabsContainer.getChildAt(position);
                    ColorTrackView right = (ColorTrackView) tabsContainer.getChildAt(position + 1);

                    left.setDirection(1);
                    right.setDirection(0);
                    left.setProgress(1 - positionOffset);
                    right.setProgress(positionOffset);
                }
            }
            View view = tabsContainer.getChildAt(currentPosition);
            scrollToChild(position, (int) (positionOffset * tabsContainer.getChildAt(position).getWidth()));
            invalidate();

            if (delegatePageListener != null) {
                delegatePageListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == 1) {
                checkIfScroll = true;
            } else if (state == 0) {
                checkIfScroll = false;
            }
//            if (state == ViewPager.SCROLL_STATE_IDLE) {
//                scrollToChild(pager.getCurrentItem(), 0);
//            }

            if (delegatePageListener != null) {
                delegatePageListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (delegatePageListener != null) {
                delegatePageListener.onPageSelected(position);
            }
            changeColorTrackViewColor(position);
        }

    }

    public void setScrollOffset(int scrollOffsetPx) {
        this.scrollOffset = scrollOffsetPx;
        invalidate();
    }

    private void changeColorTrackViewColor(int index) {
        for (int i = 0; i < tabCount; i++) {
            ColorTrackView view = (ColorTrackView) tabsContainer.getChildAt(i);
            view.setDirection(0);
            view.setProgress(((index == i) ? 1.0f : 0));
        }
    }

    private void scrollToChild(int position, int offset) {

        if (tabCount == 0) {
            return;
        }

        int newScrollX = tabsContainer.getChildAt(position).getLeft() + offset;


        if (position > 0 || offset > 0) {
            newScrollX -= scrollOffset;
        }

        if (newScrollX != lastScrollX) {
            lastScrollX = newScrollX;
            scrollTo(newScrollX, 0);
        }

    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        currentPosition = savedState.currentPosition;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPosition = currentPosition;
        return savedState;
    }

    static class SavedState extends BaseSavedState {
        int currentPosition;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPosition = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPosition);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
