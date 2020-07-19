package us.ttyl.sprinklercontroller;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.text.DecimalFormat;
import java.util.Calendar;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.Callable;
public class UpdateTimeActivity extends AppCompatActivity {
    TextView mStartTimeText;
    TextView mEndTimeText;
    ImageButton mStartTimeBtn;
    ImageButton mEndTimeBtn;
    Spinner mZone;
    Spinner mDay;
    ImageButton addBtn;

    private final CompositeDisposable disposables = new CompositeDisposable();

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

        setContentView(R.layout.activity_update_time);
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
                disposables.add(updateSprrinklerItemObservalble(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<String>() {
                            @Override public void onComplete() {
                                UpdateTimeActivity.this.finish();
                            }

                            @Override public void onError(Throwable e) {
                                Log.e(TAG, "onError()", e);
                            }

                            @Override public void onNext(String string) {
                                Log.d(TAG, "onNext(" + string + ")");
                            }
                        }));
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

    private Observable<String> updateSprrinklerItemObservalble(final long id) {
        return Observable.defer(new Callable<ObservableSource<? extends String>>() {
            @Override
            public ObservableSource<? extends String> call() throws Exception {
                SprinklerDao.getInstance().updateSprinkerItem(id,
                        SprinkerUtils.getDayOfWeekNumber((String) mDay.getSelectedItem()),
                        mStartTimeText.getText().toString(),
                        mEndTimeText.getText().toString(),
                        (String)mZone.getSelectedItem());
                return Observable.just("done");
            }
        });
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
            return new TimePickerDialog(this.getContext(), this, hour, minute,
                    DateFormat.is24HourFormat(this.getContext()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            DecimalFormat nf = new DecimalFormat("00");
            mTimeText.setText("" + hourOfDay + ":" + nf.format(minute));
        }
    }
}


