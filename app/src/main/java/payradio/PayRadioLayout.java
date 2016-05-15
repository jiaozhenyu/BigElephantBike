package payradio;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.administrator.bigelephantbike.R;


public class PayRadioLayout extends LinearLayout implements Checkable {

	private Context context;
	private ImageView payLogo;
	private TextView payTitle;
	private TextView payDesc;
	private RadioButton payChecked;	
	
	private boolean mChecked;	////鐘舵?佹槸鍚﹂?変腑
	private int id;
    private int mButtonResource;
    private boolean mBroadcasting;
    private Drawable mButtonDrawable;
    private OnCheckedChangeListener mOnCheckedChangeListener;	////閫変腑鐘舵?佹敼鍙樼洃鍚?
    private OnCheckedChangeListener mOnCheckedChangeWidgetListener;
    
    private static final int[] CHECKED_STATE_SET = {
        android.R.attr.state_checked
    };

	public PayRadioLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.payment_list_item,this);
		
		payLogo = (ImageView) findViewById(R.id.pay_icon);
		payTitle = (TextView) findViewById(R.id.pay_name);
		payDesc = (TextView) findViewById(R.id.pay_desc);
		payChecked = (RadioButton) findViewById(R.id.pay_check);
		
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PayRidioButton);
		
		Drawable d = array.getDrawable(R.styleable.PayRidioButton_radio);
		if (d != null) {
//			setButtonDrawable(d);
			payChecked.setButtonDrawable(d);
		}
		
		String title = array.getString(R.styleable.PayRidioButton_mtitle);
		if (title != null) {
			setTextTitle(title);
		}
		
		String str = array.getString(R.styleable.PayRidioButton_desc);
		if (str != null) {
			setTextDesc(str);
		}
		
		Drawable logo = array.getDrawable(R.styleable.PayRidioButton_mlogo);
		if (logo != null) {
			setDrawableLogin(logo);
		}
		
		boolean checked = array.getBoolean(R.styleable.PayRidioButton_checked, false);
		payChecked.setChecked(checked);
		
		array.recycle();
		setClickable(true);
		
		id = getId();
	}
	
	@Override
	public boolean isChecked() {
		// TODO Auto-generated method stub
		return mChecked;
	}

	@Override
	public void setChecked(boolean checked) {
		// TODO Auto-generated method stub
		if (mChecked != checked) {
            mChecked = checked;
            payChecked.refreshDrawableState();

            // Avoid infinite recursions if setChecked() is called from a listener
            if (mBroadcasting) {
                return;
            }

            mBroadcasting = true;
            if (mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener.onCheckedChanged(this, mChecked);
            }
            if (mOnCheckedChangeWidgetListener != null) {
                mOnCheckedChangeWidgetListener.onCheckedChanged(this, mChecked);
            }
            mBroadcasting = false;            
        }
	}

	@Override
	public void toggle() {
		// TODO Auto-generated method stub
		if (!isChecked()) {
			setChecked(!mChecked);
		}
	}
	
	@Override
	public boolean performClick() {
		// TODO Auto-generated method stub
		/*
         * XXX: These are tiny, need some surrounding 'expanded touch area',
         * which will need to be implemented in Button if we only override
         * performClick()
         */

        /* When clicked, toggle the state */
        toggle();
		return super.performClick();
	}
	
	/**
     * Register a callback to be invoked when the checked state of this button
     * changes.
     *
     * @param listener the callback to call on checked state change
     */
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    /**
     * Register a callback to be invoked when the checked state of this button
     * changes. This callback is used for internal purpose only.
     *
     * @param listener the callback to call on checked state change
     * @hide
     */
    void setOnCheckedChangeWidgetListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeWidgetListener = listener;
    }
    
    /**
     * Interface definition for a callback to be invoked when the checked state
     * of a compound button changed.
     */
    public static interface OnCheckedChangeListener {
        /**
         * Called when the checked state of a compound button has changed.
         *
         * @param buttonView The compound button view whose state has changed.
         * @param isChecked  The new checked state of buttonView.
         */
        void onCheckedChanged(PayRadioLayout buttonView, boolean isChecked);
    }
    
    /**
     * Set the background to a given Drawable, identified by its resource id.
     *
     * @param resid the resource id of the drawable to use as the background 
     */
    public void setButtonDrawable(int resid) {
        if (resid != 0 && resid == mButtonResource) {
            return;
        }

        mButtonResource = resid;

        Drawable d = null;
        if (mButtonResource != 0) {
            d = getResources().getDrawable(mButtonResource);
        }
        setButtonDrawable(d);
    }
    
    
    
    /**
     * Set the background to a given Drawable
     *
     * @param d The Drawable to use as the background
     */
    public void setButtonDrawable(Drawable d) {
    	if (d != null) {
            if (mButtonDrawable != null) {
                mButtonDrawable.setCallback(null);
//                unscheduleDrawable(mButtonDrawable);
                payChecked.unscheduleDrawable(mButtonDrawable);
            }
            d.setCallback(this);
            d.setState(getDrawableState());
            d.setVisible(getVisibility() == VISIBLE, false);
            mButtonDrawable = d;
            mButtonDrawable.setState(null);
//            setMinHeight(mButtonDrawable.getIntrinsicHeight());
        }
    	
    	//涓昏鍔熻兘鏄牴鎹綋鍓嶇殑鐘舵?佸?煎幓鏇存崲瀵瑰簲鐨勮儗鏅疍rawable瀵硅薄  
        payChecked.refreshDrawableState();
    }
    
    public void setTextDesc(String s) {
    	if (s != null) {
    		payDesc.setText(s);
        }
    }
    
    public void setTextTitle(String s) {
		if (s != null) {
			payTitle.setText(s);
		}
	}
    
    public String getTextTitle() {
    	String s = payTitle.getText().toString();
		return s == null ? "" : s;
	}
    
    public void setDrawableLogin (Drawable d) {
    	if (d != null) {
    		payLogo.setImageDrawable(d);
    	}
    }
    
    public void setChangeImg(int checkedId) {
    	System.out.println(">>" + checkedId);
    	System.out.println(">>" + id);
		if (checkedId == id) {
			payChecked.setChecked(true);
		} else {
			payChecked.setChecked(false);
		}
	}
    
    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            payChecked.mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    //鑾峰緱褰撳墠鐨勭姸鎬佸睘鎬?--- 鏁村瀷闆嗗悎 锛? 璋冪敤Drawable绫荤殑setState鏂规硶鍘昏幏鍙栬祫婧愩??  
    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        
        if (mButtonDrawable != null) {
            int[] myDrawableState = getDrawableState();
            
            // Set the state of the Drawable
            mButtonDrawable.setState(myDrawableState);
            
            invalidate();
        }
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || who == mButtonDrawable;
    }
    
    static class SavedState extends BaseSavedState {
        boolean checked;

        /**
         * Constructor called from {@link CompoundButton#onSaveInstanceState()}
         */
        SavedState(Parcelable superState) {
            super(superState);
        }
        
        /**
         * Constructor called from {@link #CREATOR}
         */
        private SavedState(Parcel in) {
            super(in);
            checked = (Boolean)in.readValue(null);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeValue(checked);
        }

        @Override
        public String toString() {
            return "CompoundButton.SavedState{"
                    + Integer.toHexString(System.identityHashCode(this))
                    + " checked=" + checked + "}";
        }

        public static final Creator<SavedState> CREATOR
                = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    @Override
    public Parcelable onSaveInstanceState() {
        // Force our ancestor class to save its state
    	//濡傛灉璁剧疆锛屽垯鏂囨湰瑙嗗浘浼氫繚鎸佸畬鏁寸殑鏂囨湰鍐呭锛屼互鍙婅薄褰撳墠鍏夋爣浣嶇疆杩欐牱鐨勯檮杩戜俊鎭?. 
//        setFreezesText(true);
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);

        ss.checked = isChecked();
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
  
        super.onRestoreInstanceState(ss.getSuperState());
        setChecked(ss.checked);
        requestLayout();
    }
    
}
