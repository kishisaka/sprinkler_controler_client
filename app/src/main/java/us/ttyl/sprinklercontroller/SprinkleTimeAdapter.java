package us.ttyl.sprinklercontroller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by test on 12/16/2016.
 */

public class SprinkleTimeAdapter extends RecyclerView.Adapter<SprinkleTimeAdapter.CustomViewHolder>{

    private static final String TAG = "SprinkleTimeAdapter";
    List<Time> times;
    Context mContext;
    ExecutorService mPool = Executors.newFixedThreadPool(1);

    public SprinkleTimeAdapter(Context context,List <Time> times){
        this.mContext = context;
        this.times = times;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_item, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {
        final Time time = this.times.get(position);
        holder.day.setText("" + SprinkerUtils.getDayFullNameFromDayNumber(time.day));
        holder.zone.setText("" + time.zone);
        holder.start.setText(time.start);
        holder.end.setText(time.end);
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, UpdateTimeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("id", time.id);
                bundle.putString("zone", time.zone);
                bundle.putString("start", time.start);
                bundle.putString("end", time.end);
                bundle.putString("day", holder.day.getText().toString());
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i < getItemCount(); i ++) {
                    Time time = times.get(i);
                    if (times.get(position).id == time.id) {
                        times.remove(i);
                    }
                    notifyDataSetChanged();
                }
                mPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        SprinklerDao sd = SprinklerDao.getInstance();
                        try {
                            sd.removeItem(time.id);
                        } catch(IOException e) {
                            Log.e(TAG, "", e);
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.times.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        protected ImageButton update;
        protected ImageButton delete;
        protected TextView day;
        protected TextView start;
        protected TextView end;
        protected TextView zone;

        public CustomViewHolder(View view) {
            super(view);
            this.update = (ImageButton) view.findViewById(R.id.update_btn);
            this.delete = (ImageButton) view.findViewById(R.id.delete_btn);
            this.day = (TextView) view.findViewById(R.id.day_text);
            this.start = (TextView) view.findViewById(R.id.start_text);
            this.end = (TextView) view.findViewById(R.id.end_text);
            this.zone = (TextView) view.findViewById(R.id.zone_text);
        }
    }
}
