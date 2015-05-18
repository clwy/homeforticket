package com.homeforticket.module.message.adapter;

import java.util.List;

import com.bumptech.glide.Glide;
import com.homeforticket.R;
import com.homeforticket.module.buyticket.model.BuyticketInfo;
import com.homeforticket.module.buyticket.model.TicketInfo;
import com.homeforticket.module.firstpage.model.RecordInfo;
import com.homeforticket.module.message.model.MessageInfo;
import com.homeforticket.module.message.model.SystemInfo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SystemMessageAdapter extends BaseAdapter {

	private Context mContext;
	private List<SystemInfo> list;

    public SystemMessageAdapter(Context context) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_system_message, parent, false);
            holder.date = (TextView) convertView.findViewById(R.id.message_date);
            holder.title = (TextView) convertView.findViewById(R.id.system_message_title);
            holder.content = (TextView) convertView.findViewById(R.id.message_content);
            holder.remark = (TextView) convertView.findViewById(R.id.message_remark);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final SystemInfo info = list.get(position);
        holder.date.setText(info.getNewsDate());
        if ("0".equals(info.getType())) {
            holder.title.setText(R.string.system_message_string);
        }
        
        holder.content.setText(info.getContent());
        holder.remark.setText(info.getTitle());

        return convertView;
    }

	/**
	 * 清空列表
	 */
	public void clear() {
		list.clear();
	}

	public List<SystemInfo> getList() {
		return list;
	}

	public void setList(List<SystemInfo> list) {
		this.list = list;
	}

	static class ViewHolder {
	    TextView date;
	    TextView title;
	    TextView content;
	    TextView remark;
	}

}
