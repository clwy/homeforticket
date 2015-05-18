/*
 * Copyright (C) 2012 yueyueniao
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.homeforticket.framework;

import com.homeforticket.R;
import com.homeforticket.util.DisplayUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;


public class CenterView extends RelativeLayout {

    private View mCenterView;
	private RelativeLayout mBgShade;
	private Context mContext;
	private static CenterView sCenterView;

	public CenterView(Context context) {
		super(context);
		init(context);
	}

	public static CenterView getInstance() {
		return sCenterView;
	}

	private void init(Context context) {
		sCenterView = this;
		mContext = context;
		mBgShade = new RelativeLayout(context);

		LayoutParams bgParams = new LayoutParams(DisplayUtil.getScreenWidth(context), DisplayUtil.getScreenHeight(context));
		bgParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		mBgShade.setLayoutParams(bgParams);

	}

	public CenterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public CenterView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public void setCenterView(View view) {
		LayoutParams aboveParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		LayoutParams bgParams = new LayoutParams(DisplayUtil.getScreenWidth(mContext), DisplayUtil.getScreenHeight(mContext));
		bgParams.addRule(RelativeLayout.CENTER_IN_PARENT);

		View bgShadeContent = new View(mContext);
//		bgShadeContent.setBackgroundResource(R.drawable.shade_bg);
		mBgShade.addView(bgShadeContent, bgParams);

		addView(mBgShade, bgParams);
		addView(view, aboveParams);
		mCenterView = view;
		mCenterView.bringToFront();
	}
}
