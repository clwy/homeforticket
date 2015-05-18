
package com.homeforticket.module.firstpage.adapter;

import java.util.ArrayList;
import java.util.List;

import com.homeforticket.R;
import com.homeforticket.module.firstpage.model.OrderStatusInfo;
import com.homeforticket.module.firstpage.model.OrderStatusMessage;
import com.homeforticket.module.firstpage.model.ProductChannelInfo;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HorizontalProductChannelGridAdapter extends BaseAdapter
{

    private LayoutInflater mInflater = null;
    protected List<ProductChannelInfo> mInfos = new ArrayList<ProductChannelInfo>();
    protected int mSelectedIndex = 0;

    public HorizontalProductChannelGridAdapter(Context context){
        mInflater = LayoutInflater.from(context);
    }

    public void setDataSet(List<ProductChannelInfo> orderStatusMessage){
        mInfos = orderStatusMessage;
    }

    @Override
    public int getCount(){
        return mInfos.size();
    }

    @Override
    public Object getItem(int position){
        if (mInfos != null) {
            return mInfos.get(position);
        }
        
        return null;
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = null;

        if (mInfos != null && position < mInfos.size())
        {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.horizontal_grid_item, null);
                holder = new ViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.horizontal_grid_title);
                holder.imageselected = (ImageView) convertView.findViewById(R.id.selected_icon);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final ProductChannelInfo orderStatusInfo = mInfos.get(position);
            holder.title.setText(orderStatusInfo.getName());
            holder.title.setTextColor(position == mSelectedIndex ? Color.parseColor("#1db2f6")
                    : Color.parseColor("#979797"));
            holder.imageselected.setVisibility(position == mSelectedIndex ? View.VISIBLE
                    : View.INVISIBLE);
        }

        return convertView;
    }

    public void setSelectedIndex(int index) {
        mSelectedIndex = index;
    }

    private class ViewHolder {
        TextView title;
        ImageView imageselected;
    }
}
