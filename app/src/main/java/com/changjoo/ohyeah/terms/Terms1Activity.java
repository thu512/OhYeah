package com.changjoo.ohyeah.terms;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.changjoo.ohyeah.Activity;
import com.changjoo.ohyeah.R;

public class Terms1Activity extends Activity {
    ImageButton btn1;
    ImageButton btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms1);

        btn1=(ImageButton)findViewById(R.id.btn1);
        btn2=(ImageButton)findViewById(R.id.btn2);


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
