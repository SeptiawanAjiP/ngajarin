package pisangmolen.com.ajari.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import pisangmolen.com.ajari.Model.Mapel;
import pisangmolen.com.ajari.Model.User;

/**
 * Created by aji on 11/19/2017.
 */

public class SpinnerMapelAdapter extends ArrayAdapter<Mapel> {
    private Context context;
    private ArrayList<Mapel> values;

    public SpinnerMapelAdapter(Context context,int textViewResourceId,ArrayList<Mapel> values){
        super(context,textViewResourceId,values);
        this.context = context;
        this.values = values;
        Log.d("book_adapter",values.size()+"");
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Nullable
    @Override
    public Mapel getItem(int position) {
        return values.get(position) ;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setTextSize(16);
        label.setText(values.get(position).getNamaMapel().toString());
        return label;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setTextSize(16);
        label.setText(values.get(position).getNamaMapel().toString());
        return label;
    }
}
