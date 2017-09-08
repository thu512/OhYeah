package com.gogolab.ohyeah.terms;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.gogolab.ohyeah.Activity;
import com.gogolab.ohyeah.R;

public class Terms2Activity extends Activity {
    ImageButton btn1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms2);
        btn1 = (ImageButton)findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
