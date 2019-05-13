package org.elastos.app.hivedemo;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.elastos.carrier.ConnectionStatus;
import org.elastos.carrier.FriendInfo;

import java.util.List;


public class FriendsAdapter extends BaseAdapter {

    private Context context;
    private List<FriendInfo> friendInfos;

    public FriendsAdapter(Context context, List<FriendInfo> friendInfos) {
        this.context = context;
        this.friendInfos = friendInfos;
    }

    @Override
    public int getCount() {
        return friendInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return friendInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder;
        if (convertView == null) {
            holder = new MyViewHolder();
            convertView = View.inflate(context, R.layout.friend_item, null);
            holder.friend_imageView_icon = convertView.findViewById(R.id.friend_imageView_icon);
            holder.friend_name = convertView.findViewById(R.id.friend_name);
            holder.friend_textView_info = convertView.findViewById(R.id.friend_textView_info);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }

        FriendInfo friendInfo = friendInfos.get(position);

        holder.friend_name.setText(friendInfo.getUserId().substring(0, 8));
        if(friendInfo.getConnectionStatus() == ConnectionStatus.Connected){
            holder.friend_textView_info.setText("online");
            holder.friend_textView_info.setTextColor(Color.GREEN);
        }else{
            holder.friend_textView_info.setText("offline");
            holder.friend_textView_info.setTextColor(Color.BLACK);

        }

        return convertView;
    }

    class MyViewHolder {
        ImageView friend_imageView_icon;
        TextView friend_name, friend_textView_info;

    }
}
