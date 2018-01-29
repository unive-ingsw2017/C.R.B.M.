package it.unive.dais.cevid.datadroid.template;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

public class HelpActivity extends AppCompatActivity {
    private ImageSwitcher imageSwitcher;
    private Button next;
    private Button previous;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        next = (Button) findViewById(R.id.next);
        previous = (Button) findViewById(R.id.prev);

        imageSwitcher = (ImageSwitcher) findViewById(R.id.imgsw);
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                return imageView;
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSwitcher.setImageResource(R.drawable.big_logo);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSwitcher.setImageResource(R.drawable.ic_search);
            }
        });
    }
}
