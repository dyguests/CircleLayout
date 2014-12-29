package com.fanhl.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.fanhl.circlelayout.R;

/**
 * Created by fanhl on 14/12/28.
 */
public class CircleLayout extends ViewGroup {
    private static final String TAG = CircleLayout.class.getSimpleName();

    /**
     * 顺时针
     */
    public static final int CLOCKWISE = 0;
    /**
     * 逆时针
     */
    public static final int COUNTERCLOCKWISE = 1;

    /**
     * 圆形
     */
    public static final int CIRCLE = 0;
    /**
     * 矩形
     */
    public static final int RECTANGLE = 1;

    public static final int ANGLE_360 = 360;


    /**
     * 开始角度
     * right angle = 0
     * top angle = 90
     * left angle = 180
     * bottom angle = 270
     */
    int mStartAngle;
    /**
     * 结束角度
     * right angle = 0
     * top angle = 90
     * left angle = 180
     * bottom angle = 270
     */
    int mEndAngle;

    /**
     * 旋转方向
     */
    int mRotateDirection = CLOCKWISE;

    int mPathShape = CIRCLE;

    public CircleLayout(Context context) {
        this(context, null);
    }

    public CircleLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleLayout);

        mStartAngle = typedArray.getInt(R.styleable.CircleLayout_startAngle, 0) % ANGLE_360;
        mEndAngle = typedArray.getInt(R.styleable.CircleLayout_endAngle, mStartAngle) % ANGLE_360;

        mRotateDirection = typedArray.getInt(R.styleable.CircleLayout_rotateDirection, CLOCKWISE);

        mPathShape = typedArray.getInt(R.styleable.CircleLayout_pathShape, CIRCLE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int childCount = getChildCount();
        int specSizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int specSizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(specSizeWidth, specSizeHeight);

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            this.measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        Log.d(TAG, "l:" + l + " t:" + t + " r:" + r + " b" + b);

        int width = getWidth();
        int height = getHeight();
        Log.d(TAG, "onLayout width:" + width + " height:" + height);

        int childCount = getChildCount();

        int mLeft = 0;
        int mTop = 0;
        int mRight = width;
        int mBottom = height;


        int[] maxChildWidthHeight = getMaxChildWidthHeight();


        for (int i = 0; i < childCount; i++) {
            float childAngle = getChildAngle(i, childCount);

            View child = getChildAt(i);

            int[] childCenterXY;
            //圆形 CIRCLE
            if (mPathShape != RECTANGLE) {
                childCenterXY = getChildCircleCenterXY(maxChildWidthHeight, childAngle, mLeft, mTop, mRight, mBottom);
            }
            //矩形 RECTANGLE
            else {
                childCenterXY = getChildRectCenterXY(child, childAngle, mLeft, mTop, mRight, mBottom);
            }
            int x = childCenterXY[0];
            int y = childCenterXY[1];


            int cl = x - child.getMeasuredWidth() / 2;
            int ct = y - child.getMeasuredHeight() / 2;
            int cr = x + child.getMeasuredWidth() / 2;
            int cb = y + child.getMeasuredHeight() / 2;
            child.layout(cl, ct, cr, cb);

            Log.d(TAG, "onLayout child:" + i + " childAngle:" + childAngle
                    + " left:" + cl + " top:" + ct + " right:" + cr + " bottom:" + cb);

        }
    }

    private int[] getMaxChildWidthHeight() {
        int maxM = 0;
        int maxN = 0;

        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);

            maxM = Math.max(maxM, child.getMeasuredWidth());
            maxN = Math.max(maxN, child.getMeasuredHeight());
        }
        return new int[]{maxM, maxN};
    }

    /**
     * 取得控件的角度
     *
     * @param childIndex
     * @param childCount
     * @return
     */

    private float getChildAngle(int childIndex, int childCount) {
        int startAngle;
        int endAngle;

        //顺时针  Clockwise
        if (mRotateDirection != COUNTERCLOCKWISE) {
            startAngle = mStartAngle > mEndAngle ? mStartAngle : mStartAngle + ANGLE_360;
            endAngle = mEndAngle;
        }
        //逆时针 　Counterclockwise
        else {
            startAngle = mStartAngle;
            endAngle = mStartAngle < mEndAngle ? mEndAngle : mEndAngle + ANGLE_360;
        }

        if (childCount == 1) {
            return (startAngle + endAngle) / 2;
        }

        //当开始角度等于结束角度(完整的圆)时,保证第一个子视图不会与最后一个子视图重叠
        //when (mStartAngle == mEndAngle) ,set fullCircleFlag=1 to confirm 1st childView  and last childView do not overlap
        int fullCircleFlag;
        if (mStartAngle == mEndAngle) {
            fullCircleFlag = 0;
        } else {
            fullCircleFlag = -1;
        }

        //FIXME 处理当只有一个子视图时取中间角度
        float childAngle = ((float) (startAngle * (childCount + fullCircleFlag - childIndex) + endAngle * childIndex)) / (childCount + fullCircleFlag);

        return childAngle;
    }

    private int[] getChildRectCenterXY(View child, float childAngle, int mLeft, int mTop, int mRight, int mBottom) {
        int x;
        int y;

        int centerX = (mLeft + mRight) / 2;
        int centerY = (mTop + mBottom) / 2;


        int childWidth = child.getMeasuredWidth();
        int childHeight = child.getMeasuredHeight();

        int left = mLeft + childWidth / 2;
        int top = mTop + childHeight / 2;
        int right = mRight - childWidth / 2;
        int bottom = mBottom - childHeight / 2;

        int frameWidth = Math.abs(right - left);
        int frameHeight = Math.abs(bottom - top);

        double childSin = Math.sin(Math.toRadians(childAngle));//-1~1
        double childCos = Math.cos(Math.toRadians(childAngle));//-1~1
        double frameSin = getFrameDiagonalSin(left, top, right, bottom);//0~1

        if (Math.abs(childSin) > frameSin) {
            if (childAngle % ANGLE_360 < 180) {
                y = top;
                x = (int) (centerX + frameHeight * childCos / childSin / 2);
            } else {
                y = bottom;
                x = (int) (centerX - frameHeight * childCos / childSin / 2);
            }
        } else {
            if (childAngle % ANGLE_360 > 90 && childAngle % ANGLE_360 < 270) {
                x = left;
                y = (int) (centerY + frameWidth * childSin / childCos / 2);
            } else {
                x = right;
                y = (int) (centerY - frameWidth * childSin / childCos / 2);
            }
        }


        return new int[]{x, y};
    }

    private int[] getChildCircleCenterXY(int[] maxChildWH, float childAngle, int mLeft, int mTop, int mRight, int mBottom) {
        int x;
        int y;

        int centerX = (mLeft + mRight) / 2;
        int centerY = (mTop + mBottom) / 2;


        int left = mLeft + maxChildWH[0] / 2;
        int top = mTop + maxChildWH[1] / 2;
        int right = mRight - maxChildWH[0] / 2;
        int bottom = mBottom - maxChildWH[1] / 2;

        double childSin = Math.sin(Math.toRadians(childAngle));//-1~1
        double childCos = Math.cos(Math.toRadians(childAngle));//-1~1

        //水平半径 长轴
        int hR = Math.abs(right - left) / 2;
        //垂直半径 短轴
        int vR = Math.abs(bottom - top) / 2;

        x = (int) (centerX + hR * childCos);
        y = (int) (centerY - vR * childSin);

        return new int[]{x, y};
    }

    private double getFrameDiagonalSin(int left, int top, int right, int bottom) {
        int width = Math.abs(right - left);
        int height = Math.abs(bottom - top);
        double hypotenuse = Math.sqrt(width * width + height * height);

        return height / hypotenuse;
    }


    public int getStartAngle() {
        return mStartAngle;
    }

    public void setStartAngle(int mStartAngle) {
        if (this.mStartAngle != mStartAngle) {
            this.mStartAngle = mStartAngle;
            requestLayout();
        }
    }

    public void setStartAngleAndEndAngle(int mStartAngle) {
        if (this.mStartAngle != mStartAngle || this.mEndAngle != mStartAngle) {
            this.mStartAngle = mStartAngle;
            this.mEndAngle = mStartAngle;
            requestLayout();
        }
    }

    public int getEndAngle() {
        return mEndAngle;
    }

    public void setEndAngle(int mEndAngle) {
        if (this.mEndAngle != mEndAngle) {
            this.mEndAngle = mEndAngle;
            requestLayout();
        }
    }

    public int getRotateDirection() {
        return mRotateDirection;
    }

    public void setRotateDirection(int mRotateDirection) {
        if (this.mRotateDirection != mRotateDirection) {
            this.mRotateDirection = mRotateDirection;
            requestLayout();
        }
    }

    public int getPathShape() {
        return mPathShape;
    }

    public void setPathShape(int mPathShape) {
        if (this.mPathShape != mPathShape) {
            this.mPathShape = mPathShape;
            requestLayout();
        }
    }
}
