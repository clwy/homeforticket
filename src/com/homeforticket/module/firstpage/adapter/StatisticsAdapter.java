package com.homeforticket.module.firstpage.adapter;

import java.util.List;

import com.bumptech.glide.Glide;
import com.homeforticket.R;
import com.homeforticket.module.buyticket.model.BuyticketInfo;
import com.homeforticket.module.buyticket.model.TicketInfo;
import com.homeforticket.module.firstpage.model.RecordInfo;
import com.homeforticket.module.firstpage.model.StatisticsInfo;
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

public class StatisticsAdapter extends BaseAdapter {

	private Context mContext;
	private List<StatisticsInfo> list;

    public StatisticsAdapter(Context context) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_statistics, parent, false);
            holder.date = (TextView) convertView.findViewById(R.id.date_title);
            holder.total = (TextView) convertView.findViewById(R.id.total_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final StatisticsInfo info = list.get(position);
        holder.date.setText(info.getDate());
        holder.total.setText(info.getTotal());

        return convertView;
    }

	/**
	 * 清空列表
	 */
	public void clear() {
		list.clear();
	}

	public List<StatisticsInfo> getList() {
		return list;
	}

	public void setList(List<StatisticsInfo> list) {
		this.list = list;
	}

	static class ViewHolder {
	    TextView date;
	    TextView total;
	}

}
