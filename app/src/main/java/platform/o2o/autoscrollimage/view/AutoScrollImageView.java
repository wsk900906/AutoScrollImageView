package platform.o2o.autoscrollimage.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;


/**
 * Created by Swifty.Wang on 2015/6/16.
 */
public class AutoScrollImageView extends HorizontalScrollView implements Runnable {
    Drawable drawable;
    Handler handler;
    private boolean circleScroll = true;
    int maxScrollX;
    int targetScrollX = 0;
    int K = 2;
    boolean scrolltoRight = true;

    public AutoScrollImageView(Context context) {
        super(context);
        setHorizontalScrollBarEnabled(false);
    }

    public AutoScrollImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setHorizontalScrollBarEnabled(false);
    }

    public AutoScrollImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setHorizontalScrollBarEnabled(false);
    }

    public void setImage(Drawable d) {
        drawable = d;
        int h = drawable.getIntrinsicHeight();
        int w = drawable.getIntrinsicWidth();
        ImageView imageView = new ImageView(getContext());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setImageDrawable(d);
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.requestLayout();
        this.addView(imageView);
        this.requestLayout();
    }

    public void setImageResource(int id) {
        setImage(getContext().getResources().getDrawable(id));
    }

    public void startScroll() {
        //start scroll
        this.post(this);
    }

    /**
     * default is true, scroll to end then back scroll
     *
     * @param b
     */
    public void isCircleScroll(boolean b) {
        circleScroll = b;
    }

    public void setTouchAble(boolean b) {
        if (!b) {
            setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return true;
                }
            });
        }
    }

    @Override
    public void run() {
        maxScrollX = getChildAt(0).getMeasuredWidth() - getMeasuredWidth();
        if (maxScrollX > 0) {
            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (circleScroll) {
                        if (targetScrollX <= maxScrollX && targetScrollX >= 0) {
                            if (scrolltoRight) {
                                targetScrollX += K;
                            } else {
                                targetScrollX -= K;
                            }
                        } else {
                            if (scrolltoRight) {
                                targetScrollX -= K;
                            } else {
                                targetScrollX += K;
                            }
                            scrolltoRight = !scrolltoRight;
                        }
                    } else {
                        if (targetScrollX <= getChildAt(0).getMeasuredWidth()) {
                            if (scrolltoRight) {
                                targetScrollX += K;
                            } else {
                                targetScrollX -= K;
                            }
                        }
                    }
                    smoothScrollTo(targetScrollX, 0);
                    requestLayout();

                }
            };
            new Thread(delay).start();
        }
        Log.e("scroll", getChildAt(0).getMeasuredWidth() + " " + targetScrollX + scrolltoRight);
    }

    Runnable delay = new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(80);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0);
            }
        }
    };
}
