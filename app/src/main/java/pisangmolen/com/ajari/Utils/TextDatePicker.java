package pisangmolen.com.ajari.Utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by user on 11/15/2017.
 */

public class TextDatePicker implements DatePickerDialog.OnDateSetListener, View.OnClickListener {

    private Context mContext;
    private EditText mEditText;
    private Calendar mCalendar;

    public TextDatePicker(Context context, int editTextId){
        Activity activity = (Activity) context;
        mEditText = (EditText) activity.findViewById(editTextId);
        mEditText.setOnClickListener(this);
        mContext = context;
        mCalendar = Calendar.getInstance();
    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateEditText();
    }

    @Override
    public void onClick(View view) {
        DatePickerDialog dialog = new DatePickerDialog(mContext, this,
                mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog.show();
    }

    public void updateEditText(){
        String format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        mEditText.setText(sdf.format(mCalendar.getTime()));
    }

    public String getText(){
        return mEditText.getText().toString();
    }
}
