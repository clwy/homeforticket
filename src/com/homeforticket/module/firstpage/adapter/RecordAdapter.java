package com.homeforticket.module.firstpage.adapter;

import java.util.List;

import com.bumptech.glide.Glide;
import com.homeforticket.R;
import com.homeforticket.module.buyticket.model.BuyticketInfo;
import com.homeforticket.module.buyticket.model.TicketInfo;
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

public class RecordAdapter extends BaseAdapter {

	private Context mContext;
	private List<RecordInfo> list;

    public RecordAdapter(Context context) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_record, parent, false);
            holder.date = (TextView) convertView.findViewById(R.id.date_title);
            holder.income = (TextView) convertView.findViewById(R.id.incom_title);
            holder.currentMoney = (TextView) convertView.findViewById(R.id.current_money_title);
            holder.remark = (TextView) convertView.findViewById(R.id.abstract_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final RecordInfo recordInfo = list.get(position);
        holder.date.setText(recordInfo.getDate());
        holder.remark.setText(recordInfo.getTitle());
        holder.currentMoney.setText(recordInfo.getCurrentMoney());
        String type = recordInfo.getType();
        
        if ("0".equals(type)) {
            holder.income.setText("+" + recordInfo.getIncome());
            holder.income.setTextColor(Color.parseColor("#ff9024"));
        } else if ("1".equals(type)) {
            holder.income.setText("-" + recordInfo.getWithdraw());
            holder.income.setTextColor(Color.parseColor("#1db2f6"));
        }

        return convertView;
    }

	/**
	 * 清空列表
	 */
	public void clear() {
		list.clear();
	}

	public List<RecordInfo> getList() {
		return list;
	}

	public void setList(List<RecordInfo> list) {
		this.list = list;
	}

	static class ViewHolder {
	    TextView date;
	    TextView income;
	    TextView currentMoney;
	    TextView remark;
	}

}
