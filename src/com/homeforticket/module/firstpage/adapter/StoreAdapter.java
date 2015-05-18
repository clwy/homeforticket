package com.homeforticket.module.firstpage.adapter;

import java.util.List;

import com.bumptech.glide.Glide;
import com.homeforticket.R;
import com.homeforticket.module.buyticket.activity.TicketActivity;
import com.homeforticket.module.buyticket.model.BuyticketInfo;
import com.homeforticket.module.buyticket.model.TicketInfo;
import com.homeforticket.module.firstpage.model.StoreInfo;
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

public class StoreAdapter extends BaseAdapter {

	private Context mContext;
	private List<StoreInfo> list;
	private boolean mIsNew = true;

    public StoreAdapter(Context context) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_store, parent, false);
            holder.itemPic = (ImageView) convertView.findViewById(R.id.item_pic);
            holder.name = (TextView) convertView.findViewById(R.id.ticket_title);
            holder.level = (TextView) convertView.findViewById(R.id.ticket_level);
            holder.price = (TextView) convertView.findViewById(R.id.new_price);
            holder.commissionPrice = (TextView) convertView.findViewById(R.id.commission_price);
            holder.location = (TextView) convertView.findViewById(R.id.ticket_location);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final StoreInfo info = list.get(position);
        Glide.with(mContext).load(info.getImg()).into(holder.itemPic);
        holder.name.setText(info.getName());
        holder.level.setText(info.getLevel());
        holder.price.setText(info.getPrice());
        holder.commissionPrice.setText(info.getCommission());
        holder.location.setText(info.getAddress());
        
        if (mIsNew) {
            holder.price.setTextColor(Color.parseColor("#ff9024"));
        } else {
            holder.price.setTextColor(Color.parseColor("#979797")); 
        }
        
        if (convertView != null) {
            convertView.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, TicketActivity.class);
                    intent.putExtra("id", info.getId());
                    intent.putExtra("current", info.getPrice());
                    intent.putExtra("original", info.getPrice());
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

	public List<StoreInfo> getList() {
		return list;
	}

	public void setList(List<StoreInfo> list) {
		this.list = list;
	}

	/**
     * @return the mIsNew
     */
    public boolean isIsNew() {
        return mIsNew;
    }

    /**
     * @param mIsNew the mIsNew to set
     */
    public void setIsNew(boolean mIsNew) {
        this.mIsNew = mIsNew;
    }

    static class ViewHolder {
	    ImageView itemPic;
	    TextView name;
	    TextView level;
	    TextView price;
	    TextView commissionPrice;
	    TextView location;
	}

}
