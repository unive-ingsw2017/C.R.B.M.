package it.unive.dais.cevid.datadroid.template;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import it.unive.dais.cevid.datadroid.template.DatabaseUtils.DBHelper;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread myThread = new Thread() {
            @Override
            public void run() {
                try {
                    DBHelper db = DBHelper.getSingleton(getApplicationContext());
                    db.getReadableDatabase();
                    db.getULSS();
                    sleep(2000);
                    db.close();
                    Intent main = new Intent(getApplicationContext(), MapsActivity.class);
                    startActivity(main);
                    finish();
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                }


            }
        };

        myThread.start();
    }
}
