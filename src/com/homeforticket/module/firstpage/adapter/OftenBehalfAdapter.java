package com.homeforticket.module.firstpage.adapter;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.bumptech.glide.Glide;
import com.homeforticket.R;
import com.homeforticket.constant.SysConstants;
import com.homeforticket.module.buyticket.activity.TicketActivity;
import com.homeforticket.module.buyticket.model.BuyticketInfo;
import com.homeforticket.module.buyticket.model.TicketInfo;
import com.homeforticket.module.firstpage.model.AddSceneMessage;
import com.homeforticket.module.firstpage.model.BehalfInfo;
import com.homeforticket.module.firstpage.parser.AddSceneMessageParser;
import com.homeforticket.module.login.activity.LoginActivity;
import com.homeforticket.module.me.model.SetUserInfoMessage;
import com.homeforticket.module.me.parser.SetUserInfoMessageParser;
import com.homeforticket.module.message.model.MessageInfo;
import com.homeforticket.request.RequestJob;
import com.homeforticket.request.RequestListener;
import com.homeforticket.util.SharedPreferencesUtil;
import com.homeforticket.util.ToastUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class OftenBehalfAdapter extends BaseAdapter {

	private Context mContext;
	private List<BehalfInfo> list;
	private String mProductType;

    public OftenBehalfAdapter(Context context) {
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

        final BehalfInfo behalfInfo = list.get(position);
        Glide.with(mContext).load(behalfInfo.getPicName()).centerCrop().into(holder.itemPic);
        holder.posName.setText(behalfInfo.getCity());
        holder.TicketName.setText(behalfInfo.getSceneName());
        holder.TicketLevel.setText(behalfInfo.getSceneLevel());
        holder.currentCost.setText(behalfInfo.getRetailPrice());
        holder.originalCost.setText(behalfInfo.getMarketPrice());
        
        if (convertView != null) {
            convertView.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, TicketActivity.class);
                    intent.putExtra("id", behalfInfo.getSceneId());
                    intent.putExtra("current", behalfInfo.getRetailPrice());
                    intent.putExtra("original", behalfInfo.getMarketPrice());
                    if (!TextUtils.isEmpty(mProductType)) {
                        intent.putExtra("productType", mProductType);
                    }
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

	public List<BehalfInfo> getList() {
		return list;
	}

	public void setList(List<BehalfInfo> list) {
		this.list = list;
	}

	/**
     * @return the mProductType
     */
    public String getProductType() {
        return mProductType;
    }

    /**
     * @param mProductType the mProductType to set
     */
    public void setProductType(String mProductType) {
        this.mProductType = mProductType;
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
