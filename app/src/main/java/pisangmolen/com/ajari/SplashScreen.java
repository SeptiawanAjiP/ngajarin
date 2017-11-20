package pisangmolen.com.ajari;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.HashMap;

import pisangmolen.com.ajari.Helper.SessionManager;

public class SplashScreen extends AppCompatActivity {

    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        sessionManager = new SessionManager(getApplicationContext());
        startAnimation();
    }


    private void startAnimation() {
//        rope =  YoYo.with(Techniques.FadeInUp)
//                .duration(1500)
//                .interpolate(new AccelerateDecelerateInterpolator())
//                .playOn(mTarget);

        Thread splash = new Thread(){
            public void run(){
                try{
                    sleep(2000);



                        if(sessionManager.getUserAccount().getToken()==null){
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                            startActivity(intent);
                            finish();
                        }


                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        };
        splash.start();
    }

    public boolean adaKoneksi() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

}
