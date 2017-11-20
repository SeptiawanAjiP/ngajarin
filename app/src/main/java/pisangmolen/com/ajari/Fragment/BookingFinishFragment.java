package pisangmolen.com.ajari.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import pisangmolen.com.ajari.Helper.Connection;
import pisangmolen.com.ajari.Helper.NgajarinApp;
import pisangmolen.com.ajari.Helper.SessionManager;
import pisangmolen.com.ajari.Model.BookingComplete;
import pisangmolen.com.ajari.Model.BookingProgress;
import pisangmolen.com.ajari.Model.Konstanta;
import pisangmolen.com.ajari.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookingFinishFragment extends Fragment {

    ArrayList<BookingComplete> arrayList = new ArrayList<>();
    private View rootView;
    private LinearLayoutManager llm;
    private SessionManager sessionManager;
    private AVLoadingIndicatorView avi;


    public BookingFinishFragment() {
        // Required empty public constructor
    }

    public static BookingFinishFragment newInstance() {
        BookingFinishFragment fragment = new BookingFinishFragment();

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arrayList.add(new BookingComplete("SD", "Bahasa Indonesia", "Nella Karisma", "20 Nov, 14:00"));
        arrayList.add(new BookingComplete("SMP", "Biologi", "Nella Karisma", "19 Nov, 14:00"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_booking_finish, container, false);
        avi = (AVLoadingIndicatorView)rootView.findViewById(R.id.avi);

        llm = new LinearLayoutManager(this.getContext());
        sessionManager = new SessionManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        getData();

        return rootView;
    }

    public void setAdapter(ArrayList<BookingComplete> bookingCompletes){
        avi.setVisibility(View.GONE);
        avi.hide();
        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.list_booking_finish);
        rv.setVisibility(View.VISIBLE);
        rv.setLayoutManager(llm);
        rv.setAdapter(new BookingFinishAdapter(this.getActivity(), bookingCompletes));
        rv.getAdapter().notifyDataSetChanged();
    }

    public void getData(){
        avi.setVisibility(View.VISIBLE);
        avi.show();
        final ArrayList<BookingComplete> bookingCompletes = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Connection.getAlamatServer() + Connection.getListHistoryBook(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("history_res",response);
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equals("success")){
                        JSONArray jsonArray = jsonObject.getJSONArray("list");
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            BookingComplete bookingComplete = new BookingComplete(jsonObject1.getString("nama_kategori"),
                                    jsonObject1.getString("nama_mapel"),"Nella Karisma",jsonObject1.getString("tanggal"));
                            bookingCompletes.add(bookingComplete);
                        }
                        setAdapter(bookingCompletes);
                    }else{

                    }
                }catch (Exception e){
                    Log.d("history",e.toString());
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
