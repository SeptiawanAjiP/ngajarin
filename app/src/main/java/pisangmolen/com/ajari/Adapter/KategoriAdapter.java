package pisangmolen.com.ajari.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import pisangmolen.com.ajari.Model.Kategori;
import pisangmolen.com.ajari.R;

/**
 * Created by aji on 11/14/2017.
 */

public class KategoriAdapter extends BaseAdapter {
    private ArrayList<Kategori> kategoris;
    private Activity activity;

    public KategoriAdapter(Activity activity,ArrayList<Kategori> kategoris){
        this.activity = activity;
        this.kategoris = kategoris;
    }

    @Override
    public int getCount() {
        return kategoris.size();
    }

    @Override
    public Object getItem(int i) {
        return kategoris.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view1 = view;
        ViewHolder holder = null;

        if(view == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view1 = inflater.inflate(R.layout.list_kategori,null);
            holder.namaKategori = (TextView)view1.findViewById(R.id.nama_kategori);
            holder.image = (ImageView)view1.findViewById(R.id.gambar_kategori);
            view1.setTag(holder);
        }else{
            holder = (ViewHolder)view.getTag();
        }

        Kategori kategori = (Kategori)getItem(i);
        holder.namaKategori.setText(kategori.getNamaKategori());


        return view1;
    }

    public static class ViewHolder{
        TextView namaKategori;
        ImageView image;
    }
}
