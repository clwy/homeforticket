package com.homeforticket.module.firstpage.adapter;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.bumptech.glide.Glide;
import com.homeforticket.R;
import com.homeforticket.constant.SysConstants;
import com.homeforticket.module.buyticket.model.BuyticketInfo;
import com.homeforticket.module.buyticket.model.TicketInfo;
import com.homeforticket.module.firstpage.model.DistributorInfo;
import com.homeforticket.module.firstpage.model.RecordInfo;
import com.homeforticket.module.firstpage.model.SetDistributorInfoMessage;
import com.homeforticket.module.firstpage.parser.SetDistributorInfoMessageParser;
import com.homeforticket.module.message.model.MessageInfo;
import com.homeforticket.request.RequestJob;
import com.homeforticket.request.RequestListener;
import com.homeforticket.util.CircleTransform;
import com.homeforticket.util.SharedPreferencesUtil;
import com.homeforticket.util.ToastUtil;

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

public class DistributorSettingAdapter extends BaseAdapter implements RequestListener{

	private Context mContext;
	private List<DistributorInfo> list;
	private int mPos;

    public DistributorSettingAdapter(Context context) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_distributor_setting, parent, false);
            holder.headimg = (ImageView) convertView.findViewById(R.id.distributor_img);
            holder.name = (TextView) convertView.findViewById(R.id.distributor_name);
            holder.cancel = (Button) convertView.findViewById(R.id.cacel_button);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final int pos = position;
        final DistributorInfo info = list.get(position);
        if (!TextUtils.isEmpty(info.getImg())) {
            Glide.with(mContext).load(info.getImg()).transform(new CircleTransform(mContext)).into(holder.headimg);
        }
        
        holder.name.setText(info.getName());
        holder.cancel.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                requestStatus(info.getId(), "3", pos);
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

	public List<DistributorInfo> getList() {
		return list;
	}

	public void setList(List<DistributorInfo> list) {
		this.list = list;
	}

	static class ViewHolder {
	    ImageView headimg;
	    TextView name;
	    Button cancel;
	}
	
    private void requestStatus(String id, String status, int pos) {
        mPos = pos;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", "resellerStatus");
            jsonObject.put("id", id);
            jsonObject.put("status", status);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            RequestJob job = new RequestJob(SysConstants.SERVER, jsonObject.toString(),
                    new SetDistributorInfoMessageParser(), SysConstants.REQUEST_POST);
            job.setRequestListener(this);
            job.doRequest();
        }
    }

    @Override
    public void onStartRequest(RequestJob job) {
        
    }

    @Override
    public void onSuccess(RequestJob job) {
        SetDistributorInfoMessage message = (SetDistributorInfoMessage) job.getBaseType();
        String code = message.getCode();
        if ("10000".equals(code)) {
            String token = message.getToken();
            if (!TextUtils.isEmpty(token)) {
                SharedPreferencesUtil.saveString(SysConstants.TOKEN, token);
            }
            
            list.remove(mPos);
            notifyDataSetChanged();
        } else {
            ToastUtil.showToast(message.getMessage());
        }
    }

    @Override
    public void onFail(RequestJob job) {
        ToastUtil.showToast(job.getFailNotice());
    }

}
