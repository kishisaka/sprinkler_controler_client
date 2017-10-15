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

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UpdateTimeActivity extends AppCompatActivity {
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
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        final long id = bundle.getLong("id");
        final String zone = bundle.getString("zone");
        final String start = bundle.getString("start");
        final String end = bundle.getString("end");
        final String day = bundle.getString("day");

        setContentView(R.layout.activity_add_new_time);
        mStartTimeText = (TextView)findViewById(R.id.start_time_text);
        mStartTimeText.setText(start);
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
        mEndTimeText.setText(end);
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
                Log.i(TAG, (String)mZone.getSelectedItem());
                mPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            SprinklerDao.getInstance().updateSprinkerItem(id,
                                    SprinkerUtils.getDayOfWeekNumber((String) mDay.getSelectedItem()),
                                    mStartTimeText.getText().toString(),
                                    mEndTimeText.getText().toString(),
                                    (String)mZone.getSelectedItem());
                            UpdateTimeActivity.this.runOnUiThread(new Runnable(){
                                @Override
                                public void run() {
                                    UpdateTimeActivity.this.finish();
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
        for(int i = 0; i < daysAdapter.getCount(); i ++ ){
            if (daysAdapter.getItem(i).equals(day)){
                mDay.setSelection(i);
            }
        }

        ArrayAdapter<CharSequence> zoneAdapter = ArrayAdapter.createFromResource(this,
                R.array.zone_array, android.R.layout.simple_spinner_item);
        zoneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mZone.setAdapter(zoneAdapter);
        for(int i = 0; i < zoneAdapter.getCount(); i ++ ){
            if (zoneAdapter.getItem(i).equals(zone)){
                mZone.setSelection(i);
            }
        }
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


