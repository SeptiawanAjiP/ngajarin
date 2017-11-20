package pisangmolen.com.ajari.Adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import pisangmolen.com.ajari.Model.BookingProgress;
import pisangmolen.com.ajari.Model.Konstanta;
import pisangmolen.com.ajari.R;

/**
 * Created by user on 11/13/2017.
 */

public class BookingProgressAdapter extends RecyclerView.Adapter{

    Context context;
    ArrayList<BookingProgress> bookingList;

    public BookingProgressAdapter(Context context, ArrayList<BookingProgress> BookingList){
        this.context = context;
        this.bookingList = BookingList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false);
        return new BookingProgressViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((BookingProgressViewHolder)holder).setData(bookingList.get(position));
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    private class BookingProgressViewHolder extends RecyclerView.ViewHolder {
        private ImageView ico, flagcomplete;
        private TextView jenjang, materi, status, jadwal;

        public BookingProgressViewHolder(View itemView) {
            super(itemView);
            ico = (ImageView) itemView.findViewById(R.id.booking_icon);
            flagcomplete = (ImageView) itemView.findViewById(R.id.flag_complete);
            jenjang = (TextView) itemView.findViewById(R.id.booking_school);
            materi = (TextView) itemView.findViewById(R.id.booking_materi);
            status = (TextView) itemView.findViewById(R.id.booking_status);
            jadwal = (TextView) itemView.findViewById(R.id.booking_jadwal);
        }

        public void setData(BookingProgress data){
            if(!data.getStatus().equals("tunggu")){
                flagcomplete.setVisibility(View.VISIBLE);
                status.setTextColor(context.getResources().getColor(android.R.color.holo_green_light));
            }else{
                flagcomplete.setVisibility(View.GONE);
                status.setTextColor(context.getResources().getColor(android.R.color.holo_red_light));
            }

            switch(data.getType()){
                case "SD":
                    ico.setImageResource(R.drawable.ic_sd);
                    break;
                case  "SMP":
                    ico.setImageResource(R.drawable.ic_smp);
                    break;
                case "SMA":
                    ico.setImageResource(R.drawable.ic_sma);
                    break;
                case "Mengaji":
                    ico.setImageResource(R.drawable.ic_ngaji);
                    break;
                case "Musik":
                    ico.setImageResource(R.drawable.ic_musik);
                    break;
                case "Melukis":
                    ico.setImageResource(R.drawable.ic_lukis);
                    break;
            }

            jenjang.setText(data.getType());
            status.setText(data.getStatus());
            materi.setText(data.getMateri());
            jadwal.setText(data.getTanggalMulai());
        }
    }
}
