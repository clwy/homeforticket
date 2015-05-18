package com.homeforticket.module.message.adapter;

import java.util.List;

import com.homeforticket.R;
import com.homeforticket.module.message.model.MessageInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageAdapter extends BaseAdapter {

	private Context mContext;
	private List<MessageInfo> list;

    public MessageAdapter(Context context, List<MessageInfo> list) {
        this.mContext = context;
        this.list = list;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_message, parent, false);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        return convertView;
    }

	/**
	 * 清空列表
	 */
	public void clear() {
		list.clear();
	}

	public List<MessageInfo> getList() {
		return list;
	}

	public void setList(List<MessageInfo> list) {
		this.list = list;
	}

	static class ViewHolder {
		TextView messageTitle; 
		ImageView messageIcon; 

	}

}
