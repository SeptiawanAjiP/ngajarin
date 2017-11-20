package pisangmolen.com.ajari.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import pisangmolen.com.ajari.OrderActivity;
import pisangmolen.com.ajari.R;
import pisangmolen.com.ajari.Utils.Konstanta;

public class HomeFragment extends Fragment implements View.OnClickListener{

    CardView sd, smp, sma, mengaji, musik, lukis;
    Button requestMateri;
    CarouselView carouselView;

    int[] images = {
            R.drawable.foto_1,
            R.drawable.foto_2,
            R.drawable.foto_3
    };

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        sd = (CardView) rootView.findViewById(R.id.order_sd);
        smp = (CardView)rootView.findViewById(R.id.order_smp);
        sma = (CardView)rootView.findViewById(R.id.order_sma);
        mengaji = (CardView)rootView.findViewById(R.id.order_ngaji);
        musik = (CardView)rootView.findViewById(R.id.order_musik);
        lukis = (CardView)rootView.findViewById(R.id.order_lukis);

        sd.setOnClickListener(this);
        smp.setOnClickListener(this);
        sma.setOnClickListener(this);
        mengaji.setOnClickListener(this);
        musik.setOnClickListener(this);
        lukis.setOnClickListener(this);

        carouselView = (CarouselView) rootView.findViewById(R.id.corausel_view);
        carouselView.setPageCount(images.length);
        carouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                Glide.with(getActivity())
                        .load(images[position])
                        .into(imageView);
            }
        });

        return rootView;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.order_sd:{
                Intent i = new Intent(getActivity(), OrderActivity.class);
                i.putExtra(Konstanta.TAG_JENJANG, Konstanta.TYPE_SD);
                getActivity().startActivity(i);
                break;
            }
            case R.id.order_smp:{
                Intent i = new Intent(getActivity(), OrderActivity.class);
                i.putExtra(Konstanta.TAG_JENJANG, Konstanta.TYPE_SMP);
                getActivity().startActivity(i);
                break;
            }
            case R.id.order_sma:{
                Intent i = new Intent(getActivity(), OrderActivity.class);
                i.putExtra(Konstanta.TAG_JENJANG, Konstanta.TYPE_SMA);
                getActivity().startActivity(i);
                break;
            }
            case R.id.order_ngaji: {
                Intent i = new Intent(getActivity(), OrderActivity.class);
                i.putExtra(Konstanta.TAG_JENJANG, Konstanta.TYPE_MENGAJI);
                getActivity().startActivity(i);
                break;
            }
            case R.id.order_musik: {
                Intent i = new Intent(getActivity(), OrderActivity.class);
                i.putExtra(Konstanta.TAG_JENJANG, Konstanta.TYPE_MUSIK);
                getActivity().startActivity(i);
                break;
            }
            case R.id.order_lukis: {
                Intent i = new Intent(getActivity(), OrderActivity.class);
                i.putExtra(Konstanta.TAG_JENJANG, Konstanta.TYPE_LUKIS);
                getActivity().startActivity(i);
                break;
            }
        }
    }
}
