package us.ttyl.sprinklercontroller;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddTimeActivity extends AppCompatActivity {
    TextView mStartTimeText;
    TextView mEndTimeText;
    ImageButton mStartTimeBtn;
    ImageButton mEndTimeBtn;
    Spinner mZone;
    Spinner mDay;
    ImageButton addBtn;

    ExecutorService mPool = Executors.newFixedThreadPool(1);

    public static final String TAG = "AddTimeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_time);
        mStartTimeText = (TextView)findViewById(R.id.start_time_text);
        mStartTimeBtn = (ImageButton)findViewById(R.id.start_time_btn);
        mStartTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerFragment newFragment = new TimePickerFragment();
                newFragment.setTextView(mStartTimeText);
                newFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });

        mEndTimeText = (TextView)findViewById(R.id.end_time_text);
        mEndTimeBtn = (ImageButton)findViewById(R.id.end_time_btn);
        mEndTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerFragment newFragment = new TimePickerFragment();
                newFragment.setTextView(mEndTimeText);
                newFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });

        mDay = (Spinner) findViewById(R.id.add_day);
        mZone = (Spinner) findViewById(R.id.add_zone);
        addBtn = (ImageButton)findViewById(R.id.add_btn);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            SprinklerDao.getInstance().addSprinklerItem(
                                    SprinkerUtils.getDayOfWeekNumber((String) mDay.getSelectedItem()),
                                    mStartTimeText.getText().toString(),
                                    mEndTimeText.getText().toString(),
                                    (String) mZone.getSelectedItem());
                            AddTimeActivity.this.runOnUiThread(new Runnable(){
                                @Override
                                public void run() {
                                    AddTimeActivity.this.finish();
                                }
                            });
                        }
                        catch(IOException e) {
                            Log.e(TAG, "add failed", e);
                        }
                    }
                });
            }
        });

        ArrayAdapter<CharSequence> daysAdapter = ArrayAdapter.createFromResource(this,
                R.array.days_array, android.R.layout.simple_spinner_item);
        daysAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDay.setAdapter(daysAdapter);

        ArrayAdapter<CharSequence> zoneAdapter = ArrayAdapter.createFromResource(this,
                R.array.zone_array, android.R.layout.simple_spinner_item);
        zoneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mZone.setAdapter(zoneAdapter);
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        TextView mTimeText;

        public void setTextView(TextView timeText) {
            mTimeText = timeText;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);


            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            DecimalFormat nf = new DecimalFormat("00");
            mTimeText.setText("" + hourOfDay + ":" + nf.format(minute));
        }
    }
}


