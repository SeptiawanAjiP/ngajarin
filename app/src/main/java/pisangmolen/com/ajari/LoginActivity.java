package pisangmolen.com.ajari;

import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import pisangmolen.com.ajari.Helper.Connection;
import pisangmolen.com.ajari.Helper.NgajarinApp;
import pisangmolen.com.ajari.Helper.SessionManager;
import pisangmolen.com.ajari.Model.User;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements OnClickListener, TextWatcher {

    private static final String TAG = "PhoneAuthActivity";

    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private SessionManager sessionManager;

    //parameter
    private final String TYPE = "type";
    private final String TELEPON = "telepon";
    private final String FB = "fb";
    private final String GOOGLE = "google";
    private final String ID_VERIFICATION = "id_verification";

    private String idVerification;
    // UI references.
    private EditText mPhone;
    private Button mButtonDaftar, mButtonFacebook;
    private TextView mButtomAction;
    private TextView notRegistered;
    private AVLoadingIndicatorView avi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mPhone = (EditText) findViewById(R.id.input_phoneemail);
        mButtonDaftar = (Button) findViewById(R.id.input_daftar_button);
        mButtonFacebook = (Button) findViewById(R.id.input_dafatr_facebook);
        mButtomAction = (TextView) findViewById(R.id.text_action);
        notRegistered = (TextView)findViewById(R.id.phone_not_registered);
        avi = (AVLoadingIndicatorView)findViewById(R.id.avi);

        mPhone.addTextChangedListener(this);
        mButtonFacebook.setOnClickListener(this);
        mButtonDaftar.setOnClickListener(this);
        mButtomAction.setOnClickListener(this);

        sessionManager = new SessionManager(getApplicationContext());
        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                updateUI(STATE_VERIFY_SUCCESS, phoneAuthCredential);
                signInWithPhoneAuthCredential(phoneAuthCredential);
                Log.d(TAG, "onVerificationCompleted:" + phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    Toast.makeText(LoginActivity.this, "Invalid number phone", Toast.LENGTH_SHORT).show();
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                    // [END_EXCLUDE]

                }

                updateUI(STATE_VERIFY_FAILED);
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                Log.d(TAG, "onCodeSent:" + s);
                mVerificationId = s;
                mResendToken = forceResendingToken;
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        // [START_EXCLUDE]
        if (mVerificationInProgress && validatePhoneNumber()) {
            startPhoneNumberVerification(mPhone.getText().toString());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS,mVerificationInProgress);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }

    private boolean validatePhoneNumber() {
        String phoneNumber = mPhone.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            mPhone.setError("Invalid phone number.");
            //mPhoneNumberField.setTextColor(Color.parseColor("#ff1744"));
            return false;
        }

        return true;
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        mVerificationInProgress = true;
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // [START_EXCLUDE]
                            updateUI(STATE_SIGNIN_SUCCESS, user);
                            // [END_EXCLUDE]
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]

                                // [END_EXCLUDE]
                            }
                            // [START_EXCLUDE silent]
                            // Update UI
                            updateUI(STATE_SIGNIN_FAILED);
                            // [END_EXCLUDE]
                        }
                    }
                });
    }

    private void updateUI(int uiState) {
        updateUI(uiState, mAuth.getCurrentUser(), null);
    }

    private void updateUI(int uiState, FirebaseUser user) {
        updateUI(uiState, user, null);
    }

    private void updateUI(int uiState, PhoneAuthCredential cred) {
        updateUI(uiState, null, cred);
    }

    private void updateUI(int  uiState,FirebaseUser user,PhoneAuthCredential cred){
        switch (uiState){
            case STATE_INITIALIZED:
                break;
            case STATE_CODE_SENT:
                Log.d("Shinta","kode terkirim");
                Toast.makeText(this, "Kode Terkirim", Toast.LENGTH_SHORT).show();
                break;
            case STATE_VERIFY_FAILED:
                Log.d("Shinta","verifikasi gagal");
                Toast.makeText(this, "Verifikasi Gagal", Toast.LENGTH_SHORT).show();
                break;
            case STATE_VERIFY_SUCCESS:
                Log.d("Shinta","Verifikasi sukses");
                Toast.makeText(this, "Verifikasi sukses", Toast.LENGTH_SHORT).show();
                if(cred !=null){
                    if(cred.getSmsCode() != null){
                        Log.d("Shinta","smscode : "+cred.getSmsCode());
                    }else {
                        Log.d("Shinta","creednull");
                    }
                }
                break;
            case STATE_SIGNIN_FAILED:
                notRegistered.setText("Verifikasi gagal");
                mButtonDaftar.setVisibility(View.VISIBLE);
                avi.hide();
                Log.d("Shinta","sign in failde");
                Toast.makeText(this, "sign in failed", Toast.LENGTH_SHORT).show();
                break;
            case STATE_SIGNIN_SUCCESS:
                notRegistered.setText("Verifikasi sukses");

                Toast.makeText(this, "Anda telah login !", Toast.LENGTH_SHORT).show();
                Log.d("Shinta","Anda telah login !");
                break;
        }

        if(user == null){
            Log.d("Shinta","Anda tidak loginn");
        }else{
            Toast.makeText(this, user.getUid(), Toast.LENGTH_SHORT).show();
            getAccessToken(mPhone.getText().toString(),user.getUid());

        }
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    //mengirim kode jika tidak bisa otomatis
    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    private void signOut() {
        mAuth.signOut();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.input_daftar_button:{
                notRegistered.setVisibility(View.GONE);
                avi.setVisibility(View.VISIBLE);
                avi.show();
                mButtonDaftar.setVisibility(View.GONE);
                accountCheck(mPhone.getText().toString());
                break;
            }
            case R.id.text_action:{
                startActivity(new Intent(this, RegisterActivity.class));
                finish();
                break;
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if(charSequence.length() > 0){
            mButtonDaftar.setEnabled(true);
            mButtonDaftar.setBackgroundTintList(this.getResources().getColorStateList(R.color.colorAccent));
        }else{
            mButtonDaftar.setEnabled(false);
            mButtonDaftar.setBackgroundTintList(this.getResources().getColorStateList(android.R.color.darker_gray));
        }

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private boolean isPhoneValid(String phone) {
        //TODO: Replace this with your own logic
        return phone.length() > 8;
    }

    public void accountCheck(final String userInput){

        if (!TextUtils.isEmpty(userInput) && !isPhoneValid(userInput)) {
            mPhone.setError(getString(R.string.error_invalid_phone));
        }else{
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Connection.getAlamatServer() + Connection.getLOGIN(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("accountCheck",response);
                    Log.d("accountCheck",Connection.getLOGIN());
                    try{

                        JSONObject jsonObject = new JSONObject(response);
                        if(jsonObject.getString("status").equals("success")){
                            notRegistered.setVisibility(View.VISIBLE);
                            notRegistered.setText(jsonObject.getString("message"));
                            InputMethodManager inputManager = (InputMethodManager)
                                    getSystemService(Context.INPUT_METHOD_SERVICE);

                            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                            startPhoneNumberVerification(userInput);

                        }else{
                            avi.hide();
                            avi.setVisibility(View.GONE);
                            notRegistered.setVisibility(View.VISIBLE);
                            notRegistered.setText(jsonObject.getString("message"));
                            mButtonDaftar.setVisibility(View.VISIBLE);
                        }
                    }catch (Exception e){
                        Log.d("accontCheckError",e.toString());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map = new HashMap<>();
                    map.put(TYPE,TELEPON);
                    map.put(TELEPON,userInput);
                    return map;
                }
            };

            NgajarinApp.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

        }

    }

    public void getAccessToken(final String phone,final String idVerification){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Connection.getAlamatServer() + Connection.getGetDataByPhone(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    Log.d("getAccessToken",response);
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equals("success")){
                        User user = new User();
                        JSONObject json = jsonObject.getJSONObject("data");
                        user.setToken(jsonObject.getString("token"));


                        user.setEmail(json.getString("email"));
                        user.setUsername(json.getString("username"));
                        user.setPathFoto(json.getString("path_foto"));
                        user.setTelepon(json.getString("telepon"));
                        user.setQrCode(json.getString("qr_code"));
                        user.setJenisKelamin(json.getString("jenis_kelamin"));
                        sessionManager.createLoginSession(user.getToken(),user.getEmail(),user.getUsername(),user.getPathFoto(),user.getTelepon(),user.getQrCode(),user.getJenisKelamin());
                        Intent intent = new Intent(LoginActivity.this,MainMenuActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(LoginActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put(TELEPON,phone);
                map.put(ID_VERIFICATION,idVerification);
                return map;
            }
        };

        NgajarinApp.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

}

