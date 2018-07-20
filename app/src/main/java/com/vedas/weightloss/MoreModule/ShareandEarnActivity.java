package com.vedas.weightloss.MoreModule;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.qrcode.QRCodeWriter;
import com.vedas.weightloss.R;

import static android.R.id.content;

/**
 * Created by Rise on 12/06/2018.
 */

public class ShareandEarnActivity extends AppCompatActivity {

    ImageView imageView;
    Button btnScan;
    EditText editText;
    String EditTextValue;
    Thread thread;
    public final static int QRcodeWidth = 350;
    Bitmap bitmap;
    Button button, share;
    TextView tv_qr_readTxt;
    Toolbar toolbar;
    ImageView btn_back;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_earn);
        setToolbar();
        init();

    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btn_back = (ImageView) toolbar.findViewById(R.id.back);
        btn_back.setBackgroundResource(R.drawable.ic_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView toolbartext = (TextView) toolbar.findViewById(R.id.toolbar_text);
        toolbartext.append("Share & Earn");
    }

    private void init() {
        imageView = (ImageView) findViewById(R.id.qrcode);

       
        //editText = (EditText) findViewById(R.id.editText);
        // button = (Button) findViewById(R.id.button);
        //btnScan = (Button) findViewById(R.id.btnScan);
        tv_qr_readTxt = (TextView) findViewById(R.id.qrtext);



        /*imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!editText.getText().toString().isEmpty()) {
                   // EditTextValue = editText.getText().toString();

                    try {
                        bitmap = TextToImageEncode(EditTextValue);

                        imageView.setImageBitmap(bitmap);

                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                } else {
                    editText.requestFocus();
                    Toast.makeText(getApplicationContext(), "Please Enter Your Scanned Test", Toast.LENGTH_LONG).show();
                }

            }
        });

*/
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                IntentIntegrator integrator = new IntentIntegrator(ShareandEarnActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCaptureActivity(CaptureActivityPortrait.class);
                integrator.setOrientationLocked(false);
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();

            }
        });


    }


    Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;

        QRCodeWriter writer = new QRCodeWriter();
        try {
            bitMatrix = writer.encode(String.valueOf(content), BarcodeFormat.QR_CODE, 200, 200);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            ((ImageView) findViewById(R.id.qrcode)).setImageBitmap(bmp);

        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Log.e("Scan", "Cancelled scan");

            } else {
                Log.e("Scan", "Scanned");


                tv_qr_readTxt.setText(result.getContents());
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            // This is important, otherwise the response will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
