
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
import com.homeforticket.module.firstpage.model.DistributorInfoMessage;
import com.homeforticket.module.firstpage.model.RecordInfo;
import com.homeforticket.module.firstpage.model.SetDistributorInfoMessage;
import com.homeforticket.module.firstpage.parser.DistributorInfoMessageParser;
import com.homeforticket.module.firstpage.parser.SetDistributorInfoMessageParser;
import com.homeforticket.module.login.activity.LoginActivity;
import com.homeforticket.module.message.model.MessageInfo;
import com.homeforticket.request.RequestJob;
import com.homeforticket.request.RequestListener;
import com.homeforticket.util.CircleTransform;
import com.homeforticket.util.SharedPreferencesUtil;
import com.homeforticket.util.ToastUtil;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
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

public class DistributorNameAdapter extends BaseAdapter implements RequestListener {

    private Context mContext;
    private List<DistributorInfo> list;
    private int mPos;
    private String mStatus;

    public DistributorNameAdapter(Context context) {
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
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.list_item_distributor_name, parent, false);
            holder.headimg = (ImageView) convertView.findViewById(R.id.distributor_img);
            holder.name = (TextView) convertView.findViewById(R.id.distributor_name);
            holder.des = (TextView) convertView.findViewById(R.id.des);
            holder.start = (Button) convertView.findViewById(R.id.start_button);
            holder.pause = (Button) convertView.findViewById(R.id.pause_button);
            holder.delete = (Button) convertView.findViewById(R.id.delete_button);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final int pos = position;
        final DistributorInfo info = list.get(position);
        if (!TextUtils.isEmpty(info.getImg())) {
            Glide.with(mContext).load(info.getImg()).transform(new CircleTransform(mContext))
                    .into(holder.headimg);
        }

        holder.name.setText(info.getName());
        String status = "启用";
        if ("0".equals(info.getStatus())) {
            status = "暂停";
        } else if ("1".equals(info.getStatus())) {
            status = "启用";
        } else if ("2".equals(info.getStatus())) {
            status = "删除";
        }
        holder.des.setText(status);
        holder.start.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                requestStatus(info.getId(), "1", pos);
            }
        });
        holder.pause.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                requestStatus(info.getId(), "0", pos);
            }
        });
        holder.delete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                requestStatus(info.getId(), "2", pos);
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
        TextView des;
        Button start;
        Button pause;
        Button delete;
    }
    
    private void requestStatus(String id, String status, int pos) {
        mPos = pos;
        mStatus = status;
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
            
            list.get(mPos).setStatus(mStatus);
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
