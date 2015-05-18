
package com.homeforticket.module.buyticket.adapter;

import com.bumptech.glide.Glide;
import com.homeforticket.R;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

/**
 * @Title: TopPagerAdapter.java
 * @Package com.homeforticket.module.buyticket.adapter
 * @Description: TODO
 * @author LR
 * @date 2015年5月3日 上午9:06:03
 */
public class TopPagerAdapter extends PagerAdapter {
    private String[] mSceneImage;
    private Context mContext;

    public TopPagerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public void destroyItem(View view, int position, Object object) {
        ((ViewPager) view).removeView((View) object);
    }

    @Override
    public void finishUpdate(View arg0) {

    }

    @Override
    public int getCount() {
        if (mSceneImage != null) {
            return mSceneImage.length;
        }
        return 0;
    }

    @Override
    public Object instantiateItem(View view, int position) {

        View convertView = LayoutInflater.from(mContext)
                .inflate(R.layout.list_item_top_pic, null);
        ImageView iv = (ImageView) convertView.findViewById(R.id.top_pic);
        Glide.with(mContext).load(mSceneImage[position]).centerCrop().into(iv);

        ((ViewPager) view).addView(convertView, 0);
        return convertView;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return (arg0 == arg1);
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {

    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(View arg0) {

    }

    /**
     * @return the mSceneImage
     */
    public String[] getmSceneImage() {
        return mSceneImage;
    }

    /**
     * @param mSceneImage the mSceneImage to set
     */
    public void setmSceneImage(String[] mSceneImage) {
        this.mSceneImage = mSceneImage;
    }
}
