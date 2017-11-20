package pisangmolen.com.ajari.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import pisangmolen.com.ajari.Model.BookingComplete;
import pisangmolen.com.ajari.Model.BookingProgress;
import pisangmolen.com.ajari.Model.Konstanta;
import pisangmolen.com.ajari.R;

/**
 * Created by user on 11/13/2017.
 */

public class BookingFinishAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<BookingComplete> bookingList;

    public BookingFinishAdapter(Context context, ArrayList<BookingComplete> BookingList){
        this.context = context;
        this.bookingList = BookingList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_booking_finish, parent, false);
        return new BookingFinishViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((BookingFinishViewHolder)holder).setData(bookingList.get(position));
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    private class BookingFinishViewHolder extends RecyclerView.ViewHolder {
        private ImageView ico;
        private TextView jenjang, materi, mentor, jadwal;

        public BookingFinishViewHolder(View itemView) {
            super(itemView);
            ico = (ImageView) itemView.findViewById(R.id.booking_icon);
            jenjang = (TextView) itemView.findViewById(R.id.booking_school);
            materi = (TextView) itemView.findViewById(R.id.booking_materi);
            mentor = (TextView) itemView.findViewById(R.id.booking_mentor_name);
            jadwal = (TextView) itemView.findViewById(R.id.booking_jadwal);
        }

        public void setData(BookingComplete data){

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
            materi.setText(data.getMateri());
            jadwal.setText(data.getTanggalMulai());
            mentor.setText(data.getMentor());

        }
    }
}
