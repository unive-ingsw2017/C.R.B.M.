package it.unive.dais.cevid.datadroid.template;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class HelpActivity extends AppCompatActivity {
    private ImageSwitcher imageSwitcher;
    private Button next;
    private Button previous;
    private static final int MAX_IMGS = 10;
    private List<Integer> images = Collections.EMPTY_LIST;
    private int current_position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        next = (Button) findViewById(R.id.next);
        previous = (Button) findViewById(R.id.prev);

        images = new ArrayList<>();
        current_position = 0;

        imageSwitcher = (ImageSwitcher) findViewById(R.id.imgsw);

        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                return imageView;
            }
        });

        imageSwitcher.setImageResource(R.drawable.big_logo);
        previous.setVisibility(View.INVISIBLE);

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_position--;
                if (current_position == 0)
                    previous.setVisibility(View.INVISIBLE);
                imageSwitcher.setImageResource(R.drawable.big_logo);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_position++;
                if (current_position == 1)
                    previous.setVisibility(View.VISIBLE);
                imageSwitcher.setImageResource(R.drawable.big_logo);
            }
        });
    }
}
