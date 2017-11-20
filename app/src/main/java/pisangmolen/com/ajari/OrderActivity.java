package pisangmolen.com.ajari;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pisangmolen.com.ajari.Adapter.SpinnerMapelAdapter;
import pisangmolen.com.ajari.Fragment.HomeFragment;
import pisangmolen.com.ajari.Helper.Connection;
import pisangmolen.com.ajari.Helper.NgajarinApp;
import pisangmolen.com.ajari.Helper.SessionManager;
import pisangmolen.com.ajari.Model.Mapel;
import pisangmolen.com.ajari.Model.User;
import pisangmolen.com.ajari.Utils.Konstanta;
import pisangmolen.com.ajari.Utils.TextDatePicker;
import pisangmolen.com.ajari.Utils.TextTimePicker;

public class OrderActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText alamat,catatanEt;
    private Spinner jumlahMurid, durasiSpinner, mapel;
    private TextDatePicker datePicker;
    private TextTimePicker timeMulaiPicker;
    private AVLoadingIndicatorView avi,aviHarga;
    private RelativeLayout rlBook;
    private CardView cardOrder;
    private TextView hargaTv;


    private int jenjangType;
    private int harga,durasi,jumlahSiswa,idMapel;
    private String catatan;
    private SessionManager sessionManager;
    private static final String ID_KATEGORI = "id_kategori";

    private ArrayList<String> jumlahMurids;
    private ArrayList<Mapel> mapels;
    private ArrayList<String> durasis;

    private SpinnerMapelAdapter spinnerMapelAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        jenjangType = this.getIntent().getIntExtra(Konstanta.TAG_JENJANG, Konstanta.TYPE_SD);
        sessionManager = new SessionManager(getApplicationContext());

        jumlahMurids = new ArrayList<>();
        mapels = new ArrayList<>();
        durasis = new ArrayList<>();

        switch (jenjangType){
            case Konstanta.TYPE_SD:{
                getSupportActionBar().setTitle("Booking-SD");
                break;
            }
            case Konstanta.TYPE_SMA:{
                getSupportActionBar().setTitle("Booking-SMA");
                break;
            }
            case Konstanta.TYPE_SMP:{
                getSupportActionBar().setTitle("Booking-SMP");
                break;
            }
            case Konstanta.TYPE_MENGAJI:{
                getSupportActionBar().setTitle("Booking-Mengaji");
                break;
            }
            case Konstanta.TYPE_MUSIK:{
                getSupportActionBar().setTitle("Booking-Musik");
                break;
            }
            case Konstanta.TYPE_LUKIS:{
                getSupportActionBar().setTitle("Booking-Lukis");
                break;
            }
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getDataFormField(Integer.toString(jenjangType));



        cardOrder = (CardView) findViewById(R.id.button_order);
        alamat = (EditText) findViewById(R.id.alamat_les);
        catatanEt = (EditText) findViewById(R.id.catatan_les);
        jumlahMurid = (Spinner) findViewById(R.id.siswa_les);
        durasiSpinner = (Spinner) findViewById(R.id.durasi_les);
        mapel = (Spinner)findViewById(R.id.pelajaran_les);
        avi = (AVLoadingIndicatorView)findViewById(R.id.avi);
        aviHarga = (AVLoadingIndicatorView)findViewById(R.id.avi_total_harga);
        rlBook = (RelativeLayout)findViewById(R.id.rl_book);
        datePicker = new TextDatePicker(this, R.id.tanggal_les);
        timeMulaiPicker = new TextTimePicker(this, R.id.jam_mulai_les);
        hargaTv = (TextView)findViewById(R.id.harga);

        avi.show();


        datePicker.updateEditText();
        timeMulaiPicker.updateEditText();
        cardOrder.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                break;
            }
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.button_order:{
//                String data = "Tanggal : "+datePicker.getText()+"\n"+
//                              "Jam     : "+timeMulaiPicker.getText()+"\n"+
//                              "siswa   : "+jumlahMurid.getSelectedItem().toString()+"\n"+
//                              "durasi  : "+durasiSpinner.getSelectedItem().toString();
//                Log.d("Order Details", data);
                book();
                break;
            }
        }
    }

    public void getDataFormField(final String idKategori){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Connection.getAlamatServer() + Connection.getFormField(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("book_1",response);
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equals("success")){

                        JSONArray pilihanMapel = jsonObject.getJSONArray("mapel");
                        JSONArray pilihanDurasi = jsonObject.getJSONArray("durasi");
                        JSONArray pilihanMurid = jsonObject.getJSONArray("jumlah_murid");

                        Log.d("book_4",pilihanMapel.length()+"");
                        for(int i=0;i<pilihanMapel.length();i++){
                            JSONObject mapelJson = pilihanMapel.getJSONObject(i);
                            Mapel mapel = new Mapel();
                            mapel.setIdMapel(mapelJson.getString("id_mapel"));
                            mapel.setNamaMapel(mapelJson.getString("nama_mapel"));
                            mapels.add(mapel);
                        }

                        for(int j=0;j<pilihanDurasi.length();j++){
                            JSONObject durasiJson = pilihanDurasi.getJSONObject(j);
                            durasis.add(durasiJson.getString("durasi"));
                        }

                        for (int k=0;k<pilihanMurid.length();k++){
                            JSONObject jumlahMuridJson = pilihanMurid.getJSONObject(k);
                            jumlahMurids.add(jumlahMuridJson.getString("jumlah"));
                        }
                        Log.d("book_3",mapels.size()+"");
                        spinnerMapelAdapter = new SpinnerMapelAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, mapels);
                        mapel.setAdapter(spinnerMapelAdapter);
                        mapel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                Mapel mapel = spinnerMapelAdapter.getItem(i);
                                idMapel = Integer.parseInt(mapel.getIdMapel());
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });


                        final ArrayAdapter<String> adapterJumlahMurid = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,jumlahMurids);
                        jumlahMurid.setAdapter(adapterJumlahMurid);
                        jumlahMurid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                jumlahSiswa = Integer.parseInt(adapterJumlahMurid.getItem(i));
                                cekPrice(durasi,jumlahSiswa);
                                ((TextView) view).setTextColor(Color.BLACK);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });


                        final ArrayAdapter<String> adapterDurasi = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,durasis);
                        durasiSpinner.setAdapter(adapterDurasi);
                        durasiSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                ((TextView) view).setTextColor(Color.BLACK);
                                durasi = Integer.parseInt(adapterDurasi.getItem(i));
                                cekPrice(durasi,jumlahSiswa);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                        avi.hide();
                        rlBook.setVisibility(View.VISIBLE);

                    }else {
                        Toast.makeText(OrderActivity.this, "Ada error server", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Log.d("book_2",e.toString());
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
                map.put(User.TOKEN,sessionManager.getUserAccount().getToken());
                map.put(ID_KATEGORI,idKategori);
                return map;
            }
        };

        NgajarinApp.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void cekPrice(final int durasi, final int jumlahMurid){
        aviHarga.setVisibility(View.VISIBLE);
        aviHarga.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Connection.getAlamatServer() + Connection.getPRICE(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equals("success")){
                        harga = jsonObject.getInt("total");
                        aviHarga.hide();
                        aviHarga.setVisibility(View.GONE);
                        aviHarga.setVisibility(View.GONE);
                        hargaTv.setVisibility(View.VISIBLE);
                        hargaTv.setText("Total : Rp. "+harga);
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
                map.put("token",sessionManager.getUserAccount().getToken());
                map.put("durasi",Integer.toString(durasi));
                map.put("jumlah_murid",Integer.toString(jumlahMurid));
                return map;
            }
        };

        NgajarinApp.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void book(){
        if(catatanEt.getText().toString().isEmpty()){
            catatan = "";
        }else{
            catatan = catatanEt.getText().toString();
        }
        if(alamat.getText().toString().isEmpty()){
            Toast.makeText(this, R.string.blank_alamat, Toast.LENGTH_SHORT).show();
        }else{
            rlBook.setVisibility(View.GONE);
            avi.setVisibility(View.VISIBLE);
            avi.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Connection.getAlamatServer() + Connection.getBOOK(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{

                        avi.hide();
                        avi.setVisibility(View.GONE);
                        JSONObject jsonObject = new JSONObject(response);
                        if(jsonObject.getString("status").equals("success")){
                            rlBook.setVisibility(View.VISIBLE);
                            Toast.makeText(OrderActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(OrderActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                    map.put("token",sessionManager.getUserAccount().getToken());
                    map.put("id_mapel",Integer.toString(idMapel));
                    map.put("tanggal",datePicker.getText());
                    map.put("jam_mulai",timeMulaiPicker.getText());
                    map.put("durasi",Integer.toString(durasi));
                    map.put("jumlah_siswa",Integer.toString(jumlahSiswa));
                    map.put("alamat",alamat.getText().toString());
                    map.put("catatan",catatan);
                    return map;
                }
            };
            NgajarinApp.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        }
    }
}
