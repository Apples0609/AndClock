package com.ycuwq.datepicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ycuwq.datepicker.date.DatePicker;
import com.ycuwq.datepicker.date.DatePickerDialogFragment;

import cn.smiles.andclock.R;

public class DatepickerDemoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datepicker_activity_main);

        TextView dateTv = (TextView) findViewById(R.id.tv_date);
        DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(v -> {
            DatePickerDialogFragment datePickerDialogFragment = new DatePickerDialogFragment();
            datePickerDialogFragment.setOnDateChooseListener(new DatePickerDialogFragment.OnDateChooseListener() {
                @Override
                public void onDateChoose(int year, int month, int day) {
                    Toast.makeText(getApplicationContext(), year + "-" + month + "-" + day, Toast.LENGTH_SHORT).show();
                }
            });
            datePickerDialogFragment.show(getSupportFragmentManager(), "DatePickerDialogFragment");
        });
        datePicker.setOnDateSelectedListener(new DatePicker.OnDateSelectedListener() {
            @Override
            public void onDateSelected(int year, int month, int day) {
                dateTv.setText(year + "-" + month + "-" + day);
            }
        });
    }
}