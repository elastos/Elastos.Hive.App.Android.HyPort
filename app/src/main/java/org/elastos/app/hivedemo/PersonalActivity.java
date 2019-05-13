package org.elastos.app.hivedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;

public class PersonalActivity extends AppCompatActivity {

    private ImageView myaddressqrcode;
    private static SimpleCarrier simpleCarrier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Personal information");
        simpleCarrier = SimpleCarrier.getInstance();
        initView();

    }

    private void initView() {
        myaddressqrcode = findViewById(R.id.myaddressqrcode);
        myaddressqrcode.setImageBitmap(QRCodeUtils.createQRCodeBitmap(simpleCarrier.MyAddress()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
