
package com.homeforticket.module.firstpage.adapter;

import java.util.ArrayList;
import java.util.List;

import com.homeforticket.R;
import com.homeforticket.module.firstpage.model.OrderStatusInfo;
import com.homeforticket.module.firstpage.model.OrderStatusMessage;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HorizontalGridAdapter extends BaseAdapter
{

    private LayoutInflater mInflater = null;
    protected List<OrderStatusInfo> mOrderStatusInfo = new ArrayList<OrderStatusInfo>();
    protected int mSelectedIndex = 0;

    public HorizontalGridAdapter(Context context){
        mInflater = LayoutInflater.from(context);
    }

    public void setDataSet(List<OrderStatusInfo> orderStatusMessage){
        mOrderStatusInfo = orderStatusMessage;
    }

    @Override
    public int getCount(){
        return mOrderStatusInfo.size();
    }

    @Override
    public Object getItem(int position){
        if (mOrderStatusInfo != null) {
            return mOrderStatusInfo.get(position);
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

        if (mOrderStatusInfo != null && position < mOrderStatusInfo.size())
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

            final OrderStatusInfo orderStatusInfo = mOrderStatusInfo.get(position);
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
