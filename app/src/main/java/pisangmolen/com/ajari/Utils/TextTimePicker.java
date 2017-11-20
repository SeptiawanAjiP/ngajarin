package pisangmolen.com.ajari.Utils;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by user on 11/15/2017.
 */

public class TextTimePicker implements TimePickerDialog.OnTimeSetListener, View.OnClickListener {

    private EditText mEditText;
    private Context mContext;
    private Calendar mCalendar;

    public TextTimePicker(Context context, int editTextId){
        Activity activity = (Activity) context;
        mEditText = (EditText) activity.findViewById(editTextId);
        mEditText.setOnClickListener(this);
        mContext = context;
        mCalendar = Calendar.getInstance();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mCalendar.set(Calendar.MINUTE, minute);
        mCalendar.set(Calendar.SECOND, 0);

        if(mCalendar.getTimeInMillis()< c.getTimeInMillis()){
            Toast.makeText(mContext, "Tidak dapat melakukan booking pada waktu tersebut", Toast.LENGTH_SHORT).show();
        }else{
            updateEditText();
        }

    }

    @Override
    public void onClick(View view) {
        TimePickerDialog dialog = new TimePickerDialog(mContext, this,
                mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), true);

        dialog.show();
    }

    public void updateEditText(){
        String format = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        mEditText.setText(sdf.format(mCalendar.getTime()));
    }

    public String getText(){
        return mEditText.getText().toString();
    }
    public Calendar getCalendar(){return mCalendar;}
}
