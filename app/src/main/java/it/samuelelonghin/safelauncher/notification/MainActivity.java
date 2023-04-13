package it.samuelelonghin.safelauncher.notification;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import it.samuelelonghin.safelauncher.support.BaseFullActivity;
import it.samuelelonghin.safelauncher.R;

public class MainActivity extends BaseFullActivity implements MyListener {
    private TextView txtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("P", "********** CIAO BELLO");
        new NotificationService().setListener(this);
        txtView = findViewById(R.id.textView);
    }

    @Override
    public void setValue(String packageName) {
        txtView.append("                 " + packageName);
    }
}