package us.ttyl.sprinklercontroller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SprinklerControllerActivity extends AppCompatActivity {

    public static final String TAG = "SCA";

    ExecutorService mPool = Executors.newFixedThreadPool(5);
    RecyclerView mTimeList;
    FloatingActionButton mAddButton;
    SprinkleTimeAdapter mSprinklerTimeAdapter;
    TextView mWeatherCondtion;
    TextView mWeatherTemp;
    TextView mTime;
    TextView mSprinklerStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sprinkler_controller);
        mSprinklerStatus = (TextView)findViewById(R.id.sprinkler_status);
        mWeatherCondtion = (TextView)findViewById(R.id.weather_condition);
        mWeatherTemp = (TextView)findViewById(R.id.weather_temperature);
        mTime = (TextView)findViewById(R.id.time);

        mTimeList = (RecyclerView)findViewById(R.id.time_list);
        mTimeList.setLayoutManager(new LinearLayoutManager(this));
        setupItemTouchHelper();
        mAddButton = (FloatingActionButton) findViewById(R.id.add_time);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SprinklerControllerActivity.this, AddTimeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupItemTouchHelper() {
        ItemTouchHelper.SimpleCallback itemTouchHelper = new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int swipedPosition = viewHolder.getAdapterPosition();
                Time swipedTime = mSprinklerTimeAdapter.times.get(swipedPosition);
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return super.getSwipeDirs(recyclerView, viewHolder);
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();

        mPool.execute(new Runnable() {
            @Override
            public void run() {
                SprinklerDao sprinklerDao = SprinklerDao.getInstance();
                try {
                    final SprinklerStatus status = sprinklerDao.getCurrentStatus();

                    SprinklerData data = sprinklerDao.getSprinklerData();
                    Log.i(TAG, data.getTimezone()+ "\n" + data.getWeatherundergroundAPIKey()
                            + "\n" + data.getZipcode() + "\n");
                    final List<Time> times = data.getTimes();
                    final CurrentTime currentTime = sprinklerDao.getCurrentTime();
                    SprinklerControllerActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mSprinklerStatus.setText("1:" + status.getStatus(status.status1) + " 2:" + status.getStatus(status.status2) + " 3:"
                                    + status.getStatus(status.status3) + " 4:" + status.getStatus(status.status4) + " 5:" + status.getStatus(status.status5)
                                    + " 6:" + status.getStatus(status.status6) + " 7:" + status.getStatus(status.status7) + " 8:" + status.getStatus(status.status8));
                            mWeatherTemp.setText(currentTime.getWeather_temp());
                            mWeatherCondtion.setText(currentTime.getWeather_condition());
                            mTime.setText(currentTime.getTime());

                            mSprinklerTimeAdapter = new SprinkleTimeAdapter(
                                    SprinklerControllerActivity.this, times);
                            mTimeList.setAdapter(mSprinklerTimeAdapter);
                            mSprinklerTimeAdapter.notifyDataSetChanged();
                        }
                    });
                } catch (IOException e) {
                    Log.e(TAG, "explosion", e);
                }
            }
        });

    }
}
