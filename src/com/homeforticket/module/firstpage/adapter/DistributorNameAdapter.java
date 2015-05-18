
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

public class DistributorNameAdapter extends BaseAdapter {

    private Context mContext;
    private List<DistributorInfo> list;

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

        final DistributorInfo info = list.get(position);
        if (!TextUtils.isEmpty(info.getImg())) {
            Glide.with(mContext).load(info.getImg()).transform(new CircleTransform(mContext))
                    .into(holder.headimg);
        }

        holder.name.setText(info.getName());
        holder.des.setText(info.getStatus());
        holder.start.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });
        holder.pause.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });
        holder.delete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

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

}
