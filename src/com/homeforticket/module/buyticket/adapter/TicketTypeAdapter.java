
package com.homeforticket.module.buyticket.adapter;

import java.util.HashMap;
import java.util.List;

import com.bumptech.glide.Glide;
import com.homeforticket.R;
import com.homeforticket.module.buyticket.activity.TicketActivity;
import com.homeforticket.module.buyticket.model.BuyticketInfo;
import com.homeforticket.module.buyticket.model.SceneTicketInfo;
import com.homeforticket.module.buyticket.model.TicketInfo;
import com.homeforticket.module.message.model.MessageInfo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class TicketTypeAdapter extends BaseAdapter {

    private Context mContext;
    private List<SceneTicketInfo> list;
    private int mChooseIndex = 0;

    public TicketTypeAdapter(Context context) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_ticket_type,
                    parent, false);
            holder.ticketName = (TextView) convertView.findViewById(R.id.ticket_type_title);
            holder.currentPrice = (TextView) convertView.findViewById(R.id.current_price);
            holder.originalTitle = (TextView) convertView.findViewById(R.id.original_price_title);
            holder.originalPrice = (TextView) convertView.findViewById(R.id.original_price);
            holder.selectedButton = (ImageView) convertView.findViewById(R.id.selected_button);
            holder.originalTitle.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.originalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final SceneTicketInfo ticketInfo = list.get(position);
        holder.ticketName.setText(ticketInfo.getProductName());
        holder.currentPrice.setText(ticketInfo.getRetailPrice());
        holder.originalPrice.setText(ticketInfo.getMarketPrice());
        holder.selectedButton.setBackgroundResource(mChooseIndex == position ? R.drawable.selected : R.drawable.unselected);
        return convertView;
    }

    /**
     * 清空列表
     */
    public void clear() {
        list.clear();
    }

    public List<SceneTicketInfo> getList() {
        return list;
    }

    public void setList(List<SceneTicketInfo> list) {
        this.list = list;
    }

    public int getChooseIndex() {
        return mChooseIndex;
    }

    public void setChooseIndex(int mChooseIndex) {
        this.mChooseIndex = mChooseIndex;
    }

    static class ViewHolder {
        TextView ticketName;
        TextView currentPrice;
        TextView originalPrice;
        TextView originalTitle;
        ImageView selectedButton;
    }

}
