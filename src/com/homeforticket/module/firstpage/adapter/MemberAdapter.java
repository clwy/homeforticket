package com.homeforticket.module.firstpage.adapter;

import java.util.List;

import com.bumptech.glide.Glide;
import com.homeforticket.R;
import com.homeforticket.module.buyticket.model.BuyticketInfo;
import com.homeforticket.module.buyticket.model.TicketInfo;
import com.homeforticket.module.firstpage.model.OrderContentInfo;
import com.homeforticket.module.firstpage.model.RecordInfo;
import com.homeforticket.module.message.model.MessageInfo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MemberAdapter extends BaseAdapter {

	private Context mContext;
	private List<OrderContentInfo> list;
	private boolean isShow;

    public MemberAdapter(Context context) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_order_member, parent, false);
            holder.name = (TextView) convertView.findViewById(R.id.member_name);
            holder.mobile = (TextView) convertView.findViewById(R.id.member_mobile);
            holder.card = (TextView) convertView.findViewById(R.id.member_card);
            holder.seat = (TextView) convertView.findViewById(R.id.member_seat);
            holder.seat.setVisibility(View.GONE);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final OrderContentInfo info = list.get(position);
        holder.name.setText(info.getBuyerName());
        holder.mobile.setText(info.getBuyerMobile());
        holder.card.setText(info.getBuyerICard());
        
        if (isShow) {
            holder.seat.setVisibility(View.VISIBLE);
            holder.seat.setText(info.getSeat());
        } else {
            holder.seat.setVisibility(View.GONE);
        }

        return convertView;
    }

	/**
	 * 清空列表
	 */
	public void clear() {
		list.clear();
	}

	public List<OrderContentInfo> getList() {
		return list;
	}

	public void setList(List<OrderContentInfo> list) {
		this.list = list;
	}

	/**
     * @return the isShow
     */
    public boolean isShow() {
        return isShow;
    }

    /**
     * @param isShow the isShow to set
     */
    public void setShow(boolean isShow) {
        this.isShow = isShow;
    }

    static class ViewHolder {
	    private TextView name;
	    private TextView mobile;
	    private TextView card;
	    private TextView seat;
	}

}
