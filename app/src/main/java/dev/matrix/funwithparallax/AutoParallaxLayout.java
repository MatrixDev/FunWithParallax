package dev.matrix.funwithparallax;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

/**
 * @author rostyslav.lesovyi
 */
public class AutoParallaxLayout extends FrameLayout implements ViewTreeObserver.OnPreDrawListener {

	private int mOffsetX;
	private int mOffsetY;
	private float mStrength = 0.1f;
	private int[] mLocationCache = new int[2];

	public AutoParallaxLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();

		getViewTreeObserver().addOnPreDrawListener(this);
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();

		getViewTreeObserver().removeOnPreDrawListener(this);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(
				updateMeasureSpec(widthMeasureSpec),
				updateMeasureSpec(heightMeasureSpec)
		);

		int width = (int) (getMeasuredWidth() / (1 + 2 * mStrength));
		int height = (int) (getMeasuredHeight() / (1 + 2 * mStrength));

		setMeasuredDimension(width, height);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		mOffsetX = (int) ((right - left) * mStrength);
		mOffsetY = (int) ((bottom - top) * mStrength);

		super.onLayout(changed, left - mOffsetX, top - mOffsetY, right + mOffsetX, bottom + mOffsetY);

		for (int index = getChildCount() - 1; index >= 0; --index) {
			View view = getChildAt(index);
			view.offsetLeftAndRight(-mOffsetX);
			view.offsetTopAndBottom(-mOffsetY);
		}
	}

	@Override
	public boolean onPreDraw() {
		getLocationInWindow(mLocationCache);

		View root = getRootView();
		int width = getWidth();
		int height = getHeight();
		int rootWidth = root.getWidth();
		int rootHeight = root.getHeight();

		// parallax effect [0..1]
		float parallaxX = width < rootWidth ? mLocationCache[0] / (float) (rootWidth - width) : .5f;
		float parallaxY = height < rootHeight ? mLocationCache[1] / (float) (rootHeight - height) : .5f;

		// parallax offset [-value..+value]
		float offsetX = (parallaxX * 2 - 1f) * mOffsetX;
		float offsetY = (parallaxY * 2 - 1f) * mOffsetY;

		for (int index = getChildCount() - 1; index >= 0; --index) {
			View view = getChildAt(index);
			view.setTranslationX(-offsetX);
			view.setTranslationY(-offsetY);
		}

		return true;
	}

	private int updateMeasureSpec(int measureSpec) {
		int mode = MeasureSpec.getMode(measureSpec);
		if (mode != MeasureSpec.EXACTLY) {
			return measureSpec;
		}
		int size = MeasureSpec.getSize(measureSpec);
		int offset = (int) (size * mStrength);
		return MeasureSpec.makeMeasureSpec(size + offset * 2, mode);
	}
}
