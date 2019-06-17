package cn.edu.hebtu.software.sharemateclient.tools;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class GradationScrollView extends ScrollView {
    private ScrollViewListener scrollViewListener = null;

    public interface ScrollViewListener
    {
        void onScrollChanged(GradationScrollView scrollView, int x, int y, int oldx, int oldy);
    }

    public GradationScrollView(Context context) {
        super(context);
    }

    public GradationScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GradationScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, l, t, oldl, oldt);
        }
    }
}
