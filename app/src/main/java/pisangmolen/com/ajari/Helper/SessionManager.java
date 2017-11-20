package pisangmolen.com.ajari.Helper;

import android.content.Context;
import android.content.SharedPreferences;

import net.glxn.qrgen.android.QRCode;

import pisangmolen.com.ajari.Model.User;

/**
 * Created by aji on 11/19/2017.
 */

public class SessionManager {

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private final String SESSION = "session";
    public final String ID = "id";
    public final String TOKEN = "token";
    public final String USERNAME = "username";
    public final String PATH_FOTO = "pathFoto";
    public final String EMAIL = "email";
    public final String TELEPON = "telepon";
    public final String QRCODE = "qr_code";
    public final String JENIS_KELAMIN = "jenisKelamin"
;
    public SessionManager(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(SESSION,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void createLoginSession(String token,String email,String username,String pathFoto,String telepon,String qrCode,String jenisKelamin){
        editor.putString(TOKEN,token);
        editor.putString(EMAIL,email);
        editor.putString(USERNAME,username);
        editor.putString(PATH_FOTO,pathFoto);
        editor.putString(TELEPON,telepon);
        editor.putString(QRCODE,qrCode);
        editor.putString(JENIS_KELAMIN,jenisKelamin);
        editor.commit();
    }

    public User getUserAccount(){
        User user = new User();

        user.setId(sharedPreferences.getString(ID,""));
        user.setToken(sharedPreferences.getString(TOKEN,null));
        user.setUsername(sharedPreferences.getString(USERNAME,""));
        user.setPathFoto(sharedPreferences.getString(PATH_FOTO,""));
        user.setEmail(sharedPreferences.getString(EMAIL,""));
        user.setTelepon(sharedPreferences.getString(TELEPON,""));
        user.setJenisKelamin(sharedPreferences.getString(JENIS_KELAMIN,""));
        return user;
    }

    public void deletAccount(){
        sharedPreferences.edit().clear().commit();
    }
}
