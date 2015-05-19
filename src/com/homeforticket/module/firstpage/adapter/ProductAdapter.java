
package com.homeforticket.module.firstpage.adapter;

import java.util.HashMap;
import java.util.List;

import com.bumptech.glide.Glide;
import com.homeforticket.R;
import com.homeforticket.module.buyticket.activity.TicketActivity;
import com.homeforticket.module.buyticket.model.BuyticketInfo;
import com.homeforticket.module.buyticket.model.TicketInfo;
import com.homeforticket.module.firstpage.activity.ClientInfoActivity;
import com.homeforticket.module.firstpage.activity.ProductNavigationManageActivity;
import com.homeforticket.module.firstpage.model.ClientInfo;
import com.homeforticket.module.firstpage.model.OrderInfo;
import com.homeforticket.module.firstpage.model.ProductInfo;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ProductAdapter extends BaseAdapter {
    private Context mContext;
    private List<ProductInfo> list;
    private String mType;

    public ProductAdapter(Context context) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_product, parent,
                    false);
            holder.img = (ImageView) convertView.findViewById(R.id.product_img);
            holder.name = (TextView) convertView.findViewById(R.id.product_name);
            holder.type = (TextView) convertView.findViewById(R.id.travel_type);
            holder.address = (TextView) convertView.findViewById(R.id.travel_address);
            holder.preferentialPrice = (TextView) convertView.findViewById(R.id.preferential_price);
            holder.rePrice = (TextView) convertView.findViewById(R.id.price);
            holder.sellPrice = (TextView) convertView.findViewById(R.id.sell_price);
            holder.sellPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.travelLayout = (RelativeLayout) convertView.findViewById(R.id.travel_layout);
            holder.productLayout = (RelativeLayout) convertView
                    .findViewById(R.id.product_content_layout);
            holder.hotelLayout = (RelativeLayout) convertView.findViewById(R.id.hotel_layout);
            holder.ticketLayout = (RelativeLayout) convertView.findViewById(R.id.ticket_layout);
            holder.hotelLevel = (TextView) convertView.findViewById(R.id.level);
            holder.hotelAddress = (TextView) convertView.findViewById(R.id.address);
            holder.hotelPrice = (TextView) convertView.findViewById(R.id.hotel_price);
            holder.ticketAddress = (TextView) convertView.findViewById(R.id.ticket_address);
            holder.ticketLevel = (TextView) convertView.findViewById(R.id.ticket_level);
            holder.ticketMardPrice = (TextView) convertView.findViewById(R.id.ticket_origin_price);
            holder.ticketRePrice = (TextView) convertView.findViewById(R.id.ticket_current_price);
            holder.ticketMardPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ProductInfo info = list.get(position);
        holder.name.setText(info.getName());

        if (ProductNavigationManageActivity.TYPE_TRAVEL.equals(mType)) {
            holder.travelLayout.setVisibility(View.VISIBLE);
            holder.productLayout.setVisibility(View.GONE);
            holder.hotelLayout.setVisibility(View.GONE);
            holder.ticketLayout.setVisibility(View.GONE);
            Glide.with(mContext).load(info.getImg()).into(holder.img);
        } else if (ProductNavigationManageActivity.TYPE_LOCAL.equals(mType)
                || ProductNavigationManageActivity.TYPE_SPECIAL.equals(mType)) {
            holder.travelLayout.setVisibility(View.GONE);
            holder.productLayout.setVisibility(View.VISIBLE);
            holder.hotelLayout.setVisibility(View.GONE);
            holder.ticketLayout.setVisibility(View.GONE);
            Glide.with(mContext).load(info.getImg()).into(holder.img);
        } else if (ProductNavigationManageActivity.TYPE_HOTEL.equals(mType)) {
            holder.travelLayout.setVisibility(View.GONE);
            holder.productLayout.setVisibility(View.GONE);
            holder.hotelLayout.setVisibility(View.VISIBLE);
            holder.ticketLayout.setVisibility(View.GONE);
            Glide.with(mContext).load(info.getImg()).into(holder.img);
        } else if (ProductNavigationManageActivity.TYPE_TICKET.equals(mType)) {
            holder.travelLayout.setVisibility(View.GONE);
            holder.productLayout.setVisibility(View.GONE);
            holder.hotelLayout.setVisibility(View.GONE);
            holder.ticketLayout.setVisibility(View.VISIBLE);
            holder.name.setText(info.getSceneName());
            Glide.with(mContext).load(info.getPicName()).into(holder.img);
        }

        holder.type.setText(info.getType());
        holder.address.setText(info.getAddress());
        holder.rePrice.setText(info.getPrice());
        holder.preferentialPrice.setText(info.getPrice());
        holder.sellPrice.setText(mContext.getResources().getString(R.string.price_sign) + info.getRetailPrice());
        holder.hotelLevel.setText(info.getLevel());
        holder.hotelAddress.setText(info.getAddress());
        holder.hotelPrice.setText(info.getPrice());
        holder.ticketAddress.setText(info.getCity());
        holder.ticketLevel.setText(info.getSceneLevel());
        holder.ticketMardPrice.setText(mContext.getResources().getString(R.string.price_sign) + info.getMarketPrice());
        holder.ticketRePrice.setText(info.getRetailPrice());
        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ProductNavigationManageActivity.TYPE_TICKET.equals(mType)) {
                    Intent intent = new Intent(mContext, TicketActivity.class);
                    intent.putExtra("id", info.getSceneId());
                    intent.putExtra("current", info.getRetailPrice());
                    intent.putExtra("original", info.getMarketPrice());
                    mContext.startActivity(intent);
                }
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

    public List<ProductInfo> getList() {
        return list;
    }

    public void setList(List<ProductInfo> list) {
        this.list = list;
    }

    /**
     * @return the mType
     */
    public String getType() {
        return mType;
    }

    /**
     * @param mType the mType to set
     */
    public void setType(String mType) {
        this.mType = mType;
    }

    static class ViewHolder {
        private ImageView img;
        private TextView name;
        private TextView type;
        private TextView address;
        private TextView preferentialPrice;
        private TextView sellPrice;
        private TextView rePrice;
        private RelativeLayout travelLayout;
        private RelativeLayout productLayout;
        private RelativeLayout hotelLayout;
        private RelativeLayout ticketLayout;
        private TextView hotelLevel;
        private TextView hotelAddress;
        private TextView hotelPrice;
        private TextView ticketMardPrice;
        private TextView ticketRePrice;
        private TextView ticketLevel;
        private TextView ticketAddress;
    }

}
