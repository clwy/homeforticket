package com.homeforticket.common.share;

import com.homeforticket.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.RelativeLayout;

public class OauthDialog extends Dialog {

	private WebView mWebView;
	private RelativeLayout webViewContainer;
	private RelativeLayout mContent;

	public OauthDialog(Context context, WebView webView) {
		super(context, R.style.ContentOverlay);
		mWebView = webView;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContent = new RelativeLayout(getContext());
		addContentView(mContent, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		setUpWebView();
	}

	private void setUpWebView() {
		webViewContainer = new RelativeLayout(getContext());
		LayoutParams fill = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mWebView.setLayoutParams(fill);
		webViewContainer.addView(mWebView);

		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		// Resources resources = getContext().getResources();
		// lp.leftMargin =
		// resources.getDimensionPixelSize(R.dimen.dialog_left_margin);
		// lp.topMargin =
		// resources.getDimensionPixelSize(R.dimen.dialog_top_margin);
		// lp.rightMargin =
		// resources.getDimensionPixelSize(R.dimen.dialog_right_margin);
		// lp.bottomMargin =
		// resources.getDimensionPixelSize(R.dimen.dialog_bottom_margin);
		mContent.addView(webViewContainer, lp);
	}

}
