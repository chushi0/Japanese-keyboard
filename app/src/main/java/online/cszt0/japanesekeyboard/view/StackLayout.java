package online.cszt0.japanesekeyboard.view;

import java.util.Iterator;
import java.util.LinkedList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class StackLayout extends FrameLayout {

	private LinkedList<View> stack;

	public StackLayout(Context context) {
		this(context, null);
	}

	public StackLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		stack = new LinkedList<>();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		int childCount = getChildCount();
		if (childCount > 0) {
			push(0);
		}
	}

	@Override
	public void addView(View child, int index, ViewGroup.LayoutParams params) {
		super.addView(child, index, params);
		child.setVisibility(GONE);
	}

	@Override
	public void removeAllViews() {
		super.removeAllViews();
		Iterator<View> iterator = stack.iterator();
		while (iterator.hasNext()) {
			iterator.next();
			iterator.remove();
		}
	}

	@Override
	public void removeView(View view) {
		super.removeView(view);
		Iterator<View> iterator = stack.iterator();
		while (iterator.hasNext()) {
			View v = iterator.next();
			if (v == view) {
				iterator.remove();
			}
		}
	}

	@Override
	public void removeViewAt(int index) {
		View view = getChildAt(index);
		super.removeViewAt(index);
		Iterator<View> iterator = stack.iterator();
		while (iterator.hasNext()) {
			View v = iterator.next();
			if (v == view) {
				iterator.remove();
			}
		}
	}

	/**
	 * 将指定控件“入栈”
	 * 
	 * @param index
	 *            控件位置
	 */
	public void push(int index) {
		View view = getChildAt(index);
		// 隐藏当前显示内容
		if (stack.size() > 0) {
			stack.getLast().setVisibility(GONE);
		}
		// 显示指定控件
		view.setVisibility(VISIBLE);
		stack.push(view);
	}

	/**
	 * “出栈”
	 */
	public void pop() {
		stack.pop().setVisibility(GONE);
		// 显示上一个
		if (stack.size() > 0) {
			stack.getLast().setVisibility(VISIBLE);
		}
	}

}
