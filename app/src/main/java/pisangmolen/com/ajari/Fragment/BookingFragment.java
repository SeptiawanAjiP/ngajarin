package pisangmolen.com.ajari.Fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pisangmolen.com.ajari.Adapter.BookingFragmentAdapter;
import pisangmolen.com.ajari.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookingFragment extends Fragment {


    public BookingFragment() {
        // Required empty public constructor
    }

    public static BookingFragment newInstance() {
        BookingFragment fragment = new BookingFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_booking, container, false);
        final ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.booking_pager);
        final TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.booking_tab);

        viewPager.setAdapter(new BookingFragmentAdapter(this.getChildFragmentManager()));
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager, true);
        return rootView;
    }

}
