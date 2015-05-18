
package com.homeforticket.fragment;

import com.homeforticket.util.ToastUtil;

import android.support.v4.app.Fragment;
import android.widget.Toast;

public class BaseFragment extends Fragment {
    protected boolean mIsVisibleToUser;

    public boolean isFirst() {
        return true;
    }

    public boolean isEnd() {
        return true;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        mIsVisibleToUser = isVisibleToUser;
        super.setUserVisibleHint(isVisibleToUser);
    }

    public interface MyPageChangeListener {
        public void onPageSelected(int position);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 显示提示信息
     * 
     * @param s
     */
    public void showToast(String s) {
        if (getActivity() != null && !getActivity().isFinishing()) {
            ToastUtil.showToast(s, Toast.LENGTH_LONG);
        }
    }

    public void showToast(int resId) {
        showToast(getString(resId));
    }
    
    /**
     * 重置页面
     */
    public void resetFragment() {

    }

    /**
     * 底部切换通知fragment开始执行方法
     */
    public void initFragment() {

    }
}
