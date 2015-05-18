package com.homeforticket.module.buyticket.adapter;

import java.util.List;

import com.bumptech.glide.Glide;
import com.homeforticket.R;
import com.homeforticket.module.buyticket.activity.TicketActivity;
import com.homeforticket.module.buyticket.model.BuyticketInfo;
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
import android.widget.TextView;

public class BuyticketAdapter extends BaseAdapter {

	private Context mContext;
	private List<TicketInfo> list;

    public BuyticketAdapter(Context context) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_buyticket, parent, false);
            holder.itemPic = (ImageView) convertView.findViewById(R.id.item_pic);
            holder.posName = (TextView) convertView.findViewById(R.id.ticket_location);
            holder.TicketName = (TextView) convertView.findViewById(R.id.ticket_title);
            holder.TicketLevel = (TextView) convertView.findViewById(R.id.ticket_level);
            holder.currentCost = (TextView) convertView.findViewById(R.id.current_cost);
            holder.originalCost = (TextView) convertView.findViewById(R.id.original_cost);
            holder.originalCost.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG ); 
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final TicketInfo ticketInfo = list.get(position);
        Glide.with(mContext).load(ticketInfo.getPicName()).into(holder.itemPic);
        holder.posName.setText(ticketInfo.getCity());
        holder.TicketName.setText(ticketInfo.getSceneName());
        holder.TicketLevel.setText(ticketInfo.getSceneLevel());
        holder.currentCost.setText(ticketInfo.getPrice());
        holder.originalCost.setText(ticketInfo.getRetailPrice());
        
        if (convertView != null) {
            convertView.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, TicketActivity.class);
                    intent.putExtra("id", ticketInfo.getSceneId());
                    intent.putExtra("current", ticketInfo.getRetailPrice());
                    intent.putExtra("original", ticketInfo.getMarketPrice());
                    mContext.startActivity(intent);
                }
            });
        }

        return convertView;
    }

	/**
	 * 清空列表
	 */
	public void clear() {
		list.clear();
	}

	public List<TicketInfo> getList() {
		return list;
	}

	public void setList(List<TicketInfo> list) {
		this.list = list;
	}

	static class ViewHolder {
	    ImageView itemPic;
	    TextView posName;
	    TextView TicketName;
	    TextView TicketLevel;
	    TextView currentCost;
	    TextView originalCost;

	}

}
