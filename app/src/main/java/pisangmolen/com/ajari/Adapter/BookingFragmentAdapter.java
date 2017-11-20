package pisangmolen.com.ajari.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import pisangmolen.com.ajari.Fragment.BookingFinishFragment;
import pisangmolen.com.ajari.Fragment.BookingOnProgressFragment;

/**
 * Created by user on 11/13/2017.
 */

public class BookingFragmentAdapter extends FragmentPagerAdapter {

    public BookingFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: {
                return BookingOnProgressFragment.newInstance();
            }
            case 1: {
                return BookingFinishFragment.newInstance();
            }
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:{
                return "Dalam Proses";
            }
            case 1:{
                return "Selesai";
            }
            default:{
                return null;
            }
        }
    }
}
