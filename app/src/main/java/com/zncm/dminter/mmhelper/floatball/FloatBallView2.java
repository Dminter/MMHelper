//package com.zncm.dminter.mmhelper.floatball;
//
//import android.accessibilityservice.AccessibilityService;
//import android.annotation.TargetApi;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Rect;
//import android.os.AsyncTask;
//import android.os.Build;
//import android.os.Vibrator;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewConfiguration;
//import android.view.WindowManager;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//
//import com.zncm.dminter.mmhelper.Constant;
//import com.zncm.dminter.mmhelper.OpenInentActivity;
//import com.zncm.dminter.mmhelper.R;
//import com.zncm.dminter.mmhelper.WatchingAccessibilityService;
//import com.zncm.dminter.mmhelper.data.CardInfo;
//import com.zncm.dminter.mmhelper.data.EnumInfo;
//import com.zncm.dminter.mmhelper.ft.MyFt;
//import com.zncm.dminter.mmhelper.utils.MyPath;
//import com.zncm.dminter.mmhelper.utils.NotiHelper;
//import com.zncm.dminter.mmhelper.utils.Xutils;
//
//import java.lang.reflect.Field;
//
//public class FloatBallView2 extends LinearLayout {
//    public static final String TAG = "tag";
//
//    private ImageView mImgBg;
//    private ImageView mImgBigBall;
//    private ImageView mImgBall;
//    private int mTouchSlop;
//    private int mStatusBarHeight;
//    private int mOffsetToParent;
//    private int mOffsetToParentY;
//    private float mMImg_big_ballX;
//    private float mMImg_big_ballY;
//
//    private final static long LONG_CLICK_LIMIT = 300;
//    private final static long REMOVE_LIMIT = 1500;
//    private final static long CLICK_LIMIT = 200;
//
//    private final static int MODE_NONE = 0x000;
//    private final static int MODE_DOWN = 0x001;
//    private final static int MODE_UP = 0x002;
//    private final static int MODE_LEFT = 0x003;
//    private final static int MODE_RIGHT = 0x004;
//    private final static int MODE_MOVE = 0x005;
//    private final static int MODE_GONE = 0x006;
//
//    private final static int OFFSET = 30;
//
//    private int mCurrentMode;
//
//
//    private long mLostDownTime;
//    private boolean mIsTouching;
//    private boolean mIsLongTouch;
//    private float mLastDownX;
//    private float mLastDownY;
//    private FloatBallService mBallService;
//    private Vibrator mVibrator;
//    private WindowManager mWindowManager;
//    private WindowManager.LayoutParams mParams;
//    private FrameLayout mRoot;
//    AccessibilityService mService;
//
//    private Context ctx;
//
//
//    public FloatBallView2(Context context) {
//        this(context, null);
//    }
//
//    public FloatBallView2(Context context, AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public FloatBallView2(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        this.ctx = context;
//
//        mBallService = (FloatBallService) context;
//        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
//        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        init(context);
//    }
//
//    private void init(Context context) {
//
//
//        View view = View.inflate(context, R.layout.layout_ball, this);
//        mImgBall = (ImageView) view.findViewById(R.id.img_ball);
//        mImgBigBall = (ImageView) view.findViewById(R.id.img_big_ball);
//        mImgBg = (ImageView) view.findViewById(R.id.img_bg);
//        mRoot = (FrameLayout) view.findViewById(R.id.root);
//
//
//        //获得触发事件的最小距离
//        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
//
//        //获取标题栏高度
//        mStatusBarHeight = getStatusBarHeight();
////        getStatusBarHeight(this);
//        mOffsetToParent = dip2px(25f);
//        mOffsetToParentY = mStatusBarHeight + mOffsetToParent;
//
//        mImgBigBall.post(new Runnable() {
//            @Override
//            public void run() {
//                mMImg_big_ballX = mImgBigBall.getX();//如果方法外面的画 获取不到坐标值
//                mMImg_big_ballY = mImgBigBall.getY();
////                Log.e(TAG, "里面的=" + "mMImg_big_ballX=" + mMImg_big_ballX + " mMImg_big_ballY=" + mMImg_big_ballY);
//            }
//        });
//
////        mMImg_big_ballX = mImgBigBall.getX();
////        mMImg_big_ballY = mImgBigBall.getY();
////        Log.e(TAG, "外面的=" + "mMImg_big_ballX=" + mMImg_big_ballX + " mMImg_big_ballY=" + mMImg_big_ballY);
//
//
//        mRoot.setOnTouchListener(new OnTouchListener() {
//
//
//            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//            @Override
//            public boolean onTouch(View view, final MotionEvent motionEvent) {
//
//                switch (motionEvent.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        mIsTouching = true;
//                        mImgBall.setVisibility(View.GONE);
//                        mImgBigBall.setVisibility(View.VISIBLE);
//
//                        mLostDownTime = System.currentTimeMillis();
//                        mLastDownX = motionEvent.getX();
//                        mLastDownY = motionEvent.getY();
//
//                        postDelayed(new Runnable() {
//
//                            @Override
//                            public void run() {
//                                if (!mIsLongTouch && mIsTouching && mCurrentMode == MODE_NONE) {
//                                    mIsLongTouch = isLongClick(motionEvent);
//                                }
//                            }
//                        }, LONG_CLICK_LIMIT);
//
//
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        if (!mIsLongTouch && isTouchSlop(motionEvent)) {//如果不是长按也打不到移动要求 直接不处理
//                            return true;
//                        }
//
//                        if (mIsLongTouch && (mCurrentMode == MODE_NONE || mCurrentMode == MODE_MOVE)) {//移动悬浮球
//                            mParams.x = (int) (motionEvent.getRawX() - mOffsetToParent);//距离边界都有一定的距离
//                            mParams.y = (int) (motionEvent.getRawY() - mOffsetToParentY);
//                            mWindowManager.updateViewLayout(FloatBallView2.this, mParams);
//                            mMImg_big_ballX = mImgBigBall.getX();//更新大圆的坐标
//                            mMImg_big_ballY = mImgBigBall.getY();
//                            mCurrentMode = MODE_MOVE;
//                        } else {//判断是上下左右
//                            doGesture(motionEvent);
//                        }
//
//                        break;
//                    case MotionEvent.ACTION_CANCEL:
//                    case MotionEvent.ACTION_UP:
//                        mIsTouching = false;
//                        if (mIsLongTouch) {
//                            mIsLongTouch = false;
//                        } else if (isCheck(motionEvent)) {
////                            AccessibilityUtil.doBack(mBallService);
//                            if (!initService()) {
//                                return false;
//                            }
//                            Xutils.debug("ret...");
//                            AccessibilityUtil.doBack(FloatBallView2.this.mService);
//                        } else {
//                            doUp();
//                        }
//                        mImgBigBall.setVisibility(View.GONE);
//                        mImgBall.setVisibility(View.VISIBLE);
//                        mCurrentMode = MODE_NONE;
//                        break;
//                }
//
//
//                return true;
//            }
//        });
//    }
//
//
//    private boolean initService() {
//        if (mService == null && WatchingAccessibilityService.getInstance() != null) {
//            mService = WatchingAccessibilityService.getInstance();
//        } else {
//            MyFt.getActivityDlg(FloatBallView2.this.ctx);
//            return false;
//        }
//        return true;
//    }
//
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//    private void doUp() {
//
//        if (!initService()) {
//            return;
//        }
//
//        switch (mCurrentMode) {
//            case MODE_LEFT:
//                Intent intent = new Intent(this.ctx, OpenInentActivity.class);
//                intent.putExtra("pkName", Constant.OPENINENT_LIKE);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                ctx.startActivity(intent);
//                break;
//            case MODE_RIGHT:
//                AccessibilityUtil.doGetActivity(mService);
//                break;
//            case MODE_UP:
//                AccessibilityUtil.doRecent(mService);
//                break;
//            case MODE_DOWN:
//                AccessibilityUtil.doHome(mService);
//                break;
//        }
//
//        //大圆归位
//        mImgBigBall.setX(mMImg_big_ballX);
//        mImgBigBall.setY(mMImg_big_ballY);
//    }
//
//    private boolean isCheck(MotionEvent motionEvent) {//判断是否点击
//        boolean flag = false;
//        float absX = Math.abs(motionEvent.getX() - mLastDownX);
//        float absY = Math.abs(motionEvent.getY() - mLastDownY);
//        long offTime = System.currentTimeMillis() - mLostDownTime;
//        if (absX < mTouchSlop * 2 && absY < mTouchSlop * 2 && offTime < CLICK_LIMIT) {
//            flag = true;
//        } else if (absX < mTouchSlop * 2 && absY < mTouchSlop * 2 && offTime >= CLICK_LIMIT) {
//            Intent intent = new Intent(this.ctx, OpenInentActivity.class);
//            intent.putExtra("pkName", Constant.OPENINENT_BALL);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            NotiHelper.noti("悬浮球", "", "", intent, true, false, Constant.n_id);
//            setVisibility(GONE);
//        }
//        return flag;
//    }
//
//    private void doGesture(MotionEvent motionEvent) {
//        float offX = motionEvent.getX() - mLastDownX;
//        float offY = motionEvent.getY() - mLastDownY;
//
//        float absX = Math.abs(offX);
//        float absY = Math.abs(offY);
//
//
//        if (absX < mTouchSlop && absY < mTouchSlop) {
//            return;
//        }
//
//        if (absX > absY) {//说明是左右滑动
//            if (offX > 0) {//向右滑
//                if (mCurrentMode == MODE_RIGHT) {
//                    return;
//                }
//                mCurrentMode = MODE_RIGHT;
//                mImgBigBall.setX(mMImg_big_ballX + OFFSET);
//                mImgBigBall.setY(mMImg_big_ballY);
//
//            } else {//向左滑
//                if (mCurrentMode == MODE_LEFT) {
//                    return;
//                }
//                mCurrentMode = MODE_LEFT;
//                mImgBigBall.setX(mMImg_big_ballX - OFFSET);
//                mImgBigBall.setY(mMImg_big_ballY);
//            }
//        } else {//上下
//            if (offY > 0) {//向下
//                if (mCurrentMode == MODE_DOWN) {
//                    return;
//                }
//                mCurrentMode = MODE_DOWN;
//                mImgBigBall.setX(mMImg_big_ballX);
//                mImgBigBall.setY(mMImg_big_ballY + OFFSET);
//
//                //如果长时间下拉则,移除自己
////                postDelayed(new Runnable() {
////                    @Override
////                    public void run() {
////                        if (mCurrentMode == MODE_DOWN && mIsTouching) {
////                            toRemove();
////                            mCurrentMode = MODE_GONE;
////                        }
////                    }
////                }, REMOVE_LIMIT);
//
//            } else {//向上
//                if (mCurrentMode == MODE_UP) {
//                    return;
//                }
//                mCurrentMode = MODE_UP;
//                mImgBigBall.setX(mMImg_big_ballX);
//                mImgBigBall.setY(mMImg_big_ballY - OFFSET);
//            }
//        }
//
//        Log.e(TAG, "x=" + mImgBigBall.getX() + " y=" + mImgBigBall.getY());
//
//    }
//
//    private void toRemove() {
//        mVibrator.vibrate(100);
//        FloatWindowManager.removeBallView(getContext());
//    }
//
//    private boolean isTouchSlop(MotionEvent motionEvent) {
//        boolean flag = false;
//        float offX = Math.abs(motionEvent.getX() - mLastDownX);
//        float offY = Math.abs(motionEvent.getY() - mLastDownY);
//        if (offX < mTouchSlop && offY < mTouchSlop) {
//            flag = true;
//        }
//        return flag;
//    }
//
//
//    class ScreenCap extends AsyncTask<Void, Void, Void> {
//
//        protected Void doInBackground(Void... params) {
//            try {
//                MyFt.clickCard(ctx, new CardInfo(EnumInfo.cType.CMD.getValue(), "screencap -p " + MyPath.getPathFolder(null) + Xutils.getFileSaveTime() + ".png", ""));
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            Xutils.tShort("已截屏~");
//        }
//    }
//
//
//    private boolean isLongClick(MotionEvent motionEvent) {
//        boolean flag = false;
//        float motionEventX = motionEvent.getX();
//        float motionEventY = motionEvent.getY();
//        float offX = Math.abs(motionEventX - mLastDownX);
//        float offY = Math.abs(motionEventY - mLastDownY);
//
//        long offTime = System.currentTimeMillis() - mLostDownTime;
//
//        if (offX < mTouchSlop && offY < mTouchSlop && offTime >= LONG_CLICK_LIMIT) {
//            //振动
////            mVibrator.vibrate(100);
//            new ScreenCap().execute();
//            flag = true;
//        }
//
//        return flag;
//    }
//
//    /**
//     * 获取状态栏高度还是这种方式靠谱
//     *
//     * @return
//     */
//    public int getStatusBarHeight() {
//        int statusBarHeight = 0;
//        try {
//            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
//            Object o = clazz.newInstance();
//            Field barHeight = clazz.getField("status_bar_height");
//            int x = (int) barHeight.get(o);
//            statusBarHeight = getResources().getDimensionPixelSize(x);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Log.e(TAG, "用反射=" + statusBarHeight);
//        return statusBarHeight;
//    }
//
//    /**
//     * 不靠谱
//     *
//     * @param view
//     * @return
//     */
//    private int getStatusBarHeight(View view) {
//        Rect rect = new Rect();
//        getWindowVisibleDisplayFrame(rect);
//        Log.e(TAG, "不用反射=" + rect.top);
//        return rect.top;
//    }
//
//
//    private int dip2px(float dip) {
//        float density = getContext().getResources().getDisplayMetrics().density;
//        return (int) (dip * density + 0.5f * (dip >= 0 ? 1 : -1));
//    }
//
//    public void setLayoutParam(WindowManager.LayoutParams params) {
//        this.mParams = params;
//
//    }
//}