package pisangmolen.com.ajari.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pisangmolen.com.ajari.Adapter.BookingFinishAdapter;
import pisangmolen.com.ajari.Adapter.BookingProgressAdapter;
import pisangmolen.com.ajari.Helper.Connection;
import pisangmolen.com.ajari.Helper.NgajarinApp;
import pisangmolen.com.ajari.Helper.SessionManager;
import pisangmolen.com.ajari.Model.BookingComplete;
import pisangmolen.com.ajari.Model.BookingProgress;
import pisangmolen.com.ajari.Model.Konstanta;
import pisangmolen.com.ajari.R;

public class BookingOnProgressFragment extends Fragment {
    private View rootView;
    private LinearLayoutManager llm;
    private SessionManager sessionManager;
    private AVLoadingIndicatorView avi;
    public static BookingOnProgressFragment newInstance() {
        BookingOnProgressFragment fragment = new BookingOnProgressFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_booking_on_progress, container, false);
        avi = (AVLoadingIndicatorView)rootView.findViewById(R.id.avi);
        sessionManager = new SessionManager(getContext());
        ArrayList<BookingProgress> arrayList = new ArrayList<>();
        arrayList.add(new BookingProgress("SD", "Biologi", "menunggu konfirmasi", "20 Nov, 19:00"));
        arrayList.add(new BookingProgress("SMP", "Biologi", "sudah di konfirmasi", "19 Nov, 14:00"));

        llm = new LinearLayoutManager(this.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        getData();

        return rootView;
    }

    public void setAdapter(ArrayList<BookingProgress> bookingProgresses){
        avi.setVisibility(View.GONE);
        avi.hide();
        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.list_booking_progress);
        rv.setVisibility(View.VISIBLE);
        rv.setLayoutManager(llm);
        rv.setAdapter(new BookingProgressAdapter(this.getActivity(), bookingProgresses));
        rv.getAdapter().notifyDataSetChanged();
    }

    public void getData(){
        avi.setVisibility(View.VISIBLE);
        avi.show();
        final ArrayList<BookingProgress> bookingProgresses = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Connection.getAlamatServer() + Connection.getListBook(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equals("success")){
                        JSONArray jsonArray = jsonObject.getJSONArray("list");
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            BookingProgress bookingProgress = new BookingProgress(jsonObject1.getString("nama_kategori"),
                                    jsonObject1.getString("nama_mapel"),
                                    jsonObject1.getString("status"),
                                    jsonObject1.getString("tanggal")+", "+jsonObject1.getString("jam_mulai"));
                            bookingProgresses.add(bookingProgress);
                        }
                        setAdapter(bookingProgresses);
                    }else{

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
                return map;
            }
        };

        NgajarinApp.getInstance(getContext()).addToRequestQueue(stringRequest);
    }



}
