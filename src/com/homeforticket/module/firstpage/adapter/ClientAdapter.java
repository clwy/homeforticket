package com.homeforticket.module.firstpage.adapter;

import java.util.List;

import com.bumptech.glide.Glide;
import com.homeforticket.R;
import com.homeforticket.module.buyticket.model.BuyticketInfo;
import com.homeforticket.module.buyticket.model.TicketInfo;
import com.homeforticket.module.firstpage.activity.ClientInfoActivity;
import com.homeforticket.module.firstpage.model.ClientInfo;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ClientAdapter extends BaseAdapter {

	private Context mContext;
	private List<ClientInfo> list;

    public ClientAdapter(Context context) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_client, parent, false);
            holder.userHeadImg = (ImageView) convertView.findViewById(R.id.user_headimg);
            holder.userName = (TextView) convertView.findViewById(R.id.user_name);
            holder.userTel = (TextView) convertView.findViewById(R.id.user_tel);
            holder.manageButton = (Button) convertView.findViewById(R.id.manage_button);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ClientInfo info = list.get(position);
        Glide.with(mContext).load(info.getHeadimg()).into(holder.userHeadImg);
        holder.userName.setText(info.getName());
        holder.userTel.setText(info.getTel());
        holder.manageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ClientInfoActivity.class);
                intent.putExtra("ClientInfo", info);
                mContext.startActivity(intent);
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

	public List<ClientInfo> getList() {
		return list;
	}

	public void setList(List<ClientInfo> list) {
		this.list = list;
	}

	static class ViewHolder {
	    ImageView userHeadImg;
	    TextView userName;
	    TextView userTel;
	    Button manageButton;
	}

}
