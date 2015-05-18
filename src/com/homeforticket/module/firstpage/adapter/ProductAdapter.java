
package com.homeforticket.module.firstpage.adapter;

import java.util.HashMap;
import java.util.List;

import com.bumptech.glide.Glide;
import com.homeforticket.R;
import com.homeforticket.module.buyticket.model.BuyticketInfo;
import com.homeforticket.module.buyticket.model.TicketInfo;
import com.homeforticket.module.firstpage.activity.ClientInfoActivity;
import com.homeforticket.module.firstpage.model.ClientInfo;
import com.homeforticket.module.firstpage.model.OrderInfo;
import com.homeforticket.module.firstpage.model.RecordInfo;
import com.homeforticket.module.message.model.MessageInfo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductAdapter extends BaseAdapter {
    private Context mContext;
    private List<OrderInfo> list;
    private HashMap<String, String> mStatus = new HashMap<String, String>();

    public ProductAdapter(Context context) {
        this.mContext = context;
        init();
    }

    private void init() {
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_order, parent,
                    false);
            holder.username = (TextView) convertView.findViewById(R.id.user_name);
            holder.orderState = (TextView) convertView.findViewById(R.id.order_state);
            holder.orderTitle = (TextView) convertView.findViewById(R.id.order_title);
            holder.pic = (ImageView) convertView.findViewById(R.id.item_pic);
            holder.leaveMessage = (TextView) convertView.findViewById(R.id.leave_words);
            holder.singlePrice = (TextView) convertView.findViewById(R.id.single_price);
            holder.count = (TextView) convertView.findViewById(R.id.count);
            holder.totalPrice = (TextView) convertView.findViewById(R.id.total_price);
            holder.countMiddle = (TextView) convertView.findViewById(R.id.count_middle);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final OrderInfo info = list.get(position);
        Glide.with(mContext).load(info.getPic()).centerCrop().into(holder.pic);
        holder.username.setText(info.getBuyName());
        holder.orderTitle.setText(info.getSceneName());
        holder.leaveMessage.setText(info.getContractNote());
        holder.singlePrice.setText(mContext.getString(R.string.price_sign) + info.getPrice());
        holder.count.setText(mContext.getString(R.string.X) + info.getTotalnum());
        holder.countMiddle.setText(info.getTotalnum());
        holder.totalPrice.setText(info.getPaidAmount());
        
        if (mStatus.containsKey(info.getOrderState())) {
            holder.orderState.setText(mStatus.get(info.getOrderState()));
        }

        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;
    }

    /**
     * 清空列表
     */
    public void clear() {
        list.clear();
    }

    public List<OrderInfo> getList() {
        return list;
    }

    public void setList(List<OrderInfo> list) {
        this.list = list;
    }

    static class ViewHolder {
        TextView username;
        TextView orderState;
        TextView orderTitle;
        ImageView pic;
        TextView leaveMessage;
        TextView singlePrice;
        TextView count;
        TextView totalPrice;
        TextView countMiddle;
    }

}
