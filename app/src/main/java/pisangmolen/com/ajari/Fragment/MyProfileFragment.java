package pisangmolen.com.ajari.Fragment;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;

import pisangmolen.com.ajari.Dialog.QRCodeDialog;
import pisangmolen.com.ajari.Helper.SessionManager;
import pisangmolen.com.ajari.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfileFragment extends Fragment  {

    private RelativeLayout rlQrCode,rlEdit,rlLogout;
    private View view;
    private AVLoadingIndicatorView avi;
    private TextView nameProfil,phoneProfil;

    private SessionManager sessionManager;

    public MyProfileFragment() {
        // Required empty public constructor
    }

    public static MyProfileFragment newInstance() {
        MyProfileFragment fragment = new MyProfileFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_profile,container,false);

        rlQrCode = (RelativeLayout)view.findViewById(R.id.rl_qr_code);
        rlEdit = (RelativeLayout)view.findViewById(R.id.rl_edit_profil);
        rlLogout = (RelativeLayout)view.findViewById(R.id.rl_logout);
        nameProfil = (TextView)view.findViewById(R.id.name_profile);
        phoneProfil = (TextView)view.findViewById(R.id.phone_profile);

        avi = (AVLoadingIndicatorView)view.findViewById(R.id.avi);

        sessionManager = new SessionManager(getContext());

        nameProfil.setText(sessionManager.getUserAccount().getUsername());
        phoneProfil.setText(sessionManager.getUserAccount().getTelepon());

        rlQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QRCodeDialog qrCodeDialog = new QRCodeDialog(getActivity(),sessionManager.getUserAccount().getToken());
                qrCodeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                qrCodeDialog.show();
            }
        });
        return view;
    }

}
