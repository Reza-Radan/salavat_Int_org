package custom_dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;

import com.flyco.animation.BaseAnimatorSet;
import com.nineoldandroids.animation.Animator;

public abstract class BaseDialog<T extends BaseDialog<T>> extends Dialog {
    /** mTag(æ—¥å¿—) */
    protected String mTag;
    /** mContext(ä¸Šä¸‹æ–‡) */
    protected Context mContext;
    /** (DisplayMetrics)è®¾å¤‡å¯†åº¦ */
    protected DisplayMetrics mDisplayMetrics;
    /** enable dismiss outside dialog(è®¾ç½®ç‚¹å‡»å¯¹è¯�æ¡†ä»¥å¤–åŒºåŸŸ,æ˜¯å�¦dismiss) */
    protected boolean mCancel;
    /** dialog width scale(å®½åº¦æ¯”ä¾‹) */
    protected float mWidthScale = 1;
    /** dialog height scale(é«˜åº¦æ¯”ä¾‹) */
    protected float mHeightScale;
    /** mShowAnim(å¯¹è¯�æ¡†æ˜¾ç¤ºåŠ¨ç”») */
    private BaseAnimatorSet mShowAnim;
    /** mDismissAnim(å¯¹è¯�æ¡†æ¶ˆå¤±åŠ¨ç”») */
    private BaseAnimatorSet mDismissAnim;
    /** top container(æœ€ä¸Šå±‚å®¹å™¨) */
    protected LinearLayout mLlTop;
    /** container to control dialog height(ç”¨äºŽæŽ§åˆ¶å¯¹è¯�æ¡†é«˜åº¦) */
    protected LinearLayout mLlControlHeight;
    /** the child of mLlControlHeight you create.(åˆ›å»ºå‡ºæ�¥çš„mLlControlHeightçš„ç›´æŽ¥å­�View) */
    protected View mOnCreateView;
    /** is mShowAnim running(æ˜¾ç¤ºåŠ¨ç”»æ˜¯å�¦æ­£åœ¨æ‰§è¡Œ) */
    private boolean mIsShowAnim;
    /** is DismissAnim running(æ¶ˆå¤±åŠ¨ç”»æ˜¯å�¦æ­£åœ¨æ‰§è¡Œ) */
    private boolean mIsDismissAnim;
    /** max height(æœ€å¤§é«˜åº¦) */
    protected float mMaxHeight;
    /** show Dialog as PopupWindow(åƒ�PopupWindowä¸€æ ·å±•ç¤ºDialog) */
    private boolean mIsPopupStyle;
    /** automatic dimiss dialog after given delay(åœ¨ç»™å®šæ—¶é—´å�Ž,è‡ªåŠ¨æ¶ˆå¤±) */
    private boolean mAutoDismiss;
    /** delay (milliseconds) to dimiss dialog(å¯¹è¯�æ¡†æ¶ˆå¤±å»¶æ—¶æ—¶é—´,æ¯«ç§’å€¼) */
    private long mAutoDismissDelay = 1500;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    /**
     * method execute order:
     * show:constrouctor---show---oncreate---onStart---onAttachToWindow
     * dismiss:dismiss---onDetachedFromWindow---onStop
     */
    public BaseDialog(Context context) {
        super(context);
        setDialogTheme();
        mContext = context;
        mTag = getClass().getSimpleName();
        setCanceledOnTouchOutside(true);
    }

    public BaseDialog(Context context, boolean isPopupStyle) {
        this(context);
        mIsPopupStyle = isPopupStyle;
    }

    /** set dialog theme(è®¾ç½®å¯¹è¯�æ¡†ä¸»é¢˜) */
    private void setDialogTheme() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);// android:windowNoTitle
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));// android:windowBackground
        getWindow().addFlags(LayoutParams.FLAG_DIM_BEHIND);// android:backgroundDimEnabledé»˜è®¤æ˜¯trueçš„
    }

    /**
     * inflate layout for dialog ui and return (å¡«å……å¯¹è¯�æ¡†æ‰€éœ€è¦�çš„å¸ƒå±€å¹¶è¿”å›ž)
     * <pre>
     * public View onCreateView() {
     *      View inflate = View.inflate(mContext, R.layout.dialog_share, null);
     *      return inflate;
     * }
     * </pre>
     */
    public abstract View onCreateView();

    public void onViewCreated(View inflate) {
    }

    /** set Ui data or logic opreation before attatched window(åœ¨å¯¹è¯�æ¡†æ˜¾ç¤ºä¹‹å‰�,è®¾ç½®ç•Œé�¢æ•°æ�®æˆ–è€…é€»è¾‘) */
    public abstract void setUiBeforShow();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mDisplayMetrics = mContext.getResources().getDisplayMetrics();
        mMaxHeight = mDisplayMetrics.heightPixels - StatusBarUtils.getHeight(mContext);
        // mMaxHeight = mDisplayMetrics.heightPixels;

        mLlTop = new LinearLayout(mContext);
        mLlTop.setGravity(Gravity.CENTER);

        mLlControlHeight = new LinearLayout(mContext);
        mLlControlHeight.setOrientation(LinearLayout.VERTICAL);

        mOnCreateView = onCreateView();
        mLlControlHeight.addView(mOnCreateView);
        mLlTop.addView(mLlControlHeight);
        onViewCreated(mOnCreateView);

        if (mIsPopupStyle) {
            setContentView(mLlTop, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
        } else {
            setContentView(mLlTop, new ViewGroup.LayoutParams(mDisplayMetrics.widthPixels, (int) mMaxHeight));
        }

        mLlTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCancel) {
                    dismiss();
                }
            }
        });

        mOnCreateView.setClickable(true);
    }

    /** get actual created view(èŽ·å�–å®žé™…åˆ›å»ºçš„View) */
    public View getCreateView() {
        return mOnCreateView;
    }

    /**
     * when dailog attached to window,set dialog width and height and show anim
     * (å½“dailogä¾�é™„åœ¨windowä¸Š,è®¾ç½®å¯¹è¯�æ¡†å®½é«˜ä»¥å�Šæ˜¾ç¤ºåŠ¨ç”»)
     */
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        setUiBeforShow();

        int width;
        if (mWidthScale == 0) {
            width = ViewGroup.LayoutParams.WRAP_CONTENT;
        } else {
            width = (int) (mDisplayMetrics.widthPixels * mWidthScale);
        }

        int height;
        if (mHeightScale == 0) {
            height = ViewGroup.LayoutParams.WRAP_CONTENT;
        } else if (mHeightScale == 1) {
//            height = ViewGroup.LayoutParams.MATCH_PARENT;
            height = (int) mMaxHeight;
        } else {
            height = (int) (mMaxHeight * mHeightScale);
        }

        mLlControlHeight.setLayoutParams(new LinearLayout.LayoutParams(width, height));

        if (mShowAnim != null) {
            mShowAnim.listener(new BaseAnimatorSet.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    mIsShowAnim = true;
                }

                @Override
                public void onAnimationRepeat(Animator animator) {
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    mIsShowAnim = false;
                    delayDismiss();
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                    mIsShowAnim = false;
                }
            }).playOn(mLlControlHeight);
        } else {
            BaseAnimatorSet.reset(mLlControlHeight);
            delayDismiss();
        }
    }


    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        this.mCancel = cancel;
        super.setCanceledOnTouchOutside(cancel);
    }

    @Override
    public void show() {
        super.show();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    public void dismiss() {
        if (mDismissAnim != null) {
            mDismissAnim.listener(new BaseAnimatorSet.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    mIsDismissAnim = true;
                }

                @Override
                public void onAnimationRepeat(Animator animator) {
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    mIsDismissAnim = false;
                    superDismiss();
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                    mIsDismissAnim = false;
                    superDismiss();
                }
            }).playOn(mLlControlHeight);
        } else {
            superDismiss();
        }
    }

    /** dismiss without anim(æ— åŠ¨ç”»dismiss) */
    public void superDismiss() {
        super.dismiss();
    }

    /** dialog anim by styles(åŠ¨ç”»å¼¹å‡ºå¯¹è¯�æ¡†,styleåŠ¨ç”»èµ„æº�) */
    public void show(int animStyle) {
        Window window = getWindow();
        window.setWindowAnimations(animStyle);
        show();
    }

    /** show at location only valid for mIsPopupStyle true(æŒ‡å®šä½�ç½®æ˜¾ç¤º,å�ªå¯¹isPopupStyleä¸ºtrueæœ‰æ•ˆ) */
    public void showAtLocation(int gravity, int x, int y) {
        if (mIsPopupStyle) {
            Window window = getWindow();
            LayoutParams params = window.getAttributes();
            window.setGravity(gravity);
            params.x = x;
            params.y = y;
        }

        show();
    }

    /** show at location only valid for mIsPopupStyle true(æŒ‡å®šä½�ç½®æ˜¾ç¤º,å�ªå¯¹isPopupStyleä¸ºtrueæœ‰æ•ˆ) */
    public void showAtLocation(int x, int y) {
        int gravity = Gravity.LEFT | Gravity.TOP;//Left Top (å��æ ‡åŽŸç‚¹ä¸ºå�³ä¸Šè§’)
        showAtLocation(gravity, x, y);
    }

    /** set window dim or not(è®¾ç½®èƒŒæ™¯æ˜¯å�¦æ˜�æš—) */
    public T dimEnabled(boolean isDimEnabled) {
        if (isDimEnabled) {
            getWindow().addFlags(LayoutParams.FLAG_DIM_BEHIND);
        } else {
            getWindow().clearFlags(LayoutParams.FLAG_DIM_BEHIND);
        }
        return (T) this;
    }

    /** set dialog width scale:0-1(è®¾ç½®å¯¹è¯�æ¡†å®½åº¦,å� å±�å¹•å®½çš„æ¯”ä¾‹0-1) */
    public T widthScale(float widthScale) {
        this.mWidthScale = widthScale;
        return (T) this;
    }

    /** set dialog height scale:0-1(è®¾ç½®å¯¹è¯�æ¡†é«˜åº¦,å� å±�å¹•å®½çš„æ¯”ä¾‹0-1) */
    public T heightScale(float heightScale) {
        mHeightScale = heightScale;
        return (T) this;
    }

    /** set show anim(è®¾ç½®æ˜¾ç¤ºçš„åŠ¨ç”») */
    public T showAnim(BaseAnimatorSet showAnim) {
        mShowAnim = showAnim;
        return (T) this;
    }

    /** set dismiss anim(è®¾ç½®éš�è—�çš„åŠ¨ç”») */
    public T dismissAnim(BaseAnimatorSet dismissAnim) {
        mDismissAnim = dismissAnim;
        return (T) this;
    }

    /** automatic dimiss dialog after given delay(åœ¨ç»™å®šæ—¶é—´å�Ž,è‡ªåŠ¨æ¶ˆå¤±) */
    public T autoDismiss(boolean autoDismiss) {
        mAutoDismiss = autoDismiss;
        return (T) this;
    }

    /** set dealy (milliseconds) to dimiss dialog(å¯¹è¯�æ¡†æ¶ˆå¤±å»¶æ—¶æ—¶é—´,æ¯«ç§’å€¼) */
    public T autoDismissDelay(long autoDismissDelay) {
        mAutoDismissDelay = autoDismissDelay;
        return (T) this;
    }

    private void delayDismiss() {
        if (mAutoDismiss && mAutoDismissDelay > 0) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dismiss();
                }
            }, mAutoDismissDelay);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mIsDismissAnim || mIsShowAnim || mAutoDismiss) {
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {
        if (mIsDismissAnim || mIsShowAnim || mAutoDismiss) {
            return;
        }
        super.onBackPressed();
    }

    /** dp to px */
    protected int dp2px(float dp) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
