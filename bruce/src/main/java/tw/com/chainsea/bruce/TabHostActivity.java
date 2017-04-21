package tw.com.chainsea.bruce;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.Map;

/**
 * MultiFragmentActivity
 * Created by 90Chris on 2015/1/14.
 */
public abstract class TabHostActivity extends FragmentActivity {

    private TextView mTvTitle;
    private ImageView mIvLeftFirst;
    private ImageView mIvLeftSecond;

    public class TabsInfo {
        Class fragment;
        String name;
        int drawable;
        int rightDrawable;
        int leftFirstDrawable;
        int leftSecondDrawable;
        OnTitleBarListener titleBarListener;

        public TabsInfo( String name, int drawable, Class fragment ) {
            this.fragment = fragment;
            this.name = name;
            this.drawable = drawable;
        }

        public TabsInfo( String name, int drawable, Class fragment, int rightDrawable, OnTitleBarListener titleBarListener) {
            this.fragment = fragment;
            this.name = name;
            this.drawable = drawable;
            this.rightDrawable = rightDrawable;
            this.titleBarListener = titleBarListener;
        }

        public TabsInfo(String name, int drawable, Class fragment, int rightDrawable, int leftFirstDrawable,
                        int leftSecondDrawable, OnTitleBarListener listener) {
            this.fragment = fragment;
            this.name = name;
            this.drawable = drawable;
            this.rightDrawable = rightDrawable;
            this.leftFirstDrawable = leftFirstDrawable;
            this.leftSecondDrawable = leftSecondDrawable;
            this.titleBarListener = listener;
        }

    }

    public interface OnTitleBarListener {
        void onRightClick();
        void onFirstClick();
        void onSecondClick();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //http://stackoverflow.com/questions/7469082/getting-exception-illegalstateexception-can-not-perform-this-action-after-onsa
        //super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bruce_activity_tabs);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.bruce_titlebar_tab);

        FragmentTabHost mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.tabs_content);
        mTabHost.setBackgroundResource(R.drawable.bruce_tabhost_bg);
        final Map<String, TabsInfo> tabsMap = tabsInfoMap();
        for ( String key : tabsMap.keySet()) {
            View view = LayoutInflater.from(this).inflate(R.layout.bruce_tab_item, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.tab_item_icon);
            imageView.setImageResource(tabsMap.get(key).drawable);
            TextView textView = (TextView) view.findViewById(R.id.tab_item_name);
            textView.setText(tabsMap.get(key).name);
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(key).setIndicator(view);
            mTabHost.addTab(tabSpec, tabsMap.get(key).fragment, null);
        }

        mTvTitle = (TextView)findViewById(R.id.tab_title_layout_center);
        final ImageView ivRight = (ImageView)findViewById(R.id.tab_title_layout_right);
        mIvLeftFirst = (ImageView) findViewById(R.id.tab_title_layout_left_first);
        mIvLeftSecond = (ImageView) findViewById(R.id.tab_title_layout_left_second);
        mTvTitle.setText(tabsMap.get(tabsMap.keySet().iterator().next()).name);
        setRightTitleView(tabsMap, tabsMap.keySet().iterator().next(), ivRight);

        setLeftFirstView(tabsMap, tabsMap.keySet().iterator().next(), mIvLeftFirst);
        setLeftSecondView(tabsMap, tabsMap.keySet().iterator().next(), mIvLeftSecond);
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(final String s) {
                TabsInfo info = tabsMap.get(s);
                mTvTitle.setText(info.name);
                setRightTitleView(tabsMap, s, ivRight);
                setLeftFirstView(tabsMap, s, mIvLeftFirst);
                setLeftSecondView(tabsMap, s, mIvLeftSecond);
            }
        });
    }

    public abstract Map<String, TabsInfo> tabsInfoMap();

    /**
     * deal with right view of titlebar
     * @param tabsMap map of tabs
     * @param tag key
     * @param imageView layout
     */
    private void setRightTitleView(Map<String, TabsInfo> tabsMap, String tag, ImageView imageView) {
        final int imageId = tabsMap.get(tag).rightDrawable;
        imageView.setVisibility(View.VISIBLE);
        if ( imageId != 0) {
            final OnTitleBarListener listener = tabsMap.get(tag).titleBarListener;
            imageView.setImageResource(imageId);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onRightClick();
                }
            });
        } else {
            imageView.setVisibility(View.GONE);
        }
    }

    public void setLeftFirstView(Map<String, TabsInfo> tabsMap, String tag, ImageView imageView) {
        int imageId = tabsMap.get(tag).leftFirstDrawable;
        imageView.setVisibility(View.VISIBLE);
        if (imageId != 0) {
            final OnTitleBarListener listener = tabsMap.get(tag).titleBarListener;
            imageView.setImageResource(imageId);
            imageView.setSelected(true);
            mTvTitle.setText("全部");
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    trigSelected();
                    listener.onFirstClick();
                }
            });
        } else {
            imageView.setVisibility(View.GONE);
        }
    }

    public void setLeftSecondView(Map<String, TabsInfo> tabsMap, String tag, ImageView imageView) {
        int imageId = tabsMap.get(tag).leftSecondDrawable;
        imageView.setVisibility(View.VISIBLE);
        if (imageId != 0) {
            final OnTitleBarListener listener = tabsMap.get(tag).titleBarListener;
            imageView.setImageResource(imageId);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    trigSelected();
                    listener.onSecondClick();
                }
            });
        } else {
            imageView.setVisibility(View.GONE);
        }
    }

    private void trigSelected() {
        if (mIvLeftFirst.isSelected() && !mIvLeftSecond.isSelected()) {
            mIvLeftFirst.setSelected(false);
            mIvLeftSecond.setSelected(true);
        } else if (!mIvLeftFirst.isSelected() && mIvLeftSecond.isSelected()) {
            mIvLeftFirst.setSelected(true);
            mIvLeftSecond.setSelected(false);
        }
    }

    public ImageView getRightView() {
        return (ImageView)findViewById(R.id.tab_title_layout_right);
    }

    public ImageView getLeftFirst() {
        return (ImageView) findViewById(R.id.tab_title_layout_left_first);
    }

    public ImageView getLeftSecond() {
        return (ImageView) findViewById(R.id.tab_title_layout_left_second);
    }

    public void setCenterText(String title) {
        mTvTitle.setText(title);
    }
}
