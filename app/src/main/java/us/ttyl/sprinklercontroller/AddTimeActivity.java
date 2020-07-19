package us.ttyl.sprinklercontroller;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddTimeActivity extends AppCompatActivity {
    TextView mStartTimeText;
    TextView mEndTimeText;
    ImageButton mStartTimeBtn;
    ImageButton mEndTimeBtn;
    Spinner mZone;
    CheckBox mSundaySelect;
    CheckBox mMondaySelect;
    CheckBox mTuesdaySelect;
    CheckBox mWednesdaySelect;
    CheckBox mThursdaySelect;
    CheckBox mFridaySelect;
    CheckBox mSaturdaySelect;
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

        mSundaySelect = (CheckBox) findViewById(R.id.sunday_select);
        mMondaySelect = (CheckBox) findViewById(R.id.monday_select);
        mTuesdaySelect = (CheckBox) findViewById(R.id.tuesday_select);
        mWednesdaySelect = (CheckBox) findViewById(R.id.wednesday_select);
        mThursdaySelect= (CheckBox) findViewById(R.id.thursday_select);
        mFridaySelect = (CheckBox) findViewById(R.id.friday_select);
        mSaturdaySelect = (CheckBox) findViewById(R.id.saturday_select);
        mZone = (Spinner) findViewById(R.id.add_zone);
        addBtn = (ImageButton)findViewById(R.id.add_btn);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        List<String> selectedDays = getSelectedDays();
                        try {
                            for(int index = 0; index < selectedDays.size(); index ++) {
                                SprinklerDao.getInstance().addSprinklerItem(
                                       selectedDays.get(index),
                                        mStartTimeText.getText().toString(),
                                        mEndTimeText.getText().toString(),
                                        (String) mZone.getSelectedItem());
                            }
                        }
                        catch(IOException e) {
                            Log.e(TAG, "add failed", e);
                        }
                        finally {
                            AddTimeActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AddTimeActivity.this.finish();
                                }
                            });
                        }
                    }
                });
            }
        });

        ArrayAdapter<CharSequence> zoneAdapter = ArrayAdapter.createFromResource(this,
                R.array.zone_array, android.R.layout.simple_spinner_item);
        zoneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mZone.setAdapter(zoneAdapter);
    }

    public List<String> getSelectedDays() {
        List<String> selectedDays = new ArrayList();
        if (mSundaySelect.isChecked()) {
            selectedDays.add(SprinkerUtils.getDayOfWeekNumber(mSundaySelect.getText().toString()));
        }
        if (mMondaySelect.isChecked()) {
            selectedDays.add(SprinkerUtils.getDayOfWeekNumber(mMondaySelect.getText().toString()));
        }
        if (mTuesdaySelect.isChecked()) {
            selectedDays.add(SprinkerUtils.getDayOfWeekNumber(mTuesdaySelect.getText().toString()));
        }
        if (mWednesdaySelect.isChecked()) {
            selectedDays.add(SprinkerUtils.getDayOfWeekNumber(mWednesdaySelect.getText().toString()));
        }
        if (mThursdaySelect.isChecked()) {
            selectedDays.add(SprinkerUtils.getDayOfWeekNumber(mThursdaySelect.getText().toString()));
        }
        if (mFridaySelect.isChecked()) {
            selectedDays.add(SprinkerUtils.getDayOfWeekNumber(mFridaySelect.getText().toString()));
        }
        if (mSaturdaySelect.isChecked()) {
            selectedDays.add(SprinkerUtils.getDayOfWeekNumber(mSaturdaySelect.getText().toString()));
        }
        return selectedDays;
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


