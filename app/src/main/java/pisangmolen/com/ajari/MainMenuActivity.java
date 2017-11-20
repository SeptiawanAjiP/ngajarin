package pisangmolen.com.ajari;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import pisangmolen.com.ajari.Adapter.KategoriAdapter;
import pisangmolen.com.ajari.Fragment.BookingFragment;
import pisangmolen.com.ajari.Fragment.HomeFragment;
import pisangmolen.com.ajari.Fragment.MyProfileFragment;
import pisangmolen.com.ajari.Model.Kategori;

public class MainMenuActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            displaySelectedFragment(item.getItemId());
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);

    }

    private void displaySelectedFragment(int itemId){
        Fragment fragment = null;

        switch (itemId){
            case R.id.navigation_home:{
                fragment = HomeFragment.newInstance();
                break;
            }
            case R.id.navigation_booking:{
                fragment = BookingFragment.newInstance();
                break;
            }
            case R.id.navigation_profile:{
                fragment = MyProfileFragment.newInstance();
                break;
            }
        }
        if(fragment != null){
            FragmentTransaction st = getSupportFragmentManager().beginTransaction();
            st.replace(R.id.container, fragment);
            st.commit();
        }

    }

}
