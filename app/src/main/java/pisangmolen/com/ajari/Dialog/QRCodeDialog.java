package pisangmolen.com.ajari.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.wang.avi.AVLoadingIndicatorView;

import net.glxn.qrgen.android.QRCode;

import pisangmolen.com.ajari.Fragment.MyProfileFragment;
import pisangmolen.com.ajari.R;

/**
 * Created by aji on 11/15/2017.
 */

public class QRCodeDialog extends Dialog {
    private Activity activity;
    private String token;
    private final static int QRcodeWidth = 600 ;
    private Bitmap bitmap ;
    private ImageView qrCode;
    private LinearLayout llTutup;

    MyProfileFragment myProfileFragment;

    public QRCodeDialog(Activity activity,String token){
        super(activity);
        this.activity = activity;
        this.token = token;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_qr_code);
        qrCode = (ImageView)findViewById(R.id.qr_code);
        llTutup = (LinearLayout)findViewById(R.id.ll_tutup);

        myProfileFragment = new MyProfileFragment();
        bitmap = QRCode.from("dfkshdk12208302khdf").bitmap();
        qrCode.setImageBitmap(bitmap);


//        try{
//            bitmap = TextToImageEncode(token);
//            qrCode.setVisibility(View.VISIBLE);
//            qrCode.setImageBitmap(bitmap);
//
//        }catch (WriterException w){
//            w.printStackTrace();
//        }
//
        llTutup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    /*
    Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        activity.getResources().getColor(R.color.black):activity.getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 600, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }
    */


}
