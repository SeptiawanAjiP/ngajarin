package pisangmolen.com.ajari;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher{

    private static final String TAG = "PhoneAuthActivity";

    // UI references.
    private EditText mNameView, mPhoneView, mEmailView;
    private Button mButtonDaftar, mButtonFacebook;
    private TextView mActionBottom,notRegistered;
    private AVLoadingIndicatorView avi;

    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";
    private final String TYPE = "type";
    private final String TELEPON = "telepon";
    private final String ID_VERIFICATION = "id_verification";

    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;
    private FirebaseAuth mAuth;
    private SessionManager sessionManager;
    // [END declare_auth]

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.input_email);
        mPhoneView = (EditText) findViewById(R.id.input_phone);
        mNameView = (EditText) findViewById(R.id.input_name) ;
        mButtonDaftar = (Button) findViewById(R.id.input_daftar_button);
        mButtonFacebook = (Button) findViewById(R.id.input_dafatr_facebook);
        mActionBottom = (TextView) findViewById(R.id.text_action);
        avi = (AVLoadingIndicatorView)findViewById(R.id.avi);
        notRegistered = (TextView)findViewById(R.id.phone_not_registered);

        mButtonFacebook.setOnClickListener(this);
        mButtonDaftar.setOnClickListener(this);
        mActionBottom.setOnClickListener(this);

        sessionManager = new SessionManager(getApplicationContext());

        mAuth = FirebaseAuth.getInstance();

        mEmailView.addTextChangedListener(this);
        mPhoneView.addTextChangedListener(this);
        mNameView.addTextChangedListener(this);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                updateUI(STATE_VERIFY_SUCCESS,phoneAuthCredential);
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    Toast.makeText(RegisterActivity.this, "Invalid number phone", Toast.LENGTH_SHORT).show();
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

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mNameView.setError(null);
        mPhoneView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String phone = mPhoneView.getText().toString();
        String name = mNameView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(phone) && !isPhoneValid(phone)) {
            mPhoneView.setError(getString(R.string.error_invalid_password));
            focusView = mPhoneView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;

        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (TextUtils.isEmpty(name)) {
            mNameView.setError(getString(R.string.error_field_required));
            focusView = mNameView;
            cancel = true;

        } else if (!isNameValid(name)) {
            mNameView.setError(getString(R.string.error_invalid_name));
            focusView = mNameView;
            cancel = true;
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


    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPhoneValid(String phone) {
        //TODO: Replace this with your own logic
        return phone.length() > 8;
    }

    private boolean isNameValid(String name) {
        //TODO: Replace this with your own logic
        return !name.matches(".*\\d+.*");
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.input_daftar_button:{
                attemptLogin();
                accountCheck(mPhoneView.getText().toString());
                break;
            }
            case R.id.text_action:{
                startActivity(new Intent(this, LoginActivity.class));
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

    }

    @Override
    public void afterTextChanged(Editable editable) {

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

            register(mNameView.getText().toString(),mEmailView.getText().toString(),mPhoneView.getText().toString(),user.getUid());

        }
    }

    public void accountCheck(final String userInput){
        avi.show();
        mButtonDaftar.setVisibility(View.INVISIBLE);
        if (!TextUtils.isEmpty(userInput) && !isPhoneValid(userInput)) {
            mPhoneView.setError(getString(R.string.error_invalid_phone));
        }else{
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Connection.getAlamatServer() + Connection.getLOGIN(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("accountCheck",response);
                    Log.d("accountCheck",Connection.getLOGIN());
                    try{

                        JSONObject jsonObject = new JSONObject(response);
                        if(jsonObject.getString("status").equals("fail")){
                            notRegistered.setVisibility(View.VISIBLE);
                            notRegistered.setText("Verifikasi akan dikirimkan melalui SMS");
                            InputMethodManager inputManager = (InputMethodManager)
                                    getSystemService(Context.INPUT_METHOD_SERVICE);

                            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                            startPhoneNumberVerification(userInput);

                        }else{
                            avi.hide();
                            avi.setVisibility(View.GONE);
                            notRegistered.setVisibility(View.VISIBLE);
                            notRegistered.setText("Nomor telepon sudah terdaftar di ngajar.in");
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

    public void register(final String username, final String email, final String phone, final String idVerification){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Connection.getAlamatServer() + Connection.getRegisterPhone(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equals("success")){
                        Toast.makeText(RegisterActivity.this, "Berhasil Daftar", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(RegisterActivity.this, "gagal daftar", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Log.d("error_register",e.toString());
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
                map.put("username",username);
                map.put("email",email);
                map.put("telepon",phone);
                map.put("id_verification",idVerification);
                return map;
            }
        };
        NgajarinApp.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

}
