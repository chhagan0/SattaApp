package com.madhurmilan.milan.activityclass;

import static com.madhurmilan.milan.shareprefclass.Utility.BroadCastStringForAction;
import static com.madhurmilan.milan.shareprefclass.Utility.myReceiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textview.MaterialTextView;
import com.kalyankuber.alpha.R;
import com.madhurmilan.milan.shareprefclass.SharPrefClass;
import com.madhurmilan.milan.shareprefclass.Utility;
import com.madhurmilan.milan.shareprefclass.YourService;

public class UserHelpActivity extends AppCompatActivity {

    private MaterialTextView mn1, mN2, whatsAN,emailId;
    private MaterialToolbar toolbar;
    private IntentFilter mIntentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_help);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.matka_blue));

        intVariables();
        loadData();
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void loadData() {
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(BroadCastStringForAction);
        Intent serviceIntent = new Intent(this, YourService.class);
        startService(serviceIntent);

//        mn1.setText(SharPrefClass.getContactObject(this, SharPrefClass.KEY_PHONE_NUMBER1));
        mn1.setText("9216869685");
//        mN2.setText(SharPrefClass.getContactObject(this, SharPrefClass.KEY_PHONE_NUMBER2));
        mN2.setText("9216869685");
//        whatsAN.setText(SharPrefClass.getContactObject(this, SharPrefClass.KEY_WHATSAP_NUMBER));
        whatsAN.setText("9216869685");
        emailId.setText(SharPrefClass.getContactObject(this, SharPrefClass.KEY_REACH_US_EMAIL));
    }

    public void phoneNum1(View view) {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.CALL_PHONE}, 100);
//        }else {
//            Intent callIntent = new Intent(Intent.ACTION_CALL);
//            callIntent.setData(Uri.parse("tel:"+ mn1.getText()));
//            startActivity(callIntent);
//        }

        setClipboard(this, "9216869685");
    }
    public void phoneNum2(View view) {
        String url = SharPrefClass.getContactObject(UserHelpActivity.this, SharPrefClass.KEY_PHONE_NUMBER2);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    private void setClipboard(Context context, String text) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, "Number Copied", Toast.LENGTH_SHORT).show();
    }

    public void whatsAppBtn(View view) {
        String url = "https://api.whatsapp.com/send?phone="+ 91+whatsAN.getText();
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
    public void emailBtn(View view) {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{SharPrefClass.getContactObject(this, SharPrefClass.KEY_REACH_US_EMAIL)});
         email.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.sub_mail));
        email.putExtra(Intent.EXTRA_TEXT, getString(R.string.msg_mail));
        email.setType("message/rfc822");
        startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        registerReceiver(myReceiver, mIntentFilter);
    }
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(myReceiver, mIntentFilter);
    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myReceiver);
    }

    private void intVariables() {
        toolbar = findViewById(R.id.toolbar);
        mn1 = findViewById(R.id.phone_num_1);
        mN2 = findViewById(R.id.phone_num_2);
        whatsAN = findViewById(R.id.whats_app_num);
        emailId = findViewById(R.id.emailId);
        MaterialTextView dataConText = findViewById(R.id.dataConText);
        Utility utility = new Utility(dataConText);
    }

}