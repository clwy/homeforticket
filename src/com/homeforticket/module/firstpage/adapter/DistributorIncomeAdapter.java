package com.homeforticket.module.firstpage.adapter;

import java.util.List;

import com.bumptech.glide.Glide;
import com.homeforticket.R;
import com.homeforticket.module.buyticket.model.BuyticketInfo;
import com.homeforticket.module.buyticket.model.TicketInfo;
import com.homeforticket.module.firstpage.model.DistributorInfo;
import com.homeforticket.module.firstpage.model.RecordInfo;
import com.homeforticket.module.message.model.MessageInfo;
import com.homeforticket.util.CircleTransform;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DistributorIncomeAdapter extends BaseAdapter {

	private Context mContext;
	private List<DistributorInfo> list;

    public DistributorIncomeAdapter(Context context) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_distributor_income, parent, false);
            holder.headimg = (ImageView) convertView.findViewById(R.id.distributor_img);
            holder.name = (TextView) convertView.findViewById(R.id.distributor_name);
            holder.income = (TextView) convertView.findViewById(R.id.income);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final DistributorInfo info = list.get(position);
        if (!TextUtils.isEmpty(info.getImg())) {
            Glide.with(mContext).load(info.getImg()).transform(new CircleTransform(mContext)).into(holder.headimg);
        }
        
        holder.name.setText(info.getName());
        String total = info.getTotal();
        if (TextUtils.isEmpty(total)) {
            holder.income.setText("￥0.00");
        } else {
            holder.income.setText("￥" + info.getTotal());
        }
        
        return convertView;
    }

	/**
	 * 清空列表
	 */
	public void clear() {
		list.clear();
	}

	public List<DistributorInfo> getList() {
		return list;
	}

	public void setList(List<DistributorInfo> list) {
		this.list = list;
	}

	static class ViewHolder {
	    ImageView headimg;
	    TextView name;
	    TextView income;
	}

}
