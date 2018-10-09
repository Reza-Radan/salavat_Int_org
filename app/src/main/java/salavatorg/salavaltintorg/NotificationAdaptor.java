package salavatorg.salavaltintorg;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import salavatorg.salavaltintorg.dao.Notification;

/**
 * Created by masoomeh on 2/20/18.
 */

class NotificationAdaptor extends RecyclerView.Adapter<NotificationAdaptor.holder> {
    List<Notification> notifications;
    NotificationListActivity notificationActivity;
    public NotificationAdaptor(NotificationListActivity notificationActivity, List<Notification> notifications) {
        this.notifications = notifications;
        this.notificationActivity = notificationActivity;
    }

    @Override
    public holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return new holder(v);

    }

    @Override
    public void onBindViewHolder(holder holder, final int position) {
        Notification item = notifications.get(position);
        holder.text.setText(notifications.get(position).getTitle()+"11");
        holder.desc.setText(notifications.get(position).getDesc());
        holder.date.setText(notifications.get(position).getDate());

        Log.e("tag" ,notifications.get(position).getTitle());
//        holder.image.setImageBitmap(null);
//        Picasso.with(holder.image.getContext()).cancelRequest(holder.image);
//        Picasso.with(holder.image.getContext()).load(item.getImage()).into(holder.image);
        holder.itemView.setTag(item);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("tag" ,"onClickonClickonClick");
                Intent intent = new Intent(notificationActivity , NotificationPageActivity.class);

                intent.putExtra("header",notifications.get(position).getTitle());
                intent.putExtra("footer",notifications.get(position).getFooter());
                intent.putExtra("body",notifications.get(position).getDesc());
                notificationActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }


    public static class holder extends RecyclerView.ViewHolder {

        public TextView text ,desc ,date;

        public holder(View itemView) {
            super(itemView);
//            image = (ImageView) itemView.findViewById(R.id.image);
            text =  itemView.findViewById(R.id.txtvNo);
            desc = itemView.findViewById(R.id.txtvdes);
            date = itemView.findViewById(R.id.txtvdate);
        }
    }
}
