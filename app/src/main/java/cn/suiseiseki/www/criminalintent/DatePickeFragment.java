package cn.suiseiseki.www.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Administrator on 2016/2/27.
 */
public class DatePickeFragment extends DialogFragment {

    private Date mDate;
    public final static String EXTRA_DATE = "cn.suiseiseki.www.CrimePagerActivity.date";
    private void sendResult(int resultCode)
    {
        if(getTargetFragment() == null)
            return;
        Intent i = new Intent();
        i.putExtra(EXTRA_DATE,mDate);
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,i);
    }

    public static DatePickeFragment newInstance(Date date)
    {
        Bundle argument = new Bundle();
        argument.putSerializable(EXTRA_DATE, date);
        DatePickeFragment fragment = new DatePickeFragment();
        fragment.setArguments(argument);
        return fragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {

        mDate = (Date)getArguments().getSerializable(EXTRA_DATE);
        Log.d("TEST", "Argument get");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        View v = (getActivity().getLayoutInflater()).inflate(R.layout.dialog_date_pick, null);
        DatePicker datePicker = (DatePicker)v.findViewById(R.id.dialog_date_picker);
        Log.d("Test", "Picker created");

        OnDateChangedListener myListener = new OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mDate = new GregorianCalendar(year,monthOfYear,dayOfMonth).getTime();
                getArguments().putSerializable(EXTRA_DATE, mDate);
                Log.d("TEST", "Argument put");
            }
        };
        datePicker.init(year, month, day, myListener);

        Log.d("TEST", "init finishied");

        return new AlertDialog.Builder(getActivity()).
                setView(v).
                setTitle(R.string.date_picker_title).
                setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(Activity.RESULT_OK);
                        Log.d("TEST","RESULT_OK sended");
                    }
                }).
                create();
    }

}
